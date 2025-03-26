package pawnboard.controller.Player;

import pawnboard.model.Card;

/**
 * Interface for listening to player actions.
 * Implementing classes will handle actions such as placing a card or passing a turn.
 */
public interface PlayerActionListener<C extends Card> {
  /**
   * Called when a player attempts to place a card on the board.
   *
   * @param player The player performing the action.
   * @param card The card being placed.
   * @param row The row where the card is being placed.
   * @param col The column where the card is being placed.
   */
  void onMove(UserPlayer player, C card, int row, int col);

  /**
   * Called when a player decides to pass their turn.
   *
   * @param player The player who is passing.
   */
  void onPass(UserPlayer player);
}
