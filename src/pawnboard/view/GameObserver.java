package pawnboard.view;

import pawnboard.controller.PlayerActionListener;

/**
 * Represents an observer in the observer design pattern.
 */
public interface GameObserver {
  /**
   * Called when the game state changes.
   * Implementing classes should define the specific actions to be taken
   * in response to the game state change.
   */
  void gameStateChanged();
}
