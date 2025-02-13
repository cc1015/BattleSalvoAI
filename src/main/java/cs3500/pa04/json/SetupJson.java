package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.ShipType;
import java.util.Map;

public record SetupJson(
  @JsonProperty("width") int width,
  @JsonProperty("height") int height,
  @JsonProperty("fleet-spec") FleetSpecJson fleetSpec) {

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Map<ShipType, Integer> getFleetSpec() {
    return fleetSpec.makeMap();
  }
}
