package pawnboard.influences;

import pawnboard.model.InfluenceType;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;

public class UpgradeInfluence implements InfluenceEffect<PawnCell, Player, PawnCard> {
  @Override
  public void apply(PawnsGame<PawnCell, Player, PawnCard> game, int row, int col, Player currentPlayer) {
    PawnCell influencedCell = game.getCellAt(row, col);
    influencedCell.updateChangedValueAmount(1);
  }

  @Override
  public boolean appliesTo(InfluenceType type) {
    return type == InfluenceType.U;
  }

  @Override
  public InfluenceType getInfluenceType() {
    return InfluenceType.U;
  }
}
