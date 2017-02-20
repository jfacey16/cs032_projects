package edu.brown.cs.jfacey.stars;

import java.util.List;
import java.util.Objects;

import edu.brown.cs.jfacey.datastructs.Kdtree;
import edu.brown.cs.jfacey.readers.CSVParser;
import edu.brown.cs.jfacey.readers.Command;

/**
 * This class is an implementation of the command interface that defines a
 * command to load a list of stars into the kdtree.
 *
 * @author jfacey
 *
 */
public class StarsCommand implements Command {

  private Kdtree<Star> iKd;

  /**
   * This constructor just sets the value of the current kdtree.
   *
   * @param kdtree
   */
  public StarsCommand(Kdtree<Star> kdtree) {
    iKd = kdtree;
  }

  @Override
  public boolean execute(String[] inputs) {
    // stars command check
    if (Objects.equals(inputs[0], "stars")) {
      // make sure there are enough inputs
      if (inputs.length != 2) {
        System.out.println("ERROR: Incorrect input format. "
            + "Format should be stars <filepath>");
        return true;
      }
      // instantiate csvparser which parses the list of stars
      // to add to the tree
      CSVParser csvParser = new CSVParser(inputs[1]);

      List<Star> starList;
      // prints error message if csvfile can't be parsed
      try {

        StarConverter starConverter = new StarConverter(csvParser.parseFile());
        starList = starConverter.convertStars();
      } catch (RuntimeException e) {

        System.out.println("ERROR: " + e.getMessage());
        return true;
      }
      // instantiates kdtree with given star list
      iKd.setRoot(iKd.buildTree(starList, 1));
      System.out
          .println("Read " + starList.size() + " stars from " + inputs[1]);

      return true;

    } else {

      return false;
    }
  }
}
