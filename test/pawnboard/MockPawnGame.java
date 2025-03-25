package pawnboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pawnboard.model.Card;
import pawnboard.model.Cell;
import pawnboard.model.Cost;

import pawnboard.model.InfluenceType;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.view.GameObserver;

/**
 * Represents a mock version of the pawn game for testing.
 * Includes a log system to record actions performed.
 */
public class MockPawnGame implements MockGame<PawnCell, Player, PawnCard> {
  private final Appendable log;
  private final int width;
  private final int height;
  private final boolean[][] legalMoves;
  // Stores { RedScore, BlueScore } per row
  private Map<Integer, int[]> fakeRowScores;
  private List<PawnCard> redHand;
  private List<PawnCard> blueHand;

  /**
   * Constructs a MockPawnGame object with the log.
   * The game is initialized with a predefined board size.
   * @param log the appendable
   */
  public MockPawnGame(Appendable log) {
    this.log = log;
    this.width = 5;
    this.height = 3;
    this.legalMoves = new boolean[height][width];
    this.fakeRowScores = new HashMap<>();

    InfluenceType[][] influenceGrid = {
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I, InfluenceType.X},
            {InfluenceType.I, InfluenceType.I, InfluenceType.C, InfluenceType.I, InfluenceType.I},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X},
    };

    PawnCard card = new PawnCard("StubCard", Cost.One, 1, influenceGrid);
    this.redHand = new ArrayList<>();
    this.redHand.add(card);
    this.blueHand = new ArrayList<>();
    this.blueHand.add(card);

    // Default: all moves are illegal unless specified
    for (int r = 0; r < height; r++) {
      fakeRowScores.put(r, new int[]{0, 0}); // Red = 0, Blue = 0
      Arrays.fill(legalMoves[r], false); // All moves illegal by default
    }
  }

  @Override
  public void setLegalMove(int row, int col) {
    if (row > height || col > width || row < 0 || col < 0) {
      throw new IllegalArgumentException("row or or column out of bounds");
    }
      legalMoves[row][col] = true;
      logMessage("Legal move created at: (" + row + ", " + col + ")");

  }

  @Override
  public void setFakeRowScore(int row, Player player, int score) {
    if (row < 0 || row > width || score < 0) {
      throw new IllegalArgumentException("row or score out of bounds");
    }

    int[] scores = fakeRowScores.get(row);
    if (player == Player.RED) {
      scores[0] = score; // Red's score
    } else {
      scores[1] = score; // Blue's score
    }
    fakeRowScores.put(row, scores);
    logMessage("Row " + row + " score for player" + player + ": " + score);
  }

  @Override
  public void addCardToHand(Player player, PawnCard card) {
    if (card == null) {
      throw new IllegalArgumentException("card cannot be null");
    }
    if (player == Player.RED) {
      redHand.add(card);
    }
    else {
      blueHand.add(card);
    }
    logMessage("Card added to " + player + "'s hand: " + card.getName());
  }

  @Override
  public void startGame(List<PawnCard> deck1, List<PawnCard> deck2, int handSize) {
    logMessage("startGame called with path: " + deck1 + deck2 +  ", handSize: " + handSize);
  }

  @Override
  public void placeCardAt(Player player, int cardIdx, int row, int col) {
    logMessage("placeCardAt called with Player: " + player +
            ", cardIdx: " + cardIdx + ", Position: (" + row + ", " + col + ")");
  }

  @Override
  public void passTurn() {
    logMessage("passTurn called with Player: " + getCurrentPlayer());
  }

  @Override
  public void startTurn(Player player) {
    logMessage("startTurn called with Player: " + player);
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
  public boolean placeCardLegal(Player player, PawnCard card, int row, int col) {
    logMessage("Checked legality at (" + row + "," + col + ")");
    return legalMoves[row][col];
  }

  @Override
  public PawnCell[][] getBoard() {
    return null;
  }

  @Override
  public PawnCell getCellAt(int row, int col) {
    return null;
  }

  @Override
  public Player getCurrentPlayer() {
    return null;
  }

  @Override
  public List<PawnCard> getHand(Player player) {
    logMessage("Checked hand of player: " + player);
    return player == Player.RED ? new ArrayList<>(redHand) : new ArrayList<>(blueHand);
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

  @Override
  public void addObserver(GameObserver observer) {
  }

  @Override
  public void removeObserver(GameObserver observer) {
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
