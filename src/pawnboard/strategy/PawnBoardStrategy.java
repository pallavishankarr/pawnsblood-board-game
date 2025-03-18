package pawnboard.strategy;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public interface PawnBoardStrategy {
  Move chooseMove(ReadonlyPawnsGame<PawnCell> model, Player player);
}
