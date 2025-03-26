package pawnboard.model;

/**
 * PawnCell implements the class Cell and contains the fields
 * of the owner, pawn count of the cell, card of the cell  and if the cell
 * is empty or not.
 */
public class PawnCell implements Cell<Player, PawnCard> {

  private Player owner;
  private Count pawnCount;
  private PawnCard card;


  /**
   * Constructs a pawn cell object with nothing in it.
   */
  public PawnCell() {
    this.card = null;
    this.pawnCount = Count.Zero;

  }

  /**
   * Constructs a PawnCell object with an owner and pawncount.
   *
   * @param owner     the owner of the cell
   * @param pawnCount the amount of pawns in the cell
   * @throws IllegalArgumentException if the pawn count is less than 0 or greater
   *                                  than 3
   */
  public PawnCell(Player owner, Count pawnCount) {

    this.owner = owner;
    this.pawnCount = pawnCount;
  }

  @Override
  public void  addPawn(Player player) {
    if (this.isCellEmpty()) {
      this.owner = player;
      this.pawnCount = Count.One;
    }

    else if (!this.owner.equals(player)) {
      return;
    }

    else {
      switch (pawnCount) {
        case One:
          pawnCount = Count.Two;
          break;
        case Two:
          pawnCount = Count.Three;
          break;
        case Three:
          System.out.println("Max pawn count reached. Staying at Three.");
          return;  // Prevents resetting to One

        default:
          break;
      }
    }
  }

  @Override
  public void placeCard(PawnCard card) {
    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null");
    }
    this.card = card;
    this.pawnCount = Count.Zero;
  }

  @Override
  public Player getOwner() {
    return this.owner;
  }

  @Override
  public Count getPawnCount() {
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
  public PawnCard getCard() {
    return this.card;
  }

  @Override
  public boolean isCellEmpty() {
    return (pawnCount == Count.Zero && !hasCard());
  }
}
