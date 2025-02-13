package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ArrayNode;

public record ReportDamageJson(
    @JsonProperty("coordinates") ArrayNode coordinates
) {
  public ArrayNode getCoordinates() {
    return coordinates;
  }
}
