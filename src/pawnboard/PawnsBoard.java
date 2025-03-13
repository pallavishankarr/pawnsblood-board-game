package pawnboard;

import java.io.File;

import pawnboard.model.CardInitializer;
import pawnboard.model.Cell;
import pawnboard.model.PawnCard;
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

    game.startTurn(Player.RED);
    System.out.println(game.getHand(Player.RED).size());
    game.placeCardAt(Player.RED, 0,1,0);
    System.out.println(view);
    System.out.println(game.getHand(Player.RED).size());

    // security
    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0,1,4);
    System.out.println(view);

    game.startTurn(Player.RED);
    System.out.println("Player Red Hand Size Before Pass:" + game.getHand(Player.RED).size());
    game.passTurn(Player.RED);
    System.out.println("Player Red Hand Size:" + game.getHand(Player.RED).size());

    // bee
    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0,2,4);
    System.out.println(view);

    // bee
    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0,0,0);
    System.out.println(view);

    // mandragora
    // blue is occupying previously red pawns
    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0,1,3);
    System.out.println(view);
    
    game.startTurn(Player.RED);
    System.out.println(game.getHand(Player.RED).size());
    game.passTurn(Player.RED);
    System.out.println(view);

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 0,1,1);
    System.out.println(view);

    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 0, 2, 0);
    System.out.println(view);

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 2, 0, 4);
    System.out.println(view);

    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 2, 2, 1);
    System.out.println(view);

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 4, 0, 3);
    System.out.println(view);

    game.startTurn(Player.RED);
    game.placeCardAt(Player.RED, 4, 2, 2);
    System.out.println(view);

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 1, 1, 2);
    System.out.println(view);

    game.startTurn(Player.RED);
    game.passTurn(Player.RED);
    System.out.println(view);

    game.startTurn(Player.BLUE);
    game.placeCardAt(Player.BLUE, 2, 0, 2);
    System.out.println(view);

  }


}
