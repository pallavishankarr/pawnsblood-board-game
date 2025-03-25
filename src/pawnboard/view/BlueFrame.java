package pawnboard.view;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

/**
 * Represents specific implementation of the game window for the Blue player.
 */
public class BlueFrame extends GameFrame {

  /**
   * Constructs a BlueFrame object for the blue player and initializes the game.
   * @param model the model that holds current state
   */
  public BlueFrame(ReadonlyPawnsGame<PawnCell> model) {
    super(model, Player.BLUE);
  }
}
