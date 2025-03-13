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

    game.drawCard(Player.RED);
    System.out.println("Initial Board:");
    System.out.println(view);

    game.drawCard(Player.RED);
    System.out.println(game.getHand(Player.RED).size());
    game.placeCardAt(Player.RED, 0,0,0);
    System.out.println(view);

    game.placeCardAt(Player.BLUE, 0,0,4);
    System.out.println(view);

    System.out.println("Player Red Hand Size Before Pass:" + game.getHand(Player.RED).size());
    game.passTurn(Player.RED);
    System.out.println("Player Red Hand Size:" + game.getHand(Player.RED).size());

  }


}
