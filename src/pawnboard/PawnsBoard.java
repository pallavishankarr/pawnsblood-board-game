package pawnboard;

import java.io.File;

import pawnboard.model.CardInitializer;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;
import pawnboard.view.GameView;
import pawnboard.view.PawnGameTextualView;

public final class PawnsBoard {


  public static void main(String[] args) {
    String path = "docs" + File.separator + "deck.config";
    PawnGameImpl game = new PawnGameImpl(3,5);
    GameView view = new PawnGameTextualView(game);
    game.startGame(path,5);

    System.out.println("Initial Board:");
    System.out.println(view);

    game.placeCardAt(Player.RED, 0,0,0);
    System.out.println(view);

    game.placeCardAt(Player.RED, 1,0,0);
    System.out.println(view);
  }

}
