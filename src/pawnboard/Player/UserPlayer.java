package pawnboard.Player;

import pawnboard.model.Player;



/**
 * The UserPlayer interface represents a player who actively participates in the game
 * by making moves such as placing a card or passing their turn.
 */
public interface UserPlayer {
  /**
   * Player makes a move either by placing a card or passing a turn.
   */
  void makeMove();

  /**
   * Gets what color player is associated with this user player.
   * @return the Player enum associated with this player.
   */
  Player getPlayer();

  /**
   * Returns whether this userplayer is a machine or not.
   * @return true if this userplayer is a machine, false if not
   */
  boolean isMachine();
}
