package pawnboard.view;

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

public class HighContrastGameFrame extends GameFrame {

  /**
   * Constructs a GameFrame for a specific player and game model.
   *
   * @param model  the model to observe
   * @param player the player associated with the game frame
   * @throws IllegalArgumentException if the model is null
   */
  public HighContrastGameFrame(ReadonlyPawnsGame<PawnCell, Player,
          PawnCard> model, Player player) {
    super(model, player);

    super.remove(super.board);
    super.board = new HighContrastBoardPanel(model, super.hand);
    super.add(board, BorderLayout.CENTER);
    super.display();
  }

  public static void main (String[] args) {
    String biggerPath = "docs" + File.separator + "biggerDeck";
    CardInitializer cardInitializer = new CardInitializer();
    Map<String, java.util.List<PawnCard>> decks =
            CardInitializer.initializeDeck(biggerPath, biggerPath);
    List<PawnCard> deck = decks.get("deck1");
    PawnGameImpl model = new PawnGameImpl(5, 7);
    model.startGame(deck, deck, 5);
    HighContrastGameFrame gamePanel = new HighContrastGameFrame(model,Player.RED);
  }


}
