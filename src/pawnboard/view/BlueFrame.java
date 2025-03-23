package pawnboard.view;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public class BlueFrame extends GameFrame {

  public BlueFrame(ReadonlyPawnsGame<PawnCell> model) {
    super(model, Player.BLUE);
  }
}
