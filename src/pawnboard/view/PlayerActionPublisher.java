package pawnboard.view;

import pawnboard.controller.PlayerActionListener;

/**
 * Represents a way for the view to register a playeractionlistener.
 * Allows the view to notify the controller of a player action.
 */
public interface PlayerActionPublisher {
  /**
   * Registers a PlayerActionListener to handle player actions from the view.
   * @param listener the PlayerActionListener to be registered.
   */
  void addPlayerActionListener(PlayerActionListener listener);

  /**
   * Returns the selected Card Index.
   * @return card index
   */
  int getSelectedCardIndex();
}
