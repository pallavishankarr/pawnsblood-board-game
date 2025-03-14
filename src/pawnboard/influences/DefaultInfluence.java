package pawnboard.influences;

import pawnboard.model.Cell;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;

/**
 * Represents an influence where a pawn is added to an empty cell.
 * Or increases the pawn count if the player owns the cell.
 */
public class DefaultInfluence implements InfluenceEffect {

  @Override
  public void apply(PawnsGame<PawnCell> game, int row, int col, Player currentPlayer) {
    Cell influencedCell = game.getCellAt(row, col);

    // If the cell is empty, add a pawn
    if (influencedCell.isCellEmpty()) {
      influencedCell.addPawn(currentPlayer);
    }

    // If the cell is owned by the opponent and change ownership
    else if (influencedCell.getOwner() != currentPlayer) {
      influencedCell.changeOwnership(currentPlayer);

    }

    // If the player owns the cell and pawn count is less than 3, increase pawn count
    else if (influencedCell.getPawnCount() < 3) {
      influencedCell.addPawn(currentPlayer);
    }
  }
}

