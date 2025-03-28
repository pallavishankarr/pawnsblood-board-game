package pawnboard.view;

/**
 * Represents the main window for displaying the game.
 * Provides methods to display and update the game view.
 */
public interface GameWindow {
  /**
   * Displays the game window to the user.
   */
  void display();

  /**
   * Updates the game view to reflect the latest game state.
   * This method should be called whenever the game state changes
   * to ensure GUI remains up to date.
   */
  void updateView();

  /**
   * Returns the board panel of the game window.
   * @return the board panel
   */
  BoardPanel getBoardPanel();

  /**
   * Returns the hand panel of the game window.
   * @return the hand panel
   */
  HandPanel getHandPanel();
}
