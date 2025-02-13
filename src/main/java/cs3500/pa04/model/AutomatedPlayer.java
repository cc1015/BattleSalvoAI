package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents an AI player in the BattleSalvo game.
 */
public class AutomatedPlayer extends AbPlayer {

  /**
   * Calls super constructor to initalize.
   */
  public AutomatedPlayer(GameData gameData, Random rand) {
    super(gameData, rand);
  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return "AI";
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    ArrayList<Coord> emptyCoords = gameData.getOpponentEmptyCoords();

    // create a list of coordinates that are adjacent to hit coordinates
    // pick n (number of remaining ships) number of random numbers in this list
    // if list length is less than the number of shots, pick random from all empty coords
    // check if adjacent cell has at least 3 empty next to it

    ArrayList<Coord> chosenCoords = new ArrayList<>();

    Coord currentTarget = null;

    int numShots = Math.min(this.gameData.getOwnShipCount(), emptyCoords.size());

    for (int i = 0; i < numShots; i += 1) {
      boolean foundValidCoordinate = false;

      while (!foundValidCoordinate) {
        int nextCoord = this.rand.nextInt(emptyCoords.size());
        currentTarget = emptyCoords.get(nextCoord);

        if (!chosenCoords.contains(currentTarget)) {
          foundValidCoordinate = true;
        }
      }

      chosenCoords.add(currentTarget);
    }

    gameData.updateOpponentBoardMiss(chosenCoords);

    return chosenCoords;
  }

}
