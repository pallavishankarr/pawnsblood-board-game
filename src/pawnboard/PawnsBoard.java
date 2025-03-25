package pawnboard;

import java.io.File;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import pawnboard.controller.CardInitializer;
import pawnboard.controller.PawnsGameController;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;
import pawnboard.view.BlueFrame;
import pawnboard.view.GameWindow;
import pawnboard.view.RedFrame;

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
   *
   * @param args the command-line arguments (not used in this implementation)
   */
  public static void main(String[] args) {
    String path = "docs" + File.separator + "deck.config";
    Readable input = new StringReader("");
    Appendable output = new StringBuilder();

    PawnGameImpl model = new PawnGameImpl(3, 5);
    PawnsGameController controller = new PawnsGameController(input, output);

    CardInitializer cardInitializer = new CardInitializer();
    Map<String, List<PawnCard>> decks = CardInitializer.initializeDeck(path, path);
    List<PawnCard> deck1 = decks.get("deck1");
    List<PawnCard> deck2 = decks.get("deck2");

    model.startGame(deck1, deck2, 5);

    RedFrame redFrame = new RedFrame(model);
    BlueFrame blueFrame = new BlueFrame(model);


    controller.playGame(model, redFrame, blueFrame, path, path, 5);

    System.out.println(output);

    redFrame.setVisible(true);
    blueFrame.setVisible(true);

    System.out.println("Initial Board:");
    System.out.println(redFrame);
    placeCard(model, redFrame, Player.RED, 0, 1, 0);
    placeCard(model, blueFrame, Player.BLUE, 0, 1, 4);
    passTurn(model, redFrame, Player.RED);
    placeCard(model, blueFrame, Player.BLUE, 0, 2, 4);
    placeCard(model, redFrame, Player.RED, 0, 0, 0);
    placeCard(model, blueFrame, Player.BLUE, 0, 1, 3);
    passTurn(model, redFrame, Player.RED);
    placeCard(model, blueFrame, Player.BLUE, 0, 0, 4);
    placeCard(model, redFrame, Player.RED, 0, 2, 0);
    placeCard(model, blueFrame, Player.BLUE, 1, 0, 3);
    placeCard(model, redFrame, Player.RED, 2, 2, 1);
    placeCard(model, blueFrame, Player.BLUE, 4, 0, 2);
    placeCard(model, redFrame, Player.RED, 4, 2, 2);
    placeCard(model, blueFrame, Player.BLUE, 2, 1, 2);
    placeCard(model, redFrame, Player.RED, 4, 1, 1);
    System.out.println(model.getWinner());
  }

  private static void placeCard(PawnGameImpl game, GameWindow window,
                                Player player, int cardIdx, int row, int col) {
    game.startTurn(player);
    game.placeCardAt(player, cardIdx, row, col);
    System.out.println(window);
  }

  private static void passTurn(PawnGameImpl game, GameWindow window, Player player) {
    game.startTurn(player);
    game.passTurn();
    System.out.println(window);
  }


}
