package pawnboard.view;

<<<<<<< Updated upstream
=======
import java.awt.*;
import java.io.File;

>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
    board = new BoardPanel(model);
    this.add(board);
    hand = new HandPanel(model);
    this.add(hand);
    redScore = new RedScorePanel(model);
    //this.add(redScore);
    blueScore = new BlueScorePanel(model);
    //this.add(blueScore);
=======
    setLayout(new BorderLayout());

    redScore = new RedScorePanel(model);
    this.add(redScore);
    board = new BoardPanel(model);
    this.add(board);
//    hand = new HandPanel(model);
//    this.add(hand);

    blueScore = new BlueScorePanel(model);
    this.add(blueScore);

    this.add(redScore, BorderLayout.WEST);  // Left side
    this.add(board, BorderLayout.CENTER);
    this.add(blueScore, BorderLayout.EAST);
>>>>>>> Stashed changes
    setVisible(true);
  }
  @Override
  public void display() {

  }

  @Override
  public void updateView() {
    repaint();
  }
}
