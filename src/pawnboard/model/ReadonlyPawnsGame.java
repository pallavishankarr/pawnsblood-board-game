package pawnboard.model;

import java.util.List;

import pawnboard.view.GameObserver;

public interface ReadonlyPawnsGame<C extends Cell> {

  /**
   * Retrieves the number of cells that make up the width of the board.
   * Number of cells in the largest row
   * @return the width of the board
   */
  int getWidth();

  /**
   * Retrieves the number of cells that make up the height of the board.
   * Number of rows in the highest column
   * @return the height of the board
   */
  int getHeight();

  /**
   * Determines if the placing card with a given card is legal.
   * @param player the player placing the card
   * @param card the card being placed
   * @param row row of where card should be placed
   * @param col col of where card should be placed
   * @return boolean based on if not placing card is legal
   * @throws IllegalStateException if the game hasn't started
   */
  boolean placeCardLegal(Player player, Card card, int row, int col) ;

  /**
   * Returns a deep copy of the current board state.
   * @throws IllegalStateException if the game has not started
   * @return a deep copy of the board, preserving the current game state
   */
  C[][] getBoard();

  /**
   * Returns the cell at the indicated position on the board.
   * @param row the row to access
   * @param col the column to acess
   * @return the cell in the valid position
   * @throws IllegalArgumentException if the row and column are not a valid location
   *                                  for a Cell on the board
   */
  Cell getCellAt(int row, int col);

  /**
   * Gets the current player.
   * @return The player whose turn it is
   * @throws IllegalStateException if the game has not started
   */
  Player getCurrentPlayer();

  /**
   * Returns a copy of the given player's current hand. If their hand is empty, then an empty
   * list is returned.
   * @return a copy of the player's current hand
   * @throws IllegalStateException if the game has not started
   */
  List<Card> getHand(Player player);

  /**
   * Returns the number of cards left in a player's deck.
   * @param player the player whose deck needs to be accessed
   * @return the number of cards left in the players deck.
   * @throws IllegalStateException if the game has not started
   */
  int getRemainingDeckSize(Player player);

  /**
   * Gets the winner of the game.
   * @return the player who won
   * @throws IllegalStateException if the game has not started
   */
  String getWinner();

  /**
   * Checks and returns the score of the Player.
   * @param player checks the score of the player
   * @return the integer of the Score
   * @throws IllegalStateException if the game has not started
   */
  int checkScore(Player player);

  /**
   * Checks the Score of the row based on the Player.
   * @param player player that score is being checked
   * @param rowIdx the row index that is being checked
   * @return the score of the
   * @throws IllegalArgumentException if the rowIdx is out of row bounds
   */
  int checkScoreRow(Player player, int rowIdx);

  /**
   * Determines if the game has ended.
   * @return true if the game is over, false if the game is not over
   * @throws IllegalStateException if the game has not started
   */
  boolean isGameOver();

  /**
   * Determines which Player owns the cell at a given coordinate.
   * @param row row number of the index
   * @param col column number of the index
   * @return Player which owns the cell
   * @throws IllegalStateException if the game has not started
   */
  Player getOwnerAt(int row, int col);

  // observer methods
  void addObserver(GameObserver observer);

  void removeObserver(GameObserver observer);
}
