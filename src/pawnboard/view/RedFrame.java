package pawnboard.view;

import javax.swing.*;

import pawnboard.model.PawnCell;
import pawnboard.model.ReadonlyPawnsGame;

public class RedFrame extends JFrame implements GameWindow {

  private BoardPanel board;
  private HandPanel hand;
  private RedScorePanel redScore;
  private BlueScorePanel blueScore;

  public RedFrame(ReadonlyPawnsGame<PawnCell> model) {
    super();
    setSize(1200, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    board = new BoardPanel(model);
    this.add(board);
    hand = new HandPanel(model);
    this.add(hand);
    redScore = new RedScorePanel(model);
    //this.add(redScore);
    blueScore = new BlueScorePanel(model);
    //this.add(blueScore);
    setVisible(true);
  }
  @Override
  public void display() {

  }

  @Override
  public void updateView() {

  }
}
