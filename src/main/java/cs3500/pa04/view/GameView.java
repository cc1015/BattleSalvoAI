package cs3500.pa04.view;

import java.io.IOException;

/**
 * The view class for BattleSalvo.
 */
public class GameView {
  private Appendable output;

  /**
   * Sets output field to given Appendable.
   *
   * @param output The Appendable to be set to the output field.
   */
  public GameView(Appendable output) {
    this.output = output;
  }

  /**
   * Displays board to user.
   *
   * @param board The boar to be displayed.
   */
  public void displayBoard(String board) {
    displayMessage(board);
  }

  /**
   * Displays the welcome dialogue to the user.
   */
  public void displayWelcome() {
    displayMessage("Welcome to BattleSalvo Game!");
  }

  /**
   * Displays the fleet options to the user.
   *
   * @param shipCount The max fleet size.
   */
  public void displayShipOptions(int shipCount) {
    displayMessage(
        "Please enter your fleet in the order [Carrier Battleship Destroyer Submarine].\n"
            + "Remember, your fleet may not exceed size " + shipCount
    + " (enter \"STOP\" to quit the game)");
  }

  /**
   * Displays the error that user entered incorrect fleet size.
   *
   * @param shipCount The max fleet size.
   */
  public void displayShipOptionsError(int shipCount) {
    displayMessage("Please make sure you fleet size is " + shipCount + "!");
  }

  /**
   * Displays the height and width options to the user.
   */
  public void displayBoardOptions() {
    displayMessage("Please enter a board height and width any size between 6 and 15 inclusive "
        + "(height width):");
  }

  /**
   * Displays the error that user entered height and width out of bounds or incorrectly.
   */
  public void displayBoardOptionsError() {
    displayMessage("Please enter dimensions that are between 6 and 15 inclusive!");
  }

  /**
   * Displays the error that user entered inputs as words or other characters (not numbers).
   */
  public void displayNumberEntryError() {
    displayMessage("Please enter your inputs in the correct format and as numbers!");
  }

  /**
   * Displays the shooting options to the user.
   *
   * @param shotCount The number of shots the user can take.
   */
  public void displayShootingOption(int shotCount) {
    displayMessage(
        "Please enter " + shotCount + " shots in the format x, y. "
            + "Each shot should be on its own line\n" + "The top left cell is coordinate 0, 0"
    + " (enter \"STOP\" to quit the game)");
  }

  /**
   * Displays error message to user if they input out of bounds coordinates.
   */
  public void displayShootingOptionError() {
    displayMessage("Please enter coordinates that are within the range of the board!");
  }

  /**
   * Displays the winner's name once game has ended
   */
  public void displayWinner(String winnerMessage) {
    displayMessage(winnerMessage);
  }

  /**
   * Appends the given message to the output field. Throws RuntimeException if message cannot
   * be appended.
   *
   * @param message The message to be appended.
   */
  private void displayMessage(String message) {
    try {
      output.append(message + "\n");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
