package cs3500.pa04.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cs3500.pa04.Driver;
import cs3500.pa04.json.CoordJson;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.SetupJson;
import cs3500.pa04.json.ShipJson;
import cs3500.pa04.model.AbPlayer;
import cs3500.pa04.model.Coord;
import cs3500.pa04.model.Ship;
import cs3500.pa04.model.ShipType;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a controller for the game of BattleSalvo played against the server
 */
public class ProxyController implements Controller {
  private final AbPlayer player;
  private final Socket server;
  private final InputStream in;
  private final PrintStream out;
  private final ObjectMapper mapper = new ObjectMapper();

  /**
   * Represents a controller for the game of BattleSalvo played against the server
   *
   * @param server the server to get responses for the game
   * @param player the computer player to play against the server
   */
  public ProxyController(Socket server, AbPlayer player) throws IOException {
    this.server = server;
    this.player = player;
    this.in = server.getInputStream();
    this.out = new PrintStream(server.getOutputStream());

  }

  /**
   * Runs a new game of BattleSalvo with the server
   */
  public void executeGame() {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);

      while (!this.server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);
        delegateMessage(message);
      }
    } catch (IOException e) {
      // Disconnected from server or parsing exception
      //System.out.println("disconnected");
    }
  }

  /**
   * Does the appropriate action depending on server response
   *
   * @param message the message received from the server
   */
  private void delegateMessage(MessageJson message) {
    String name = message.messageName();
    JsonNode arguments = message.arguments();

    if ("join".equals(name)) {
      this.handleJoin();
    } else if ("setup".equals(name)) {
      this.handleSetup(arguments);
    } else if ("take-shots".equals(name)) {
      this.handleTakeShots();
    } else if ("report-damage".equals(name)) {
      this.handleReportDamage(arguments);
    } else if ("successful-hits".equals(name)) {
      this.handleSuccessfulHits(arguments);
    } else if ("end-game".equals(name)) {
      this.handleEndGame(arguments);
    } else {
      throw new IllegalStateException("Invalid method name.");
    }
  }

  /**
   * Joins the game of BattleSalvo with the server
   *
   */
  private void handleJoin() {
    ObjectNode args = this.mapper.createObjectNode();
    args.put("name", this.player.name());
    args.put("game-type", "MULTI");
    MessageJson response = new MessageJson("join", args);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    this.out.print(jsonResponse);
  }

  /**
   * Delegates to the player setup method and sends the response to the server.
   *
   * @param arguments The setup arguments.
   */
  private void handleSetup(JsonNode arguments) {
    SetupJson setup = this.mapper.convertValue(arguments, SetupJson.class);
    int width = setup.getWidth();
    int height = setup.getHeight();
    Map<ShipType, Integer> specs = setup.getFleetSpec();

    List<Ship> ships = player.setup(height, width, specs);

    ShipJson[] shipJsons = new ShipJson[ships.size()];

    // for every ship, make a CoordJson of its starting coordinate and make a ShipJson with that
    // CoordJson, the ship's length, and the ship's direction.
    for (int i = 0; i < ships.size(); i += 1) {
      Ship ship = ships.get(i);
      Coord coord = ship.getCoordinates().get(0);
      CoordJson coordJson = new CoordJson(coord.getX(), coord.getY());
      int length = ship.getType().getSize();
      String direction = ship.getDirection().toString();
      ShipJson shipJson = new ShipJson(coordJson, length, direction);
      shipJsons[i] = shipJson;
    }

    ArrayNode array = mapper.valueToTree(shipJsons);
    JsonNode fleet = mapper.createObjectNode().set("fleet", array);
    MessageJson response = new MessageJson("setup", fleet);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    this.out.print(jsonResponse);
  }

  private void handleTakeShots() {
    List<Coord> coords = player.takeShots();
    CoordJson[] coordJsons = new CoordJson[coords.size()];

    for (int i = 0; i < coords.size(); i += 1) {
      Coord c = coords.get(i);
      CoordJson coordJson = new CoordJson(c.getX(), c.getY());
      coordJsons[i] = coordJson;
    }

    ArrayNode array = mapper.valueToTree(coordJsons);
    JsonNode coordinates = mapper.createObjectNode().set("coordinates", array);
    MessageJson response = new MessageJson("take-shots", coordinates);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    this.out.print(jsonResponse);
  }

  private void handleReportDamage(JsonNode arguments) {
    JsonNode coordJsons = arguments.get("coordinates");

    List<Coord> coords = new ArrayList<>();

    if (coordJsons.isArray()) {
      for (JsonNode jsonNode : coordJsons) {
        int x = jsonNode.get("x").asInt();
        int y = jsonNode.get("y").asInt();

        Coord newCoord = new Coord(x, y);
        coords.add(newCoord);
      }
    }

    List<Coord> successfulCoords = player.reportDamage(coords);

    CoordJson[] successfulCoordJsons = new CoordJson[successfulCoords.size()];

    for (int i = 0; i < successfulCoords.size(); i += 1) {
      Coord c = successfulCoords.get(i);
      CoordJson coordJson = new CoordJson(c.getX(), c.getY());
      successfulCoordJsons[i] = coordJson;
    }

    ArrayNode array = mapper.valueToTree(successfulCoordJsons);
    JsonNode coordinatesJsonNode = mapper.createObjectNode().set("coordinates", array);
    MessageJson response = new MessageJson("report-damage", coordinatesJsonNode);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    this.out.print(jsonResponse);
  }

  private void handleSuccessfulHits(JsonNode arguments) {
    JsonNode coordJsons = arguments.get("coordinates");

    List<Coord> coords = new ArrayList<>();

    if (coordJsons.isArray()) {
      for (JsonNode jsonNode : coordJsons) {
        int x = jsonNode.get("x").asInt();
        int y = jsonNode.get("y").asInt();

        Coord newCoord = new Coord(x, y);
        coords.add(newCoord);
      }
    }
    this.player.successfulHits(coords);

    JsonNode empty = mapper.createObjectNode();
    MessageJson response = new MessageJson("successful-hits", empty);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    this.out.print(jsonResponse);
  }

  private void handleEndGame(JsonNode arguments) {
    String gameResult = arguments.get("result").toString();
    String reason = arguments.get("reason").toString();

    if (gameResult.equals("\"WIN\"")) {
      Driver.winCount += 1;
    }
    else if (gameResult.equals("\"DRAW\"")) {
      Driver.drawCount += 1;
    }
    else if (gameResult.equals("\"LOSE\"")) {
      Driver.loseCount += 1;
    }
    else {
      Driver.other += gameResult;
    }

    JsonNode empty = mapper.createObjectNode();
    MessageJson response = new MessageJson("end-game", empty);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    this.out.print(jsonResponse);
  }
}
