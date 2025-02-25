package pawnboard.influences;

import pawnboard.model.Cell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;

/**
 * Represents an influnece where a pawn is added to the cell.
 */
public class AddPawnInfluence implements InfluenceEffect {

  @Override
  public void apply(PawnsGame game, int row, int col, Player currentPlayer) {
    Cell influencedCell = game.getCellAt(row, col);
    if (influencedCell.isCellEmpty()) {
      influencedCell.addPawn(currentPlayer);
    }
  }
}

