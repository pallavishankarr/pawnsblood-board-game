package pawnboard.controller;


import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pawnboard.MockPawnGame;
import pawnboard.Player.HumanPlayer;
import pawnboard.Player.UserPlayer;
import pawnboard.model.Cost;
import pawnboard.model.InfluenceType;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;
import pawnboard.view.GameWindow;
import pawnboard.view.RedFrame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;


public class PawnGameControllerTest {

  private final CardInitializer cardInitializer = new CardInitializer();
  private MockPawnGame mockModel;
  private StringBuilder output;
  private PawnsGameController controller;
  private MockGameInterface mockGameWindow;
  private MockPawnGameController mockController;
  private PawnGameImpl pawnGame;
  private GameWindow redWindow;
  private Map<String, List<PawnCard>> decks = new HashMap<>();
  private List<PawnCard> deck = new ArrayList<>();
  private List<PawnCard> deck2 = new ArrayList<>();
  private MockBoardPanel mockBoardPanel;
  private MockHandPanel mockHandPanel;
  private MockUserPlayer mockUserPlayer;
  private UserPlayer player;
  private String path;
  private InfluenceType[][] influenceGrid;


  @Before
  public void setUp() {

    path = "docs" + File.separator + "deck.config";

    decks = CardInitializer.initializeDeck(path, path);
    deck = decks.get("deck1");
    deck2 = decks.get("deck2");
    output = new StringBuilder();


    mockModel = new MockPawnGame(output);
    mockHandPanel = new MockHandPanel(mockModel, Player.RED);
    mockBoardPanel = new MockBoardPanel(mockModel, mockHandPanel, output);

    mockGameWindow = new MockGameWindow(mockHandPanel, mockBoardPanel);
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
    assertThrows(IllegalArgumentException.class, () -> new PawnsGameController
            (pawnGame, null, redWindow));

    assertThrows(IllegalArgumentException.class, () -> new PawnsGameController(null, player,
            redWindow));

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

}
