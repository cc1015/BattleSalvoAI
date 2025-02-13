package cs3500.pa04.model;

public class Coord {
  private int x;
  private int y;
  private CoordinateStatus status;
  private int probability;

  public Coord(int x, int y, CoordinateStatus status, int probability) {
    this.x = x;
    this.y = y;
    this.status = status;
    this.probability = probability;
  }

  public Coord(int x, int y) {
    this.x = x;
    this.y = y;
    this.status = CoordinateStatus.EMPTY;
    this.probability = 50;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public CoordinateStatus getStatus() {
    return status;
  }

  public int getProbability() {
    return this.probability;
  }

  public void setStatus(CoordinateStatus newStatus) {
    this.status = newStatus;
  }

  public void setProbability(int probability) {
    this.probability = probability;
  }

  public void changeProbability(int change) {
    this.probability += change;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Coord)) {
      return false;
    }
    Coord that = (Coord) other;
    return this.status.equals(that.status) &&
        this.x == that.getX() && this.y == that.getY();
  }

  @Override
  public String toString() {
    String coords = this.x + " " + this.y;
    String status = this.status.toString();

    return coords + " " + status + " " + this.probability;
  }
}
