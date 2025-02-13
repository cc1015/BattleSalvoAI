package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CoordJson(
    @JsonProperty("x") int x,
    @JsonProperty("y") int y){
  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}
