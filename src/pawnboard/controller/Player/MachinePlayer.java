package pawnboard.controller.Player;

import pawnboard.strategy.PawnBoardStrategy;

public class MachinePlayer implements AbstractPlayer {
  private PawnBoardStrategy strategy;

  MachinePlayer(PawnBoardStrategy strategy) {
    this.strategy = strategy;
  }
  @Override
  public void makeMove() {

  }
}
