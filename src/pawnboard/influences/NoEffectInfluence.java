package pawnboard.influences;

import pawnboard.model.PawnsGame;
import pawnboard.model.Player;

/**
 * Represents an influnece where nothing happens to the cell
 */
public class NoEffectInfluence implements InfluenceEffect {
  @Override
  public void apply(PawnsGame game, int row, int col, Player currentPlayer) {
    
  }
}

