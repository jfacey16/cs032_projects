package edu.brown.cs.jfacey.readers;

import java.util.Objects;

/**
 * This class is an implementation of the command interface that defines a
 * command
 *
 * @author jfacey
 *
 */
public class CorpusCommand implements Command {

  /**
   *
   *
   *
   */
  public CorpusCommand() {

  }

  @Override
  public boolean execute(String[] inputs) {
    // corpus command check
    if (Objects.equals(inputs[0], "corpus")) {
      // make sure there are enough inputs
      if (inputs.length != 2) {
        System.out.println("ERROR: Incorrect input format. "
            + "Format should be corpus <filepath>");
        return true;
      }

      // execute command here
      System.out.println("corpus " + inputs[1] + " added");
      return true;

    } else {

      return false;
    }
  }
}
