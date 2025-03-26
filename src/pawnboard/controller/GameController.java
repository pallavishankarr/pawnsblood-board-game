package pawnboard.controller;

import pawnboard.model.Card;
import pawnboard.model.Cell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.view.GameWindow;

/**
 * Driver of the PawnsBoard game. Reads user input to determine whether a player
 * shall pass or place a card, and displays the current game state.
 * @param <C> the type of cell to be used
 * @param <P> the type of player to be used
 * @param <T> the type of card to be used
 */
public interface GameController<C extends Cell<P, T>, P extends Player, T extends Card> {

  /**
   * Starts and plays the game using user input from the player.
   * @param model state of the game
   * @param redView view for the red View
   * @param blueView view for the blue View
   * @param redFilePath file path of the red player deck
   * @param blueFilePath file path of the blue player deck
   * @param handSize the starting handSize
   * @throws IllegalStateException if controller is unable to successfully recieve input,
   *                               transmit output, or if the game cannot be started
   * @throws IllegalArgumentException if the model or view are null
   */
  void playGame(PawnsGame<C, P, T> model, GameWindow redView, GameWindow blueView,
                                 String redFilePath, String blueFilePath, int handSize);

  /**
   * Handles a card click event from the player.
   * This method processes the action of selecting a card, such as placing it on the board or
   * taking another related action based on the game's rules.
   * @param cardIdx the index of the card that was clicked (0-index based)
   * @param player the player who clicked the card
   * @throws IllegalArgumentException if the cardIdx is not a positive integer
   */
  void handleCardClick(int cardIdx, P player);

  /**
   * Handles a cell click event from the player.
   * This method processes the action of selecting a cell on the game board, such as placing a
   * card or interacting with the pawns on that cell.
   * @param row the row index of the cell that was clicked (0-index based)
   * @param col the column index of the cell that was clicked (0-index based)
   * @throws IllegalArgumentException if the row or col is not a positive integer
   */
  void handleCellClick(int row, int col);

  /**
   * Handles a key press event during the game.
   * This method processes keyboard inputs to trigger certain actions within the game, such as
   * using hotkeys to pass a turn or undo a move.
   * @param key the key pressed by the player
   */
  void handleKeyPress(char key);
}
