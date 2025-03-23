package pawnboard.controller;

import pawnboard.model.Card;
import pawnboard.model.Cell;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.view.GameWindow;

/**
 * Driver of the PawnsBoard game. Reads user input to determine whether a player
 * shall pass or place a card, and displays the current game state.
 */
public interface GameController {
  /**
   * Starts and plays the game using user input from the player.
   * @param model state of the game
   * @param redView view for the red View
   * @param blueView view for the blue View
   * @param redFilePath file path of the red player deck
   * @param blueFilePath file path of the blue player deck
   * @param handSize the starting handSize
   * @param <C> the type of Cell used in the model
   * @throws IllegalStateException if controller is unable to successfully recieve input,
   *    transmit output, or if the game cannot be started
   * @throws IllegalArgumentException if the model or view are null
   */
  <C extends Cell> void playGame(PawnsGame<PawnCell> model, GameWindow redView, GameWindow blueView,
                                 String redFilePath, String blueFilePath, int handSize);

  void handleCardClick(int cardIdx, Player player);

  void handleCellClick(int row, int col);

  void handleKeyPress(char key);
}
