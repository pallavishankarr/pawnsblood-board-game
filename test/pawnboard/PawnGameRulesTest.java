package pawnboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import pawnboard.controller.CardInitializer;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;
import pawnboard.player.MachinePlayer;
import pawnboard.strategy.FillFirstStrategy;

/**
 * Tests the rules of Pawns Board: passing and ending the game, what happens after the game
 * is over, legal and illegal card placement, drawing cards, scoring, and the AI player.
 *
 * <p>The tests use the real deck in docs/deck.config, so they do not depend on any specific
 * card. When a test needs a legal move, it searches the current player's hand and the board
 * for one, the same way the Fill First strategy does.
 */
public class PawnGameRulesTest {
    private static final String DECK_PATH = "docs/deck.config";
    private static final int ROWS = 3;
    private static final int COLS = 5;
    private static final int HAND_SIZE = 5;
    private static final int MAX_TURNS = 200;

    private PawnGameImpl model;

    @Before
    public void setUp() {
        Map<String, List<PawnCard>> decks = CardInitializer.initializeDeck(DECK_PATH, DECK_PATH);
        model = new PawnGameImpl(ROWS, COLS);
        // Copy the decks so each player draws from their own list.
        model.startGame(new ArrayList<>(decks.get("deck1")),
                new ArrayList<>(decks.get("deck2")), HAND_SIZE);
    }

    // ---------------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------------

    /**
     * Finds the first legal move for the current player.
     *
     * @return {cardIndex, row, col}, or null if the current player has no legal move
     */
    private int[] findFirstLegalMove() {
        Player player = model.getCurrentPlayer();
        List<PawnCard> hand = model.getHand(player);
        for (int i = 0; i < hand.size(); i++) {
            for (int r = 0; r < model.getHeight(); r++) {
                for (int c = 0; c < model.getWidth(); c++) {
                    if (model.placeCardLegal(player, hand.get(i), r, c)) {
                        return new int[]{i, r, c};
                    }
                }
            }
        }
        return null;
    }

    /** Plays the first legal move for the current player and returns it. */
    private int[] playFirstLegalMove() {
        int[] move = findFirstLegalMove();
        assertNotNull("The test deck should let " + model.getCurrentPlayer()
                + " make a move here", move);
        model.placeCardAt(model.getCurrentPlayer(), move[0], move[1], move[2]);
        return move;
    }

    /** Checks that every cell has 0 to 3 pawns, and that any cell with pawns has an owner. */
    private void assertBoardIsValid() {
        for (int r = 0; r < model.getHeight(); r++) {
            for (int c = 0; c < model.getWidth(); c++) {
                PawnCell cell = model.getCellAt(r, c);
                int pawns = cell.getPawnCount().getValue();
                assertTrue("Cell (" + r + ", " + c + ") has " + pawns + " pawns",
                        pawns >= 0 && pawns <= 3);
                if (pawns > 0) {
                    assertNotNull("Cell (" + r + ", " + c + ") has pawns but no owner", cell.getOwner());
                }
            }
        }
    }

    // ---------------------------------------------------------------------------
    // Passing and ending the game
    // ---------------------------------------------------------------------------

    @Test
    public void testSinglePassDoesNotEndGame() {
        model.passTurn();
        assertFalse(model.isGameOver());
        assertEquals(Player.BLUE, model.getCurrentPlayer());
    }

    @Test
    public void testTwoPassesInARowEndGame() {
        model.passTurn(); // Red
        model.passTurn(); // Blue
        assertTrue(model.isGameOver());
    }

    @Test
    public void testPassPlayPassDoesNotEndGame() {
        model.passTurn();       // Red passes
        playFirstLegalMove();   // Blue plays a card, breaking the streak
        model.passTurn();       // Red passes again
        assertFalse(model.isGameOver());
    }

    @Test
    public void testPassesSeparatedByPlaysDoNotEndGame() {
        // The bug this guards against: an early pass by one player combining with a
        // much later pass by the other player to end the game.
        model.passTurn();       // Red passes
        playFirstLegalMove();   // Blue plays
        playFirstLegalMove();   // Red plays
        model.passTurn();       // Blue passes
        assertFalse(model.isGameOver());
        assertEquals(Player.RED, model.getCurrentPlayer());
    }

    @Test
    public void testGameStateCanStillBeReadAfterGameOver() {
        // The bug this guards against: ending the game made every method throw
        // "Game has not started."
        model.passTurn();
        model.passTurn();
        assertTrue(model.isGameOver());

        assertNotNull(model.getWinner());
        assertNotNull(model.getHand(Player.RED));
        assertNotNull(model.getHand(Player.BLUE));
        assertEquals(0, model.checkScore(Player.RED));
        assertEquals(0, model.checkScore(Player.BLUE));
    }

    @Test
    public void testNoCardsPlayedIsATie() {
        model.passTurn();
        model.passTurn();
        assertEquals("Tie", model.getWinner());
    }

    @Test(expected = IllegalStateException.class)
    public void testPassAfterGameOverThrows() {
        model.passTurn();
        model.passTurn();
        model.passTurn();
    }

    @Test(expected = IllegalStateException.class)
    public void testPlaceCardAfterGameOverThrows() {
        model.passTurn();
        model.passTurn();
        model.placeCardAt(model.getCurrentPlayer(), 0, 0, 0);
    }

    // ---------------------------------------------------------------------------
    // Placing cards
    // ---------------------------------------------------------------------------

    @Test
    public void testCannotPlaceOnOpponentCell() {
        PawnCard card = model.getHand(Player.RED).get(0);
        assertFalse(model.placeCardLegal(Player.RED, card, 0, COLS - 1));
    }

    @Test
    public void testCannotPlaceOnEmptyCell() {
        PawnCard card = model.getHand(Player.RED).get(0);
        assertFalse(model.placeCardLegal(Player.RED, card, 0, COLS / 2));
    }

    @Test
    public void testCannotPlaceWithoutEnoughPawns() {
        // Red's starting cells have 1 pawn, so any card costing 2 or more is illegal there.
        List<PawnCard> hand = model.getHand(Player.RED);
        PawnCard expensive = null;
        for (PawnCard card : hand) {
            if (card.getPawnCost().getValue() >= 2) {
                expensive = card;
                break;
            }
        }
        assumeTrue("Red's starting hand has no card costing 2 or more", expensive != null);
        assertFalse(model.placeCardLegal(Player.RED, expensive, 0, 0));
    }

    @Test
    public void testCannotPlaceOnOccupiedCell() {
        int[] move = playFirstLegalMove(); // Red plays
        model.passTurn();                  // Blue passes

        PawnCard card = model.getHand(Player.RED).get(0);
        assertFalse(model.placeCardLegal(Player.RED, card, move[1], move[2]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlacingOnOccupiedCellThrows() {
        int[] move = playFirstLegalMove(); // Red plays
        model.passTurn();                  // Blue passes
        model.placeCardAt(Player.RED, 0, move[1], move[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlacingOutOfTurnThrows() {
        model.placeCardAt(Player.BLUE, 0, 0, COLS - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCardIndexThrows() {
        model.placeCardAt(Player.RED, 99, 0, 0);
    }

    // ---------------------------------------------------------------------------
    // Drawing cards
    // ---------------------------------------------------------------------------

    @Test
    public void testPlayerDrawsAtStartOfTurn() {
        // Red has already drawn for the first turn; Blue has not.
        assertEquals(HAND_SIZE + 1, model.getHand(Player.RED).size());
        assertEquals(HAND_SIZE, model.getHand(Player.BLUE).size());

        int blueDeckBefore = model.getRemainingDeckSize(Player.BLUE);
        model.passTurn();

        assertEquals(HAND_SIZE + 1, model.getHand(Player.BLUE).size());
        assertEquals(blueDeckBefore - 1, model.getRemainingDeckSize(Player.BLUE));
    }

    @Test
    public void testPassingKeepsTheDrawnCard() {
        model.passTurn();       // Red passes with HAND_SIZE + 1 cards
        playFirstLegalMove();   // Blue plays, and Red's next turn starts with another draw
        assertEquals(HAND_SIZE + 2, model.getHand(Player.RED).size());
    }

    // ---------------------------------------------------------------------------
    // Scoring
    // ---------------------------------------------------------------------------

    @Test
    public void testAllRowsScoreZeroAtStart() {
        for (int r = 0; r < ROWS; r++) {
            assertEquals(0, model.checkScoreRow(Player.RED, r));
            assertEquals(0, model.checkScoreRow(Player.BLUE, r));
        }
    }

    @Test
    public void testPlacedCardCountsTowardRowScore() {
        int[] move = findFirstLegalMove();
        assertNotNull(move);
        PawnCard card = model.getHand(Player.RED).get(move[0]);
        model.placeCardAt(Player.RED, move[0], move[1], move[2]);

        int changed = model.getCellAt(move[1], move[2]).getChangedValue();
        int expected = Math.max(card.getValue() + changed, 0);
        assertEquals(expected, model.checkScoreRow(Player.RED, move[1]));
        assertEquals(0, model.checkScoreRow(Player.BLUE, move[1]));
    }

    @Test
    public void testOnlyCardOnBoardWinsTheGame() {
        int[] move = playFirstLegalMove(); // Red plays
        assumeTrue("The card Red played has no score",
                model.checkScoreRow(Player.RED, move[1]) > 0);

        model.passTurn(); // Blue
        model.passTurn(); // Red
        assertTrue(model.isGameOver());
        assertEquals(Player.RED.toString(), model.getWinner());
    }

    // ---------------------------------------------------------------------------
    // Full games and the AI
    // ---------------------------------------------------------------------------

    @Test
    public void testFullGameEndsAndKeepsBoardValid() {
        // Both players always play the first legal move, and pass when they have none.
        for (int turn = 0; turn < MAX_TURNS && !model.isGameOver(); turn++) {
            int[] move = findFirstLegalMove();
            if (move == null) {
                model.passTurn();
            } else {
                model.placeCardAt(model.getCurrentPlayer(), move[0], move[1], move[2]);
            }
            assertBoardIsValid();
        }
        assertTrue("The game should end within " + MAX_TURNS + " turns", model.isGameOver());
    }

    @Test
    public void testMachinePlayerAlwaysEndsItsTurn() {
        // The bug this guards against: the AI doing nothing on its turn, freezing the game.
        MachinePlayer blueAi = new MachinePlayer(Player.BLUE, model, new FillFirstStrategy());

        for (int turn = 0; turn < MAX_TURNS && !model.isGameOver(); turn++) {
            Player before = model.getCurrentPlayer();

            if (before == Player.RED) {
                int[] move = findFirstLegalMove();
                if (move == null) {
                    model.passTurn();
                } else {
                    model.placeCardAt(Player.RED, move[0], move[1], move[2]);
                }
            } else {
                blueAi.makeMove();
            }

            if (!model.isGameOver()) {
                assertTrue("The turn should move on after " + before + " acts",
                        model.getCurrentPlayer() != before);
            }
        }
        assertTrue("The game should end within " + MAX_TURNS + " turns", model.isGameOver());
    }
}