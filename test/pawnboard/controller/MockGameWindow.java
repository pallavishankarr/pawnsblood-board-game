package pawnboard.controller;

import pawnboard.player.UserPlayer;
import pawnboard.model.Player;
import pawnboard.view.BoardPanel;
import pawnboard.view.HandPanel;


/**
 * A mock implementation of the GameWindow interface used for
 * testing or simulating the game's graphical interface.
 */
public class MockGameWindow implements MockGameInterface {
  Appendable output = new StringBuilder();
  private boolean viewUpdated = false;
  private final HandPanel handPanel;
  private final BoardPanel boardPanel;

  /**
   * Constructs a MockGameWindow with the specified hand panel and board panel.
   * @param handPanel The panel displaying the player's hand of cards.
   * @param boardPanel The panel displaying the game board.
   */
  MockGameWindow(MockHandPanel handPanel, MockBoardPanel boardPanel) {
    this.handPanel = handPanel;
    this.boardPanel = boardPanel;
  }


  @Override
  public void display() {
    System.out.println("Display simulated.");
  }

  @Override
  public void updateView() {
    viewUpdated = true;
    System.out.println("View updated.");
  }

  @Override
  public BoardPanel getBoardPanel() {
    return boardPanel;
  }

  @Override
  public HandPanel getHandPanel() {
    return handPanel;
  }

  @Override
  public void updateWindowTitle(Player currentPlayer, Player userPlayer) {
    //simulates that the update window update title
  }

  @Override
  public void showTurnNotification(UserPlayer currentUserPlayer) {
    //simulates that the show turn notification

  }

  @Override
  public void showOnGameOver(String winner, int redScore, int blueScore) {
    //simulates the notification for the show On Game over
  }

  @Override
  public void showInvalidMoveNotification(String message) {
    //simulates the notification
  }


  @Override
  public boolean isViewUpdated() {
    return viewUpdated;
  }
}