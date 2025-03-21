package pawnboard.model;

/**
 * PawnCell implements the class Cell and contains the fields
 * of the owner, pawn count of the cell, card of the cell  and if the cell
 * is empty or not.
 */
public class PawnCell implements Cell {

  private Player owner;
  private Cost pawnCount;
  private Card card;
  private boolean hasPawn;

  /**
   * Constructs a pawn cell object with nothing in it.
   */
  public PawnCell() {
    this.card = null;
  }

  /**
   * Constructs a PawnCell object with an owner and pawncount.
   *
   * @param owner     the owner of the cell
   * @param pawnCount the amount of pawns in the cell
   * @throws IllegalArgumentException if the pawn count is less than 0 or greater
   *                                  than 3
   */
  public PawnCell(Player owner, Cost pawnCount) {

    this.owner = owner;
    this.pawnCount = pawnCount;
  }

  @Override
  public void  addPawn(Player player) {
    if (this.isCellEmpty()) {
      this.owner = player;

    }

    else if (!this.owner.equals(player)) {
      return;
    }

    else if(pawnCount == Cost.One) {
      pawnCount = Cost.Two;
    }

    else if(pawnCount == Cost.Two) {
      pawnCount = Cost.Three;
    }

    else
      pawnCount = Cost.One;

  }

  @Override
  public void placeCard(Card card) {
    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null");
    }
    this.card = card;
    this.hasPawn = false;
  }

  @Override
  public Player getOwner() {
    return this.owner;
  }

  @Override
  public Cost getPawnCount() {
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
    return (!hasPawn && !hasCard());
  }
}
