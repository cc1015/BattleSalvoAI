package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a BattleSalvo game's data for one player.
 */
public class GameData {
  private Board ownBoard;
  private Board opponentBoard;
  private List<Coord> mostRecentShots;

  /**
   * Initializes the ownBoard and opponentBoard with the given height and width. Sets
   * mostRecentShots to an empty list.
   *
   * @param boardHeight The board height.
   * @param boardWidth  The board width.
   */
  public GameData(int boardHeight, int boardWidth) {
    this.ownBoard = new Board(boardHeight, boardWidth);
    this.opponentBoard = new Board(boardHeight, boardWidth);
    this.mostRecentShots = new ArrayList<>();
  }

  /**
   * Return's board width.
   *
   * @return Board width.
   */
  public int getBoardWidth() {
    return this.ownBoard.getWidth();
  }

  /**
   * Returns board height.
   *
   * @return Board height.
   */
  public int getBoardHeight() {
    return this.ownBoard.getHeight();
  }

  /**
   * Return's the most recent shots.
   *
   * @return The most recent shots.
   */
  public List<Coord> getMostRecentShots() {
    return mostRecentShots;
  }

  /**
   * Return's the empty coordinates of the opponent's board.
   *
   * @return The empty coordinates of the opponent's own board.
   */
  public ArrayList<Coord> getOpponentEmptyCoords() {
    return opponentBoard.getEmptyCoords();
  }

  /**
   * Return's the player's number of ships on their board.
   *
   * @return The number of ships on the player's own board.
   */
  public int getOwnShipCount() {
    return ownBoard.getTotalShipCount();
  }

  /**
   * Return's ownBoard as a 2d array.
   *
   * @return ownBoard as a 2d array.
   */
  public Coord[][] getOwnBoard() {
    return ownBoard.getGrid();
  }

  /**
   * Return's opponentBoard as a 2d array.
   *
   * @return opponentBoard as a 2d array.
   */
  public Coord[][] getOpponentBoard() {
    return opponentBoard.getGrid();
  }

  /**
   * Sets most recent shots to the given list of shots.
   *
   * @param nextShots The list of coordinates mostRecentShots is set to.
   */
  public void updateMostRecentShots(List<Coord> nextShots) {
    this.mostRecentShots = nextShots;
  }

  /**
   * Returns the possible coordinates that the ownBoard has for the given ship size.
   *
   * @param shipSize The ship size to be placed.
   * @return The possible coordinates that the ownBoard has for the given ship size.
   */
  public List<List<Coord>> getPossibleCoords(int shipSize) {
    return ownBoard.getPossibleCoords(shipSize);
  }

  /**
   * Places the given ship at the given coordinates on the ownBoard.
   *
   * @param ship   The ship to be placed.
   * @param coords The coordinates that the ship is being placed at.
   * @return The placed ship.
   */
  public Ship placeOwnShip(ShipType ship, List<Coord> coords) {
    return ownBoard.placeShip(ship, coords);
  }

  /**
   * Updates the ownBoard with the given shots. Returns a list of the shots that hit a ship.
   *
   * @param shots The coordinates where the shots are.
   * @return A list of the shots that hit a ship.
   */
  public List<Coord> updateOwnBoard(List<Coord> shots) {
    ArrayList<Coord> hitCoords = new ArrayList<>();

    for (Coord coord : shots) {
      if (ownBoard.shoot(coord)) {
        hitCoords.add(coord);
      }
    }
    return hitCoords;
  }

  /**
   * Updates the opponentBoard with the given shots. Places a miss on the opponentBoard for the
   * shots taken that were not a part of the given list.
   *
   * @param shotsThatHitOpponentShips The shots that sucessfully hit opponent ships.
   */
  public void updateOpponentBoardHit(List<Coord> shotsThatHitOpponentShips) {
    for (Coord coord : shotsThatHitOpponentShips) {
      opponentBoard.placeHit(coord);
    }
    opponentBoard.updateProbabilitiesMiss();
  }

  public void updateOpponentBoardMiss(List<Coord> shots) {
    for (Coord coord : shots) {
      opponentBoard.placeMiss(coord);
    }
  }

  /**
   * Determines whether the game has ended (if own board or opponent board has all ships sunk).
   *
   * @return Whether the game has ended.
   */
  public boolean endGame() {
    return ownBoard.allShipsSunk();
  }

  /**
   * Returns the ownBoard as a String.
   *
   * @return The ownBoard as a String.
   */
  public String ownBoardToString() {
    return ownBoard.toString();
  }

  /**
   * Returns the opponentBoard as a String.
   *
   * @return The opponentBoard as a String.
   */
  public String opponentBoardToString() {
    return opponentBoard.toString();
  }
}
