package cs3500.pa04.controller;

import cs3500.pa04.model.AutomatedPlayer;
import cs3500.pa04.model.Coord;
import cs3500.pa04.model.GameData;
import cs3500.pa04.model.ManualPlayer;
import cs3500.pa04.model.Player;
import cs3500.pa04.model.ShipType;
import cs3500.pa04.view.GameView;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Represents a BattleSalvo controller.
 */
public class BattleSalvoController implements Controller {
  Player manualPlayer;
  Player aiPlayer;
  GameData manualData;
  GameData aiData;
  GameView view;
  Scanner scanner;
  boolean userStop;

  /**
   * Initializes view with the given output, and initializes scanner with the given output.
   * Initializes manualData, aiData, manualPlayer, and aiPlayer with the user input height
   * and width.
   *
   * @param input
   * @param output
   */
  public BattleSalvoController(Readable input, Appendable output, Random rand) {
    this.view = new GameView(output);
    this.scanner = new Scanner(input);
    view.displayWelcome();
    userStop = false;
    gatherInitialData(rand);
  }

  /**
   * Loops the game until a player wins.
   */
  public void executeGame() {
    setupBoards();
    if (!userStop) {
      view.displayBoard(manualData.opponentBoardToString());
      view.displayBoard(manualData.ownBoardToString());
    }

    if (!userStop) {
      while (!gameOver() && !userStop) {
        gatherCoords();
        runShootOut();
        if (!userStop) {
          view.displayBoard(manualData.opponentBoardToString());
          view.displayBoard(manualData.ownBoardToString());
        }
      }
      view.displayWinner(winner());
    }
  }

  /**
   * Takes in user input of board height and width and initializes manualData, aiData, manualPlayer,
   * and aiPlayer with the user input height and width.
   */
  private void gatherInitialData(Random rand) {
    view.displayBoardOptions();
    String dimensions = readUserInput();

    ArrayList<String> parsedDimensions = parseUserInput(dimensions);
    int height = 0;
    int width = 0;
    boolean validInput = true;

    try {
      height = Integer.parseInt(parsedDimensions.get(0));
      width = Integer.parseInt(parsedDimensions.get(1));
    } catch (Exception e) {
      view.displayNumberEntryError();
      validInput = false;
    }

    if (validInput && (invalidDimensions(width, height))) {
      view.displayBoardOptionsError();
      validInput = false;
    }

    if (validInput) {
      this.manualData = new GameData(height, width);
      this.aiData = new GameData(height, width);

      this.manualPlayer = new ManualPlayer(this.manualData, rand);
      this.aiPlayer = new AutomatedPlayer(this.aiData, rand);
    } else {
      gatherInitialData(rand);
    }
  }

  /**
   * Determines whether both dimensions are between 6 and 15.
   *
   * @param x width.
   * @param y height.
   * @return Whether both dimensions are between 6 and 15.
   */
  private boolean invalidDimensions(int x, int y) {
    final int MIN_LENGTH = 6;
    final int MAX_LENGTH = 15;
    boolean widthInvalidSmall = x < MIN_LENGTH;
    boolean widthInvalidLarge = x > MAX_LENGTH;
    boolean heightInvalidSmall = y < MIN_LENGTH;
    boolean heightInvalidLarge = y > MAX_LENGTH;

    return widthInvalidSmall || widthInvalidLarge || heightInvalidSmall || heightInvalidLarge;
  }


  /**
   * Takes in user input of ship fleet and sets up the user's board and the ai's board.
   */
  private void setupBoards() {
    int shipCount = Math.min(manualData.getBoardWidth(), manualData.getBoardHeight());

    view.displayShipOptions(shipCount);
    String fleet = readUserInput();

    if (userStop) {
      return;
    }

    ArrayList<String> parsedFleet = parseUserInput(fleet);

    boolean validInput = true;

    int carrierCount = 0;
    int battleshipCount = 0;
    int destoyerCount = 0;
    int subCount = 0;

    try {
      carrierCount = Integer.parseInt(parsedFleet.get(0));
      battleshipCount = Integer.parseInt(parsedFleet.get(1));
      destoyerCount = Integer.parseInt(parsedFleet.get(2));
      subCount = Integer.parseInt(parsedFleet.get(3));
    } catch (Exception e) {
      view.displayNumberEntryError();
      validInput = false;
    }

    int givenFleetSize = carrierCount + battleshipCount + destoyerCount + subCount;

    if (validInput && (givenFleetSize != shipCount)) {
      view.displayShipOptionsError(shipCount);
      validInput = false;
    }

    if (validInput) {
      EnumMap<ShipType, Integer> specifications = new EnumMap<>(ShipType.class);
      specifications.put(ShipType.CARRIER, carrierCount);
      specifications.put(ShipType.BATTLESHIP, battleshipCount);
      specifications.put(ShipType.DESTROYER, destoyerCount);
      specifications.put(ShipType.SUBMARINE, subCount);

      manualPlayer.setup(manualData.getBoardHeight(), manualData.getBoardWidth(), specifications);
      aiPlayer.setup(aiData.getBoardHeight(), aiData.getBoardWidth(), specifications);
    } else {
      setupBoards();
    }
  }

  /**
   * Takes in user input of the coordinates to shoot at.
   */
  private void gatherCoords() {
    int coordCount =
        Math.min(manualData.getOwnShipCount(), manualData.getOpponentEmptyCoords().size());

    view.displayShootingOption(coordCount);

    ArrayList<Coord> coordinates = new ArrayList<>();

    boolean validInput = true;

    for (int i = 0; i < coordCount; i += 1) {
      String coord = readUserInput();
      ArrayList<String> parsedCoord = parseUserInput(coord);

      if (userStop) {
        return;
      }

      int x = 0;
      int y = 0;

      try {
        x = Integer.parseInt(parsedCoord.get(0));
        y = Integer.parseInt(parsedCoord.get(1));
      } catch (Exception e) {
        view.displayNumberEntryError();
        validInput = false;
      }

      if (validInput &&
          !(x < 0 || x > manualData.getBoardWidth() || y < 0 || y > manualData.getBoardHeight())) {
        Coord newCoord = new Coord(x, y);
        coordinates.add(newCoord);
      } else {
        validInput = false;
      }
    }

    if (validInput) {
      manualData.updateMostRecentShots(coordinates);
    } else {
      view.displayShootingOptionError();
      gatherCoords();
    }
  }

  /**
   * Runs the shooting between the user and the ai player.
   */
  private void runShootOut() {
    List<Coord> manualToAiShots = manualPlayer.takeShots();
    List<Coord> aiToManual = aiPlayer.takeShots();

    aiData.updateMostRecentShots(aiToManual);

    List<Coord> successfulShotsOnManualBoard = manualPlayer.reportDamage(aiToManual);
    List<Coord> successfulShotsOnAiBoard = aiPlayer.reportDamage(manualToAiShots);

    manualPlayer.successfulHits(successfulShotsOnAiBoard);
    aiPlayer.successfulHits(successfulShotsOnManualBoard);
  }

  /**
   * Determines if the game is over.
   *
   * @return Whether the game is over.
   */
  private boolean gameOver() {
    boolean gameOver = manualData.endGame() || aiData.endGame();
    return gameOver;
  }

  /**
   * Returns the name of the winner of the game.
   *
   * @return The name of the winner.
   */
  private String winner() {
    if (aiData.endGame()) {
      return manualPlayer.name() + " won!";
    } else {
      return "Game has ended.";
    }
  }

  /**
   * Uses scanner to read the next line of user input.
   *
   * @return The next line of user input as a String.
   */
  private String readUserInput() {
    StringBuilder line = new StringBuilder();
    if (scanner.hasNextLine()) {
      line.append(scanner.nextLine());
    }
    if (line.toString().equals("STOP")) {
      userStop = true;
    }
    return line.toString();
  }


  /**
   * Parses the given string and returns a list of the elements that were separated by a space.
   *
   * @param input The String to be parsed.
   * @return List of Strings that were separated by spaces in the given String.
   */
  private ArrayList<String> parseUserInput(String input) {
    ArrayList<String> inputs = new ArrayList<>();
    StringBuilder element = new StringBuilder();

    // check each character of string for space, save each non-space sequence of characters as
    // an element of the returned list
    for (int i = 0; i < input.length(); i += 1) {
      if (input.charAt(i) == ' ') {
        inputs.add(element.toString());
        element = new StringBuilder();
      } else {
        element.append(input.charAt(i));
      }
    }

    if (!element.isEmpty()) {
      inputs.add(element.toString());
    }

    return inputs;
  }
}
