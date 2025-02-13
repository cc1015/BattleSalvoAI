package cs3500.pa04.model;

import java.util.ArrayList;

/**
 * Represents a direction.
 */
public enum CoordDirection {
  NORTH, SOUTH, EAST, WEST;

  /**
   * Returns the change in x for this CoordDirection.
   *
   * @return The change in x for this CoordDirection.
   */
  public int getDx() {
    switch (this) {
      case EAST:
        return 1;

      case WEST:
        return -1;

      default:
        return 0;
    }
  }

  /**
   * Returns the change in y for this CoordDirection.
   *
   * @return The change in y for this CoordDirection.
   */
  public int getDy() {
    switch (this) {
      case SOUTH:
        return 1;

      case NORTH:
        return -1;

      default:
        return 0;
    }
  }

  /**
   * Returns the only necessary directions for traversing a 2d array.
   *
   * @return The only necessary directions for traversing a 2d array.
   */
  public static ArrayList<CoordDirection> getNecessaryValues() {
    ArrayList<CoordDirection> coordDirections = new ArrayList<>();
    coordDirections.add(CoordDirection.SOUTH);
    coordDirections.add(CoordDirection.EAST);
    return coordDirections;
  }
}
