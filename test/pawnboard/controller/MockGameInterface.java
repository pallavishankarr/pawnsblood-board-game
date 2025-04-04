package pawnboard.controller;

import pawnboard.view.GameWindow;

public interface MockGameInterface extends GameWindow {


  void display();

  void updateView();


  boolean isViewUpdated();

}
