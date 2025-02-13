package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.ShipType;
import java.util.EnumMap;
import java.util.Map;

public record FleetSpecJson(
  @JsonProperty("CARRIER") int carrierCount,
  @JsonProperty("BATTLESHIP") int battleshipCount,
  @JsonProperty("DESTROYER") int destoyerCount,
  @JsonProperty("SUBMARINE") int subCount) {

  public Map<ShipType, Integer> makeMap() {
    Map<ShipType, Integer> specs = new EnumMap<>(ShipType.class);
    specs.put(ShipType.CARRIER, carrierCount);
    specs.put(ShipType.BATTLESHIP, battleshipCount);
    specs.put(ShipType.DESTROYER, destoyerCount);
    specs.put(ShipType.SUBMARINE, subCount);

    return specs;
  }

  }
