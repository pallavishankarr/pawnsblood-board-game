package pawnboard.Player;

import pawnboard.controller.PlayerActionListener;

public interface SetController {

  /**
   * Sets the controller for the player
   * @param listener listener that needs to be set
   */
  void setController(PlayerActionListener listener);
}
