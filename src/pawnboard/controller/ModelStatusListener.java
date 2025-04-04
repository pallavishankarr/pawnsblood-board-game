package pawnboard.controller;

/**
 * Interface notifies observers of changes in the model's state during the game.
 * It defines methods to handle updates related to the game's turn, score,
 * and game-over status.
 */
public interface ModelStatusListener {
  /**
   * Notifies when the turn has changed
   */
  void onTurnChanged();

  /**
   * Notifies when the game is over and who the winner is
   */
  void onGameOver();

  /**
   *
   */
  void onCardPlaced();
}
