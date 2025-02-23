package pawnboard.influences;

import pawnboard.Cell;
import pawnboard.PawnsGame;
import pawnboard.Player;

public class AddPawnInfluence implements InfluenceEffect {

  @Override
  public void apply(PawnsGame game, int row, int col, Player currentPlayer) {
    Cell influencedCell = game.getCellAt(row, col);
    if (influencedCell.isCellEmpty()) {
      influencedCell.addPawn();
    }
  }
}
