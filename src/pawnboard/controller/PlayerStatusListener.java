package pawnboard.controller;

/**
 * Represents methods for handling player actions in the game.
 * Allows for the observer to listen and process player actions.
 */
public interface PlayerStatusListener {

  /**
   * Called when a player selects a card from their hand.
   *
   * @param cardIdx the index of the selected card (0-index based)
   * @throws IllegalArgumentException if the cardIdx is not a positive integer
   */
  void onCardClick(int cardIdx);

  /**
   * Called when a player selects a cell on the game board.
   *
   * @param row the row index of the selected cell (0-index based)
   * @param col the column of the selected cell (0-index based)
   * @throws IllegalArgumentException if the row or col is not a positive integer
   */
  void onCellClick(int row, int col);

  /**
   * Called when a player confirms their move after selecting card and cell.
   */
  void onConfirmTurn();

  /**
   * Called when a player chooses to pass their turn to the other player.
   */
  void onPassTurn();

}
