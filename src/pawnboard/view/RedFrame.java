package pawnboard.view;

import java.io.File;

import javax.swing.*;

import pawnboard.model.PawnCell;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;
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
    setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

    board = new BoardPanel(model);
    this.add(board);
    hand = new HandPanel(model);
    this.add(hand);
//    redScore = new RedScorePanel(model);
//    this.add(redScore);
//    blueScore = new BlueScorePanel(model);
//    this.add(blueScore);
    setVisible(true);
  }
  @Override
  public void display() {

  }

  @Override
  public void updateView() {

  }

  // Test Main Method to Launch Frame
  public static void main(String[] args) {
    String path = "docs" + File.separator + "deck.config";
    String biggerPath = "docs" + File.separator + "biggerDeck";
    // Create a mock ReadonlyPawnsGame instance
    PawnGameImpl model = new PawnGameImpl(5,7);
    model.startGame(biggerPath, 5);
    model.placeCardAt(Player.RED, 0, 0,0);
    new RedFrame(model);
  }
}
