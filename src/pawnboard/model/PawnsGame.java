package pawnboard.model;

import java.util.List;

/**
 * The PawnsGame interface defines the structure and rules for a playable game.
 * It includes methods for initializing the board, starting the game,
 * handling players, and determining game outcomes.
 */
public interface PawnsGame<C extends Card> {

  /**
   * Starts the game with the given hand size and initializes the board.
   * @param path the file path that has the card file
   * @param handSize the given hand size for each player
   * @throws IllegalStateException if the game has already been started
   * @throws IllegalArgumentException if either players deck is empty,
   *                                  if the players deck are not the same size, if either
   *                                  deck contains a null object,
   *                                  if there are not enough cards in either deck to
   *                                  fill up the whole board,
   *                                  if handSize is not positive (i.e. 0 or less)
   *                                  or is greater than a third of the deck size
   */
  void startGame(String path, int handSize);

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
   * Places a card from the players hand to a given position on the board.
   * @param cardIdx index of the card in hand to place (0-index based)
   * @param row row to place the card in (0-index based)
   * @param col col to be placed the card in (0-index based)
   * @throws IllegalStateException if the game has not started or
   *                               if that players turn has not started
   * @throws IllegalArgumentException if the cardIdx is our of bounds
   *                                  of the hand, the given position on
   *                                  the board is out of bounds, if
   *                                  there is already a card occupying that position,
   *                                  or if isn't that players turn
   */
  void placeCardAt(Player player, int cardIdx, int row, int col);

  /**
   * Allows a player to pass their turn.
   * @param player the player passing their turn
   * @throws IllegalStateException if the game has not started
   *                               or if the players, turn has not started
   * @throws IllegalArgumentException if it isn't that players turn
   */
  void passTurn(Player player);

  /**
   * Draws a card for the current player.
   * @param player the player who wants to drawCard
   * @throws IllegalStateException if the game has not started or if
   *                               the players turn has not started
   * @throws IllegalArgumentException if it isn't that players turn
   */
  void drawCard(Player player);

  /**
   * Starts the turn for the given player.
   * @param player the player whose turn is starting
   * @throws IllegalStateException if the game has not started yet
   * @throws IllegalArgumentException if it isn't that players turn yet
   */
  void startTurn(Player player);

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
  List<C> getHand(Player player);

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
  Player getWinner();

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
   * @throws IllegalArgumentException if the rowIdx is out of row bounds
   * @return the score of the row
   */
  int checkScoreRow(Player player, int rowIdx);

  /**
   * Returns the player who won the row.
   * @param rowIdx the index of the row being checked
   * @return Player who won the row
   * @throws IllegalStateException if the game has not started
   * @throws IllegalArgumentException if the rowIdx is out of row bounds
   */
  Player rowWinner(int rowIdx);

  /**
   * Determines if the game has ended.
   * @return true if the game is over, false if the game is not over
   * @throws IllegalStateException if the game has not started
   */
  boolean isGameOver();



  /**
   * Applies the influence of a placed card to the board.
   * Each affected cell is modified according to the games influence rules.
   * @param boardRow         the row index of the placed card (0-index)
   * @param boardCol the column index of the placed card (0-index based)
   * @param currentPlayer    the player placing the card
   * @param card the card making an influence
   * @throws IllegalStateException    if the game is not started
   * @throws IllegalArgumentException if row or col is not a position on the
   *                                  board, or if the card is null
   */
  void applyCardInfluence(int boardRow, int boardCol, Player currentPlayer, Card card);
}
