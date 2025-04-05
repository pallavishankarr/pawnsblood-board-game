package pawnboard;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import pawnboard.Player.HumanPlayer;
import pawnboard.Player.MachinePlayer;
import pawnboard.Player.UserPlayer;
import pawnboard.controller.CardInitializer;
import pawnboard.controller.PawnsGameController;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;
import pawnboard.strategy.FillFirstStrategy;
import pawnboard.strategy.MaxControlStrategy;
import pawnboard.strategy.MaximizeRowScoreStrategy;
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
    Scanner scanner = new Scanner(System.in);

// Get deck file paths
    String redFilePath = args.length > 0 ? args[0] : getUserInput(scanner, "Enter Red deck path: ");
    String blueFilePath = args.length > 1 ? args[1] : getUserInput(scanner, "Enter Blue deck path: ");

    // Get player types
    String redPlayerType = args.length > 2 ? args[2] : getUserInput(scanner, "Enter Red player type (human/strategy1/etc.): ");
    String bluePlayerType = args.length > 3 ? args[3] : getUserInput(scanner, "Enter Blue player type (human/strategy1/etc.): ");

    // Load decks
    CardInitializer cardInitializer = new CardInitializer();
    Map<String, List<PawnCard>> decks = cardInitializer.initializeDeck(redFilePath, blueFilePath);
    List<PawnCard> redDeck = decks.get("deck1");
    List<PawnCard> blueDeck = decks.get("deck2");

    // Initialize model
    PawnGameImpl model = new PawnGameImpl(3, 5);
    model.startGame(redDeck, blueDeck, 5);

    // Create views
    GameWindow redView = new RedFrame(model);
    GameWindow blueView = new BlueFrame(model);

    // Create players
    UserPlayer redPlayer = createUserPlayer(redPlayerType, Player.RED, model);
    UserPlayer bluePlayer = createUserPlayer(bluePlayerType, Player.BLUE, model);

    // Appendable for logging
    Appendable output = new StringWriter();

    // Create controllers
    PawnsGameController redController = new PawnsGameController(model, redPlayer, redView);
    PawnsGameController blueController = new PawnsGameController(model, bluePlayer, blueView);

    // Start game
    redController.playGame(redFilePath, blueFilePath, 5);
    blueController.playGame(redFilePath, blueFilePath, 5);

    System.out.println(output.toString());
  }

  private static String getUserInput(Scanner scanner, String prompt) {
    System.out.print(prompt);
    return scanner.nextLine();
  }

  private static UserPlayer createUserPlayer(String playerType, Player player, PawnGameImpl model) {
    switch (playerType.toLowerCase()) {
      case "human":
        return new HumanPlayer(player, model);
      case "strategy1":
        return new MachinePlayer(player, model, new FillFirstStrategy());
      case "strategy2":
        return new MachinePlayer(player, model, new MaximizeRowScoreStrategy());
      case "strategy3":
        return new MachinePlayer(player, model, new MaxControlStrategy());
      default:
        throw new IllegalArgumentException("Invalid player type: " + playerType);
    }
  }
}