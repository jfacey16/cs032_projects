package edu.brown.cs.jfacey.stars;

import java.util.List;
import java.util.Objects;

import org.eclipse.jetty.util.ArrayUtil;

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
   *          the kdtree value
   */
  public StarsCommand(Kdtree<Star> kdtree) {
    iKd = kdtree;
  }

  @Override
  public boolean execute(String[] inputs) {
    // stars command check
    if (Objects.equals(inputs[0], "stars")) {
      // if trailing whitespace, parse out
      if (inputs[inputs.length - 1].equals("")) {
        String[] temp = new String[inputs.length];
        temp = ArrayUtil.removeFromArray(inputs, inputs.length - 1);
        inputs = new String[inputs.length - 1];
        // copy new array over
        for (int i = 0; i < inputs.length; i++) {
          inputs[i] = temp[i];
        }
      }
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

        StarConverter starConverter = new StarConverter(
            csvParser.parseFile());
        starList = starConverter.convertStars();
      } catch (RuntimeException e) {

        System.out.println("ERROR: " + e.getMessage());
        return true;
      }
      // instantiates kdtree with given star list
      iKd.setRoot(iKd.buildTree(starList, 1));
      System.out.println("Read " + starList.size() + " stars from "
          + inputs[1]);

      return true;

    } else {

      return false;
    }
  }
}
