package pawnboard.strategy;

import java.util.Objects;

import pawnboard.model.Card;
import pawnboard.model.PawnCard;

public class Move {
  private final PawnCard card;
  private final int row;
  private final int col;

  public Move(PawnCard card, int row, int col) {
    this.card = card;
    this.row = row;
    this.col = col;
  }

  public PawnCard getCard() {
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
    if (this == o) return true; // Same reference, so they are equal
    if (o == null || getClass() != o.getClass()) return false; // Null check and class check

    Move move = (Move) o;
    return row == move.row &&
            col == move.col &&
            card.equals(move.card); // Ensure card also has a proper equals() implementation
  }


  @Override
  public int hashCode() {
    return Objects.hash(card, row, col);
  }


}