package cs3500.pa04.model;

/**
 * Represents the status of a particular coordinate on a BattleSalvo board
 */
public enum CoordinateStatus {
  EMPTY("0"),
  HIT("H"),
  CARRIER("C"),
  BATTLESHIP("B"),
  DESTROYER("D"),
  SUBMARINE("S"),
  MISS("M");

  private final String label;

  CoordinateStatus(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }
}
