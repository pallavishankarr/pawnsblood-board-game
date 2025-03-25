package pawnboard;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pawnboard.model.Card;
import pawnboard.controller.CardInitializer;
import pawnboard.model.Cell;
import pawnboard.model.Cost;
import pawnboard.model.InfluenceType;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.strategy.FillFirstStrategy;
import pawnboard.strategy.MaximizeRowScoreStrategy;
import pawnboard.strategy.PawnBoardStrategy;
import pawnboard.view.GameView;
import pawnboard.view.PawnGameTextualView;
import pawnboard.strategy.Move;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;


/**
 * Tests for the PawnsGame Implementation.
 */
public class PawnGameTest {

  String path = "docs" + File.separator + "deck.config";
  String biggerPath = "docs" + File.separator + "biggerDeck";
  String tripleFile = "docs" + File.separator + "tripleFile";
  private Player player;
  private PawnsGame<PawnCell> game;
  private MockPawnGame mockGame;
  private FillFirstStrategy fillFirstStrat;
  private MaximizeRowScoreStrategy maxRowStrat;
  private Appendable output;
  private InfluenceType[][] influenceGrid;
  private CardInitializer cardInitializer= new CardInitializer();
  Map<String, List<PawnCard>> decks = new HashMap<>();
  Map<String, List<PawnCard>> decks2 = new HashMap<>();
  List<PawnCard> deck = new ArrayList<>();
  List<PawnCard> deck2 = new ArrayList<>();




  @Before
  public void setUp() {
    output = new StringBuilder();
    mockGame = new MockPawnGame(output);
    fillFirstStrat = new FillFirstStrategy();
    maxRowStrat = new MaximizeRowScoreStrategy();
    influenceGrid = new InfluenceType[][]{
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I, InfluenceType.X},
            {InfluenceType.I, InfluenceType.I, InfluenceType.C, InfluenceType.I, InfluenceType.I},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X},
    };
    decks = cardInitializer.initializeDeck(path, path);
    deck = decks.get("deck1");
    decks2 = cardInitializer.initializeDeck(biggerPath, biggerPath);
    deck2 = decks2.get("deck1");


  }

  private static void passTurn(PawnGameImpl game, Player player) {
    game.startTurn(player);
    game.passTurn();
  }

  //test for Valid Constructor of the PawnsGame Class
  @Test
  public void testValidConstructor() {
    this.game = new PawnGameImpl(3, 5);
    assertEquals(3, this.game.getHeight());
    assertEquals(5, this.game.getWidth());
  }

  //test for Invalid Constructor of the PawnGame Class
  @Test
  public void testInvalidConstructor() {

    //Throws due to column value being even
    assertThrows(IllegalArgumentException.class, () -> new PawnGameImpl(3, 6));

    //Throws due to row being 0
    assertThrows(IllegalArgumentException.class, () -> new PawnGameImpl(0, 5));

    //Throws due to column being 1
    assertThrows(IllegalArgumentException.class, () -> new PawnGameImpl(3, 1));


  }

  //test Valid Constructor for Bigger Board
  @Test
  public void testValidConstructorForLargerBoard() {
    game = new PawnGameImpl(11, 9);
    assertEquals(11, this.game.getHeight());
    assertEquals(9, this.game.getWidth());

  }

  //test Error thrown when there are triples of card
  @Test
  public void testInvalidCardDeck() {

    Map<String, List<PawnCard>> decks3 = cardInitializer.initializeDeck(tripleFile, tripleFile);
    List<PawnCard> deck = decks3.get("deck1");


    game = new PawnGameImpl(3, 5);
    assertThrows(IllegalArgumentException.class, () -> game.startGame(deck,deck, 5));


  }

  //test for starting the Game
  @Test
  public void testLegalStartGame() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);
    assertEquals(3, game.getHeight());
    assertEquals(5, game.getWidth());

    //tests that both players are dealt with an equal number of cards
    assertEquals(5, game.getHand(Player.RED).size());
    assertEquals(5, game.getHand(Player.BLUE).size());

  }

  //tests that the Board is fully constructed
  // and that pawns are added to correct cells
  @Test
  public void testLegalStartGameConstructsBoard() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);

    //test that the first column has one pawn count for Red Pawn
    Cell cell = game.getCellAt(0, 0);
    Cell cell2 = game.getCellAt(1, 0);
    Cell cell3 = game.getCellAt(2, 0);

    Cell cell7 = game.getCellAt(0, 1);
    assertEquals(0, cell7.getPawnCount().getValue());


    assertEquals(1, cell.getPawnCount().getValue());
    assertEquals(Player.RED, cell.getOwner());
    assertEquals(1, cell2.getPawnCount().getValue());
    assertEquals(Player.RED, cell2.getOwner());
    assertEquals(1, cell3.getPawnCount().getValue());
    assertEquals(Player.RED, cell3.getOwner());

    //tests that the last column has one pawn count for Blue Pawn
    //test that the first column has one pawn count for Red Pawn
    Cell cell4 = game.getCellAt(0, 4);
    Cell cell5 = game.getCellAt(1, 4);
    Cell cell6 = game.getCellAt(2, 4);


    assertEquals(1, cell4.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell4.getOwner());
    assertEquals(1, cell5.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell5.getOwner());
    assertEquals(1, cell6.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell6.getOwner());
  }

  //tests that there are no cards on the board
  @Test
  public void testNoCardsOnStartOfGame() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 5; j++) {
        Cell cell = game.getCellAt(i, j);
        assertFalse(cell.hasCard());
      }
    }


  }

  //test that hand is less than 1/3 of the deck size
  @Test
  public void testLegalStartGameHandSize() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);

    int deckSize = game.getRemainingDeckSize(Player.RED);
    int deckSize2 = game.getRemainingDeckSize(Player.BLUE);
    int hand = game.getHand(Player.RED).size();
    int hand2 = game.getHand(Player.BLUE).size();

    assertFalse(deckSize / 3 > hand);
    assertFalse(deckSize2 / 3 > hand2);
  }

  //test that the correct Illegal Argument Exceptions are thrown
  @Test
  public void testIllegalStartGame() {
    String path2 = "CardList";

    Map<String, List<PawnCard>> decks3 = cardInitializer.initializeDeck(biggerPath, biggerPath);
    List<PawnCard> deck4 = decks3.get("deck1");


    game = new PawnGameImpl(5, 7);

    //Throws because of path should create null decks
    assertThrows(IllegalArgumentException.class, () -> game.startGame(deck2,deck2, 5));

    //Throws because hand Size is greater  than 1/3
    assertThrows(IllegalArgumentException.class, () -> game.startGame(deck4, deck4, 15));

    //Throws because not enough cards to fill up board
    assertThrows(IllegalArgumentException.class, () -> game.startGame(deck,deck, 7));


  }

  //test for getting the width of the game
  @Test
  public void testGetWidthPawnGame() {
    this.game = new PawnGameImpl(3, 5);
    assertEquals(5, this.game.getWidth());

  }

  //test for getting the height of the game
  @Test
  public void testGetHeightPawnGame() {
    this.game = new PawnGameImpl(3, 5);
    assertEquals(3, this.game.getHeight());

  }

  //tests the get owner at
  @Test
  public void testGetOwnerAt() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);
    //Gets the owner when there are pawns on the cell
    assertEquals(Player.RED, game.getOwnerAt(0, 0));
    assertEquals(Player.BLUE, game.getOwnerAt(0, 4));

    //Adds a pawn on a cell and gets the owner
    Cell cell = game.getCellAt(1, 0);
    cell.addPawn(Player.RED);
    assertEquals(Player.RED, game.getOwnerAt(1, 0));

    //places a card and gets the owner
    game.placeCardAt(Player.RED, 0, 0, 0);
    assertEquals(Player.RED, game.getOwnerAt(0, 0));

    assertNull(game.getOwnerAt(0,2));
  }

  //tests no card left to draw
  @Test
  public void testNoCardLeftToDraw() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.passTurn();

    assertEquals(0, game.getRemainingDeckSize(Player.BLUE));
    assertEquals(0, game.getRemainingDeckSize(Player.RED));

    assertEquals(20, game.getHand(Player.BLUE).size());
    assertEquals(20, game.getHand(Player.RED).size());
  }

  //test that Opposing PLayer
  // cannot make move when their turn hasn't started
  @Test
  public void testCannotMakeMove() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);

    //throws when Opposing player places Card
    assertThrows(IllegalArgumentException.class, () ->
            game.placeCardAt(Player.BLUE, 0, 0, 0));

    //throws when Opposing player tries to pass turn
    assertThrows(IllegalArgumentException.class, () ->
            game.passTurn());

  }

  //test that turn ends when Player's turn is over
  @Test
  public void testTurnEnds() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);

    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 0, 0);
    assertEquals(Player.BLUE, game.getCurrentPlayer());

  }

  //test for legally placing a Red Card At
  @Test
  public void testRedCardLegalPlaceCardAt() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);

    //adds pawn so that the card can be placed
    Cell cell = game.getCellAt(0, 0);

    //card that will be placed
    Card card = game.getHand(Player.RED).get(0);

    //places card
    game.placeCardAt(Player.RED, 0, 0, 0);

    //checks that the card has been placed and a new card has been picked up
    assertNotEquals(card, game.getHand(Player.RED).get(0));

    //Gets the card at the cell at row : 0 col : 0
    Cell cell2 = game.getCellAt(0, 0);
    PawnCard card2 = (PawnCard) cell2.getCard();

    Cell cell3 = game.getCellAt(0, 1);
    Cell cell4 = game.getCellAt(1, 0);

    //checks that the card has been placed on the cell
    assertEquals(card, card2);
    //checks that ownership has been changed to Red
    assertEquals(Player.RED, cell.getOwner());

    //makes sure that the influence has changed for the specific cells
    assertEquals(1, cell3.getPawnCount().getValue());
    assertEquals(2, cell4.getPawnCount().getValue());


  }

  //test for legally placing a Blue Card At
  @Test
  public void testBlueCardLegalPlaceCardAt() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);

    //adds pawn so that the card can be placed
    Cell cell = game.getCellAt(0, 4);

    game.passTurn();
    game.startTurn(Player.BLUE);


    //card that will be placed
    Card card = game.getHand(Player.BLUE).get(0);

    //places card
    game.placeCardAt(Player.BLUE, 0, 0, 4);

    //checks that the card has been placed and a new card has been picked up
    assertNotEquals(card, game.getHand(Player.BLUE).get(0));

    //Gets the card at the cell at row : 0 col : 0
    Cell cell2 = game.getCellAt(0, 4);
    PawnCard card2 = (PawnCard) cell2.getCard();

    Cell cell3 = game.getCellAt(2, 4);
    Cell cell4 = game.getCellAt(1, 4);

    //checks that the card has been placed on the cell
    assertEquals(card, card2);
    //checks that ownership has been changed to Blue
    assertEquals(Player.BLUE, cell.getOwner());

    //makes sure that the influence has changed for the specific cells
    assertEquals(1, cell3.getPawnCount().getValue());
    assertEquals(2, cell4.getPawnCount().getValue());

  }

  //test PLace Card Illegal
  @Test
  public void testPLaceCardIllegal() {
    game = new PawnGameImpl(3, 5);

    assertThrows(IllegalStateException.class, ()
        -> game.placeCardAt(Player.BLUE, 0, 0, 0));

    game.startGame(deck,deck, 5);

    //Not person's turn
    assertThrows(IllegalArgumentException.class, ()
        -> game.placeCardAt(Player.BLUE, 0, 0, 0));

    //Don't have right pawns
    assertThrows(IllegalArgumentException.class, ()
        -> game.placeCardAt(Player.RED, 0, 0, 4));

    //No pawns on
    assertThrows(IllegalArgumentException.class, ()
        -> game.placeCardAt(Player.RED, 0, 1, 2));

    //Out of bounds
    assertThrows(IllegalArgumentException.class, ()
        -> game.placeCardAt(Player.RED, 0, 3, 5));

    //Out of bounds for hand size
    assertThrows(IllegalArgumentException.class, ()
        -> game.placeCardAt(Player.RED, 5, 0, 0));

    game.placeCardAt(Player.RED, 0, 0, 0);

    assertThrows(IllegalStateException.class, ()
        -> game.placeCardAt(Player.RED, 0, 0, 0));
  }

  //test place card for every edge
  @Test
  public void testPlaceCardAtEdgesRed() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);

    Cell cell = game.getCellAt(0, 0);
    Cell cell2 = game.getCellAt(1, 0);
    Cell cell3 = game.getCellAt(2, 0);


    Card card = game.getHand(Player.RED).get(0);
    Card card2 = game.getHand(Player.RED).get(1);
    Card card3 = game.getHand(Player.RED).get(2);

    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 0, 0);

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 1, 0);

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 2, 0);

    assertEquals(card, cell.getCard());
    assertEquals(Player.RED, cell.getOwner());

    assertEquals(card2, cell2.getCard());
    assertEquals(Player.RED, cell2.getOwner());

    assertEquals(card3, cell3.getCard());
    assertEquals(Player.RED, cell3.getOwner());

  }


  //test place Card Legal
  @Test
  public void testPlaceCardLegal() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);

    Card card = game.getHand(Player.RED).get(0);

    assertTrue(game.placeCardLegal(Player.RED, card, 0, 0));
    assertFalse(game.placeCardLegal(Player.BLUE,card, 0, 1));



  }

  //test place card At Edges for Blue
  @Test
  public void testPlaceCardAtEdgesBlue() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);

    Cell cell = game.getCellAt(0, 4);
    Cell cell2 = game.getCellAt(1, 4);
    Cell cell3 = game.getCellAt(2, 4);


    Card card = game.getHand(Player.BLUE).get(0);
    Card card2 = game.getHand(Player.BLUE).get(1);
    Card card3 = game.getHand(Player.BLUE).get(2);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 4);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 1, 4);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 2, 4);

    assertEquals(card, cell.getCard());
    assertEquals(Player.BLUE, cell.getOwner());

    assertEquals(card2, cell2.getCard());
    assertEquals(Player.BLUE, cell2.getOwner());

    assertEquals(card3, cell3.getCard());
    assertEquals(Player.BLUE, cell3.getOwner());

  }

  //test place card At Edges for Red on BiggerBoard
  @Test
  public void testPlaceCardAtEdgesRedBigger() {
    this.game = new PawnGameImpl(5, 7);
    Map<String, List<PawnCard>> decks3 = cardInitializer.initializeDeck(biggerPath, biggerPath);
    List<PawnCard> deck4 = decks3.get("deck1");

    game.startGame(deck4, deck4, 5);

    Cell cell = game.getCellAt(0, 0);
    Cell cell2 = game.getCellAt(1, 0);
    Cell cell3 = game.getCellAt(2, 0);
    Cell cell4 = game.getCellAt(3, 0);
    Cell cell5 = game.getCellAt(4, 0);


    Card card = game.getHand(Player.RED).get(0);
    Card card2 = game.getHand(Player.RED).get(1);
    Card card3 = game.getHand(Player.RED).get(2);
    Card card4 = game.getHand(Player.RED).get(3);
    Card card5 = game.getHand(Player.RED).get(4);


    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 0, 0);

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 1, 0);

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 2, 0);

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 3, 0);

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.startTurn(Player.RED);
    cell5.addPawn(Player.RED);
    game.placeCardAt(Player.RED, 0, 4, 0);

    assertEquals(card, cell.getCard());
    assertEquals(Player.RED, cell.getOwner());

    assertEquals(card2, cell2.getCard());
    assertEquals(Player.RED, cell2.getOwner());

    assertEquals(card3, cell3.getCard());
    assertEquals(Player.RED, cell3.getOwner());

    assertEquals(card4, cell4.getCard());
    assertEquals(Player.RED, cell4.getOwner());

    assertEquals(card5, cell5.getCard());
    assertEquals(Player.RED, cell5.getOwner());

  }

  //test place card At Edges for Blue on BiggerBoard
  @Test
  public void testPlaceCardAtEdgesBlueBigger() {
    this.game = new PawnGameImpl(5, 7);
    Map<String, List<PawnCard>> decks3 = cardInitializer.initializeDeck(biggerPath, biggerPath);
    List<PawnCard> deck4 = decks3.get("deck1");
    game.startGame(deck4, deck4, 5);

    Cell cell = game.getCellAt(0, 6);
    Cell cell2 = game.getCellAt(1, 6);
    Cell cell3 = game.getCellAt(2, 6);
    Cell cell4 = game.getCellAt(3, 6);
    Cell cell5 = game.getCellAt(4, 6);


    Card card = game.getHand(Player.BLUE).get(0);
    Card card2 = game.getHand(Player.BLUE).get(1);
    Card card3 = game.getHand(Player.BLUE).get(2);
    Card card4 = game.getHand(Player.BLUE).get(3);
    Card card5 = game.getHand(Player.BLUE).get(4);


    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 6);


    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);

    game.placeCardAt(Player.BLUE, 0, 1, 6);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 2, 6);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 3, 6);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    cell5.addPawn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 4, 6);

    assertEquals(card, cell.getCard());
    assertEquals(Player.BLUE, cell.getOwner());

    assertEquals(card2, cell2.getCard());
    assertEquals(Player.BLUE, cell2.getOwner());

    assertEquals(card3, cell3.getCard());
    assertEquals(Player.BLUE, cell3.getOwner());

    assertEquals(card4, cell4.getCard());
    assertEquals(Player.BLUE, cell4.getOwner());

    assertEquals(card5, cell5.getCard());
    assertEquals(Player.BLUE, cell5.getOwner());

  }

  //test for Place Red Card at with a cell with no pawns
  @Test
  public void testRedPlaceCardAtWithNoPawns() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);

    Cell cell = game.getCellAt(2, 2);


    //Pawn Cost not met for placing the card
    assertThrows(IllegalArgumentException.class, () ->
            game.placeCardAt(Player.RED, 2, 2, 2));
  }

  //test for Place Blue Card at with a cell with no pawns
  @Test
  public void testBluePlaceCardAtWithNoPawns() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);

    game.passTurn();
    game.startTurn(Player.BLUE);

    //Pawn Cost not met for placing the card
    assertThrows(IllegalArgumentException.class, () ->
            game.placeCardAt(Player.BLUE, 0, 1, 2));
  }

  //test for Place Red Card at with a cell with not enough pawns
  @Test
  public void testRedPlaceCardAtWithNotEnoughPawns() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);

    Cell cell = game.getCellAt(0, 0);
    Card card = game.getHand(Player.RED).get(3);

    assertNotEquals(cell.getPawnCount().getValue(), card.getPawnCost().getValue());

    assertThrows(IllegalArgumentException.class, () ->
            game.placeCardAt(Player.BLUE, 3, 0, 0));

  }

  //test for Place Blue Card at cell with not enough pawns
  @Test
  public void testBluePlaceCardAtWithNotEnoughPawns() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);

    Cell cell = game.getCellAt(0, 4);
    Card card = game.getHand(Player.RED).get(3);

    assertNotEquals(cell.getPawnCount().getValue(), card.getPawnCost().getValue());

    assertThrows(IllegalArgumentException.class, () ->
            game.placeCardAt(Player.BLUE, 3, 0, 4));
  }

  //test for Place Red Card at with a cell with Blue Pawns
  @Test
  public void testRedPlaceCardAtWithBluePawns() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);

    assertThrows(IllegalArgumentException.class, () ->
            game.placeCardAt(Player.RED, 0, 0, 4));
  }

  //test for Place Blue Card at with a cell with Red Pawns
  @Test
  public void testBluePlaceCardAtWithRedPawns() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);

    game.passTurn();
    game.startTurn(Player.BLUE);

    assertThrows(IllegalArgumentException.class, () ->
            game.placeCardAt(Player.BLUE, 0, 0, 0));
  }

  //test for Red Card with more than enough Red Pawns
  @Test
  public void testRedCardWithMoreThanEnoughPawns() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);

    //adds pawn so that the card can be placed
    Cell cell = game.getCellAt(0, 0);


    //card that will be placed
    Card card = game.getHand(Player.RED).get(0);

    //places card
    game.placeCardAt(Player.RED, 0, 0, 0);

    //checks that the card has been placed and a new card has been picked up
    assertNotEquals(card, game.getHand(Player.RED).get(0));

    //Gets the card at the cell at row : 0 col : 0
    Cell cell2 = game.getCellAt(0, 0);
    PawnCard card2 = (PawnCard) cell2.getCard();

    Cell cell3 = game.getCellAt(0, 1);
    Cell cell4 = game.getCellAt(1, 0);

    //checks that the card has been placed on the cell
    assertEquals(card, card2);
    //checks that ownership has been changed to Red
    assertEquals(Player.RED, cell.getOwner());

    //makes sure that the influence has changed for the specific cells
    assertEquals(1, cell3.getPawnCount().getValue());
    assertEquals(2, cell4.getPawnCount().getValue());
  }

  //test for Red Card with more than enough Red Pawns
  @Test
  public void testBlueCardWithMoreThanEnoughPawns() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);

    //adds pawn so that the card can be placed
    Cell cell = game.getCellAt(0, 4);


    //card that will be placed
    Card card = game.getHand(Player.BLUE).get(0);

    //places card
    game.placeCardAt(Player.BLUE, 0, 0, 4);

    //checks that the card has been placed and a new card has been picked up
    assertNotEquals(card, game.getHand(Player.BLUE).get(0));

    //Gets the card at the cell at row : 0 col : 0
    Cell cell2 = game.getCellAt(0, 4);
    PawnCard card2 = (PawnCard) cell2.getCard();

    Cell cell3 = game.getCellAt(2, 4);
    Cell cell4 = game.getCellAt(1, 4);

    //checks that the card has been placed on the cell
    assertEquals(card, card2);
    //checks that ownership has been changed to Red
    assertEquals(Player.BLUE, cell.getOwner());

    //makes sure that the influence has changed for the specific cells
    assertEquals(1, cell3.getPawnCount().getValue());
    assertEquals(2, cell4.getPawnCount().getValue());

  }

  //test Apply Influence properly works when placing a Red Card
  @Test
  public void testApplyInfluenceRedCardPlaced() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);


    //Cells that should be affected after placing card
    Cell cell = game.getCellAt(0, 1);
    Cell cell2 = game.getCellAt(1, 0);

    //Cells that shouldn't be affected
    Cell cell3 = game.getCellAt(0, 4);


    //adds pawn for Blue to
    cell.addPawn(Player.BLUE);

    assertEquals(1, cell.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell.getOwner());

    assertEquals(1, cell2.getPawnCount().getValue());
    assertEquals(Player.RED, cell2.getOwner());

    assertEquals(1, cell3.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell3.getOwner());


    game.placeCardAt(Player.RED, 0, 0, 0);

    //checks that the cell ownership has changed and only one pawn on cell
    assertEquals(1, cell.getPawnCount().getValue());
    assertEquals(Player.RED, cell.getOwner());

    //checks that one pawn has been added to the cell and the ownership stays the same
    assertEquals(2, cell2.getPawnCount().getValue());
    assertEquals(Player.RED, cell2.getOwner());

    //checks that Cell has not changed after place Card
    assertEquals(1, cell3.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell3.getOwner());
  }

  //test Apply Influence properly works when placing a Blue Card
  @Test
  public void testApplyInfluenceBlueCardPlaced() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);


    //Cells that should be affected after placing card
    Cell cell = game.getCellAt(1, 4);
    Cell cell2 = game.getCellAt(0, 3);

    assertEquals(1, cell.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell.getOwner());

    assertEquals(0, cell2.getPawnCount().getValue());

    //Cells that shouldn't be affected
    Cell cell3 = game.getCellAt(2, 4);

    assertEquals(1, cell.getPawnCount().getValue());

    cell2.addPawn(Player.RED);

    assertEquals(1, cell2.getPawnCount().getValue());

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 4);

    assertEquals(2, cell.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell.getOwner());

    assertEquals(1, cell2.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell2.getOwner());

    assertEquals(1, cell3.getPawnCount().getValue());

  }

  //test Place Card Apply Influence Bigger Board for Red
  @Test
  public void testPlaceCardApplyInfluenceBiggerRed() {
    game = new PawnGameImpl(5, 7);
    Map<String, List<PawnCard>> decks3 = cardInitializer.initializeDeck(biggerPath, biggerPath);
    List<PawnCard> deck4 = decks3.get("deck1");
    game.startGame(deck4, deck4, 5);


    //Cells that should be affected after placing card
    Cell cell = game.getCellAt(0, 0);
    Cell cell2 = game.getCellAt(2, 0);
    Cell cell3 = game.getCellAt(1, 1);

    //Cells that shouldn't be affected
    Cell cell4 = game.getCellAt(0, 4);


    //adds pawn for Blue to
    cell3.addPawn(Player.BLUE);

    assertEquals(1, cell.getPawnCount().getValue());
    assertEquals(Player.RED, cell.getOwner());

    assertEquals(1, cell2.getPawnCount().getValue());
    assertEquals(Player.RED, cell2.getOwner());

    assertEquals(1, cell3.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell3.getOwner());

    assertEquals(0, cell4.getPawnCount().getValue());


    game.placeCardAt(Player.RED, 0, 1, 0);

    //checks that the cell ownership has changed and only one pawn on cell
    assertEquals(2, cell.getPawnCount().getValue());
    assertEquals(Player.RED, cell.getOwner());

    //checks that one pawn has been added to the cell and the ownership stays the same
    assertEquals(2, cell2.getPawnCount().getValue());
    assertEquals(Player.RED, cell2.getOwner());

    //checks that Cell has  changed after place Card
    assertEquals(1, cell3.getPawnCount().getValue());
    assertEquals(Player.RED, cell3.getOwner());

    //checks that Cell has not changed
    assertEquals(0, cell4.getPawnCount().getValue());

  }

  //test Apply Influence properly works when placing a Blue Card
  //on  a Bigger Board
  @Test
  public void testApplyInfluenceBlueCardPlacedBigger() {
    game = new PawnGameImpl(5, 7);
    Map<String, List<PawnCard>> decks3 = cardInitializer.initializeDeck(biggerPath, biggerPath);
    List<PawnCard> deck4 = decks3.get("deck1");
    game.startGame(deck4, deck4, 5);


    //Cells that should be affected after placing card
    Cell cell = game.getCellAt(0, 6);
    Cell cell2 = game.getCellAt(2, 6);
    Cell cell3 = game.getCellAt(1, 5);

    assertEquals(1, cell.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell.getOwner());

    assertEquals(1, cell2.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell2.getOwner());

    assertEquals(0, cell3.getPawnCount().getValue());


    //Cells that shouldn't be affected
    Cell cell4 = game.getCellAt(2, 4);

    assertEquals(0, cell4.getPawnCount().getValue());


    cell2.addPawn(Player.RED);

    assertEquals(1, cell2.getPawnCount().getValue());

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 1, 6);

    assertEquals(2, cell.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell.getOwner());

    assertEquals(2, cell2.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell2.getOwner());

    assertEquals(1, cell3.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell3.getOwner());

    assertEquals(0, cell4.getPawnCount().getValue());

  }

  //Place CArd Doesn't influence with 3 pawns on it
  @Test
  public void testApplyInfluenceWithThreePawns() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);


    //Cells that should be affected after placing card
    Cell cell = game.getCellAt(0, 1);
    Cell cell2 = game.getCellAt(1, 0);

    //Cells that shouldn't be affected
    Cell cell3 = game.getCellAt(0, 4);


    //adds pawn for Red to
    cell2.addPawn(Player.RED);
    cell2.addPawn(Player.RED);

    assertEquals(0, cell.getPawnCount().getValue());


    assertEquals(3, cell2.getPawnCount().getValue());
    assertEquals(Player.RED, cell2.getOwner());

    assertEquals(1, cell3.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell3.getOwner());


    game.placeCardAt(Player.RED, 0, 0, 0);

    //checks that the cell ownership has changed and only one pawn on cell
    assertEquals(1, cell.getPawnCount().getValue());
    assertEquals(Player.RED, cell.getOwner());

    //checks that one pawn has been added to the cell and the ownership stays the same
    assertEquals(3, cell2.getPawnCount().getValue());
    assertEquals(Player.RED, cell2.getOwner());

    //checks that Cell has not changed after place Card
    assertEquals(1, cell3.getPawnCount().getValue());
    assertEquals(Player.BLUE, cell3.getOwner());


  }

  //test PlaceCArd when a Card has already been placed
  @Test
  public void testPlaceCardAlready() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);

    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 0, 0);

    game.startTurn(Player.BLUE);
    game.passTurn();


    game.startTurn(Player.RED);


    assertThrows(IllegalArgumentException.class,
        () -> game.placeCardAt(Player.RED, 0, 0, 0));
  }

  //test Place Card when there is opposing Card that has been placed
  @Test
  public void testPlaceCardAlreadyHasOpposing() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);

    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 0, 0);
    game.startTurn(Player.BLUE);
    assertThrows(IllegalArgumentException.class, ()
        -> game.placeCardAt(Player.BLUE, 0, 0, 0));
  }

  //test for Pass Turn through the game
  @Test
  public void testPassTurnGame() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);
    assertEquals(Player.RED, game.getCurrentPlayer());
    game.startTurn(Player.RED);
    game.passTurn();
    assertEquals(Player.BLUE, game.getCurrentPlayer());
    game.startTurn(Player.BLUE);
    game.passTurn();
    assertEquals(Player.RED, game.getCurrentPlayer());

  }

  //test for legal draw Card
  @Test
  public void testLegalDrawCard() {

    game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);

    //Checks the deck size
    assertEquals(15, game.getRemainingDeckSize(Player.RED));

    int handSizeBefore = game.getHand(Player.RED).size();
    game.startTurn(Player.RED);
    int handSizeAfter = game.getHand(Player.RED).size();

    //card immediatley drawn on turn
    assertEquals(5, handSizeBefore);

    //Makes sure that the card is added to hand
    assertEquals(6, handSizeAfter);

    //Makes sure that the deck size has been reduced by one card
    assertEquals(14, game.getRemainingDeckSize(Player.RED));
  }

  //test legal get Cell At
  @Test
  public void testLegalGetCellAt() {

    game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);

    // Test a valid cell retrieval
    Cell cell = game.getCellAt(1, 1);

    // Ensure the cell is not null
    assertNotNull(cell);

  }

  //test Illegal get Cell At
  @Test
  public void testIllegalGetCellAt() {

    game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);


    assertThrows(IllegalArgumentException.class, () -> game.getCellAt(-1, 0));
    assertThrows(IllegalArgumentException.class, () -> game.getCellAt(0, -1));
    assertThrows(IllegalArgumentException.class, () -> game.getCellAt(3, 0));
    assertThrows(IllegalArgumentException.class, () -> game.getCellAt(0, 5));
    assertThrows(IllegalArgumentException.class, () -> game.getCellAt(3, 5));

  }

  //test get legal current player
  @Test
  public void testLegalGetCurrentPlayer() {

    //test the current player which should be Red Player at start of game
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);
    assertEquals(Player.RED, this.game.getCurrentPlayer());

    //Passes the turn so that the player changes
    this.game.passTurn();

    //tests that the turn has been efficiently passed
    assertEquals(Player.BLUE, this.game.getCurrentPlayer());

  }

  //test get Winner
  @Test
  public void testGetWinner() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);
    game.placeCardAt(Player.RED, 0, 0, 0);

    assertEquals("Red", game.getWinner());
  }

  //test get Winner
  @Test
  public void testGetWinnerBlue() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);
    game.startTurn(Player.RED);
    game.passTurn();
    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 4);


    assertEquals("Blue", game.getWinner());

  }

  //test for a tie
  @Test
  public void testResultsInTie() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);
    game.placeCardAt(Player.RED, 0, 0, 0);
    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 4);


    assertEquals("Tie", game.getWinner());


  }

  //test illegal if game is over
  @Test
  public void testIllegalGetWinner() {
    this.game = new PawnGameImpl(3, 5);
    PawnGameTextualView view = new PawnGameTextualView(game);

    assertThrows(IllegalStateException.class, () -> game.getWinner());

  }

  //test checkScore
  @Test
  public void testCheckScore() {

    game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);


    Cell cell = game.getCellAt(0, 0);
    Cell cell2 = game.getCellAt(0, 1);
    Cell cell3 = game.getCellAt(0, 2);
    Cell cell4 = game.getCellAt(0, 3);
    Cell cell5 = game.getCellAt(0, 4);


    cell2.changeOwnership(Player.BLUE);
    cell3.addPawn(Player.BLUE);

    cell4.changeOwnership(Player.BLUE);

    cell5.changeOwnership(Player.RED);


    game.placeCardAt(Player.RED, 0, 0, 0);

    game.startTurn(Player.BLUE);
    game.passTurn();
    game.startTurn(Player.RED);

    game.placeCardAt(Player.RED, 0, 0, 1);


    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 2);


    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 3);

    game.startTurn(Player.RED);

    game.placeCardAt(Player.RED, 0, 0, 4);

    assertEquals(5, game.checkScore(Player.RED));

    //0 because the score only  gets added if row winner
    assertEquals(0, game.checkScore(Player.BLUE));

  }

  //test check Score Row For
  @Test
  public void testLegalCheckScoreRow() {

    game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);


    Cell cell = game.getCellAt(0, 0);
    Cell cell2 = game.getCellAt(0, 1);
    Cell cell3 = game.getCellAt(0, 2);
    Cell cell4 = game.getCellAt(0, 3);
    Cell cell5 = game.getCellAt(0, 4);


    cell2.changeOwnership(Player.BLUE);
    cell3.addPawn(Player.BLUE);

    cell4.changeOwnership(Player.BLUE);

    cell5.changeOwnership(Player.RED);


    game.placeCardAt(Player.RED, 0, 0, 0);

    game.startTurn(Player.BLUE);
    game.passTurn();
    game.startTurn(Player.RED);

    game.placeCardAt(Player.RED, 0, 0, 1);


    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 2);


    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 3);

    game.startTurn(Player.RED);

    game.placeCardAt(Player.RED, 0, 0, 4);

    assertEquals(5, game.checkScoreRow(Player.RED, 0));
    assertEquals(3, game.checkScoreRow(Player.BLUE, 0));
  }

  //test checkScore for Blue
  @Test
  public void testCheckScoreBlue() {

    game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);


    Cell cell = game.getCellAt(0, 0);
    Cell cell2 = game.getCellAt(0, 1);
    Cell cell3 = game.getCellAt(0, 2);
    Cell cell4 = game.getCellAt(0, 3);
    Cell cell5 = game.getCellAt(0, 4);


    cell3.addPawn(Player.BLUE);
    cell4.addPawn(Player.BLUE);
    cell4.addPawn(Player.BLUE);
    cell5.changeOwnership(Player.BLUE);
    cell5.addPawn(Player.BLUE);


    cell.changeOwnership(Player.BLUE);


    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 0);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 1);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 2);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);

    game.placeCardAt(Player.BLUE, 0, 0, 3);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 4);

    assertEquals(9, game.checkScore(Player.BLUE));

  }

  //test Check Score Row
  @Test
  public void testCheckScoreRowBlue() {

    game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);


    Cell cell = game.getCellAt(0, 0);
    Cell cell2 = game.getCellAt(0, 1);
    Cell cell3 = game.getCellAt(0, 2);
    Cell cell4 = game.getCellAt(0, 3);
    Cell cell5 = game.getCellAt(0, 4);


    cell3.addPawn(Player.BLUE);
    cell4.addPawn(Player.BLUE);
    cell4.addPawn(Player.BLUE);
    cell5.changeOwnership(Player.BLUE);
    cell5.addPawn(Player.BLUE);


    cell.changeOwnership(Player.BLUE);


    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 0);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 1);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 2);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);

    game.placeCardAt(Player.BLUE, 0, 0, 3);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 4);

    assertEquals(9, game.checkScoreRow(Player.BLUE, 0));

  }

  //test check Score Row with empty row
  @Test
  public void testCheckScoreRowEmpty() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);

    assertEquals(0, game.checkScoreRow(Player.BLUE, 0));
    assertEquals(0, game.checkScoreRow(Player.RED, 0));

  }

  //check Score when game is over
  @Test
  public void testCheckScoreWithEmptyBoard() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);

    assertEquals(0, game.checkScore(Player.BLUE));
    assertEquals(0, game.checkScore(Player.RED));

    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 0, 0);

    assertEquals(0, game.checkScore(Player.BLUE));
    assertEquals(2, game.checkScore(Player.RED));
  }

  @Test
  public void testRowWinnerBlue() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);
    PawnGameTextualView view = new PawnGameTextualView(game);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 4);

    game.startTurn(Player.RED);
    game.passTurn();

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 3);

    game.startTurn(Player.RED);
    game.passTurn();

    game.getCellAt(0, 2).addPawn(Player.BLUE);
    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 2);

    game.startTurn(Player.RED);
    game.passTurn();

    game.getCellAt(0, 1).addPawn(Player.BLUE);
    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 1);

    game.startTurn(Player.RED);
    game.passTurn();

    game.getCellAt(0, 0).addPawn(Player.BLUE);
    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 0);

    assertEquals(9, game.checkScore(Player.BLUE));
    assertEquals(0, game.checkScore(Player.RED));

  }

  @Test
  public void testRowWinnerRed() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);
    PawnGameTextualView view = new PawnGameTextualView(game);

    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 0, 0);

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.getCellAt(0, 1).addPawn(Player.RED);
    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 0, 1);

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.getCellAt(0, 2).addPawn(Player.RED);
    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 0, 2);

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.getCellAt(0, 3).addPawn(Player.RED);
    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 0, 3);

    game.startTurn(Player.BLUE);
    game.passTurn();

    game.getCellAt(0, 4).addPawn(Player.RED);
    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 0, 4);

    assertEquals(9, game.checkScore(Player.RED));
    assertEquals(0, game.checkScore(Player.BLUE));

  }


  //test isGameOver
  @Test
  public void testIsGameOver() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);

    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 0, 0);
    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0, 0, 4);
    assertFalse(game.isGameOver());
  }

  //test Apply Influence in the game
  @Test
  public void testApplyInfluence() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck,deck, 5);

    Cell cell = game.getCellAt(0, 0);
    Cell cell2 = game.getCellAt(0, 1);
    Cell cell3 = game.getCellAt(1, 0);

    //checks that the pawn count of the cells before card influence is applied
    assertEquals(0, cell2.getPawnCount().getValue());
    assertEquals(1, cell3.getPawnCount().getValue());

    Card card = game.getHand(Player.RED).get(0);
//    game.applyCardInfluence(0, 0, Player.RED,
//            card);

    //checks that the pawn count of the cells after card influence is applied
    assertEquals(1, cell2.getPawnCount().getValue());
    assertEquals(2, cell3.getPawnCount().getValue());

  }

  @Test
  public void testMirroredDeck() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);

    Card card = game.getHand(Player.BLUE).get(2);
    InfluenceType[][] mirroredGrid = {
            {InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X},
            {InfluenceType.I, InfluenceType.I, InfluenceType.C, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X},
    };

    assertEquals(card.getInfluenceGrid(), mirroredGrid);
  }

  @Test
  public void testInitialEmptyBoard() {
    game = new PawnGameImpl(3, 5);
    GameView view = new PawnGameTextualView(game);
    game.startGame(deck, deck, 5);

    String expected =
            "0 1 _ _ _ 1 0\n" +
                    "0 1 _ _ _ 1 0\n" +
                    "0 1 _ _ _ 1 0\n";
    assertEquals(expected, view.toString());
  }

  @Test
  public void testPlaceRedCard() {
    game = new PawnGameImpl(3, 5);
    GameView view = new PawnGameTextualView(game);
    game.startGame(deck,deck, 5);

    // Place "Security" card (assume index 0 in hand) at row 1, col 2
    game.placeCardAt(Player.RED, 0, 0, 0);

    // Expected board after placement
    String expectedAfter =
            "2 R 1 _ _ 1 0\n" +
                    "0 2 _ _ _ 1 0\n" +
                    "0 1 _ _ _ 1 0\n";

    assertEquals(expectedAfter, view.toString());
  }

  @Test
  public void testPlayUntilCompletion() {
    PawnGameImpl game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);

    placeCard(game, Player.RED, 0, 1, 0);
    placeCard(game, Player.BLUE, 0, 1, 4);
    passTurn(game, Player.RED);
    placeCard(game, Player.BLUE, 0, 2, 4);
    placeCard(game, Player.RED, 0, 0, 0);
    placeCard(game, Player.BLUE, 0, 1, 3);
    passTurn(game, Player.RED);
    placeCard(game, Player.BLUE, 0, 0, 4);
    placeCard(game, Player.RED, 0, 2, 0);
    placeCard(game, Player.BLUE, 1, 0, 3);
    placeCard(game, Player.RED, 2, 2, 1);
    placeCard(game, Player.BLUE, 4, 0, 2);
    placeCard(game, Player.RED, 4, 2, 2);
    placeCard(game, Player.BLUE, 2, 1, 2);
    placeCard(game, Player.RED, 4, 1, 1);

    assertTrue(game.isGameOver());
    assertEquals("Blue", game.getWinner());
  }

  private void placeCard(PawnGameImpl game, Player player, int cardIdx, int row, int col) {
    game.startTurn(player);
    game.placeCardAt(player, cardIdx, row, col);
  }

  @Test
  public void testStartTurn() {
    game = new PawnGameImpl(3, 5);
    assertThrows(IllegalStateException.class, () -> {
      game.startTurn(Player.RED);
    });

  }

  @Test
  public void testStartTurnWrongPlayer() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);
    assertThrows(IllegalArgumentException.class, () -> {
      game.startTurn(Player.BLUE);
    });
  }

  @Test
  public void testGetCurrentPlayerBeforeGameStartThrowsException() {
    game = new PawnGameImpl(3, 5);
    assertThrows(IllegalStateException.class, () -> game.getCurrentPlayer());
  }


  @Test
  public void testGetCurrentPlayerAfterGameStarts() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);

    assertEquals(Player.RED, game.getCurrentPlayer());

    game.startTurn(Player.RED);
    game.passTurn();

    assertEquals(Player.BLUE, game.getCurrentPlayer());
  }

  @Test
  public void testGetHand() {
    game = new PawnGameImpl(3, 5);
    game.startGame(deck, deck, 5);

    assertEquals(5, game.getHand(Player.RED).size());
    assertEquals(5, game.getHand(Player.RED).size());
    assertEquals(15, game.getRemainingDeckSize(Player.RED));
    assertEquals(15, game.getRemainingDeckSize(Player.BLUE));
  }

  @Test
  public void testGetBoardCopyThrow() {
    game = new PawnGameImpl(5,7);
    assertThrows(IllegalStateException.class, () -> game.getBoard());
  }

  @Test
  public void testGetBoardCopy() {
    game = new PawnGameImpl(5,7);
    Map<String, List<PawnCard>> decks3 = cardInitializer.initializeDeck(biggerPath, biggerPath);
    List<PawnCard> deck4 = decks3.get("deck1");
    game.startGame(deck4, deck4, 5);
    PawnCell[][] copiedBoard = game.getBoard();

    game.placeCardAt(Player.RED, 0, 0, 0);
    Cell copiedCell = copiedBoard[0][0];

    assertEquals(copiedBoard[0].length, game.getWidth());
    assertEquals(copiedBoard.length, game.getHeight());
    assertNotEquals(copiedCell.getCard(),
            game.getCellAt(0,0).getCard());
  }

  @Test
  public void testGetBoardCopyPawnPlacement() {
    game = new PawnGameImpl(5, 7);
    Map<String, List<PawnCard>> decks3 = cardInitializer.initializeDeck(biggerPath, biggerPath);
    List<PawnCard> deck4 = decks3.get("deck1");
    game.startGame(deck4, deck4, 5);
    PawnCell[][] copiedBoard = game.getBoard();

    for (int r = 0; r < copiedBoard.length; r++) {
      for (int c = 0; c < copiedBoard[0].length; c++) {
        PawnCell cell = copiedBoard[r][c];

        if (c == 0) {
          assertEquals(Player.RED, cell.getOwner());
          assertTrue(cell.getPawnCount().getValue() > 0);
        } else if (c == copiedBoard[0].length - 1) {
          assertEquals(Player.BLUE, cell.getOwner());
          assertTrue(cell.getPawnCount().getValue() > 0);
        } else { // All other cells should be empty
          assertNull(cell.getOwner());
          assertEquals(0, cell.getPawnCount().getValue());
        }
      }
    }
  }

  @Test
  public void testGetBoardCopy2() {
    game = new PawnGameImpl(5,7);
    Map<String, List<PawnCard>> decks3 = cardInitializer.initializeDeck(biggerPath, biggerPath);
    List<PawnCard> deck4 = decks3.get("deck1");
    game.startGame(deck4, deck4, 5);

    game.placeCardAt(Player.RED, 0, 0, 0);
    PawnCell[][] copiedBoard = game.getBoard();
    Cell copiedCell = copiedBoard[0][0];
    Cell copiedCell2 = copiedBoard[4][0];

    assertEquals(copiedCell.getOwner(),
            game.getCellAt(0,0).getOwner());
    assertEquals(copiedCell.getCard(),
            game.getCellAt(0,0).getCard());
    assertEquals(copiedCell2.getPawnCount(),
            game.getCellAt(4,0).getPawnCount());

    System.out.println(game.getCellAt(0, 0));
  }

  //testing the strategy
  @Test
  public void testMove() {
    InfluenceType[][] influenceGrid = new InfluenceType[][]{
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I, InfluenceType.X},
            {InfluenceType.I, InfluenceType.I, InfluenceType.C, InfluenceType.I, InfluenceType.I},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X},
    };
    Card card = new PawnCard("TestCard", Cost.Two, 5, influenceGrid);
    Move move = new Move(card, 0, 3);


    String moveString = "Move{" +
            "card=TestCard (2, 5), row=0, col=3}";
    assertEquals(moveString, move.toString());
    assertEquals(0, move.getRow());
    assertEquals(3, move.getCol());
  }

  @Test
  public void testFillFirstNoValidMove() {
    List<Move> chosenMoves = fillFirstStrat.chooseMove(mockGame, Player.RED);
    assertEquals(0, chosenMoves.size());
  }

  @Test
  public void testFillFirstStopsAtFirstValidMove() {
    // only (0,0) is legal
    mockGame.setLegalMove(0, 0);
    List<Move> chosenMoves = fillFirstStrat.chooseMove(mockGame, Player.RED);
    assertEquals(1, chosenMoves.size());
    assertEquals(0, chosenMoves.get(0).getRow());
    assertEquals(0, chosenMoves.get(0).getCol());
  }

  // will only return the first position it checks
  @Test
  public void testFillFirstRowValid() {
    mockGame.setLegalMove(0, 0);
    mockGame.setLegalMove(0, 1);
    mockGame.setLegalMove(0, 2);
    mockGame.setLegalMove(0, 3);
    mockGame.setLegalMove(0, 4);

    List<Move> chosenMoves = fillFirstStrat.chooseMove(mockGame, Player.RED);
    assertEquals(1, chosenMoves.size());
    assertEquals(0, chosenMoves.get(0).getRow());
    assertEquals(0, chosenMoves.get(0).getCol());
  }

  @Test
  public void testFillFirstMultipleValid() {
    mockGame.setLegalMove(1, 2);
    mockGame.setLegalMove(0, 0);
    mockGame.setLegalMove(2, 3 );
    mockGame.setLegalMove(0, 1);

    List<Move> chosenMoves = fillFirstStrat.chooseMove(mockGame, Player.RED);
    assertEquals(1, chosenMoves.size());
    assertEquals(0, chosenMoves.get(0).getRow());
    assertEquals(0, chosenMoves.get(0).getCol());
  }

  // valid move is in the middle (1, 3)
  @Test
  public void testFillFirstValidMoveInMiddle() {
    mockGame.setLegalMove(1,3);
    List<Move> chosenMoves = fillFirstStrat.chooseMove(mockGame, Player.RED);
    assertEquals(1, chosenMoves.size());
    assertEquals(1, chosenMoves.get(0).getRow());
    assertEquals(3, chosenMoves.get(0).getCol());
  }

  // valid move is at the end edge (2, 4)
  @Test
  public void testFillFirstValidMoveInEdge() {
    mockGame.setLegalMove(2,4);
    List<Move> chosenMoves = fillFirstStrat.chooseMove(mockGame, Player.RED);
    assertEquals(1, chosenMoves.size());
    assertEquals(2, chosenMoves.get(0).getRow());
    assertEquals(4, chosenMoves.get(0).getCol());
  }

  // all moves on board are valid
  @Test
  public void testFillFirstValidMoveAllValid() {
    for (int r = 0; r < 3; r++) {
      for (int c = 0; c < 5; c++) {
        mockGame.setLegalMove(r,c);
      }
    }

    List<Move> chosenMoves = fillFirstStrat.chooseMove(mockGame, Player.RED);
    assertEquals(1, chosenMoves.size());
    assertEquals(0, chosenMoves.get(0).getRow());
    assertEquals(0, chosenMoves.get(0).getCol());
  }

  @Test
  public void testFillFirstValidMoveLastRowFirstCol() {
    mockGame.setLegalMove(2,0);
    List<Move> chosenMoves = fillFirstStrat.chooseMove(mockGame, Player.RED);
    assertEquals(1, chosenMoves.size());
    assertEquals(2, chosenMoves.get(0).getRow());
    assertEquals(0, chosenMoves.get(0).getCol());
  }

  @Test
  public void testFillFirstValidMoveRandomPosn() {
    mockGame.setLegalMove(2,0);
    mockGame.setLegalMove(1,3);
    mockGame.setLegalMove(0,3);
    mockGame.setLegalMove(2,4);

    List<Move> chosenMoves = fillFirstStrat.chooseMove(mockGame, Player.RED);
    assertEquals(1, chosenMoves.size());
    assertEquals(0, chosenMoves.get(0).getRow());
    assertEquals(3, chosenMoves.get(0).getCol());
  }

  @Test
  public void testMaximizeScoreNoAvailableMoves() {
    List<Move> chosenMoves = maxRowStrat.chooseMove(mockGame, Player.RED);
    assertTrue(chosenMoves.isEmpty());
  }

  @Test
  public void testMaxScoreSingleValidMove() {
    PawnCard card = new PawnCard("Card1", Cost.One, 3, influenceGrid);
    mockGame.addCardToHand(Player.RED, card);
    mockGame.setFakeRowScore(1, Player.RED, 2);
    mockGame.setFakeRowScore(1, Player.BLUE, 4);
    mockGame.setLegalMove(1, 2);

    List<Move> moves = maxRowStrat.chooseMove(mockGame, Player.RED);

    assertEquals(1, moves.size());
    assertEquals(card, moves.get(0).getCard());
    assertEquals(1, moves.get(0).getRow());
    assertEquals(2, moves.get(0).getCol());
  }

  @Test
  public void testMaxScoreStratChooseFirstBest() {
    PawnCard weakCard = new PawnCard("WeakCard", Cost.One, 2, influenceGrid);
    PawnCard strongCard = new PawnCard("StrongCard", Cost.Two, 5, influenceGrid);

    mockGame.addCardToHand(Player.RED, weakCard);
    mockGame.addCardToHand(Player.RED, strongCard);
    mockGame.setFakeRowScore(0, Player.RED, 3);
    mockGame.setFakeRowScore(0, Player.BLUE, 6);
    mockGame.setLegalMove(0, 1);
    mockGame.setLegalMove(0, 2);

    List<Move> moves = maxRowStrat.chooseMove(mockGame, Player.RED);

    assertEquals(1, moves.size());
    assertEquals(strongCard, moves.get(0).getCard());
  }

  @Test
  public void testMaxStratIgnoresRowsWherePlayerIsAlreadyWinning() {
    PawnCard card = new PawnCard("UnusedCard", Cost.One, 3, influenceGrid);
    mockGame.addCardToHand(Player.RED, card);
    mockGame.setFakeRowScore(1, Player.RED, 8);
    mockGame.setFakeRowScore(1, Player.BLUE, 5);
    mockGame.setLegalMove(1, 3);

    List<Move> moves = maxRowStrat.chooseMove(mockGame, Player.RED);
    assertTrue(moves.isEmpty());
  }

  @Test
  public void testMaxStratChoosesMoveThatExactlySurpassesOpponent() {
    PawnCard card = new PawnCard("ExactCard", Cost.One, 3, influenceGrid);
    mockGame.addCardToHand(Player.RED, card);
    mockGame.setFakeRowScore(2, Player.RED, 4);
    mockGame.setFakeRowScore(2, Player.BLUE, 6);
    mockGame.setLegalMove(2, 1);

    List<Move> moves = maxRowStrat.chooseMove(mockGame, Player.RED);

    assertEquals(1, moves.size());
    assertEquals(card, moves.get(0).getCard());
  }

  @Test
  public void testAvoidsMovesThatDoNotSurpassOpponent() {
    PawnCard card = new PawnCard("LowCard", Cost.One, 2, influenceGrid);
    mockGame.addCardToHand(Player.RED, card);
    mockGame.setFakeRowScore(2, Player.RED, 3);
    mockGame.setFakeRowScore(2, Player.BLUE, 8);
    mockGame.setLegalMove(2, 2);

    List<Move> moves = maxRowStrat.chooseMove(mockGame, Player.RED);

    assertTrue(moves.isEmpty());
  }

  @Test
  public void testMaxStratMoveSelectionWhenOnlyOneLegalMoveAvailable() {
    PawnCard card = new PawnCard("SoloCard", Cost.One, 4, influenceGrid);
    mockGame.addCardToHand(Player.RED, card);
    mockGame.setFakeRowScore(2, Player.RED, 5);
    mockGame.setFakeRowScore(2, Player.BLUE, 7);
    mockGame.setLegalMove(2, 0);

    List<Move> moves = maxRowStrat.chooseMove(mockGame, Player.RED);

    assertEquals(1, moves.size());
    assertEquals(card, moves.get(0).getCard());
  }

  @Test
  public void testTieBreakerWhenTwoMovesHaveEqualValue() {
    PawnCard cardA = new PawnCard("CardA", Cost.One, 4, influenceGrid);
    PawnCard cardB = new PawnCard("CardB", Cost.One, 4, influenceGrid);

    mockGame.addCardToHand(Player.RED, cardA);
    mockGame.addCardToHand(Player.RED, cardB);
    mockGame.setFakeRowScore(0, Player.RED, 4);
    mockGame.setFakeRowScore(0, Player.BLUE, 7);
    mockGame.setLegalMove(0, 2);
    mockGame.setLegalMove(0, 3);

    List<Move> moves = maxRowStrat.chooseMove(mockGame, Player.RED);

    assertEquals(1, moves.size());
    assertEquals(moves.get(0).getCard(), cardA);
  }

  @Test
  public void testMaxRowStratMultipleRowsPicksBestOne() {
    PawnCard cardA = new PawnCard("CardA", Cost.One, 4, influenceGrid);
    PawnCard cardB = new PawnCard("CardB", Cost.One, 2, influenceGrid);

    mockGame.addCardToHand(Player.RED, cardA);
    mockGame.addCardToHand(Player.RED, cardB);

    mockGame.setFakeRowScore(0, Player.RED, 3);
    mockGame.setFakeRowScore(0, Player.BLUE, 6);

    mockGame.setFakeRowScore(1, Player.RED, 5);
    mockGame.setFakeRowScore(1, Player.BLUE, 7);

    mockGame.setLegalMove(0, 2);
    mockGame.setLegalMove(1, 1);

    List<Move> moves = maxRowStrat.chooseMove(mockGame, Player.RED);

    assertEquals(1, moves.size());
    assertEquals(cardA, moves.get(0).getCard());
    assertEquals(0, moves.get(0).getRow());
  }

  @Test
  public void testMaxRowStratMultipleRowsPicksFirstWhenEqual() {
    PawnCard cardA = new PawnCard("CardA", Cost.One, 4, influenceGrid);
    PawnCard cardB = new PawnCard("CardB", Cost.One, 4, influenceGrid);

    mockGame.addCardToHand(Player.RED, cardA);
    mockGame.addCardToHand(Player.RED, cardB);

    mockGame.setFakeRowScore(0, Player.RED, 5);
    mockGame.setFakeRowScore(0, Player.BLUE, 8);

    mockGame.setFakeRowScore(1, Player.RED, 4);
    mockGame.setFakeRowScore(1, Player.BLUE, 7);

    mockGame.setLegalMove(0, 2);
    mockGame.setLegalMove(1, 1);

    List<Move> moves = maxRowStrat.chooseMove(mockGame, Player.RED);

    assertEquals(1, moves.size());
    assertEquals(cardA, moves.get(0).getCard());
    assertEquals(0, moves.get(0).getRow());
  }

  @Test
  public void testMaxRowStratNoValidMoveReturnsEmptyList() {
    PawnCard cardA = new PawnCard("CardA", Cost.One, 4, influenceGrid);

    mockGame.addCardToHand(Player.RED, cardA);

    mockGame.setFakeRowScore(0, Player.RED, 5);
    mockGame.setFakeRowScore(0, Player.BLUE, 8);

    mockGame.setFakeRowScore(1, Player.RED, 4);
    mockGame.setFakeRowScore(1, Player.BLUE, 7);

    // No legal moves
    List<Move> moves = maxRowStrat.chooseMove(mockGame, Player.RED);

    assertTrue(moves.isEmpty());
  }

  @Test
  public void testMaxRowStratNoValidCardReturnsEmptyList() {
    PawnCard cardA = new PawnCard("CardA", Cost.One, 1, influenceGrid);

    mockGame.addCardToHand(Player.RED, cardA);

    mockGame.setFakeRowScore(0, Player.RED, 5);
    mockGame.setFakeRowScore(0, Player.BLUE, 8);

    mockGame.setFakeRowScore(1, Player.RED, 4);
    mockGame.setFakeRowScore(1, Player.BLUE, 7);

    mockGame.setLegalMove(0, 2);
    mockGame.setLegalMove(1, 1);

    List<Move> moves = maxRowStrat.chooseMove(mockGame, Player.RED);

    assertTrue(moves.isEmpty());
  }

  @Test
  public void testMaxStratHandHasCardsButNoLegalMoves() {
    PawnCard card = new PawnCard("UnplayableCard", Cost.One, 3, influenceGrid);
    mockGame.addCardToHand(Player.RED, card);

    List<Move> moves = maxRowStrat.chooseMove(mockGame, Player.RED);

    assertTrue(moves.isEmpty());
  }

  @Test
  public void testMaxStratMultipleMovesWithSameScoreIncrease() {
    PawnCard cardA = new PawnCard("CardA", Cost.One, 4, influenceGrid);
    PawnCard cardB = new PawnCard("CardB", Cost.One, 4, influenceGrid);

    mockGame.addCardToHand(Player.RED, cardA);
    mockGame.addCardToHand(Player.RED, cardB);
    mockGame.setFakeRowScore(1, Player.RED, 5);
    mockGame.setFakeRowScore(1, Player.BLUE, 8);
    mockGame.setFakeRowScore(2, Player.RED, 2);
    mockGame.setFakeRowScore(2, Player.BLUE, 5);

    mockGame.setLegalMove(1, 2);
    mockGame.setLegalMove(2, 3);

    List<Move> moves = maxRowStrat.chooseMove(mockGame, Player.RED);

    assertEquals(1, moves.size());
    assertEquals(cardA, moves.get(0).getCard());
  }

  @Test
  public void testMaxStratTranscript() {
    CardInitializer cardInitializer = new CardInitializer();
    Map<String, List<PawnCard>> deck = cardInitializer.initializeDeck(path, path);
    Map.Entry<String, List<PawnCard>> firstEntry = deck.entrySet().iterator().next();
    List<PawnCard> redDeck = firstEntry.getValue();

    for (PawnCard card : redDeck) {
      mockGame.addCardToHand(Player.RED, card);
    }

    mockGame.setFakeRowScore(0, Player.RED, 0);
    mockGame.setFakeRowScore(0, Player.BLUE, 20);
    mockGame.setFakeRowScore(1, Player.RED, 4);
    mockGame.setFakeRowScore(1, Player.BLUE, 5);
    mockGame.setLegalMove(0, 2);
    mockGame.setLegalMove(1, 3);

    List<Move> moves = maxRowStrat.chooseMove(mockGame, Player.RED);
    System.out.println(output.toString());
  }

  @Test
  public void testFillFirstTranscript() {
    CardInitializer cardInitializer = new CardInitializer();
    Map<String, List<PawnCard>> deck = cardInitializer.initializeDeck(path, path);
    Map.Entry<String, List<PawnCard>> firstEntry = deck.entrySet().iterator().next();
    List<PawnCard> redDeck = firstEntry.getValue();

    for (PawnCard card : redDeck) {
      mockGame.addCardToHand(Player.RED, card);
    }

    mockGame.setLegalMove(1, 4);
    mockGame.setLegalMove(2, 4);


    List<Move> moves = fillFirstStrat.chooseMove(mockGame, Player.RED);
    System.out.println(output.toString() + "Chosen Move: (" + moves.get(0) + ")");
  }


  @Test
  public void testFillFirstStrategy() {
    PawnGameImpl model = new PawnGameImpl(3, 5);

    CardInitializer cardInitializer = new CardInitializer();
    Map<String, List<PawnCard>> decks = cardInitializer.initializeDeck(path, path);
    List<PawnCard> deck1 = decks.get("deck1");
    List<PawnCard> deck2 = decks.get("deck2");

    model.startGame(deck1, deck2, 5);

    PawnBoardStrategy boardStrategy = new FillFirstStrategy();
    System.out.print(boardStrategy.chooseMove(model, Player.RED));
    PawnCard cardA = new PawnCard("Security", Cost.One, 4, influenceGrid);
    Move move = new Move(cardA, 0, 2);
    List<Move> moves = (List<Move>) move;


  }










  









}
