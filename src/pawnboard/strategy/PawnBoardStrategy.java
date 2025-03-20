package pawnboard.strategy;
import java.util.List;

import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public interface PawnBoardStrategy {
  List<Move> chooseMove(PawnsGame<PawnCell> model, Player player);
}
