package pawnboard;

import pawnboard.model.Cell;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;

public interface MockGame extends PawnsGame<PawnCell> {

  void setLegalMove(int row, int col);

  void setFakeRowScore(int row, Player player, int score);
}
