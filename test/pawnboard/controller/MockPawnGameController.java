package pawnboard.controller;

import pawnboard.player.UserPlayer;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.view.GameFrame;
import pawnboard.view.GameWindow;

/**
 * A mock implementation of the PawnsGameController used for testing or simulating user
 * interactions in a game.
 */
public class MockPawnGameController extends PawnsGameController {

  private int selectedCardIdx = -1;
  private int selectedRow = -1;
  private int selectedCol = -1;

  /**
   * Constructs a MockPawnGameController.
   * Given game model, user player, player view, and output appendable
   * @param model The game model representing the current state of the game.
   * @param userPlayer The user player interacting with the game.
   * @param playerView The window where the player view is displayed.
   * @param output The output where game events are appended for logging or display.
   */
  public MockPawnGameController(PawnsGame<PawnCell, Player,
          PawnCard> model, UserPlayer userPlayer, GameWindow playerView, Appendable output) {
    super(model, userPlayer, (GameFrame)playerView);
  }

  @Override
  public void onCardClick(int cardIdx) {
    super.onCardClick(cardIdx);
    this.selectedCardIdx = cardIdx;
  }

  @Override
  public void onCellClick(int row, int col) {
    super.onCellClick(row, col);
    this.selectedRow = row;
    this.selectedCol = col;
  }

  // Getter methods for internal state
  public int getSelectedCardIdx() {
    return selectedCardIdx;
  }

  public int getSelectedRow() {
    return selectedRow;
  }

  public int getSelectedCol() {
    return selectedCol;
  }


}
