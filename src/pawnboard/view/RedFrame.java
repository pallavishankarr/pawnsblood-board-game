package pawnboard.view;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public class RedFrame extends GameFrame {

  public RedFrame(ReadonlyPawnsGame<PawnCell> model) {
    super(model, Player.RED);
  }
}