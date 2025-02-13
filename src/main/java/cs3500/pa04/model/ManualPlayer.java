package cs3500.pa04.model;

import java.util.List;
import java.util.Random;

/**
 * Represents a manual player of BattleSalvo.
 */
public class ManualPlayer extends AbPlayer {
  public ManualPlayer(GameData gameData, Random rand) {
    super(gameData, rand);
  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return "You";
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    return gameData.getMostRecentShots();
  }
}
