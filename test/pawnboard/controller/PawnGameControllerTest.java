package pawnboard.controller;


import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pawnboard.MockPawnGame;
import pawnboard.player.HumanPlayer;
import pawnboard.player.UserPlayer;
import pawnboard.model.Cost;
import pawnboard.model.InfluenceType;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;
import pawnboard.view.GameFrame;
import pawnboard.view.GameWindow;
import pawnboard.view.PawnGameTextualView;
import pawnboard.view.RedFrame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Represents tests for the pawngame controller.
 */
public class PawnGameControllerTest {

  private final CardInitializer cardInitializer = new CardInitializer();
  private MockPawnGame mockModel;
  private PawnsGameController controller;
  private MockPawnGameController mockController;
  private PawnGameImpl pawnGame;
  private GameWindow redWindow;
  private Map<String, List<PawnCard>> decks = new HashMap<>();
  private List<PawnCard> deck = new ArrayList<>();
  private List<PawnCard> deck2 = new ArrayList<>();
  private MockUserPlayer mockUserPlayer;
  private UserPlayer player;
  private String path;
  private InfluenceType[][] influenceGrid;
  private PawnGameTextualView textualView;
  private ReadonlyPawnsGame readOnlyModel;


  @Before
  public void setUp() {

    path = "docs" + File.separator + "deck.config";

    decks = CardInitializer.initializeDeck(path, path);
    deck = decks.get("deck1");
    deck2 = decks.get("deck2");
    StringBuilder output = new StringBuilder();


    mockModel = new MockPawnGame(output);
    MockHandPanel mockHandPanel = new MockHandPanel(mockModel, Player.RED);
    MockBoardPanel mockBoardPanel = new MockBoardPanel(mockModel, mockHandPanel, output);

    MockGameInterface mockGameWindow = new MockGameWindow(mockHandPanel, mockBoardPanel);
    mockUserPlayer = new MockUserPlayer(Player.RED, false);
    mockController = new MockPawnGameController(mockModel, mockUserPlayer, mockGameWindow, output);
    pawnGame = new PawnGameImpl(3, 5);
    pawnGame.startGame(deck, deck2, 5);
    player = new HumanPlayer(Player.RED, pawnGame);

    redWindow = new RedFrame(pawnGame);
    influenceGrid = new InfluenceType[][]{
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I, InfluenceType.X},
            {InfluenceType.I, InfluenceType.I, InfluenceType.C, InfluenceType.I, InfluenceType.I},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X},
    };


  }

  //tests that the constructor for the controller is wrong
  @Test
  public void testInvalidConstructor() {
    assertThrows(IllegalArgumentException.class, () -> new PawnsGameController(
            pawnGame, null, (GameFrame) redWindow));

    assertThrows(IllegalArgumentException.class, () -> new PawnsGameController(null, player,
            (GameFrame) redWindow));

    assertThrows(IllegalArgumentException.class, () -> new PawnsGameController(pawnGame, player,
            null));
  }

  //tests for the game initialization
  @Test
  public void testGameInitialization() {
    mockController.playGame(path, path, 5);
    assertFalse(mockModel.isGameOver());
    assertEquals(mockUserPlayer.getPlayer(), mockModel.getCurrentPlayer());
  }


  //tests that controller handles the card click
  @Test
  public void testHandleCardClickValidIndex() {
    PawnCard card = new PawnCard("UnplayableCard", Cost.One, 3, influenceGrid);
    mockModel.addCardToHand(Player.RED, card);
    mockController.onCardClick(0);
    assertEquals(0, mockController.getSelectedCardIdx());

  }


  //tests that the index doesn't change when nothing happens
  @Test
  public void testHandleCardClickInvalidIndex() {
    PawnCard card = new PawnCard("UnplayableCard", Cost.One, 3, influenceGrid);
    mockModel.addCardToHand(Player.RED, card);
    assertNotEquals(5, mockController.getSelectedCardIdx()); // Should remain -1
  }

  //tests the handle cell click
  @Test
  public void testHandleCellCLick() {
    mockController.onCellClick(2, 3);
    assertEquals(2, mockController.getSelectedRow());
    assertEquals(3, mockController.getSelectedCol());
  }

  //test the handle cell click is invalid
  @Test
  public void testHandleCellClickInvalid() {
    assertThrows(IllegalArgumentException.class, () -> mockController.onCellClick(-1, 3));
  }

  //handle the key press when confirm
  @Test
  public void testHandleKeyPressConfirm() {
    PawnCard card = new PawnCard("UnplayableCard", Cost.One, 3, influenceGrid);
    mockModel.addCardToHand(Player.RED, card);
    mockController.onCardClick(0);
    mockController.onCellClick(2, 3);

    mockController.onConfirmTurn();

    List<String> placedCardsLog = mockModel.getPlacedCardsLog();
    assertEquals(1, placedCardsLog.size());
    assertTrue(placedCardsLog.get(0).contains("Card 0 placed by Red at (2, 3)"));

  }


  //test passing a turn
  @Test
  public void testPassingTurn() {
    mockController.onPassTurn();

    List<String> placedCardsLog = mockModel.getPlacedCardsLog();
    assertEquals(1, placedCardsLog.size());
    assertTrue(placedCardsLog.get(0).contains("passTurn called with Player:Red"));
  }

  // tests placing a card using controller in upper left corner
  @Test
  public void testPlaceCard() {
    path = "docs" + File.separator + "deck.config";
    decks = CardInitializer.initializeDeck(path, path);
    deck = decks.get("deck1");
    deck2 = decks.get("deck2");
    PawnGameImpl model = new PawnGameImpl(3, 5);
    model.startGame(deck, deck2, 5);
    HumanPlayer redPlayer = new HumanPlayer(Player.RED, model);

    PawnsGameController controller = new PawnsGameController(model, redPlayer, new RedFrame(model));

    controller.onCardClick(0);
    controller.onCellClick(0, 0);
    controller.onConfirmTurn();

    PawnGameTextualView view = new PawnGameTextualView(model);
    String boardState = view.toString();
    System.out.println(boardState);

    assertEquals(boardState,
            "2 R 1 _ _ 1 0\n" +
                    "0 2 _ _ _ 1 0\n" +
                    "0 1 _ _ _ 1 0\n");
  }

  // test blue placing a card
  @Test
  public void testPlaceCardBlue() {
    path = "docs" + File.separator + "deck.config";
    decks = CardInitializer.initializeDeck(path, path);
    deck = decks.get("deck1");
    deck2 = decks.get("deck2");
    PawnGameImpl model = new PawnGameImpl(3, 5);
    model.startGame(deck, deck2, 5);
    HumanPlayer redPlayer = new HumanPlayer(Player.RED, model);
    HumanPlayer bluePlayer = new HumanPlayer(Player.BLUE, model);


    PawnsGameController controller =
            new PawnsGameController(model, redPlayer, new RedFrame(model));
    PawnsGameController controller2 =
            new PawnsGameController(model, bluePlayer, new RedFrame(model));


    controller.onCardClick(0);
    controller.onCellClick(0, 0);
    controller.onConfirmTurn();

    controller2.onCardClick(0);
    controller2.onCellClick(0, 4);
    controller2.onConfirmTurn();


    PawnGameTextualView view = new PawnGameTextualView(model);
    String boardState = view.toString();
    System.out.println(boardState);

    assertEquals(boardState,
            "2 R 1 _ 1 B 2\n" +
                    "0 2 _ _ _ 2 0\n" +
                    "0 1 _ _ _ 1 0\n");
  }

  // test passing a card doesn't affect the game board
  @Test
  public void testPassCardController() {
    path = "docs" + File.separator + "deck.config";
    decks = CardInitializer.initializeDeck(path, path);
    deck = decks.get("deck1");
    deck2 = decks.get("deck2");
    PawnGameImpl model = new PawnGameImpl(3, 5);
    model.startGame(deck, deck2, 5);
    HumanPlayer redPlayer = new HumanPlayer(Player.RED, model);

    PawnsGameController controller = new PawnsGameController(model, redPlayer, new RedFrame(model));

    controller.onPassTurn();
    PawnGameTextualView view = new PawnGameTextualView(model);
    String boardState = view.toString();
    assertEquals(boardState, "0 1 _ _ _ 1 0\n" +
            "0 1 _ _ _ 1 0\n" +
            "0 1 _ _ _ 1 0\n");
  }

  @Test
  public void testConfirmWithoutSelection() {
    path = "docs" + File.separator + "deck.config";
    decks = CardInitializer.initializeDeck(path, path);
    deck = decks.get("deck1");
    deck2 = decks.get("deck2");
    PawnGameImpl model = new PawnGameImpl(3, 5);
    model.startGame(deck, deck2, 5);
    HumanPlayer redPlayer = new HumanPlayer(Player.RED, model);

    PawnsGameController controller = new PawnsGameController(model, redPlayer, new RedFrame(model));

    controller.onConfirmTurn();

    PawnGameTextualView view = new PawnGameTextualView(model);
    String boardState = view.toString();
    assertEquals(boardState, "0 1 _ _ _ 1 0\n" +
            "0 1 _ _ _ 1 0\n" +
            "0 1 _ _ _ 1 0\n");
  }

  @Test
  public void testInvalidCellPlacement() {
    path = "docs" + File.separator + "deck.config";
    decks = CardInitializer.initializeDeck(path, path);
    deck = decks.get("deck1");
    deck2 = decks.get("deck2");
    PawnGameImpl model = new PawnGameImpl(3, 5);
    model.startGame(deck, deck2, 5);
    HumanPlayer redPlayer = new HumanPlayer(Player.RED, model);

    PawnsGameController controller = new PawnsGameController(model, redPlayer, new RedFrame(model));

    controller.onCardClick(0);

    assertThrows(IllegalArgumentException.class,
        () -> controller.onCellClick(-1, -1));
  }

  @Test
  public void testInvalidCardSelection() {
    path = "docs" + File.separator + "deck.config";
    decks = CardInitializer.initializeDeck(path, path);
    deck = decks.get("deck1");
    deck2 = decks.get("deck2");
    PawnGameImpl model = new PawnGameImpl(3, 5);
    model.startGame(deck, deck2, 5);
    HumanPlayer redPlayer = new HumanPlayer(Player.RED, model);

    PawnsGameController controller = new PawnsGameController(model, redPlayer, new RedFrame(model));

    controller.onCardClick(0);

    assertThrows(IllegalArgumentException.class,
        () -> controller.onCardClick(-1));
  }



  //tests that the turn properly changes and that the board state
  //doesn't change
  @Test
  public void testOnTurnChanged() {
    path = "docs" + File.separator + "deck.config";
    decks = CardInitializer.initializeDeck(path, path);
    deck = decks.get("deck1");
    deck2 = decks.get("deck2");
    PawnGameImpl model = new PawnGameImpl(3, 5);
    model.startGame(deck, deck2, 5);
    HumanPlayer redPlayer = new HumanPlayer(Player.RED, model);

    PawnsGameController controller = new PawnsGameController(model, redPlayer, new RedFrame(model));

    controller.onTurnChanged();
    assertEquals(Player.RED, model.getCurrentPlayer());

    PawnGameTextualView view = new PawnGameTextualView(model);
    String boardState = view.toString();
    assertEquals(boardState, "0 1 _ _ _ 1 0\n" +
            "0 1 _ _ _ 1 0\n" +
            "0 1 _ _ _ 1 0\n");


  }

  //tests when onGameOver is called
  @Test
  public void testOnGameOver() {
    path = "docs" + File.separator + "deck.config";
    decks = CardInitializer.initializeDeck(path, path);
    deck = decks.get("deck1");
    deck2 = decks.get("deck2");
    PawnGameImpl model = new PawnGameImpl(3, 5);
    model.startGame(deck, deck2, 5);
    HumanPlayer redPlayer = new HumanPlayer(Player.RED, model);

    PawnsGameController controller = new PawnsGameController(model, redPlayer, new RedFrame(model));

    controller.onGameOver();
    assertEquals("Tie", model.getWinner());
    assertEquals(0, model.checkScore(Player.RED));
    assertEquals(0, model.checkScore(Player.BLUE));

    assertEquals(Player.RED, model.getCurrentPlayer());
    PawnGameTextualView view = new PawnGameTextualView(model);
    String boardState = view.toString();
    assertEquals(boardState, "0 1 _ _ _ 1 0\n" +
            "0 1 _ _ _ 1 0\n" +
            "0 1 _ _ _ 1 0\n");


  }

  //Red wins the Game
  @Test
  public void testOnGameOverRedWins() {
    path = "docs" + File.separator + "deck.config";
    decks = CardInitializer.initializeDeck(path, path);
    deck = decks.get("deck1");
    deck2 = decks.get("deck2");
    PawnGameImpl model = new PawnGameImpl(3, 5);
    model.startGame(deck, deck2, 5);
    HumanPlayer redPlayer = new HumanPlayer(Player.RED, model);

    PawnsGameController controller = new PawnsGameController(model, redPlayer, new RedFrame(model));

    controller.onCardClick(0);
    controller.onCellClick(0,0);
    controller.onConfirmTurn();

    assertEquals(Player.BLUE, model.getCurrentPlayer());

    controller.onCardClick(0);
    controller.onCellClick(2,4);
    controller.onConfirmTurn();

    assertEquals(Player.RED, model.getCurrentPlayer());

    controller.onCardClick(0);
    controller.onCellClick(0,1);
    controller.onConfirmTurn();

    assertEquals(Player.BLUE, model.getCurrentPlayer());


    controller.onGameOver();
    assertEquals("Red", model.getWinner());
    assertEquals(3, model.checkScore(Player.RED));
    assertEquals(2, model.checkScore(Player.BLUE));

    PawnGameTextualView view = new PawnGameTextualView(model);
    String boardState = view.toString();
    assertEquals(boardState, "3 R R _ _ 1 0\n" +
           "0 2 _ _ _ 2 0\n" +
            "0 1 1 _ 1 B 2\n");




  }


  //testing on game over when the blue player wins
  @Test
  public void testOnGameBlueWins() {
    path = "docs" + File.separator + "deck.config";
    decks = CardInitializer.initializeDeck(path, path);
    deck = decks.get("deck1");
    deck2 = decks.get("deck2");
    PawnGameImpl model = new PawnGameImpl(3, 5);
    model.startGame(deck, deck2, 5);
    HumanPlayer redPlayer = new HumanPlayer(Player.RED, model);

    PawnsGameController controller = new PawnsGameController(model, redPlayer, new RedFrame(model));

    controller.onPassTurn();
    assertEquals(Player.BLUE, model.getCurrentPlayer());

    controller.onCardClick(0);
    controller.onCellClick(2,4);
    controller.onConfirmTurn();

    controller.onGameOver();

    assertEquals("Blue", model.getWinner());
    assertEquals(0, model.checkScore(Player.RED));
    assertEquals(2, model.checkScore(Player.BLUE));

    PawnGameTextualView view = new PawnGameTextualView(model);
    String boardState = view.toString();
    assertEquals(boardState, "0 1 _ _ _ 1 0\n" +
           "0 1 _ _ _ 2 0\n" +
           "0 1 _ _ 1 B 2\n");
  }

  //test that error is thrown when play game is called again
  @Test
  public void testInvalidPlayGame() {
    path = "docs" + File.separator + "deck.config";
    decks = CardInitializer.initializeDeck(path, path);
    deck = decks.get("deck1");
    deck2 = decks.get("deck2");
    PawnGameImpl model = new PawnGameImpl(3, 5);
    model.startGame(deck, deck2, 5);
    HumanPlayer redPlayer = new HumanPlayer(Player.RED, model);

    PawnsGameController controller = new PawnsGameController(model, redPlayer, new RedFrame(model));
    controller.playGame(path, path, 5);

    assertEquals(Player.RED, model.getCurrentPlayer());

    controller.onGameOver();
    assertThrows(IllegalStateException.class, () -> controller.playGame(path, path, 5));

  }


}
