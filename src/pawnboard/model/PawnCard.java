package pawnboard.model;

import java.util.List;

import pawnboard.influences.InfluenceEffect;

public class PawnCard implements Card {
  private final String name;
  private final int cost;
  private final int value;
  private final char[][] influenceGrid;
  private final List<InfluenceEffect> influenceEffects;

  public PawnCard(String name, int cost, int value, char[][] influenceGrid, List<InfluenceEffect> influenceEffects) {
    if (cost < 1 || cost > 3) {
      throw new IllegalArgumentException("Cost must be between 1 and 3");
    }
    if (value <= 0) {
      throw new IllegalArgumentException("Value must be positive integer");
    }
    if (influenceGrid.length != 5) {
      throw new IllegalArgumentException("Influence Grid must have exactly 5 rows.");
    }

    for (char[] row : influenceGrid) {
      if (row.length != 5) {
        throw new IllegalArgumentException("Each row in the Influence Grid must have exactly 5 columns.");
      }
    }

    if (influenceGrid[2][2] != 'C') {
      throw new IllegalArgumentException("the center of the grid must contain 'C'");
    }

    this.name = name;
    this.cost = cost;
    this.value = value;
    this.influenceGrid = influenceGrid;
    this.influenceEffects = influenceEffects;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getPawnCost() {
    return cost;
  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  public char[][] getInfluenceGrid() {
    return influenceGrid;
  }

  @Override
  public boolean affectsCell(int row, int col) throws IllegalArgumentException {
    if (row < 0 || row >= 5 || col < 0 || col >= 5) {
      throw new IllegalArgumentException("Invalid cell coordinates for influence grid.");
    }
    return influenceGrid[row][col] == 'I';
  }

  @Override
  public void applyInfluence(PawnsGame game, int boardRow, int boardCol, Player currentPlayer) {
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (influenceGrid[i][j] == 'I') {
          int targetRow = boardRow + (i - 2);
          int targetCol = boardCol + (j - 2);

          try {
            for (InfluenceEffect effect : influenceEffects) {
              effect.apply(game, targetRow, targetCol, currentPlayer);
            }
          } catch (IllegalArgumentException e) {
            // ignore out-of-bounds cells
          }
        }
      }
    }
  }
}
