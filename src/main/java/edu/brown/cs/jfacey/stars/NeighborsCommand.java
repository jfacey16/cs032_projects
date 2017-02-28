package edu.brown.cs.jfacey.stars;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jetty.util.ArrayUtil;

import edu.brown.cs.jfacey.datastructs.Kdtree;
import edu.brown.cs.jfacey.readers.Command;

/**
 * This class is an implementation of the command interface that defines a
 * command to do a nearest neighbors search on the given kdtree.
 *
 * @author jfacey
 *
 */
public class NeighborsCommand implements Command {

  private Kdtree<Star> iKd;

  /**
   * This constructor just sets the value of the current kdtree used for the
   * neighbors search.
   *
   * @param kdtree
   *          the kdtree value
   */
  public NeighborsCommand(Kdtree<Star> kdtree) {

    iKd = kdtree;

  }

  @Override
  public boolean execute(String[] inputs) {
    // neighbors position command check
    if (Objects.equals(inputs[0], "neighbors")) {
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
      // check enough inputs for neighbors search (no name)
      if (inputs.length == 5) {
        // error if kdtree has not yet been loaded
        if (iKd.getRoot() == null) {

          System.out.println("ERROR: Kdtree not yet loaded");
          return true;
        }
        // ready parameters for neighbors search
        List<Star> bestNeighbors = new ArrayList<>();

        int numNeighbors;
        double posX;
        double posY;
        double posZ;
        // makes sure input values are numbers
        try {

          numNeighbors = Integer.parseInt(inputs[1]);

          posX = Double.parseDouble(inputs[2]);
          posY = Double.parseDouble(inputs[3]);
          posZ = Double.parseDouble(inputs[4]);

        } catch (NumberFormatException e) {

          System.out.println("ERROR: Number of neighbors must be a valid "
              + "int and X positon, Y position, and Z position "
              + "must all be valid doubles");
          return true;
        }
        // instantiate star
        Star searchStar = new Star(0, "", posX, posY, posZ);
        // call the search
        iKd.neighbors(bestNeighbors, numNeighbors, searchStar, null);
        // prints out the ids of the found points
        for (int i = 0; i < bestNeighbors.size(); i++) {

          System.out.println(bestNeighbors.get(i).getID());
        }
        // check enough inputs for neighbors search (name)
      } else if (inputs.length == 3) {
        // check name in quotes
        if ((inputs[2].charAt(0) == '"')
            && (inputs[2].charAt(inputs[2].length() - 1) == '"')) {

          // error if kdtree has not yet been loaded
          if (iKd.getRoot() == null) {

            System.out.println("ERROR: Kdtree not yet loaded");
            return true;
          }
          // ready parameters for neighbors search
          List<Star> bestNeighbors = new ArrayList<>();

          int numNeighbors;
          // makes sure input value is a number
          try {

            numNeighbors = Integer.parseInt(inputs[1]);

          } catch (NumberFormatException e) {

            System.out.println("ERROR: Number of neighbors"
                + " must be a valid integer");
            return true;
          }

          String name = inputs[2].substring(1, inputs[2].length() - 1);
          // call the search
          iKd.neighbors(bestNeighbors, numNeighbors, null, name);
          // prints out the ids of the found points
          for (int i = 0; i < bestNeighbors.size(); i++) {

            System.out.println(bestNeighbors.get(i).getID());
          }
        } else {

          System.out.println("ERROR: Incorrect input format. "
              + "Name of star must be given in quotes");
        }
      } else {

        System.out.println("ERROR: Incorrect input "
            + "format. Format should be neighbors <number of neighbors "
            + "to find> <x coordinate> <y coordinate> <z coordinate> "
            + "or format should be neighbors "
            + "<number of neighbors to find> <name of star>");
      }

      return true;

    } else {

      return false;
    }
  }
}
