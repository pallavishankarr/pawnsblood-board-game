package pawnboard.model;

/**
 * The PawnsGame interface defines the structure and rules for a playable game.
 * It includes methods for initializing the board, starting the game,
 * handling players, and determining game outcomes.
 */
public interface PawnsGame<C extends Cell> extends ReadonlyPawnsGame<C> {

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
