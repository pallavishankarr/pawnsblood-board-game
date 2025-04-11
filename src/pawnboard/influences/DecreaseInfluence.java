package pawnboard.influences;

import pawnboard.model.Cost;
import pawnboard.model.InfluenceType;
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
    Player ogCellOwner = influencedCell.getOwner();
    if (influencedCell.hasCard()) {
      PawnCard card = influencedCell.getCard();
      influencedCell.removeCard();
      Cost pawnCost = card.getPawnCost();
      int pawnCostInt = pawnCost.getValue();
      for (int i=0; i < pawnCostInt; i++) {
        influencedCell.addPawn(ogCellOwner);
      }
      influencedCell.updateChangedValueAmount(-influencedCell.getChangedValue());
    }
    else {
      influencedCell.updateChangedValueAmount(-1);
    }
  }

  @Override
  public boolean appliesTo(InfluenceType type) {
    return type == InfluenceType.D;
  }

  @Override
  public InfluenceType getInfluenceType() {
    return InfluenceType.D;
  }
}
