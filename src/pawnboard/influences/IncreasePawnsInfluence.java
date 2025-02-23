package pawnboard.influences;

import pawnboard.Cell;
import pawnboard.PawnsGame;
import pawnboard.Player;

/**
 * Increases the amount of pawns in a cell the player owns.
 */
public class IncreasePawnsInfluence implements InfluenceEffect {

  @Override
  public void apply(PawnsGame game, int row, int col, Player currentPlayer) {
    Cell influencedCell = game.getCellAt(row, col);
    if (influencedCell.hasCard() || influencedCell.getOwner() != currentPlayer) {
      return;
    }

    if (influencedCell.getPawnCount() < 3) {
      influencedCell.addPawn(currentPlayer);
    }
  }
}
