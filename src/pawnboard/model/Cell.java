package pawnboard.model;

public interface Cell {

  /**
   * Adds a pawn to the cell.
   * @param player the player who owns the cell
   */
  void addPawn(Player player);

  /**
   * Places a card on the cell and removes any pawns on the cell.
   * @throws IllegalArgumentException if the card is null
   */
  void placeCard(Card card);

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

  /**
   * Returns the card of the cell.
   * @return Card that the cell has placed on
   */
  Card getCard();

  /**
   * Checks if the cell is empty
   * Empty means no cards or pawns are in the cell
   * @return true if the cell is empty
   */
  boolean isCellEmpty();



}
