package pawnboard.model;

/**
 * Enum for the Count of a Pawn on a Cell which could be 0, 1, 2 or 3.
 */
public enum Count {
  Zero(0),
  One(1),
  Two(2),
  Three(3);

  private int value;

  Count(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    switch (this) {
      case Zero:
        return "0";
      case One:
        return "1";
      case Two:
        return "2";
      case Three:
        return "3"; // Added missing case for 'Three'
      default:
        throw new IllegalArgumentException("Unexpected value: " + this);
    }
  }
}
