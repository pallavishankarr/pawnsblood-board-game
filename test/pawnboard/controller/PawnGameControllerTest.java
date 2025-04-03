package pawnboard.controller;

import org.junit.Before;
import org.junit.Test;

import pawnboard.MockPawnGame;
import pawnboard.Player.HumanPlayer;
import pawnboard.Player.UserPlayer;
import pawnboard.model.Player;
import pawnboard.view.GameFrame;
import pawnboard.view.GameWindow;
import pawnboard.view.RedFrame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PawnGameControllerTest {
  private MockPawnGame mockGame;
  private Appendable output;
  private UserPlayer userPlayer;
  private GameWindow playerView;
  private PawnsGameController controller;

  @Before
  public void setUp() {
    mockGame = new MockPawnGame(new StringBuilder());
    output = new StringBuilder();
    userPlayer = new HumanPlayer(Player.RED, mockGame);
    playerView = new RedFrame(mockGame);
    controller = new PawnsGameController(mockGame, userPlayer, playerView, output);
  }


  // UNIT TESTING WITH MOCK

  // test that the game starts
  @Test
  public void testGameStart() {
    controller.playGame("docs/deck.config", "docs/deck.config", 5);
    // controller properly starts game if able to call method like getHand
    assertNotNull(mockGame.getHand(Player.RED));
    assertNotNull(mockGame.getHand(Player.BLUE));
  }

  // test constructor with valid arguments
  // test controller with null arguments
  // test valid game start
  // test playGame with invalid file paths
  // test game already over
  // test valid hand size
  // test card selection
  // test card selection with invalid card index
  // test cell selection
  //
}
