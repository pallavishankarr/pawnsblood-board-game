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
  private int selectedRow = -1;
  private int selectedCol = -1;
  private Player currentPlayer;
  private PawnsGame<PawnCell> model;
  private GameWindow redView;
  private GameWindow blueView;
  private boolean gameOver = false;
  private boolean gameStarted = false;

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

    if(!hasGameStarted(model)) {
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


    }

    this.model = model;
    this.redView = redView;
    this.blueView = blueView;

    currentPlayer = model.getCurrentPlayer();

    redView.updateView();
    blueView.updateView();
  }

  @Override
  public void handleCardClick(int cardIdx, Player player) {
    if (player != currentPlayer) {
      appendOutput("It's not your turn!\n");
      return;
    }

    List<Card> hand = model.getHand(currentPlayer);
    if (cardIdx < 0 || cardIdx >= hand.size()) {
      appendOutput("Invalid card selection.\n");
      return;
    }

    selectedCardIdx = cardIdx;
    selectedRow = -1;
    selectedCol = -1;

    appendOutput("Card selected: " + hand.get(cardIdx).toString() + "\n");

    // Visually highlight the card
    redView.updateView();
    blueView.updateView();
  }

  @Override
  public void handleCellClick(int row, int col) {
    if (selectedCardIdx == -1) {
      appendOutput("Select a card first before choosing a cell.\n");
      return;
    }

    selectedRow = row;
    selectedCol = col;

    appendOutput("Cell selected: (" + row + ", " + col + "). Press confirm to place the card.\n");

    // Visually highlight the selected cell
    redView.updateView();
    blueView.updateView();
  }

  @Override
  public void handleKeyPress(char key) {
    if (key == 'c' || key == 'C') {  // Confirm Move
      if (selectedCardIdx == -1 || selectedRow == -1 || selectedCol == -1) {
        appendOutput("Select a card and a cell before confirming.\n");
        return;
      }

      try {
        model.placeCardAt(currentPlayer, selectedCardIdx, selectedRow, selectedCol);
        appendOutput("Card placed at (" + selectedRow + ", " + selectedCol + ").\n");

        // Reset selections
        selectedCardIdx = -1;
        selectedRow = -1;
        selectedCol = -1;

        // Move to next player
        currentPlayer = model.getCurrentPlayer();
        redView.updateView();
        blueView.updateView();
      } catch (IllegalArgumentException e) {
        appendOutput("Invalid move: " + e.getMessage() + "\n");
      }
    } else if (key == 'p' || key == 'P') {  // Pass Turn
      model.passTurn();
      appendOutput("Turn passed.\n");
      currentPlayer = model.getCurrentPlayer();
      redView.updateView();
      blueView.updateView();
    } else if (key == 'q' || key == 'Q') {  // Quit Game
      appendOutput("Game ended by user.\n");
      gameOver = true;
    } else {
      appendOutput("Invalid key press. Use 'c' to confirm, 'p' to pass, 'q' to quit.\n");
    }
  }

  private void appendOutput(String message) {
    try {
      output.append(message);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to write to output", e);
    }
  }

  private boolean hasGameStarted(PawnsGame<PawnCell> model) {
    try {
      model.getHand(Player.RED);  // Try accessing a player's hand
      return true;  // No exception = game started
    } catch (IllegalStateException e) {
      return false; // Exception = game not started
    }
  }





  public static void main(String[] args) {
    String simulatedInput = "place 0 0 0";
    Readable input = new StringReader(simulatedInput);
    Appendable output = new StringWriter();

    String deckPath = "docs" + File.separator + "deck.config";

    CardInitializer cardInitializer = new CardInitializer();
    Map<String, List<PawnCard>> decks = cardInitializer.initializeDeck(deckPath, deckPath);
    List<PawnCard> deck1 = decks.get("deck1");
    List<PawnCard> deck2 = decks.get("deck2");

    // Create the game model
    PawnGameImpl model = new PawnGameImpl(3, 5);

    // Create the controller
    PawnsGameController controller = new PawnsGameController(input, output);

    model.startGame(deck1, deck2, 5);

    // Create the views
    GameWindow redView = new RedFrame(model);
    GameWindow blueView = new BlueFrame(model);

    // Start the game using playGame (this will initialize the game properly)
    controller.playGame(model, redView, blueView, deckPath, deckPath, 5);

    System.out.println(output.toString());
  }




}






