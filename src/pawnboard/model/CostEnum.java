package pawnboard.model;

public enum CostEnum {
  One(1),
  Two(2),
  Three(3);

  private int value;
  CostEnum(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

}
