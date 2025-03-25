package pawnboard.strategy;

import pawnboard.model.Card;

public class Move {
  private final Card card;
  private final int row;
  private final int col;

  public Move(Card card, int row, int col) {
    this.card = card;
    this.row = row;
    this.col = col;
  }

  public Card getCard() {
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
      Move move = (Move) o;
      return row == move.row && col == move.col && card.equals(move.card);
    } else {
      return false;
    }

  }

}