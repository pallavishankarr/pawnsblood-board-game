package pawnboard.view;

import pawnboard.Player.UserPlayer;
import pawnboard.model.Player;

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



  /**
   * Updates the window to indicate whether player is waiting or playing.
   * @param currentPlayer  The player whose turn it currently is.
   * @param userPlayer    The player associated with this game window.
   */
  void updateWindowTitle(Player currentPlayer, Player userPlayer);

  /**
   * Displays a notification to indicate whose turn it is.
   * @param currentUserPlayer The player whose turn it is.
   */
  void showTurnNotification(UserPlayer currentUserPlayer);

  /**
   * Displays a notification to indicate who is the winner
   * @param winner player that won the game
   * @param redScore the score of the red player
   * @param blueScore the score of the blue player
   */
  void showOnGameOver(String winner, int redScore, int blueScore);
}
