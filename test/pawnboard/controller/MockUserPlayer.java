package pawnboard.controller;

import pawnboard.player.UserPlayer;
import pawnboard.model.Player;

/**
 * A mock implementation of the UserPlayer interface
 * used for simulating user players in a game.
 */
public class MockUserPlayer implements UserPlayer {
  private final Player player;
  private final boolean isMachine;

  /**
   * Constructs a MockUserPlayer with the given player and machine status.
   * @param player The player associated with this mock user.
   * @param isMachine A boolean indicating whether the player
   *                  is a machine (true) or a human (false).
   */
  MockUserPlayer(Player player, boolean isMachine) {
    this.player = player;
    this.isMachine = isMachine;
  }

  @Override
  public void makeMove() {
    //simulates the make move
  }

  @Override
  public Player getPlayer() {
    return player;
  }

  @Override
  public boolean isMachine() {
    return isMachine;
  }

}
