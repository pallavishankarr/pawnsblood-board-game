package pawnboard;

public interface Cell {

  /**
   * Adds a pawn to the cell.
   */
  void addPawn();

  /**
   * Places a card on the cell and removes any pawns on the cell.
   */
  void placeCard(Card card);

  /**
   * Applies influence of the card on the cell.
   */
  void applyInfluence();

  /**
   * Gets the owner of the cell.
   * @return Player who owns the cell
   */
  Player getOwner();

  /**
   * Returns the number of pawns on the cell.
   * @return int of pawns on the cell
   */
  int getPawnCount();

  /**
   * Changes the ownership to the given Player.
   */
  void changeOwnership(Player player);

  /**
   * Determines if the Cell contains a card.
   * @return true if the cells contains a card or false
   */
  boolean hasCard();



}
