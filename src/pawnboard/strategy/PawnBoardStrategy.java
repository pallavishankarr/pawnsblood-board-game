package pawnboard.strategy;
import java.util.List;

import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public interface PawnBoardStrategy {
  List<Move> chooseMove(ReadonlyPawnsGame<PawnCell> model, Player player);
}
