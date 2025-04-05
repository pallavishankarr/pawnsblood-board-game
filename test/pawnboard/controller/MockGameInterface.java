package pawnboard.controller;

import pawnboard.view.GameWindow;

/**
 * Represents the MockGameInterface methods.
 */
public interface MockGameInterface extends GameWindow {

  /**
   * Displays the model.
   */
  void display();

  /**
   * Updates the view.
   */
  void updateView();

  /**
   * Returns whether the view is updated.
   * @return true if the view is updated false if not.
   */
  boolean isViewUpdated();

}
