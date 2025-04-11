package pawnboard.influences;

import pawnboard.model.Card;
import pawnboard.model.Cell;
import pawnboard.model.InfluenceType;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;


/**
 * Represents the types of influences a card can have on the board.
 * @param <C> the type of cell to use
 * @param <P> the type of player to ue
 * @param <T> the type of card to use
 */
public interface InfluenceEffect<C extends Cell<P, T>, P extends Player, T extends Card> {

  /**
   * Applies the influence effect to the game.
   * @param game the game instance where the influence is applied
   * @param row the row where the influence is applied
   * @param col the column where the influence is applied
   * @param currentPlayer the player applying the influence
   */
  void apply(PawnsGame<C, P, T> game, int row, int col, Player currentPlayer);

  /**
   * Checks if this type applies to the given type.
   * @param type the given type
   * @return true if it applies false if not
   */
  boolean appliesTo(InfluenceType type);

  /**
   * Gets the corresponding influence type that represents the influence.
   * @return the influence type
   */
  InfluenceType getInfluenceType();
}
