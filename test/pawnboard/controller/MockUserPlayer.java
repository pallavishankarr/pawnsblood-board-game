package pawnboard.controller;

import pawnboard.Player.UserPlayer;
import pawnboard.model.Player;

public class MockUserPlayer implements UserPlayer {
  private Player player;
  private boolean isMachine;


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
