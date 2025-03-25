package pawnboard;

import pawnboard.model.Card;
import pawnboard.model.Cell;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;

/**
 *  Represents a mock version of a pawn game that simulates basic game operations.
 *  Provides methods for setting legal moves, row scores, and adding cards for testing.
 */
public interface MockGame extends PawnsGame<PawnCell> {

  /**
   * Sets a legal move at the specified position on the board.
   * @param row the row index where the move is being set
   * @param col the column index where the move is being set
   * @throws IllegalArgumentException if the row or col is out of bounds
   */
  void setLegalMove(int row, int col);

  /**
   * Sets a fake score for a specific row, adjusting the score for either the red or blue player.
   * @param row the row index for which the score is set
   * @param player the player for whom the score is set
   * @param score the score to set for the specified player at the row
   * @throws IllegalArgumentException if row out of bounds, or score is less than 0
   */
  void setFakeRowScore(int row, Player player, int score);

  /**
   * Adds a card to the specified player's hand.
   * @param player the player to whom the card is added
   * @param card the to be added to the player's hand
   * @throws IllegalArgumentException if card is null
   */
  void addCardToHand(Player player, PawnCard card);
}
