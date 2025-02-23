package pawnboard;

/**
 * The Game interface defines the structure and rules for a playable game.
 * It includes methods for initializing the board, starting the game,
 * handling players, and determining game outcomes.
 */
public interface Game {
  /**
   * Starts the game with the given hand size and initializes the board.
   * @param handSize maximum hand size for the game
   */
  void startGame(int handSize);

  /**
   * Checks if a move that is made is valid.
   * @return true or false depending on the validity
   *         of the move.
   */
  boolean validMove();

  /**
   * Changes the game state if the game is over.
   */
  void gameOver();

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
