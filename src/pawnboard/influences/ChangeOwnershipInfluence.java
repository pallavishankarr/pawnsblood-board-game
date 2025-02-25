package pawnboard.influences;

import pawnboard.model.PawnsGame;
import pawnboard.model.Cell;
import pawnboard.model.Player;

/**
 * Represents an influence where the cell has pawns and the ownership changes.
 */
public class ChangeOwnershipInfluence implements InfluenceEffect {
  @Override
  public void apply(PawnsGame game, int row, int col, Player currentPlayer) {
    Cell influencedCell = game.getCellAt(row, col);
    if (influencedCell.hasCard() || influencedCell.getOwner() == currentPlayer) {
      return;
    }

    influencedCell.changeOwnership(currentPlayer);
  }
}
