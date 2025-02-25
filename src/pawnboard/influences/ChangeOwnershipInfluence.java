package pawnboard.influences;

import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.model.Cell;

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
