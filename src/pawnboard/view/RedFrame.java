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

public class RedFrame extends GameFrame {

  public RedFrame(ReadonlyPawnsGame<PawnCell> model) {
    super(model, Player.RED);
    initialize();
  }

  @Override
  public void initialize() {
  }

  public static void main(String[] args) {
    String biggerPath = "docs" + File.separator + "biggerDeck";
    CardInitializer cardInitializer = new CardInitializer();
    Map<String, List<PawnCard>> decks = cardInitializer.initializeDeck(biggerPath, biggerPath);
    List<PawnCard> deck = decks.get("deck1");
    PawnGameImpl model = new PawnGameImpl(5, 7);
    model.startGame(deck,deck, 5);
    model.startTurn(Player.RED);
    model.placeCardAt(Player.RED, 0, 0, 0);
    model.startTurn(Player.BLUE);
    model.passTurn();
    model.startTurn(Player.RED);
    model.passTurn();
    new RedFrame(model);
  }
}