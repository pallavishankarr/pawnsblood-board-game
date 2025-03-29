package pawnboard.model;


import pawnboard.controller.ModelStatusListener;

/**
 * Interface that adds the ModelStatusListener and allows adding a ModelStatusListener.
 */
public interface AddModelStatusListener {

  /**
   * Adds the ModelStatus Listener
   * @param listener model status listener that is being added
   * @throws IllegalArgumentException when the listener that is being added
   */
  void addsModelStatusListener(ModelStatusListener listener) throws IllegalArgumentException;


}
