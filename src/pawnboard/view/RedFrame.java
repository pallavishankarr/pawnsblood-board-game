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
  }
  @Override
  public void display() {

  }

  @Override
  public void updateView() {

  }
}
