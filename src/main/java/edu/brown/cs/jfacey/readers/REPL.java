package edu.brown.cs.jfacey.readers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * This class defines an implementation of read-evaluate-print loop that can
 * take in an array of possible commands to be given.
 *
 * @author jfacey
 *
 */
public class REPL {

  private List<Command> iCommands;

  /**
   * The constructor for this class that sets the initial command values.
   *
   * @param commands
   *          the commands list for this repl
   */
  public REPL(List<Command> commands) {

    iCommands = commands;
  }

  /**
   * This method defines the actual read-evaluate-print loop for the repl. It
   * reads inputs from the inputs stream and loops through its given commands,
   * attempting to execute them.
   */
  public void execute() {
    // beginning of repl
    try (BufferedReader br = new BufferedReader(new InputStreamReader(
        System.in, "UTF8"))) {
      String input;
      // REPL infinite loop reading inputs
      while ((input = br.readLine()) != null) {
        // parse input
        String[] inputs = input.split(
            "[ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        int read = 0;

        for (int i = 0; i < iCommands.size(); i++) {

          if (iCommands.get(i).execute(inputs)) {
            read = 1;
            break;
          }
        }
        // if no command can be read
        if (read == 0) {

          System.out.println("ERROR: Invalid command given");
        }
      }
    } catch (IOException e) {

      System.out.println("ERROR: " + e.getMessage());
    }
  }
}
