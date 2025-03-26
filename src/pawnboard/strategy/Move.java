package pawnboard.strategy;

import java.util.Objects;

import pawnboard.model.Card;

/**
 * Represents a move in the Pawns Board game.
 * A move consists of a card, a row, and a column on the board.
 */
public class Move<T extends Card> {
  private final T card;
  private final int row;
  private final int col;

  /**
   * Constructs a move with the given card and placement coordinates.
   * @param card the card to be placed
   * @param row the row (0-index based)
   * @param col the col (0-index based)
   * @throws IllegalArgumentException if the card is null
   */
  public Move(T card, int row, int col) {
    if (card == null) {
      throw new IllegalArgumentException("card cannot be null");
    }
    this.card = card;
    this.row = row;
    this.col = col;
  }

  public T getCard() {
    return card;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  @Override
  public String toString() {
    return "Move{" +
            "card=" + card.toString() +
            ", row=" + row +
            ", col=" + col +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Move) {
      Move<T> move = (Move<T>) o;
      return row == move.row && col == move.col && card.equals(move.card);
    } else {
      return false;
    }

  }

  @Override
  public int hashCode() {
    return Objects.hash(card, row, col);


  }

}