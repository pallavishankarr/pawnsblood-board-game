package pawnboard.controller.Player;

import java.awt.event.ActionListener;

import pawnboard.model.Card;

/**
 * Interface for publishing player action events.
 * Implementing classes allow observers to register and receive notifications of player actions.
 */
public interface PlayerActionPublisher<C extends Card> {

  /**
   * Registers a listener to receive player action events.
   *
   * @param listener The listener to register.
   */
  void addPlayerActionListener(PlayerActionListener listener);

  /**
   * Removes a previously registered listener.
   *
   * @param listener The listener to remove.
   */
  void removePlayerActionListener(PlayerActionListener listener);

  /**
   * Notifies registered listeners when a player makes a move.
   *
   * @param player The player making the move.
   * @param card The card being placed.
   * @param row The row where the card is placed.
   * @param col The column where the card is placed.
   */
  void publishMove(AbstractPlayer player, Card card, int row, int col);

  /**
   * Notifies registered listeners when a player passes their turn.
   *
   * @param player The player who is passing.
   */
  void publishPass(AbstractPlayer player);

}
