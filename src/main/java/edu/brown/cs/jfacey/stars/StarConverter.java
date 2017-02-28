package edu.brown.cs.jfacey.stars;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines the star converter class that is used to convert a list of
 * parsed string inputs from a csvparser into a list of stars.
 *
 * @author jfacey
 *
 */
public class StarConverter {

  private List<String[]> iCSVLines;

  /**
   * This constructor sets the input list of read lines from the csvparser.
   *
   * @param csvLines
   *          csv input lines
   */
  public StarConverter(List<String[]> csvLines) {

    iCSVLines = csvLines;

  }

  /**
   * This method is used to convert the list of read lines from the csvparser
   * into a list of stars.
   *
   * @return list of stars converted from csv input lines
   */
  public List<Star> convertStars() {

    List<Star> starList = new ArrayList<>();

    for (int i = 0; i < iCSVLines.size(); i++) {
      String[] input = iCSVLines.get(i);
      // load input into new star
      Star curStar = new Star(Integer.parseInt(input[0]), input[1],
          Double.parseDouble(input[2]), Double.parseDouble(input[3]),
          Double.parseDouble(input[4]));
      // load star into stars arraylist
      starList.add(curStar);
    }

    return starList;
  }
}
