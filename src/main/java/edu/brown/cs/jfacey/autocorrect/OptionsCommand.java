package edu.brown.cs.jfacey.autocorrect;

import java.util.Objects;

import org.eclipse.jetty.util.ArrayUtil;

import edu.brown.cs.jfacey.readers.Command;

/**
 * This class is an implementation of the command interface that defines a
 * command to set the options for the autocorrector, as well as query them.
 *
 * @author jfacey
 *
 */
public class OptionsCommand implements Command {

  private AutoCorrectProject iAcData;

  /**
   * This constructor just sets the value for the current autocorrect data.
   *
   * @param acData
   *          The autocorrect data stored
   */
  public OptionsCommand(AutoCorrectProject acData) {
    iAcData = acData;
  }

  @Override
  public boolean execute(String[] inputs) {
    // prefix command check
    if (Objects.equals(inputs[0], "prefix")) {
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
      // query if only input
      if (inputs.length == 1) {
        // print on or off based on current option
        if (iAcData.getPrefix()) {
          System.out.println("prefix on");
        } else {
          System.out.println("prefix off");
        }
      } else if (inputs.length == 2) {
        if (Objects.equals(inputs[1], "on")) {
          iAcData.setPrefix(true);
        } else if (Objects.equals(inputs[1], "off")) {
          iAcData.setPrefix(false);
        } else {
          // if invalid option given, print error
          System.out.println("ERROR: Options for prefix are <on|off>");
        }
      } else {
        // incorrect input length
        System.out.println("ERROR: Incorrect input format. Should "
            + "be prefix <on|off> or prefix");
      }
      return true;
    } else if (Objects.equals(inputs[0], "whitespace")) {
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
      // query if only input
      if (inputs.length == 1) {
        // print on or off based on current option
        if (iAcData.getWhitespace()) {
          System.out.println("whitespace on");
        } else {
          System.out.println("whitespace off");
        }
      } else if (inputs.length == 2) {
        if (Objects.equals(inputs[1], "on")) {
          iAcData.setWhitespace(true);
        } else if (Objects.equals(inputs[1], "off")) {
          iAcData.setWhitespace(false);
        } else {
          // if invalid option given, print error
          System.out.println("ERROR: Options for whitespace are <on|off>");
        }
      } else {
        // incorrect input length
        System.out.println("ERROR: Incorrect input format. Should "
            + "be whitespace <on|off> or whitespace");
      }
      return true;
    } else if (Objects.equals(inputs[0], "smart")) {
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
      // query if only input
      if (inputs.length == 1) {
        // print on or off based on current option
        if (iAcData.getSmart()) {
          System.out.println("smart on");
        } else {
          System.out.println("smart off");
        }
      } else if (inputs.length == 2) {

        if (Objects.equals(inputs[1], "on")) {
          iAcData.setSmart(true);
        } else if (Objects.equals(inputs[1], "off")) {
          iAcData.setSmart(false);
        } else {
          // if invalid option given, print error
          System.out.println("ERROR: Options for smart are <on|off>");
        }
      } else {
        // incorrect input length
        System.out.println("ERROR: Incorrect input format. Should "
            + "be smart <on|off> or smart");
      }
      return true;
    } else if (Objects.equals(inputs[0], "led")) {
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
      // query if only input
      if (inputs.length == 1) {
        System.out.println("led " + iAcData.getLed());
      } else if (inputs.length == 2) {

        int led;

        try {
          // set led value
          led = Integer.parseInt(inputs[1]);
          // check to make sure value is valid
          if (led < 0) {
            System.out.println("ERROR: led value must be greater "
                + "than or equal to zero");
            return true;
          }
          iAcData.setLed(led);
        } catch (NumberFormatException e) {
          // print error if not an integer
          System.out
          .println("ERROR: led option value must be a valid integer");
        }
      } else {
        // incorrect input length
        System.out.println("ERROR: Incorrect input format. Should "
            + "be led <int> or led");
      }
      return true;
    } else {
      return false;
    }
  }
}
