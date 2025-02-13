package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a BattleSalvo board.
 */
public class Board {
  private Coord[][] grid;
  private ArrayList<Coord> emptyCoords;
  private ArrayList<Ship> ships;

  /**
   * Sets the grid field to a new 2d char array of the given height and width and fills all values
   * with '0'.
   *
   * @param height The height of the board.
   * @param width  The width of the board.
   */
  public Board(int height, int width) {
    this.grid = new Coord[height][width];
    this.emptyCoords = new ArrayList<>();
    this.ships = new ArrayList<>();

    // for each coord, make value '0' and create new coordinate of the current coord and add to list
    // of empty Coords
    for (int i = 0; i < height; i += 1) {
      for (int j = 0; j < width; j += 1) {
        Coord c = null;
        if ((i +1) * (j + 1) % 3 == 0) {
          c = new Coord(j, i);
          this.grid[i][j] = c;
          this.emptyCoords.add(c);
        } else {
          c = new Coord(j, i, CoordinateStatus.EMPTY, 45);
          this.grid[i][j] = c;
          this.emptyCoords.add(c);
        }

      }
    }
  }

  /**
   * Returns this Board's height.
   *
   * @return The Board's height.
   */
  public int getHeight() {
    return this.grid.length;
  }

  /**
   * Returns this Board's width.
   *
   * @return This Board's width.
   */
  public int getWidth() {
    return this.grid[0].length;
  }

  /**
   * Returns this Board's grid.
   *
   * @return This Board's grid.
   */
  public Coord[][] getGrid() {
    return grid;
  }

  /**
   * Returns this Board's empty coordinates.
   *
   * @return This Board's empty coordinates.
   */
  public ArrayList<Coord> getEmptyCoords() {
    return this.emptyCoords;
  }

  /**
   * Returns the total number of ships on this Board (hit and not hit).
   *
   * @return The total number of ships on this Board (hit and not hit).
   */
  public int getTotalShipCount() {
    return this.ships.size();
  }

  /**
   * Returns the possible coordinates that a ship of the given ship size could be placed at on
   * this Board.
   *
   * @param shipSize The size of the ship that needs to be placed.
   * @return A list of possible coordinates that a ship of the given ship size could be placed at.
   */
  public List<List<Coord>> getPossibleCoords(int shipSize) {
    ArrayList<List<Coord>> possibleCoords = new ArrayList<>();

    // for each empty coordinate on this Board, check if there are enough empty coordinates in
    // north and east directions from the empty coordinate such that a ship of the given ship
    // size can be held.
    for (Coord emptyCoord : this.emptyCoords) {
      int startingX = emptyCoord.getX();
      int startingY = emptyCoord.getY();

      // check north and east directions for each empty coordinate
      for (CoordDirection coordDirection : CoordDirection.getNecessaryValues()) {
        int dx = coordDirection.getDx();
        int dy = coordDirection.getDy();

        ArrayList<Coord> coordinates = new ArrayList<>();
        coordinates.add(emptyCoord);

        // check that subsequent coords in the current coordDirection is empty and in bounds for the
        // ship size
        for (int i = 1; i < shipSize; i += 1) {
          Coord newCoord = new Coord(startingX + (dx * i), startingY + (dy * i));

          // if the coordinate is in bounds and empty (can hold ship)
          if (inBounds(newCoord) && emptyCoords.contains(newCoord)) {
            coordinates.add(newCoord);
          }
        }

        // if the number of coordinates that are in bounds and empty equals the size of the ship,
        // add it to the list of possible coordinates (can hold ship)
        if (coordinates.size() == shipSize) {
          possibleCoords.add(coordinates);
        }
      }
    }

    return possibleCoords;
  }

  /**
   * Determines if the given coordinates are in this Board's range.
   *
   * @param coord The coordinate that is being evaluated.
   * @return Whether the given coordinate is in this Board's range.
   */
  private boolean inBounds(Coord coord) {
    int x = coord.getX();
    int y = coord.getY();

    return ((x >= 0) && (x < grid[0].length) && (y >= 0) && (y < grid.length));
  }

  /**
   * Places the given ShipType at the Ship's coordinates. Returns a new ship representing the ship
   * that has been placed.
   *
   * @param ship        The ShipType to be placed.
   * @param coordinates The coordinates the ShipType is placed at.
   * @return The ship that has been placed.
   */
  public Ship placeShip(ShipType ship, List<Coord> coordinates) {
    for (Coord coord : coordinates) {
      int i = coord.getY();
      int j = coord.getX();

      CoordinateStatus status = null;

      switch (ship) {
        case CARRIER:
          status = CoordinateStatus.CARRIER;
          break;
        case BATTLESHIP:
          status = CoordinateStatus.BATTLESHIP;
          break;
        case DESTROYER:
          status = CoordinateStatus.DESTROYER;
          break;
        case SUBMARINE:
          status = CoordinateStatus.SUBMARINE;
          break;
      }

      this.grid[i][j] = new Coord(j, i, status, 50);
      this.emptyCoords.remove(coord);
    }

    Ship newShip = new Ship(ship, coordinates);
    this.ships.add(newShip);
    return newShip;
  }

  /**
   * Accepts a shot on this Board. Sets the given shot coordinate to 'H' if the coordinate
   * previously had a ship or 'M' if the coordinate was previously empty or already missed.
   * Returns true of the shot hit a ship, false otherwise.
   *
   * @param shot The shot being accepted.
   * @return Whether the given coordinate was a successful shot (hit a ship).
   */
  public boolean shoot(Coord shot) {
    int i = shot.getY();
    int j = shot.getX();

    Coord shotCoord = this.grid[i][j];

    if (shotCoord.getStatus().equals(CoordinateStatus.EMPTY) ||
        shotCoord.getStatus().equals(CoordinateStatus.MISS)) {
      placeMiss(shot);
      return false;
    } else {
      placeHit(shot);
      return true;
    }
  }

  /**
   * Places a 'M' at the given coordinate and removes the given coordinate from this Board's list
   * of empty coordinates if the coordinate was previously empty.
   *
   * @param coord The coordinate the miss is placed at.
   */
  public void placeMiss(Coord coord) {
    int i = coord.getY();
    int j = coord.getX();

    this.grid[i][j].setStatus(CoordinateStatus.MISS);
    this.emptyCoords.remove(coord);
  }

  /**
   * Places a 'H' at the given coordinate.
   *
   * @param coord The coordinate the hit is placed at.
   */
  public void placeHit(Coord coord) {
    int i = coord.getY();
    int j = coord.getX();

    this.grid[i][j].setStatus(CoordinateStatus.HIT);
    ;
    updateShipsSunk();
    updateProbabilitiesHits(coord);
  }

  /**
   * Updates the number of ships hit in this Board.
   */
  private void updateShipsSunk() {
    ArrayList<Ship> updatedShipsToRemove = new ArrayList<>();

    // for each ship, check if all the ship's coordinates are hit
    for (Ship ship : ships) {
      List<Coord> coordinates = ship.getCoordinates();
      boolean allHit = true;

      // for each coordinate, check if they are hit
      for (Coord coord : coordinates) {
        int i = coord.getY();
        int j = coord.getX();

        if (!(this.grid[i][j].getStatus().equals(CoordinateStatus.HIT)) && allHit) {
          allHit = false;
        }
      }
      if (allHit) {
        updatedShipsToRemove.add(ship);
      }
    }

    // removes all ships that were sunk if they were previously not sunk
    for (Ship ship : updatedShipsToRemove) {
      this.ships.remove(ship);
    }
  }

  private void updateProbabilitiesHits(Coord coord) {
    int x = coord.getX();
    int y = coord.getY();

    Coord northCoord = new Coord(x, y - 1);
    Coord southCoord = new Coord(x, y + 1);
    Coord eastCoord = new Coord(x - 1, y);
    Coord westCoord = new Coord(x + 1, y);

    List<Coord> validCoords = new ArrayList<>();
    validCoords.add(northCoord);
    validCoords.add(southCoord);
    validCoords.add(eastCoord);
    validCoords.add(westCoord);

    for (int i = 0; i < emptyCoords.size(); i += 1) {
      Coord c = this.emptyCoords.get(i);
      if (validCoords.contains(c)) {
        c.changeProbability(25);
      }
    }

    updateProbabilitiesHits2(coord);
  }

  private void updateProbabilitiesHits2(Coord coord) {
    int x = coord.getX();
    int y = coord.getY();

    int northx = x + CoordDirection.NORTH.getDx();
    int northy = y + CoordDirection.NORTH.getDy();
    Coord northCoord = new Coord(northx, northy);

    int southx = x + CoordDirection.SOUTH.getDx();
    int southy = y + CoordDirection.SOUTH.getDy();
    Coord southCoord = new Coord(southx, southy);

    if (inBounds(northCoord) && inBounds(southCoord)) {
      Coord north = this.grid[northy][northx];
      Coord south = this.grid[southy][southx];
      if (north.getStatus().equals(CoordinateStatus.HIT) ||
          south.getStatus().equals(CoordinateStatus.HIT)) {
        north.changeProbability(10);
        south.changeProbability(10);
      }
    }

    int eastx = x + CoordDirection.EAST.getDx();
    int easty = y + CoordDirection.EAST.getDy();
    Coord eastCoord = new Coord(eastx, easty);

    int westx = x + CoordDirection.WEST.getDx();
    int westy = y + CoordDirection.WEST.getDy();
    Coord westCoord = new Coord(westx, westy);

    if (inBounds(eastCoord) && inBounds(westCoord)) {
      Coord east = this.grid[easty][eastx];
      Coord west = this.grid[westy][westx];
      if (east.getStatus().equals(CoordinateStatus.HIT) ||
          west.getStatus().equals(CoordinateStatus.HIT)) {
        east.changeProbability(10);
        west.changeProbability(10);
      }
    }
  }

  /**
   * Determines if all ships in this Board have been hit.
   *
   * @return Whether all the ships in this Board have been hit.
   */
  public boolean allShipsSunk() {
    return ships.size() == 0;
  }

  public void updateProbabilitiesMiss() {
    for (Coord coord : this.emptyCoords) {
      int x = coord.getX();
      int y = coord.getY();
      boolean verticalMiss = false;
      boolean horizontalMiss = false;

      ArrayList<Coord> verticalCoords = new ArrayList<>();
      int north1X = x + CoordDirection.NORTH.getDx();
      int north1Y = y + CoordDirection.NORTH.getDy();
      Coord north1Coord = new Coord(north1X, north1Y);

      if (inBounds(north1Coord)) {
        verticalCoords.add(this.grid[north1Y][north1X]);
      }

      int north2X = north1X + CoordDirection.NORTH.getDx();
      int north2Y = north1Y + CoordDirection.NORTH.getDy();
      Coord north2Coord = new Coord(north2X, north2Y);

      if (inBounds(north2Coord)) {
        verticalCoords.add(this.grid[north2Y][north2X]);
      }

      int south1X = x + CoordDirection.SOUTH.getDx();
      int south1Y = y + CoordDirection.SOUTH.getDy();
      Coord south1Coord = new Coord(south1X, south1Y);

      if (inBounds(south1Coord)) {
        verticalCoords.add(this.grid[south1Y][south1X]);
      }

      int south2X = south1X + CoordDirection.SOUTH.getDx();
      int south2Y = south1Y + CoordDirection.SOUTH.getDy();
      Coord south2Coord = new Coord(south2X, south2Y);

      if (inBounds(south2Coord)) {
        verticalCoords.add(this.grid[south2Y][south2X]);
      }

      if (verticalCoords.size() == 2) {
        CoordinateStatus coord1Status = verticalCoords.get(0).getStatus();
        CoordinateStatus coord2Status = verticalCoords.get(1).getStatus();
        if (coord1Status.equals(CoordinateStatus.MISS) ||
            coord2Status.equals(CoordinateStatus.MISS)) {
          verticalMiss = true;
        }
      } else if (verticalCoords.size() == 3) {
        CoordinateStatus coord1Status = verticalCoords.get(0).getStatus();
        CoordinateStatus coord2Status = verticalCoords.get(1).getStatus();
        CoordinateStatus coord3Status = verticalCoords.get(2).getStatus();
        if (coord2Status.equals(CoordinateStatus.MISS)) {
          verticalMiss = true;
        } else if (coord1Status.equals(CoordinateStatus.MISS) &&
            coord3Status.equals(CoordinateStatus.MISS)) {
          verticalMiss = true;
        }
      } else if (verticalCoords.size() == 4) {
        CoordinateStatus coord1Status = verticalCoords.get(0).getStatus();
        CoordinateStatus coord2Status = verticalCoords.get(1).getStatus();
        CoordinateStatus coord3Status = verticalCoords.get(2).getStatus();
        CoordinateStatus coord4Status = verticalCoords.get(3).getStatus();
        if (coord2Status.equals(CoordinateStatus.MISS) ||
            coord3Status.equals(CoordinateStatus.MISS)) {
          if ((coord1Status.equals(CoordinateStatus.MISS) ||
              coord2Status.equals(CoordinateStatus.MISS)) &&
              (coord3Status.equals(CoordinateStatus.MISS) ||
                  coord4Status.equals(CoordinateStatus.MISS))) {
            verticalMiss = true;
          }
        }
      }

      ArrayList<Coord> horizontalCoords = new ArrayList<>();
      int west1X = x + CoordDirection.WEST.getDx();
      int west1Y = y + CoordDirection.WEST.getDy();
      Coord west1Coord = new Coord(west1X, west1Y);

      if (inBounds(west1Coord)) {
        horizontalCoords.add(this.grid[west1Y][west1X]);
      }

      int west2X = west1X + CoordDirection.WEST.getDx();
      int west2Y = west1Y + CoordDirection.WEST.getDy();
      Coord west2Coord = new Coord(west2X, west2Y);

      if (inBounds(west2Coord)) {
        horizontalCoords.add(this.grid[west2Y][west2X]);
      }

      int east1X = x + CoordDirection.EAST.getDx();
      int east1Y = y + CoordDirection.EAST.getDy();
      Coord east1Coord = new Coord(east1X, east1Y);

      if (inBounds(east1Coord)) {
        horizontalCoords.add(this.grid[east1Y][east1X]);
      }

      int east2X = east1X + CoordDirection.EAST.getDx();
      int east2Y = east1Y + CoordDirection.EAST.getDy();
      Coord east2Coord = new Coord(east2X, east2Y);

      if (inBounds(east2Coord)) {
        horizontalCoords.add(this.grid[east2Y][east2X]);
      }

      if (horizontalCoords.size() == 2) {
        CoordinateStatus coord1Status = horizontalCoords.get(0).getStatus();
        CoordinateStatus coord2Status = horizontalCoords.get(1).getStatus();
        if (coord1Status.equals(CoordinateStatus.MISS) ||
            coord2Status.equals(CoordinateStatus.MISS)) {
          horizontalMiss = true;
        }
      } else if (horizontalCoords.size() == 3) {
        CoordinateStatus coord1Status = horizontalCoords.get(0).getStatus();
        CoordinateStatus coord2Status = horizontalCoords.get(1).getStatus();
        CoordinateStatus coord3Status = horizontalCoords.get(2).getStatus();
        if (coord2Status.equals(CoordinateStatus.MISS)) {
          horizontalMiss = true;
        } else if (coord1Status.equals(CoordinateStatus.MISS) &&
            coord3Status.equals(CoordinateStatus.MISS)) {
          horizontalMiss = true;
        }
      } else if (horizontalCoords.size() == 4) {
        CoordinateStatus coord1Status = horizontalCoords.get(0).getStatus();
        CoordinateStatus coord2Status = horizontalCoords.get(1).getStatus();
        CoordinateStatus coord3Status = horizontalCoords.get(2).getStatus();
        CoordinateStatus coord4Status = horizontalCoords.get(3).getStatus();
        if (coord2Status.equals(CoordinateStatus.MISS) ||
            coord3Status.equals(CoordinateStatus.MISS)) {
          if ((coord1Status.equals(CoordinateStatus.MISS) ||
              coord2Status.equals(CoordinateStatus.MISS)) &&
              (coord3Status.equals(CoordinateStatus.MISS) ||
                  coord4Status.equals(CoordinateStatus.MISS))) {
            horizontalMiss = true;
          }
        }
      }

      if (horizontalMiss && verticalMiss) {
        coord.setProbability(-1000);
      }
    }
  }

  /**
   * Returns this Board's grid as one String.
   *
   * @return This Board's grid as one String.
   */
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();

    // for each coord, append to the output string and append a space after.
    // for each row, append a new line after.
    for (int i = 0; i < grid.length; i += 1) {
      for (int j = 0; j < grid[0].length; j += 1) {
        String character = grid[i][j].getStatus().toString();
        output.append(character);
        output.append(" ");
      }
      output.append("\n");
    }

    return output.toString();
  }

  /**
   * Whether two Boards are equal (same toString output, then equal).
   *
   * @param other The other object being compared to.
   * @return Whether two Boards are equal.
   */
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Board)) {
      return false;
    }
    Board that = (Board) other;
    return this.toString().equals(that.toString());
  }
}
