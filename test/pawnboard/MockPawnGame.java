package pawnboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pawnboard.model.Card;
import pawnboard.model.Cell;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;

public class MockPawnGame implements MockGame {
  private final Appendable log;
  private final int width;
  private final int height;
  private final boolean[][] legalMoves;
  // Stores { RedScore, BlueScore } per row
  private Map<Integer, int[]> fakeRowScores;

  public MockPawnGame(Appendable log) {
    this.log = log;
    this.width = 3; // Set board dimensions
    this.height = 5;
    this.legalMoves = new boolean[height][width];
    this.fakeRowScores = new HashMap<>();

    // Default: all moves are illegal unless specified
    for (int r = 0; r < height; r++) {
      fakeRowScores.put(r, new int[]{0, 0}); // Red = 0, Blue = 0
      Arrays.fill(legalMoves[r], false); // All moves illegal by default
    }
  }

  @Override
  public void setLegalMove(int row, int col) {
      legalMoves[row][col] = true;
  }

  @Override
  public void setFakeRowScore(int row, Player player, int score) {
    int[] scores = fakeRowScores.get(row);
    if (player == Player.RED) {
      scores[0] = score; // Red's score
    } else {
      scores[1] = score; // Blue's score
    }
    fakeRowScores.put(row, scores);
  }

  @Override
  public void startGame(String path, int handSize) {
    logMessage("startGame called with path: " + path + ", handSize: " + handSize);
  }

  @Override
  public void placeCardAt(Player player, int cardIdx, int row, int col) {
    logMessage("placeCardAt called with Player: " + player +
            ", cardIdx: " + cardIdx + ", Position: (" + row + ", " + col + ")");
  }

  @Override
  public void passTurn(Player player) {
    logMessage("passTurn called with Player: " + player);
  }

  @Override
  public void drawCard(Player player) {
    logMessage("drawCard called with Player: " + player);
  }

  @Override
  public void startTurn(Player player) {
    logMessage("startTurn called with Player: " + player);
  }

  @Override
  public void applyCardInfluence(int boardRow, int boardCol, Player currentPlayer, Card card) {
    logMessage("applyCardInfluence at (" + boardRow + "," + boardCol +
            ") by Player: " + currentPlayer +
            " with Card: " + (card != null ? card.toString() : "null"));
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public boolean placeCardLegal(Player player, Card card, int row, int col) {
    logMessage("Checked legality at (" + row + "," + col + ")");
    return legalMoves[row][col];
  }

  @Override
  public PawnCell[][] getBoard() {
    return null;
  }

  @Override
  public Cell getCellAt(int row, int col) {
    return null;
  }

  @Override
  public Player getCurrentPlayer() {
    return null;
  }

  @Override
  public List<Card> getHand(Player player) {
    logMessage("Checked hand of player: " + player);
    return new ArrayList<>(); // Empty hand for now
  }

  @Override
  public int getRemainingDeckSize(Player player) {
    return 0;
  }

  @Override
  public String getWinner() {
    return "";
  }

  @Override
  public int checkScore(Player player) {
    return 0;
  }

  @Override
  public int checkScoreRow(Player player, int rowIdx) {
    logMessage("Checked row score for player " + player + " at row " + rowIdx);
    int[] scores = fakeRowScores.get(rowIdx);
    return player.equals(Player.RED) ? scores[0] : scores[1];
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public Player getOwnerAt(int row, int col) {
    return null;
  }

  /** Logs a message to the appendable log */
  private void logMessage(String message) {
    try {
      log.append(message).append("\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to write to log", e);
    }
  }
}
