package pawnboard.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

import pawnboard.controller.CardInitializer;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnGameImpl;
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
    hand = new HandPanel(model, player);
    board = new BoardPanel(model,hand);
    blueScore = new ScorePanel(model, Player.BLUE);

    this.add(redScore, BorderLayout.WEST);
    this.add(board, BorderLayout.CENTER);
    this.add(blueScore, BorderLayout.EAST);
    this.add(hand, BorderLayout.SOUTH);

    display();
  }

  @Override
  public void display() {
    setVisible(true);
  }

  @Override
  public void updateView() {
    repaint();
  }

  public static void main(String[] args) {
    String biggerPath = "docs" + File.separator + "biggerDeck";
    CardInitializer cardInitializer = new CardInitializer();
    Map<String, java.util.List<PawnCard>> decks = cardInitializer.initializeDeck(biggerPath, biggerPath);
    List<PawnCard> deck = decks.get("deck1");
    PawnGameImpl model = new PawnGameImpl(5, 7);
    model.startGame(deck, deck, 5);
    new RedFrame(model);
    new BlueFrame(model);
  }

}

