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
import pawnboard.view.GameFrame;
import pawnboard.view.GameWindow;

import pawnboard.view.RedFrame;



public class PawnsGameController implements GameController {
  private final Readable input;
  private final Appendable output;
  private int selectedCardIdx = -1;
  private Player currentPlayer;
  private PawnsGame<PawnCell> model;
  private GameWindow redView;
  private GameWindow blueView;
  private boolean gameOver = false;

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
    if (redView == null || blueView == null ||
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

    redView.updateView();
    blueView.updateView();

    // **Game Loop**
    while (!gameOver && !model.isGameOver()) {
      try {
        output.append("\nCurrent Player: " + currentPlayer.toString() + "\n");
        output.append("Enter move: (place [card_index] [row] [col], pass, confirm, quit)\n");
        output.append("> ");


        if (!sc.hasNextLine()) {
          break; // Exit if no more input
        }

        String userInput = sc.nextLine().trim();
        if (userInput.isEmpty()) {
          continue;
        }

        // Process user input
        String[] tokens = userInput.split("\\s+");
        switch (tokens[0].toLowerCase()) {
          case "place":
            if (tokens.length == 4) {
              try {
                int cardIndex = Integer.parseInt(tokens[1]);
                int row = Integer.parseInt(tokens[2]);
                int col = Integer.parseInt(tokens[3]);
                model.placeCardAt(currentPlayer,cardIndex, row, col);
              } catch (NumberFormatException e) {
                output.append("Invalid input. Usage: place [card_index] [row] [col]\n");
              }
            } else {
              output.append("Usage: place [card_index] [row] [col]\n");
            }
            break;

          case "pass":
            handleKeyPress('p');
            break;

          case "confirm":
            handleKeyPress('c');
            break;

          case "quit":
            handleKeyPress('q');
            break;

          default:
            output.append("Unknown command. Use place, pass, confirm, or quit.\n");
            break;
        }

        // Refresh game state
        redView.updateView();
        blueView.updateView();

      } catch (IOException e) {
        throw new IllegalStateException("Error writing to output", e);
      }
    }

    try {
      output.append("\nGame Over! " + model.getWinner() + " wins!\n");
    } catch (IOException e) {
      throw new IllegalStateException("Error writing final output", e);
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

    // Print to console (stub controller requirement)
    System.out.println("Card clicked: Index " + cardIdx + ", Player: " + currentPlayer.toString());

    // Visually highlight the card in the UI
    redView.updateView();
    blueView.updateView();

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

    // Print to console (stub controller requirement)
    System.out.println("Cell clicked: Row " + row + ", Column " + col);

    // Highlight the cell in the UI
    redView.updateView();
    blueView.updateView();

    try {
      model.placeCardAt(currentPlayer, selectedCardIdx, row, col);
      currentPlayer = model.getCurrentPlayer();

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
        output.append("Turn passed \n");
        System.out.println("Key pressed: P (Pass)");
        currentPlayer = model.getCurrentPlayer();
        redView.updateView();
        blueView.updateView();
      } else if (key == '\n' || key == 'c' || key == 'C') {  // Enter or 'C' for confirm
        System.out.println("Key pressed: Enter/C (Confirm Move)");
        output.append("Move confirmed.\n");
      } else if (key == 'q' || key == 'Q') {
        output.append("Game ended by user.\n");
        gameOver = true; // Stop the game loop
        System.out.println("Key pressed: Q (Quit Game)");
      } else {
        output.append("Invalid key press. Use 'p' to pass, 'c' to confirm, 'q' to quit.\n");
      }
    } catch (IOException e) {
      throw new IllegalStateException("Failed to write to output", e);
    }
  }




public static void main(String[] args) {
    String simulatedInput = "place 0 0 0";
    Readable input = new StringReader(simulatedInput);
    Appendable output = new StringWriter();

    String deckPath = "docs" + File.separator + "biggerDeck";
    CardInitializer cardInitializer = new CardInitializer();
    Map<String, List<PawnCard>> decks = cardInitializer.initializeDeck(deckPath, deckPath);
    List<PawnCard> deck1 = decks.get("deck1");
    List<PawnCard> deck2 = decks.get("deck2");


    PawnGameImpl model = new PawnGameImpl(3, 5);
    PawnsGameController controller = new PawnsGameController(input, output);



    GameWindow redView = new RedFrame(model);
    GameWindow blueView = new BlueFrame(model);

    controller.playGame(model, redView, blueView, deckPath, deckPath, 5);

    System.out.println(output.toString());
  }



}






