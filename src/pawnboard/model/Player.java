package pawnboard.model;

/**
 * Represents the Player color in the game.
 */
public enum Player {
  RED("Red"), BLUE("Blue");

  private final String color;

  Player(String color) {
    this.color = color;
  }

  @Override
  public String toString() {
    return color;
  }
}
