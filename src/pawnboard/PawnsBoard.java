package pawnboard;

import java.io.File;

import pawnboard.model.CardInitializer;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;

public final class PawnsBoard {


  public static void main(String[] args) {
    String path = "docs" + File.separator + "deck.config";
    PawnGameImpl game = new PawnGameImpl(3,5);
    game.startGame(path,5);

    game.placeCardAt(Player.RED, 0,0,0);



  }
}
