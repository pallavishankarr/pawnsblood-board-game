package pawnboard.view;

import java.io.File;
import java.util.List;
import java.util.Map;

import pawnboard.model.CardInitializer;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public class BlueFrame extends GameFrame {

  public BlueFrame(ReadonlyPawnsGame<PawnCell> model) {
    super(model, Player.BLUE);
    initialize();
  }

  @Override
  public void initialize() {
  }

  public static void main(String[] args) {
    String biggerPath = "docs" + File.separator + "biggerDeck";
    PawnGameImpl model = new PawnGameImpl(5, 7);
    CardInitializer cardInitializer = new CardInitializer();
    Map<String, List<PawnCard>> decks = cardInitializer.initializeDeck(biggerPath, biggerPath);
    List<PawnCard> deck = decks.get("deck");
    model.startGame(deck, deck, 5);
    model.placeCardAt(Player.RED, 0, 0, 0);
    new BlueFrame(model);
  }
}
