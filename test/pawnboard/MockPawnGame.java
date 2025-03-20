package pawnboard;

import java.util.ArrayList;
import java.util.List;

import pawnboard.model.Card;
import pawnboard.model.Cell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;

public class MockPawnGame implements PawnsGame<Cell> {
  private final List<String> log;

  public MockPawnGame() {
    this.log = new ArrayList<>();
  }

  @Override
  public void startGame(String path, int handSize) {
    String message = "startGame called with path: " + path + ", handSize: " + handSize;
    log.add(message);
    System.out.println(message);
  }

  @Override
  public void placeCardAt(Player player, int cardIdx, int row, int col) {
    String message = "placeCardAt called with Player: " + player + ", cardIdx: " + cardIdx + ", Position: (" + row + ", " + col + ")";
    log.add(message);
    System.out.println(message);
  }

  @Override
  public void passTurn(Player player) {
    String message = "passTurn called with Player: " + player;
    log.add(message);
    System.out.println(message);
  }

  @Override
  public void drawCard(Player player) {
    String message = "drawCard called with Player: " + player;
    log.add(message);
    System.out.println(message);
  }

  @Override
  public void startTurn(Player player) {
    String message = "startTurn called with Player: " + player;
    log.add(message);
    System.out.println(message);
  }

  @Override
  public void applyCardInfluence(int boardRow, int boardCol, Player currentPlayer, Card card) {
    String message = "applyCardInfluence called at (" + boardRow + ", " + boardCol + ") by Player: " + currentPlayer + " with Card: " + card;
    log.add(message);
    System.out.println(message);
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public boolean placeCardLegal(Player player, Card card, int row, int col) {
    return true;
  }

  @Override
  public Cell[][] getBoard() {
    return new Cell[0][];
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
    return List.of();
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
    return 0;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public Player getOwnerAt(int row, int col) {
    return null;
  }
}
