package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents an abstract Player in the BattleSalvo game.
 */
public abstract class AbPlayer implements Player {
  protected GameData gameData;
  protected Random rand;

  /**
   * Sets fields to given values.
   *
   * @param rand The GameData value the gameData field is set to. The random value rand is set to.
   */
  public AbPlayer(GameData gameData, Random rand) {
    this.gameData = gameData;
    this.rand = rand;
  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  public abstract String name();

  /**
   * Given th e specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    if (gameData == null) {
      gameData = new GameData(height, width);
    }

    ArrayList<Ship> allShips = new ArrayList<>();

    int carrierCount = specifications.get(ShipType.CARRIER);
    int battleshipCount = specifications.get(ShipType.BATTLESHIP);
    int destroyerCount = specifications.get(ShipType.DESTROYER);
    int submarineCount = specifications.get(ShipType.SUBMARINE);

    List<Ship> carrierShips = setupShipType(ShipType.CARRIER, carrierCount);
    List<Ship> battleShips =
        setupShipType(ShipType.BATTLESHIP, battleshipCount);
    List<Ship> destroyerShips =
        setupShipType(ShipType.DESTROYER, destroyerCount);
    List<Ship> submarineShips =
        setupShipType(ShipType.SUBMARINE, submarineCount);

    allShips.addAll(carrierShips);
    allShips.addAll(battleShips);
    allShips.addAll(destroyerShips);
    allShips.addAll(submarineShips);

    return allShips;
  }

  /**
   * A helper for setup that takes in a ShipType and the number of ships of that ShipType that
   * need to be placed on the board. Places the given number of ShipTypes on the board and
   * initializes a new Ship with corresponding for every ShipType placed.
   *
   * @param type The type of Ship to be placed.
   *
   * @param count The number of ships of a certain type that need to be placed.
   *
   * @return The placements of each ship in the given ShipType on the board
   */
  private List<Ship> setupShipType(ShipType type, int count) {
    ArrayList<Ship> ships = new ArrayList<>();

    for (int i = 0; i < count; i += 1) {
      List<List<Coord>> availableCoords = gameData.getPossibleCoords(type.getSize());
      int randomIdx = rand.nextInt(availableCoords.size());
      List<Coord> chosenCoords = availableCoords.get(randomIdx);

      Ship ship = gameData.placeOwnShip(type, chosenCoords);
      ships.add(ship);
    }

    return ships;
  }


  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  public abstract List<Coord> takeShots();

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
   * ship on this board
   */
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    return gameData.updateOwnBoard(opponentShotsOnBoard);
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    gameData.updateOpponentBoardHit(shotsThatHitOpponentShips);
  }

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  public void endGame(GameResult result, String reason) {
    System.out.println(result);
  }
}
