package cs3500.pa04.model;

import java.util.Comparator;

public class CellProbabilityComparator implements Comparator<Coord> {

  @Override
  public int compare(Coord cell1, Coord cell2) {
    int c1Prob = cell1.getProbability();
    int c2Prob = cell2.getProbability();

    if (c1Prob < c2Prob) {
      return 1;
    } else if (c1Prob > c2Prob) {
      return -1;
    } else {
      return 0;
    }
  }
}
