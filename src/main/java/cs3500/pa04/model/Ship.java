package cs3500.pa04.model;

import java.util.List;

/**
 * Represents a ship with a type and coordinates in a Battle Salvo game.
 */
public class Ship {
  private List<Coord> coordinates;
  private final ShipType type;
  private final ShipDirection direction;

  /**
   * Sets the given ShipType and Coord to the type and position fields.
   *
   * @param type The ShipType the type field is set to.
   */
  public Ship(ShipType type, List<Coord> coordinates) {
    this.type = type;
    this.coordinates = coordinates;
    this.direction = computeDirection();
  }

  /**
   * Returns this Ship's coordinates.
   *
   * @return The Ship's coordinates.
   */
  public List<Coord> getCoordinates() {
    return this.coordinates;
  }

  /**
   * Returns this Ship's type.
   *
   * @return The Ship's type.
   */
  public ShipType getType() {
    return type;
  }

  /**
   * Returns this Ship's direction.
   *
   * @return This Ship's direction.
   */
  public ShipDirection getDirection() {
    return this.direction;
  }

  private ShipDirection computeDirection() {
    Coord coord1 = this.coordinates.get(0);
    Coord coord2 = this.coordinates.get(1);

    if (coord1.getX() == coord2.getX()) {
      return ShipDirection.VERTICAL;
    }
    else {
      return ShipDirection.HORIZONTAL;
    }
  }

  /**
   * Whether two Ships are equal (if the coordinates and type are the same, then two
   * Ships are equal).
   *
   * @param other The other object being compared to.
   *
   * @return Whether two Ships are equal.
   */
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Ship)) {
      return false;
    }
    Ship that = (Ship) other;
    return this.coordinates.equals(that.coordinates) && this.type.equals(that.type);
  }
}
