package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AdvancedPlayer extends AbPlayer{
  public AdvancedPlayer(GameData gameData, Random rand) {
    super(gameData, rand);
  }

  @Override
  public String name() {
    return "cameronojala24";
  }

  @Override
  public List<Coord> takeShots() {
    ArrayList<Coord> emptyCoords = gameData.getOpponentEmptyCoords();

    Collections.shuffle(emptyCoords);
    Collections.sort(emptyCoords, new CellProbabilityComparator());

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
        currentTarget = emptyCoords.get(i);

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
