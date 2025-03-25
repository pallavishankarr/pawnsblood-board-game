package pawnboard.view;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

/**
 * Represents specific implementation of the game window for the Red player.
 */
public class RedFrame extends GameFrame {

  /**
   * Constructs a RedFrame object for the red player and initializes the game.
   * @param model the model that holds current state
   */
  public RedFrame(ReadonlyPawnsGame<PawnCell> model) {
    super(model, Player.RED);
  }
}