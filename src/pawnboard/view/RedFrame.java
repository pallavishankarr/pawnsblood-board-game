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
    setSize(600, 800);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    board = new BoardPanel(model);
    this.add(board);
    hand = new HandPanel(model);
    this.add(hand);
    redScore = new RedScorePanel(model);
    this.add(redScore);
    blueScore = new BlueScorePanel(model);
    this.add(blueScore);
  }
  @Override
  public void display() {

  }

  @Override
  public void updateView() {

  }
}
