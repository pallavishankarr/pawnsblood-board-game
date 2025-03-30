package pawnboard.controller;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import pawnboard.Player.HumanPlayer;
import pawnboard.Player.MachinePlayer;
import pawnboard.Player.UserPlayer;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.strategy.FillFirstStrategy;
import pawnboard.strategy.PawnBoardStrategy;
import pawnboard.view.BlueFrame;
import pawnboard.view.GameWindow;

import pawnboard.view.RedFrame;

/**
 * Represents controller for the PawnsBoard game.
 * Manages the game flow, handles user input, and updates view.
 *
 */
public class PawnsGameController implements GameController,
PlayerActionListener, ModelStatusListener {
  private final Appendable output;
  private final PawnsGame<PawnCell, Player, PawnCard> model;
  private Player currentPlayer;
  private final UserPlayer userPlayer;
  private final GameWindow playerView;
  private UserPlayer opponentPlayer;
  private GameWindow opponentView;
  private boolean gameOver = false;
  private int selectedCardIdx = -1;
  private int selectedRow = -1;
  private int selectedCol = -1;

  /**
   * Constructs a PawnsGameController with the specified input and output streams.
   * @param model the game model that manages the state of the game
   * @param userPlayer the UserPlayer instance representing this player
   * @param playerView the view for the specific player
   * @param opponentPlayer the opponent UserPlayer
   * @param opponentView the opponent's view
   * @param output the appendable output to show game messages
   * @throws IllegalArgumentException if model, userPlayer, playerView, or output are null
   */
  public PawnsGameController(PawnsGame<PawnCell, Player, PawnCard> model,
                             UserPlayer userPlayer, GameWindow playerView,
                             UserPlayer opponentPlayer, GameWindow opponentView,
                             Appendable output) {
    if (model == null || userPlayer == null || playerView == null || output == null ||
            opponentPlayer == null || opponentView == null) {
      throw new IllegalArgumentException("Model, userPlayer, playerView, and output must not be null");
    }
    this.model = model;
    this.userPlayer = userPlayer;
    this.playerView = playerView;
    this.output = output;
    this.opponentPlayer = opponentPlayer;
    this.opponentView = opponentView;

    playerView.getHandPanel().addPlayerActionListener(this);
    playerView.getBoardPanel().addPlayerActionListener(this);
    model.addsModelStatusListener(this);
  }

  @Override
  public void playGame(String redFilePath, String blueFilePath, int handSize) {
    if (redFilePath == null || blueFilePath == null) {
      throw new IllegalArgumentException("Invalid arguments provided.");
    }
    if (handSize < 1) {
      throw new IllegalArgumentException("Hand size must be greater than 0");
    }

    if (!hasGameStarted(model)) {
      CardInitializer cardInitializer = new CardInitializer();
      Map<String, List<PawnCard>> decks = cardInitializer.initializeDeck(redFilePath, blueFilePath);
      List<PawnCard> deck1 = decks.get("deck1");
      List<PawnCard> deck2 = decks.get("deck2");
      model.startGame(deck1, deck2, handSize);
      currentPlayer = model.getCurrentPlayer();
      updateView();
    }
  }

  private void updateView() {
    playerView.updateView(); // Refresh the current player's view
    opponentView.updateView(); // Refresh the opponent's view
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

    updateView();
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

    updateView();
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
        updateView();
      } catch (IllegalArgumentException e) {
        appendOutput("Invalid move: " + e.getMessage() + "\n");
      }
    } else if (key == 'p' || key == 'P') {  // Pass Turn
      model.passTurn();
      appendOutput("Turn passed.\n");
      currentPlayer = model.getCurrentPlayer();
      updateView();
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

  @Override
  public void onTurnChanged() {
    currentPlayer = model.getCurrentPlayer();
    updateView();
    appendOutput("It is now " + currentPlayer + "'s turn.\n");
  }

  @Override
  public void onScoreUpdate() {
    updateView();
    appendOutput("Score updated:\n");
    appendOutput("Red: " + model.checkScore(Player.RED) + "\n");
    appendOutput("Blue: " + model.checkScore(Player.BLUE) + "\n");
  }


  @Override
  public void onGameOver() {
    String winner = model.getWinner();  // Assuming model has a method getWinner()
    appendOutput("Game Over! The winner is: " + winner + "\n");
    updateView();
    gameOver = true;
  }

  public static void main(String[] args) {
    String deckPath = "docs" + File.separator + "deck.config";

    CardInitializer cardInitializer = new CardInitializer();
    Map<String, List<PawnCard>> decks = cardInitializer.initializeDeck(deckPath, deckPath);
    List<PawnCard> deck1 = decks.get("deck1");
    List<PawnCard> deck2 = decks.get("deck2");

    PawnGameImpl model = new PawnGameImpl(3, 5);
    GameWindow redView = new RedFrame(model);
    GameWindow blueView = new BlueFrame(model);

    PawnBoardStrategy strat = new FillFirstStrategy();

    UserPlayer bluePlayer = new MachinePlayer(Player.BLUE, model, strat);

    UserPlayer redPlayer = new HumanPlayer(Player.RED, model);

    Appendable output = new StringWriter();

    PawnsGameController controller = new PawnsGameController(model, redPlayer, redView, bluePlayer, blueView, output);

    ((HumanPlayer) redPlayer).addPlayerActionListener(controller);

    controller.playGame(deckPath, deckPath, 5);

    System.out.println(output.toString());
  }
}






