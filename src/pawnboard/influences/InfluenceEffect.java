package pawnboard.influences;

import pawnboard.model.PawnsGame;

/**
 * Represents the types of influences a card can have on the board.
 */
public interface InfluenceEffect {

  /**
   * Applies the influence effect to the game.
   * @param game the game instance where the influence is applied
   * @param row the row where the influence is applied
   * @param col the column where the influence is applied
   * @param currentPlayer the player applying the influence
   */
  void apply(PawnsGame game, int row, int col, Player currentPlayer);
}
