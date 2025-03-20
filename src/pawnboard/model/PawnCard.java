package pawnboard.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import pawnboard.influences.DefaultInfluence;
import pawnboard.influences.InfluenceEffect;

/**
 * Represents a card in the Pawn Board game.
 * Each card has a name, cost (how many pawns req to play), a value (scoring),
 * and an influence grid that dictates how it affects the board.
 */
public class PawnCard implements Card {
  private final String name;
  private final CostEnum cost;
  private final int value;
  private final InfluenceType[][] influenceGrid;
  private final List<InfluenceEffect> influenceEffects;

  /**
   * Constructs a PawnCard object.
   * @param name the name of the card
   * @param cost the cost of the card
   * @param value the value of the card
   * @param influenceGrid the influence grid of the card
   * @throws IllegalArgumentException if the cost is less than 1 or greater than 3,
   *         if the value is not a positive integer, if the influence grid is not 5x5 and if the
   *         influence grid doesn't have a proper center
   */
  public PawnCard(String name, CostEnum cost, int value, InfluenceType[][] influenceGrid) {
    if (cost == null) {
      throw new IllegalArgumentException("Cost cannot be null");
    }
    if (value <= 0) {
      throw new IllegalArgumentException("Value must be positive integer");
    }
    if (influenceGrid.length != 5) {
      throw new IllegalArgumentException("Influence Grid must have exactly 5 rows.");
    }

    for (InfluenceType[] row : influenceGrid) {
      if (row.length != 5) {
        throw new IllegalArgumentException("Each row in the Influence Grid must " +
                "have exactly 5 columns.");
      }
    }

    if (influenceGrid[2][2] != InfluenceType.C) {
      throw new IllegalArgumentException("the center of the grid must contain 'C'");
    }

    this.name = name;
    this.cost = cost;
    this.value = value;
    this.influenceGrid = influenceGrid;
    this.influenceEffects = new ArrayList<>(List.of(new DefaultInfluence()));
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public CostEnum getPawnCost() {
    return cost;
  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  public InfluenceType[][] getInfluenceGrid() {
    return influenceGrid;
  }

  @Override
  public List<InfluenceEffect> getInfluenceEffects() {
    return influenceEffects;
  }

  @Override
  public boolean affectsCell(int row, int col) throws IllegalArgumentException {
    if (row < 0 || row >= 5 || col < 0 || col >= 5) {
      throw new IllegalArgumentException("Invalid cell coordinates for influence grid.");
    }
    return influenceGrid[row][col] == InfluenceType.I;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }

    if (!(other instanceof PawnCard)) {
      return false;
    }

    PawnCard that = (PawnCard) other;
    return this.cost == that.cost
            &&
            this.value == that.value
            &&
            this.name.equals(that.name)
            &&
            Arrays.deepEquals(this.influenceGrid, that.influenceGrid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, cost, value, Arrays.deepHashCode(influenceGrid));
  }


  public String toString() {
    return String.format("%s (%d, %d)", name, cost.getValue(), value);
  }

}
