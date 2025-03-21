package pawnboard.model;

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
}
