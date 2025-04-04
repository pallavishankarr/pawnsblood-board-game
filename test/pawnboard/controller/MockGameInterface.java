package pawnboard.controller;

import pawnboard.Player.UserPlayer;
import pawnboard.model.Player;
import pawnboard.view.BoardPanel;
import pawnboard.view.GameWindow;
import pawnboard.view.HandPanel;

public interface MockGameInterface extends GameWindow {


  void display();

  void updateView();



 boolean isViewUpdated();

}
