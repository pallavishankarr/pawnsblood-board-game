package pawnboard.view;

import java.io.File;

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
    PawnGameImpl model = new PawnGameImpl(5, 7);
    model.startGame(biggerPath, 5);
    model.startTurn(Player.RED);
    model.placeCardAt(Player.RED, 0, 0, 0);
    model.startTurn(Player.BLUE);
    model.passTurn(Player.BLUE);
    model.startTurn(Player.RED);
    model.passTurn(Player.RED);
    new RedFrame(model);
  }
}