package pawnboard.view;

public interface GameObserver {
  /**
   * Called when the game state changes.
   */
  void gameStateChanged();
}
