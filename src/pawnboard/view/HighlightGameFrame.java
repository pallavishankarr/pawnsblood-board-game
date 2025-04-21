package pawnboard.view;

import java.awt.BorderLayout;
import java.io.File;
import java.util.List;
import java.util.Map;

import pawnboard.controller.CardInitializer;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

/**
 * Represents a game frame that can be highlighted.
 */
public class HighlightGameFrame extends GameFrame {


  /**
   * Constructs a GameFrame for a specific player and game model.
   *
   * @param model  the model to observe
   * @param player the player associated with the game frame
   * @throws IllegalArgumentException if the model is null
   */
  public HighlightGameFrame(ReadonlyPawnsGame<PawnCell, Player, PawnCard> model, Player player) {
    super(model, player);
    redScore = new ScorePanel(model, Player.RED);
    hand = new HandPanel(model, player);
    board = new HighContrastBoardPanel(model, hand);  // <- Use the new panel here
    blueScore = new ScorePanel(model, Player.BLUE);

    // Clear existing components and add new ones
    this.getContentPane().removeAll();
    this.add(redScore, BorderLayout.WEST);
    this.add(board, BorderLayout.CENTER);
    this.add(blueScore, BorderLayout.EAST);
    this.add(hand, BorderLayout.SOUTH);

    this.revalidate();
    this.repaint();
  }


  /**
   * Main to run the test board.
   * @param args arguments
   */
  public static void main(String[] args) {
    String path = "docs" + File.separator + "deck.config";
    CardInitializer cardInitializer = new CardInitializer();
    Map<String, java.util.List<PawnCard>> decks = cardInitializer.initializeDeck(path, path);
    java.util.List<PawnCard> redDeck = decks.get("deck1");
    List<PawnCard> blueDeck = decks.get("deck2");
    PawnGameImpl pawnGame = new PawnGameImpl(3,5);
    pawnGame.startGame(redDeck, blueDeck, 5);
    HighlightGameFrame highlightGameFrame = new HighlightGameFrame(pawnGame, Player.RED);
    pawnGame.placeCardAt(Player.RED, 0, 0,0);
 }


}
