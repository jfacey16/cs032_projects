package edu.brown.cs.jfacey.stars;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.brown.cs.jfacey.datastructs.Kdtree;
import edu.brown.cs.jfacey.readers.Command;

/**
 * This class is an implementation of the command interface that defines a
 * command to do a radius search on the given kdtree.
 *
 * @author jfacey
 *
 */
public class RadiusCommand implements Command {

  private Kdtree<Star> iKd;

  /**
   * This constructor just sets the value of the current kdtree used for the
   * radius search.
   *
   * @param kdtree
   */
  public RadiusCommand(Kdtree<Star> kdtree) {

    iKd = kdtree;

  }

  @Override
  public boolean execute(String[] inputs) {
    // radius position command check
    if (Objects.equals(inputs[0], "radius")) {
      if (inputs.length == 5) {
        // error if kdtree has not yet been loaded
        if (iKd.getRoot() == null) {

          System.out.println("ERROR: Kdtree not yet loaded");
          return true;
        }
        // make sure there are enough inputs
        if (inputs.length != 5) {
          System.out.println("ERROR: Incorrect "
              + "input format. Format should be radius <radius value> "
              + "<x coordinate> <y coordinate> <z coordinate>");
          return true;
        }
        // ready parameters for radius search
        List<Star> bestRadius = new ArrayList<>();

        double radius;
        double posX;
        double posY;
        double posZ;
        // makes sure input values are numbers
        try {
          radius = Double.parseDouble(inputs[1]);

          posX = Double.parseDouble(inputs[2]);
          posY = Double.parseDouble(inputs[3]);
          posZ = Double.parseDouble(inputs[4]);

        } catch (NumberFormatException e) {

          System.out.println("ERROR: Radius, X positon, Y position, "
              + "and Z position must all be valid doubles");
          return true;
        }
        // instantiate star
        Star searchStar = new Star(0, "", posX, posY, posZ);
        // call the search
        iKd.radius(bestRadius, radius, searchStar, null);
        // prints out the ids of the found points
        for (int i = 0; i < bestRadius.size(); i++) {

          System.out.println(bestRadius.get(i).getID());
        }
      } else if (inputs.length == 3) {
        // check name in quotes
        if ((inputs[2].charAt(0) == '"')
            && (inputs[2].charAt(inputs[2].length() - 1) == '"')) {

          // error if kdtree has not yet been loaded
          if (iKd.getRoot() == null) {

            System.out.println("ERROR: Kdtree not yet loaded");
            return true;
          }
          // ready parameters for radius search
          List<Star> bestRadius = new ArrayList<>();

          double radius;
          // makes sure input value is a number
          try {

            radius = Double.parseDouble(inputs[1]);

          } catch (NumberFormatException e) {

            System.out.println("ERROR: Radius must be a valid double");
            return true;
          }

          String name = inputs[2].substring(1, inputs[2].length() - 1);
          // call the search
          iKd.radius(bestRadius, radius, null, name);
          // prints out the ids of the found points
          for (int i = 0; i < bestRadius.size(); i++) {

            System.out.println(bestRadius.get(i).getID());
          }
        } else {

          System.out.println("ERROR: Incorrect input format. "
              + "Name of star must be given in quotes");

        }
      } else {

        System.out.println("ERROR: Incorrect input format. "
            + "Format should be radius <radius value> "
            + "<x coordinate> <y coordinate> <z coordinate> or "
            + "format should be radius <radius value "
            + "to find> <name of star>");
      }

      return true;

    } else {

      return false;

    }
  }
}
