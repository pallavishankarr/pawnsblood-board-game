package pawnboard.model;

/**
 * The Enum for the Cost of the card which could be one of the value 1, 2 or 3.
 */
public enum Cost {
  One(1),
  Two(2),
  Three(3);

  private int value;
  Cost(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

}
