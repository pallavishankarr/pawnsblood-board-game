package pawnboard.model;

/**
 * PawnCell implements the class Cell and contains the fields
 * of the owner, pawn count of the cell, card of the cell  and if the cell
 * is empty or not.
 */
public class PawnCell implements Cell {

  private Player owner;
  private int pawnCount;
  private Card card;

  public PawnCell() {
    this.owner = null;
    this.pawnCount = 0;
    this.card = null;
  }

  public PawnCell(Player owner, int pawnCount) {
    if (owner == null) {
      throw new IllegalArgumentException("Owner cannot be null");
    }

     if (pawnCount < 0 || pawnCount > 3 ) {
       throw new IllegalArgumentException("Pawn count cannot be less than 1 or greater than 3");
     }

     this.owner = owner;
     this.pawnCount = pawnCount;
  }

  @Override
  public void addPawn(Player player) {
    if (this.isCellEmpty()) {
      this.owner = player;
    } else if (!this.owner.equals(player)) {
      return;
    }
    if (pawnCount < 3) {
      pawnCount++;
    }
  }

  @Override
  public void placeCard(Card card) {
    this.card = card;
    this.pawnCount = 0;
  }

  @Override
  public Player getOwner() {
    return this.owner;
  }

  @Override
  public int getPawnCount() {
    return this.pawnCount;
  }

  @Override
  public void changeOwnership(Player player) {
    this.owner = player;
  }

  @Override
  public boolean hasCard() {
    return this.card != null;
  }

  @Override
  public boolean isCellEmpty() {
    return (pawnCount == 0 && !hasCard());
  }

  public Card getCard() {
    return this.card;
  }
}
