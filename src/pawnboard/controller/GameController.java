package pawnboard.controller;

/**
 * Driver of the PawnsBoard game. Reads user input to determine whether a player
 * shall pass or place a card, and displays the current game state.
 */
public interface GameController {

  /**
   * Starts and plays the game using user input from the player.
   * @param redFilePath file path of the red player deck
   * @param blueFilePath file path of the blue player deck
   * @param handSize the starting handSize
   * @throws IllegalStateException if controller is unable to successfully recieve input,
   *                               transmit output, or if the game cannot be started
   * @throws IllegalArgumentException if the model or view are null
   */
  void playGame(String redFilePath, String blueFilePath, int handSize);
}
