package pawnboard.model;

/**
 * Represents a single cell on the game board in Pawns Board.
 * A cell can contain pawns or a placed card, and ownership
 * can change.
 * @param <P> the type of player
 * @param <T> the type of card the game plays with
 */
public interface Cell<P extends Player, T extends Card> {

  /**
   * Adds a pawn to the cell.
   * @param player the player who owns the cell
   */
  void addPawn(P player);

  /**
   * Places a card on the cell and removes any pawns on the cell.
   * @throws IllegalArgumentException if the card is null
   */
  void placeCard(T card);

  /**
   * Gets the owner of the cell.
   * @return Player who owns the cell
   */
  P getOwner();

  /**
   * Returns the number of pawns on the cell.
   * @return int of pawns on the cell
   */
  Count getPawnCount();

  /**
   * Changes the ownership to the given Player.
   */
  void changeOwnership(P player);

  /**
   * Determines if the Cell contains a card.
   * @return true if the cells contains a card or false
   */
  boolean hasCard();

  /**
   * Returns the card of the cell.
   * @return Card that the cell has placed on
   */
  T getCard();

  /**
   * Checks if the cell is empty.
   * Empty means no cards or pawns are in the cell
   * @return true if the cell is empty
   */
  boolean isCellEmpty();


}
