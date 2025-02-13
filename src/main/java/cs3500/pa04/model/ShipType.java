package cs3500.pa04.model;

/**
 * Represents the ship types in Battle Salvo and the associated sizes.
 */
public enum ShipType {
  CARRIER(6), BATTLESHIP(5), DESTROYER(4), SUBMARINE(3);

  private final int size;

  /**
   * Sets this ShipType's size to the given size.
   *
   * @param size The size this ShipType's size is set to.
   */
  ShipType(int size) {
    this.size = size;
  }

  /**
   * Returns the size of this ShipType.
   *
   * @return The size of this ShipType.
   */
  public int getSize() {
    return this.size;
  }

  /**
   * Returns this ShipType as a char.
   *
   * @return The char representing this ShipType.
   */
  public char toChar() {
    return this.toString().charAt(0);
  }
}
