package pawnboard.controller;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.view.BlueFrame;
import pawnboard.view.GameWindow;

import pawnboard.view.RedFrame;

/**
 * Represents controller for the PawnsBoard game.
 * Manages the game flow, handles user input, and updates view.
 *
 */
public class PawnsGameController implements GameController<PawnCell, Player, PawnCard>,
PlayerActionListener {
  private final Appendable output;
  private int selectedCardIdx = -1;
  private int selectedRow = -1;
  private int selectedCol = -1;
  private Player currentPlayer;
  private PawnsGame<PawnCell, Player, PawnCard> model;
  private GameWindow redView;
  private GameWindow blueView;
  private boolean gameOver = false;
  private boolean gameStarted = false;

  /**
   * Constructs a PawnsGameController with the specified input and output streams.
   * @param input the input stream for reading user input
   * @param output the output stream for writing game messages
   * @throws IllegalArgumentException if either input or output is null
   */
  public PawnsGameController(Readable input, Appendable output) {
    if (input == null || output == null) {
      throw new IllegalArgumentException("Input and output must not be null");
    }
    this.output = output;
  }

  @Override
  public void playGame(PawnsGame<PawnCell, Player, PawnCard> model, GameWindow redView,
                       GameWindow blueView,String redFilePath, String blueFilePath, int handSize) {


    if (model == null || redView == null || blueView == null ||
            redFilePath == null || blueFilePath == null) {
      throw new IllegalArgumentException("Invalid arguments provided.");
    }
    if (handSize < 1) {
      throw new IllegalArgumentException("Hand size must be greater than 0");
    }

    if (!hasGameStarted(model)) {
      this.model = model;
      this.redView = redView;
      this.blueView = blueView;

      redView.getBoardPanel().addPlayerActionListener(this);
      blueView.getBoardPanel().addPlayerActionListener(this);
      redView.getHandPanel().addPlayerActionListener(this);
      blueView.getHandPanel().addPlayerActionListener(this);

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

  private void handleCardClick(int cardIdx) {
    if (cardIdx < 0) {
      throw new IllegalArgumentException("cardidx must be positive");
    }

    List<PawnCard> hand = model.getHand(currentPlayer);
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

  private void handleCellClick(int row, int col) {
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException("row and col must be positive");
    }
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

  private void handleKeyPress(char key) {
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

  private boolean hasGameStarted(PawnsGame<PawnCell, Player, PawnCard> model) {
    try {
      model.getHand(Player.RED);
      return true;
    } catch (IllegalStateException e) {
      return false;
    }
  }


  /**
   * Main method to test the controller.
   * @param args argument
   */
  public static void main(String[] args) {
    String simulatedInput = "place 0 0 0";
    Readable input = new StringReader(simulatedInput);
    Appendable output = new StringWriter();

    String deckPath = "docs" + File.separator + "deck.config";

    CardInitializer cardInitializer = new CardInitializer();
    Map<String, List<PawnCard>> decks = cardInitializer.initializeDeck(deckPath, deckPath);
    List<PawnCard> deck1 = decks.get("deck1");
    List<PawnCard> deck2 = decks.get("deck2");


    PawnGameImpl model = new PawnGameImpl(3, 5);


    PawnsGameController controller = new PawnsGameController(input, output);

    model.startGame(deck1, deck2, 5);


    GameWindow redView = new RedFrame(model);
    GameWindow blueView = new BlueFrame(model);

    controller.playGame(model, redView, blueView, deckPath, deckPath, 5);

    System.out.println(output.toString());
  }


  @Override
  public void onCardClick(int cardIdx) {
    handleCardClick(cardIdx);
  }

  @Override
  public void onCellClick(int row, int col) {
    handleCellClick(row, col);
  }

  @Override
  public void onConfirmTurn() {
    handleKeyPress('c');
  }

  @Override
  public void onPassTurn() {
    handleKeyPress('p');
  }
}






