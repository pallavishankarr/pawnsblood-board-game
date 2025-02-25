package pawnboard.model;


/**
 * The player interface represents a participant in the game.
 * It defines the essential actions a player can preform
 * such as drawing cards, making moves, and retrieving player-related information.
 */
public interface Player {

  /**
   * Plays a card from the player's hand onto the board.
   * @param game the game instance enforcing rules
   * @param cardIdx the index of card in the hand
   * @param row the row to be placed
   * @param col the column to be placed
   */
  void playCard(PawnsGame game, int cardIdx, int row, int col);

  /**
   * Passes the player's turn.
   * @param game The game instance managing turns
   */
  void passMove(PawnsGame game);

  /**
   * Draws a card from the deck into the player's hand.
   * @param game
   */
  void drawCard(PawnsGame game);

  /**
   * Gets the player's color.
   * @return The player's color
   */
  PlayerColor getColor();
}
