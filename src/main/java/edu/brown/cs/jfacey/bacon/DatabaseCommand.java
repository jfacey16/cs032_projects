package edu.brown.cs.jfacey.bacon;

import edu.brown.cs.jfacey.readers.Command;

/**
 * This class implements the command interface. It is a command for loading in a
 * database to be used in this bacon project.
 *
 * @author jfacey
 *
 */
public class DatabaseCommand implements Command {

  private BaconProject iBacon;

  /**
   * The setter to set the current bacon project.
   *
   * @param bacon
   *          the bacon project
   */
  public DatabaseCommand(BaconProject bacon) {
    iBacon = bacon;
  }

  @Override
  public boolean execute(String[] inputs) {
    if (inputs[0].equals("mdb")) {
      if (inputs.length != 2) {
        System.out.println("ERROR: input format must be mdb <sql_db>");
        return true;
      }
      // sets the database
      iBacon.setDatabase(inputs[1]);
      return true;
    } else {
      return false;
    }
  }
}
