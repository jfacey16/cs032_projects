package edu.brown.cs.jfacey.bacon;

import edu.brown.cs.jfacey.datastructs.PathInfo;
import edu.brown.cs.jfacey.readers.Command;

/**
 * This is a command to run the connect on the two input actors. If successful,
 * the path between the actors, including movies, will be printed. This command
 * just calls connect, which should generate the correct outputs to then be
 * printed.
 *
 * @author jfacey
 *
 */
public class BaconCommand implements Command {

  private BaconProject iBacon;

  /**
   * This sets the current bacon project value.
   *
   * @param bacon
   *          the bacon project value
   */
  public BaconCommand(BaconProject bacon) {
    iBacon = bacon;
  }

  @Override
  public boolean execute(String[] inputs) {
    if (inputs[0].equals("connect")) {
      if (inputs.length != 3) {
        System.out.println("ERROR: input format must be connect "
            + "<name1> <nameN> with names given in quotes");
        return true;
      }
      // get path from connect
      if (inputs[1].length() >= 2 && inputs[2].length() >= 2) {
        if (inputs[1].charAt(0) == '"'
            && inputs[1].charAt(inputs[1].length() - 1) == '"'
            && inputs[2].charAt(0) == '"'
            && inputs[2].charAt(inputs[2].length() - 1) == '"') {
          String nameOne = inputs[1].substring(1, inputs[1].length() - 1);
          String nameTwo = inputs[2].substring(1, inputs[2].length() - 1);
          PathInfo<ActorNode, MovieEdge> path = iBacon.connect(nameOne,
              nameTwo);
          // print full bacon path
          iBacon.printConnections(nameOne, nameTwo, path);
        } else {
          System.out.println("ERROR: names must be given in quotes");
        }
      } else {
        System.out.println("ERROR: names must be given in quotes");
      }

      return true;
    } else {

      return false;
    }
  }
}
