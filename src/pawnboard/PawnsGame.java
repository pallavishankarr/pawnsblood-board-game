package pawnboard;

import java.util.List;

/**
 * The PawnsGame interface defines the structure and rules for a playable game.
 * It includes methods for initializing the board, starting the game,
 * handling players, and determining game outcomes.
 */
public interface PawnsGame {

  /**
   * Starts the game with the given hand size and initializes the board.
   * @param player1deck the list of cards for the first player
   * @param player2deck the list of cards for the second player
   * @param handSize maximum hand size for the game
   */
  void startGame(List<Card> player1deck, List<Card> player2deck, int handSize);

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
   */
  void playCard(Player player, int cardIdx, int row, int col);

  /**
   * Allows a player to pass their turn.
   * @param player the player passing their turn
   */
  void passTurn(Player player);

  /**
   * Draws a card for the current player.
   */
  void drawCardForPlayer();

  /**
   * Gets the current player.
   * @return The player whose turn it is
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
   * Determines if the game has ended.
   * @return true if the game is over, false if the game is not over
   */
  boolean isGameOver();

  /**
   * Gets the winner of the game
   * @return the player who won
   */
  Player getWinner();

  /**
   * Checks and returns the score of the Player.
   * @param player checks the score of the player
   * @return the integer of the Score
   */
  int checkScore(Player player);

  /**
   * Checks the winner of the game based on the final Score.
   * @return the Player who won
   */
  Player checkWinner();
}
