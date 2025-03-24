package pawnboard.controller;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import pawnboard.model.Card;
import pawnboard.model.Cell;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.view.BlueFrame;
import pawnboard.view.GameWindow;

import pawnboard.view.RedFrame;



public class PawnsGameController implements GameController {
  private final Readable input;
  private final Appendable output;
  private int selectedCardIdx = -1; // Track selected card index (-1 means no selection)
  private Player currentPlayer;// Track the current player
  private PawnsGame<PawnCell> model;
  private GameWindow redView;
  private GameWindow blueView;

  public PawnsGameController(Readable input, Appendable output) {
    if (input == null || output == null) {
      throw new IllegalArgumentException("Input and output must not be null");
    }
    this.input = input;
    this.output = output;
  }

  @Override
  public <C extends Cell> void playGame(PawnsGame<PawnCell> model, GameWindow redView,
                                        GameWindow blueView, String redFilePath,
                                        String blueFilePath, int handSize) {
    if (model == null || redView == null || blueView == null ||
            redFilePath == null || blueFilePath == null) {
      throw new IllegalArgumentException("Invalid arguments provided.");
    }
    if (handSize < 1) {
      throw new IllegalArgumentException("Hand size must be greater than 0");
    }


    this.model = model;
    this.redView = redView;
    this.blueView = blueView;

    // Initialize decks and start the game
    CardInitializer cardInitializer = new CardInitializer();
    Map<String, List<PawnCard>> decks = cardInitializer.initializeDeck(redFilePath, blueFilePath);
    List<PawnCard> deck1 = decks.get("deck1");
    List<PawnCard> deck2 = decks.get("deck2");
    model.startGame(deck1, deck2, handSize);
    currentPlayer = model.getCurrentPlayer();

    Scanner sc = new Scanner(input);

    // Update GUI before the game starts
    redView.updateView();
    blueView.updateView();

    while (!model.isGameOver()) {
      try {
        output.append("Enter a command (, p, q): \n");
        output.append(model.toString()).append("\n");

        if (!sc.hasNext()) {
          break;
        }

        String command = sc.nextLine().trim();
        String[] parts = command.split(" ");

        if (command.equalsIgnoreCase("quit")) {
          output.append("Game ended by user.\n");
          break;
        } else if (command.equalsIgnoreCase("pass")) {
          model.passTurn();
          currentPlayer = model.getCurrentPlayer(); // Update current player
        } else if (parts.length == 4 && parts[0].equalsIgnoreCase("place")) {
          try {
            int cardIndex = Integer.parseInt(parts[1]);
            int row = Integer.parseInt(parts[2]);
            int col = Integer.parseInt(parts[3]);
            model.placeCardAt(currentPlayer, cardIndex, row, col);
          } catch (NumberFormatException e) {
            output.append("Invalid move format. Use: place <index> <row> <col>\n");
          }
        } else {
          output.append("Invalid command. Try again.\n");
        }


        redView.updateView();
        blueView.updateView();

      } catch (IOException e) {
        throw new IllegalStateException("Failed to write to output", e);
      }
    }

    try {
      output.append("Game Over!\n");
      output.append("Winner: ").append(model.getWinner()).append("\n");
    } catch (IOException e) {
      throw new IllegalStateException("Failed to write to output", e);
    }
  }

  @Override
  public void handleCardClick(int cardIdx, Player player) {
    if (player != currentPlayer) {
      try {
        output.append("It's not your turn!\n");
      } catch (IOException e) {
        throw new IllegalStateException("Failed to write to output", e);
      }
      return;
    }

    List<Card> hand = model.getHand(currentPlayer);
    if (cardIdx < 0 || cardIdx >= hand.size()) {
      try {
        output.append("Invalid card selection.\n");
      } catch (IOException e) {
        throw new IllegalStateException("Failed to write to output", e);
      }
      return;
    }

    selectedCardIdx = cardIdx;
    try {
      output.append("Card selected: " + hand.get(cardIdx).toString() + "\n");
    } catch (IOException e) {
      throw new IllegalStateException("Failed to write to output", e);
    }
  }

  @Override
  public void handleCellClick(int row, int col) {
    if (selectedCardIdx == -1) {
      try {
        output.append("Select a card first before placing it.\n");
      } catch (IOException e) {
        throw new IllegalStateException("Failed to write to output", e);
      }
      return;
    }

    try {
      model.placeCardAt(currentPlayer, selectedCardIdx, row, col);
      output.append("Card placed at (" + row + ", " + col + ").\n");
      selectedCardIdx = -1; // Reset selection after placing
      redView.updateView();
      blueView.updateView();
    } catch (IllegalArgumentException | IOException e) {
      try {
        output.append("Invalid move: " + e.getMessage() + "\n");
      } catch (IOException ioException) {
        throw new IllegalStateException("Failed to write to output", ioException);
      }
    }
  }

  @Override
  public void handleKeyPress(char key) {
    try {
      if (key == 'p' || key == 'P') {
        model.passTurn();
        output.append("Turn passed.\n");
        currentPlayer = model.getCurrentPlayer();
        redView.updateView();
        blueView.updateView();
      } else if (key == 'q' || key == 'Q') {
        output.append("Game ended by user.\n");
        System.exit(0);
      } else {
        output.append("Invalid key press. Use 'p' to pass, 'q' to quit.\n");
      }
    } catch (IOException e) {
      throw new IllegalStateException("Failed to write to output", e);
    }
  }



public static void main(String[] args) {
    String simulatedInput = "place 0 0 0\npass\nquit\n";
    Readable input = new StringReader(simulatedInput);
    Appendable output = new StringWriter();

    String deckPath = "docs" + File.separator + "biggerDeck";
    CardInitializer cardInitializer = new CardInitializer();
    Map<String, List<PawnCard>> decks = cardInitializer.initializeDeck(deckPath, deckPath);
    List<PawnCard> deck1 = decks.get("deck1");
    List<PawnCard> deck2 = decks.get("deck2");


    PawnGameImpl model = new PawnGameImpl(5, 7);
    PawnsGameController controller = new PawnsGameController(input, output);
    model.startGame(deck1,deck2, 5);

    GameWindow redView = new RedFrame(model);
    GameWindow blueView = new BlueFrame(model);

    controller.playGame(model, redView, blueView, deckPath, deckPath, 5);

    System.out.println(output.toString());
  }



}






