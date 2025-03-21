package pawnboard.model;

import java.util.List;

/**
 * The PawnsGame interface defines the structure and rules for a playable game.
 * It includes methods for initializing the board, starting the game,
 * handling players, and determining game outcomes.
 */
public interface PawnsGame<C extends Cell> extends ReadonlyPawnsGame<C> {

  /**
   * Starts the game with the given hand size and initializes the board.
   * @param deck1 the first deck
   * @param deck2 the second deck
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
  void startGame(List<PawnCard> deck1, List<PawnCard> deck2, int handSize);

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
   * Allows the current player to pass their turn.
   * @throws IllegalStateException if the game has not started
   *                               or if the players, turn has not started
   * @throws IllegalArgumentException if it isn't that players turn
   */
  void passTurn();



  /**
   * Starts the turn for the given player.
   * @param player the player whose turn is starting
   * @throws IllegalStateException if the game has not started yet
   * @throws IllegalArgumentException if it isn't that players turn yet
   */
  void startTurn(Player player);


}
