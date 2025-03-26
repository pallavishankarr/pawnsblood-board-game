package pawnboard.controller.Player;

import pawnboard.view.GameView;

public class HumanPlayer implements AbstractPlayer{
  private GameView view;

  HumanPlayer(GameView view) {
    this.view = view;
  }


  @Override
  public void makeMove() {

  }
}
