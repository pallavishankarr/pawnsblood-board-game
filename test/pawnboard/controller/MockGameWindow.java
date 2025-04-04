package pawnboard.controller;

import pawnboard.MockPawnGame;
import pawnboard.Player.UserPlayer;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;
import pawnboard.view.BoardPanel;
import pawnboard.view.GameWindow;
import pawnboard.view.HandPanel;

public class MockGameWindow implements MockGameInterface{
  private boolean viewUpdated = false;
  Appendable output = new StringBuilder();
  private HandPanel handPanel;
  private BoardPanel boardPanel;

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
  public boolean isViewUpdated() {
    return viewUpdated;
  }
}