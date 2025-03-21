package pawnboard.model;

import java.util.List;

import pawnboard.influences.InfluenceEffect;

/**
 * Interface represents a card defining its properties and behaviors.
 * It includes methods the retrieve Card properties and how a card
 * affects Cells and applies influence.
 */
public interface Card {

  /**
   * Returns the name of the card.
   * @return String of the card name
   */
  String getName();

  /**
   * Returns the pawn cost of the card.
   * @return a CostEnum of One, Two or Three
   */
  Cost getPawnCost();

  /**
   * Returns the value which is points of the card added to the score.
   * @return the value of the card
   */
  int getValue();

  /**
   * Returns the influence grid which shows what cells state changes.
   * @return a 2D array representing the influence pattern of the grid
   */
  InfluenceType[][] getInfluenceGrid();

  /**
   * Returns a list of influence effects that the card has.
   * @return a list of influence effects
   */
  List<InfluenceEffect> getInfluenceEffects();

  /**
   * Determines if the Card affects cell of the given row and column.
   * @param row row index of the cell
   * @param col col index of the cell
   * @return true if the cell is affected by the card or false if not
   * @throws IllegalArgumentException if the row and col indexes are invalid
   */
  boolean affectsCell(int row, int col) throws IllegalArgumentException;

  /**
   * Returns a String representation of the card.
   * @return String of the card
   */
  String toString();

}
