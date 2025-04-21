package pawnboard.controller;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;


import javax.swing.*;

import pawnboard.player.UserPlayer;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.view.BoardPanel;
import pawnboard.view.GameFrame;
import pawnboard.view.HighContrastBoardPanel;
import pawnboard.view.PlayerActionPublisher;


/**
 * Represents controller for the PawnsBoard game.
 * Manages the game flow, handles user input, and updates view.
 *
 */
public class PawnsGameController implements GameController,
        PlayerActionListener, ModelStatusListener {

  private final PawnsGame<PawnCell, Player, PawnCard> model;
  private Player player;
  private final UserPlayer userPlayer;
  private GameFrame playerView;
  private boolean gameOver = false;
  private int selectedCardIdx = -1;
  private int selectedRow = -1;
  private int selectedCol = -1;
  private boolean isHighContrast = false;
  private BoardPanel currentBoardPanel;


  /**
   * Constructs a PawnsGameController with the specified input and output streams.
   *
   * @param model      the game model that manages the state of the game
   * @param userPlayer the UserPlayer instance representing this player
   * @param playerView the view for the specific player
   * @throws IllegalArgumentException if model, userPlayer, playerView, or output are null
   */
  public PawnsGameController(PawnsGame<PawnCell, Player, PawnCard> model,
                             UserPlayer userPlayer, GameFrame playerView) {
    if (model == null || userPlayer == null || playerView == null) {
      throw new IllegalArgumentException(
              "Model, userPlayer, playerView, and output must not be null");
    }
    this.model = model;
    this.player = userPlayer.getPlayer();
    this.userPlayer = userPlayer;
    this.playerView = playerView;
    this.currentBoardPanel = playerView.getBoardPanel();

    playerView.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        // Call handleKeyPress to handle the logic for key press
        char key = e.getKeyChar();  // Get the key pressed
        handleKeyPress(key);
      }
    });

    playerView.setFocusable(true);
    playerView.requestFocusInWindow();

    playerView.getHandPanel().addPlayerActionListener(this);
    playerView.getBoardPanel().addPlayerActionListener(this);
    model.addsModelStatusListener(this);
  }

  @Override
  public void playGame(String redFilePath, String blueFilePath, int handSize) {
    if (redFilePath == null || blueFilePath == null) {
      throw new IllegalArgumentException("Invalid arguments provided.");
    }

    if (gameOver) {
      throw new IllegalStateException("Game has already ended.");
    }

    if (handSize < 1) {
      throw new IllegalArgumentException("Hand size must be greater than 0");
    }

    if (!model.getCurrentPlayer().equals(this.player)) {
      return;
    }

    if (!hasGameStarted(model)) {
      CardInitializer cardInitializer = new CardInitializer();
      Map<String, List<PawnCard>> decks = cardInitializer.initializeDeck(redFilePath, blueFilePath);
      List<PawnCard> deck1 = decks.get("deck1");
      List<PawnCard> deck2 = decks.get("deck2");
      model.startGame(deck1, deck2, handSize);
      updateView();
      onTurnChanged();
    }

    userPlayer.makeMove();
  }

  private void updateView() {
    playerView.updateView();
  }

  private void handleCardClick(int cardIdx) {
    if (cardIdx < 0) {
      throw new IllegalArgumentException("cardidx must be positive");
    }

    List<PawnCard> hand = model.getHand(player);
    if (cardIdx < 0 || cardIdx >= hand.size()) {
      return;
    }

    selectedCardIdx = cardIdx;
    updateView();
  }

  private void handleCellClick(int row, int col) {
    System.out.println("Handling cell click at (" + row + ", " + col + ")");
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException("row and col must be positive");
    }
    if (selectedCardIdx == -1) {
      return;
    }

    this.selectedRow = row;
    this.selectedCol = col;
    updateView();
  }

  private void handleKeyPress(char key) {

    if (key == 'c' || key == 'C') {  // Confirm Move
      if (selectedCardIdx == -1 || selectedRow == -1 || selectedCol == -1) {
        return;
      }

      try {

        model.placeCardAt(player, selectedCardIdx, selectedRow, selectedCol);
        selectedCardIdx = -1;
        selectedRow = -1;
        selectedCol = -1;

        updateView();

      } catch (IllegalArgumentException e) {
        playerView.showInvalidMoveNotification(e.getMessage());
      }
    } else if (key == 'p' || key == 'P') {
      model.passTurn();
      updateView();

    }
    else if (key == 'h' || key == 'H') {
      System.out.println("Handling key press");
      toggleBoardView();

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
    player = model.getCurrentPlayer();
    updateView();

    playerView.updateWindowTitle(player, userPlayer.getPlayer());

    if (player.equals(userPlayer.getPlayer())) {
      userPlayer.makeMove();
      updateView();

      if (!userPlayer.isMachine()) {
        playerView.showTurnNotification(userPlayer);
      }
    }
  }

  @Override
  public void onCardPlaced() {
    updateView();
  }


  @Override
  public void onGameOver() {
    String winner = model.getWinner();
    int redScore = model.checkScore(Player.RED);
    int blueScore = model.checkScore(Player.BLUE);
    if (userPlayer.isMachine()) {
      playerView.showOnGameOver(winner, redScore, blueScore);
    }
    updateView();
    gameOver = true;
  }

  public void toggleBoardView() {

    playerView.remove(currentBoardPanel);

    if (isHighContrast) {
      currentBoardPanel = new BoardPanel(model, playerView.getHandPanel());
    } else {
      System.out.println("Switching to the high contrast board...");
      currentBoardPanel = new HighContrastBoardPanel(model, playerView.getHandPanel());
    }

    currentBoardPanel.addPlayerActionListener(this);

    playerView.add(currentBoardPanel, BorderLayout.CENTER);

    playerView.revalidate();
    playerView.repaint();
    playerView.updateWindowTitle(player, userPlayer.getPlayer());

    this.isHighContrast = !isHighContrast;
    System.out.println(isHighContrast);
  }

}








