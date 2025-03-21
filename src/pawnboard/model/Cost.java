package pawnboard.model;

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
