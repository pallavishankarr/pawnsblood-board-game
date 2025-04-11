package pawnboard.model;

/**
 * Enum for the InfluenceType which could be X, C or I.
 */
public enum InfluenceType {
  X, // for when there is no influence
  C, //for when there is a Card
  I, // for when there is a Influence
  U, // for when there is an UpgradeInfluence
  D // for when there is a DecreaseInfluence
}
