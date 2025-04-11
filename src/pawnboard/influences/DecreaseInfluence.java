package pawnboard.influences;

import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;

/**
 * Represents an influence effect that decreases the changeValue of a cell by -1.
 */
public class DecreaseInfluence implements InfluenceEffect<PawnCell, Player, PawnCard> {
  @Override
  public void apply(PawnsGame<PawnCell, Player, PawnCard> game, int row, int col, Player currentPlayer) {
    PawnCell influencedCell = game.getCellAt(row, col);
    influencedCell.updateChangedValueAmount(-1);
  }
}
