package pawnboard.view;

import javax.swing.*;
import java.awt.*;

import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public abstract class GameFrame extends JFrame implements GameWindow {

  protected BoardPanel board;
  protected HandPanel hand;
  protected ScorePanel redScore;
  protected ScorePanel blueScore;
  protected final ReadonlyPawnsGame<PawnCell> model;
  protected final Player player;  // Each frame is associated with a specific player

  public GameFrame(ReadonlyPawnsGame<PawnCell> model, Player player) {
    super();
    this.model = model;
    this.player = player;
    setSize(1200, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    redScore = new ScorePanel(model, Player.RED);
    board = new BoardPanel(model);
    hand = new HandPanel(model, player); // Pass the respective player
    blueScore = new ScorePanel(model, Player.BLUE);

    this.add(redScore, BorderLayout.WEST);
    this.add(board, BorderLayout.CENTER);
    this.add(blueScore, BorderLayout.EAST);
    this.add(hand, BorderLayout.SOUTH);

    setVisible(true);
  }

  @Override
  public void display() {
    setVisible(true);
  }

  @Override
  public void updateView() {
    repaint();
  }

  // Abstract method to define frame-specific behavior
  public abstract void initialize();
}

