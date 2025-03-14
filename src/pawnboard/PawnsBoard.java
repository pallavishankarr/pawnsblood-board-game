package pawnboard;

import java.io.File;

import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;
import pawnboard.view.GameView;
import pawnboard.view.PawnGameTextualView;

/**
 * Main method meant to run a sample game of Pawns Game.
 */
public final class PawnsBoard {

  /**
   * The main method runs a sample game.
   * It initializes the game with board size, loads the deck,
   * and executes predefined moves between the two players.
   * This game shows alternates between placing and passing cards to
   * completion
   * @param args the command-line arguments (not used in this implementation)
   */
  public static void main(String[] args) {
    String path = "docs" + File.separator + "deck.config";
    PawnGameImpl game = new PawnGameImpl(3,5);
    GameView view = new PawnGameTextualView(game);
    game.startGame(path,5);

    System.out.println("Initial Board:");
    System.out.println(view);
    placeCard(game, view, Player.RED, 0, 1,0);
    placeCard(game, view, Player.BLUE, 0,1,4);
    passTurn(game, view, Player.RED);
    placeCard(game, view, Player.BLUE, 0,2,4);
    placeCard(game, view, Player.RED, 0,0,0);
    placeCard(game, view, Player.BLUE, 0,1,3);
    passTurn(game, view, Player.RED);
    placeCard(game, view, Player.BLUE, 0,0,4);
    placeCard(game, view, Player.RED, 0,2,0);
    placeCard(game, view, Player.BLUE, 1,0,3);
    placeCard(game, view, Player.RED, 2,2,1);
    placeCard(game, view, Player.BLUE, 4,0,2);
    placeCard(game, view, Player.RED, 4,2,2);
    placeCard(game, view, Player.BLUE, 2,1,2);
    placeCard(game, view, Player.RED, 4,1,1);
    System.out.println(game.getWinner());
  }

  private static void placeCard(PawnGameImpl game, GameView view,
                                Player player, int cardIdx, int row, int col) {
    game.startTurn(player);
    game.placeCardAt(player, cardIdx, row, col);
    System.out.println(view);
  }

  private static void passTurn(PawnGameImpl game, GameView view, Player player) {
    game.startTurn(player);
    game.passTurn(player);
    System.out.println(view);
  }


}
