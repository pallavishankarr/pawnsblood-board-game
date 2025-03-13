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

  /**
   * Constructs a PawnCell object with an owner and pawncount.
   * @param owner the owner of the cell
   * @param pawnCount the amount of pawns in the cell
   * @throws IllegalArgumentException if the pawn count is less than 0 or greater
   * than 3
   */
  public PawnCell(Player owner, int pawnCount) {
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
    }
    else if (!this.owner.equals(player)) {
      return;
    }
    if (pawnCount < 3) {
      pawnCount++;
    }
  }

  @Override
  public void placeCard(Card card) {
    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null");
    }
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
  public Card getCard() {
    return this.card;
  }

  @Override
  public boolean isCellEmpty() {
    return (pawnCount == 0 && !hasCard());
  }
}
