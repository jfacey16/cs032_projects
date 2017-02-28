package edu.brown.cs.jfacey.autocorrect;

import java.util.Objects;

import org.eclipse.jetty.util.ArrayUtil;

import edu.brown.cs.jfacey.readers.Command;

/**
 * This class is an implementation of the command interface that defines a
 * command to load a corpus file.
 *
 * @author jfacey
 *
 */
public class CorpusCommand implements Command {

  private AutoCorrectData iAcData;

  /**
   * This constructor just sets the value for the current autocorrect data.
   *
   * @param acData
   *          The autocorrect data stored
   */
  public CorpusCommand(AutoCorrectData acData) {
    iAcData = acData;
  }

  @Override
  public boolean execute(String[] inputs) {
    // corpus command check
    if (Objects.equals(inputs[0], "corpus")) {
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
            + "Format should be corpus <filepath>");
        return true;
      }
      // execute command here, print out error if invalid file
      try {
        iAcData.buildTrie(inputs[1]);
      } catch (RuntimeException e) {
        System.out.println("ERROR: invalid file path");
        return true;
      }
      // print out if corpus successfully added
      System.out.println("corpus " + inputs[1] + " added");

      return true;

    } else {

      return false;
    }
  }
}
