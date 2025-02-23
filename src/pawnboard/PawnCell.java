package pawnboard;

/**
 * PawnCell implements the class Cell and contains the fields
 * of the owner, pawn count of the cell, card of the cell  and if the cell
 * is empty or not.
 */
public class PawnCell implements Cell{

  private Player owner;
  private int pawnCount;
  private Card card;
  private boolean isEmpty = true;

  PawnCell() {
    this.owner = null;
    this.pawnCount = 0;
    this.card = null;
  }

  PawnCell(Player owner, int pawnCount) {
    if (owner == null) {
      throw new IllegalArgumentException("Owner cannot be null");
    }
     if (pawnCount < 1 && pawnCount < 3 ) {
       throw new IllegalArgumentException("Pawn count cannot be less than 1 or greater than 3");
     }

     this.owner = owner;
     this.pawnCount = pawnCount;
     this.isEmpty = false;

  }


  @Override
  public void addPawn() {
    pawnCount++;
    isEmpty = false;

  }

  @Override
  public void placeCard(Card card) {
    this.card = card;
    isEmpty = false;
  }

  @Override
  public void applyInfluence() {

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
    if(!this.owner.equals(player)) {
      this.owner = player;
    }
  }

  @Override
  public boolean hasCard() {
    return this.card != null;
  }
}
