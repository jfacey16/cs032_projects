package edu.brown.cs.jfacey.autocorrect;

import java.util.List;
import java.util.Objects;

import edu.brown.cs.jfacey.readers.Command;

/**
 * This class is an implementation of the command interface that defines a
 * command to run the autocorrector for the input word.
 *
 * @author jfacey
 *
 */
public class AutoCorrectCommand implements Command {

  private AutoCorrectData iAcData;

  /**
   * This constructor just sets the value for the current autocorrect data.
   *
   * @param acData
   *          The autocorrect data stored
   */
  public AutoCorrectCommand(AutoCorrectData acData) {
    iAcData = acData;
  }

  @Override
  public boolean execute(String[] inputs) {
    // ac command check
    if (Objects.equals(inputs[0], "ac")) {
      // if last input is whitespace, just return
      if (inputs[inputs.length - 1].equals("")) {
        return true;
      }
      // instantiate sentence for printing outputs and suggestions list
      String previousSentence = "";
      List<String> words;

      if (inputs.length == 1) {
        // if ac is only input, print error
        System.out.println("ERROR: ac must take argument <input text>");
        return true;

      } else if (inputs.length == 2) {
        // remake full string to print
        String queryPrintString = inputs[0] + " " + inputs[1];
        System.out.println(queryPrintString);
        // reparse on whitespace, numbers, and punctuation
        inputs = queryPrintString.split("[\\p{Punct}\\d\\s]+");
        // must recheck as number of inputs may have changed on new parsing
        if (inputs.length == 2) {
          // if only two inputs, run autocorrect without bigram
          words = iAcData.autoCorrectWord(null, inputs[1].toLowerCase());
        } else {
          // reassemble string for printing suggestions
          for (int i = 1; i < inputs.length - 1; i++) {
            previousSentence.concat(inputs[i].toLowerCase() + " ");
          }
          // run normal autocorrect with bigram
          words = iAcData.autoCorrectWord(
              inputs[inputs.length - 2].toLowerCase(),
              inputs[inputs.length - 1].toLowerCase());
        }

      } else {
        // remake full string to print
        String queryPrintString = inputs[0];

        for (int i = 1; i < inputs.length; i++) {
          queryPrintString = queryPrintString.concat(" " + inputs[i]);
        }
        // print out input
        System.out.println(queryPrintString);
        // reparse on whitespace, numbers, and punctuation
        inputs = queryPrintString.split("[\\p{Punct}\\d\\s]+");
        // reassemble string for printing suggestions
        for (int i = 1; i < inputs.length - 1; i++) {
          previousSentence = previousSentence.concat(inputs[i]
              .toLowerCase() + " ");
        }
        // run normal autocorrect with bigram
        words = iAcData.autoCorrectWord(
            inputs[inputs.length - 2].toLowerCase(),
            inputs[inputs.length - 1].toLowerCase());
      }
      // print out ordered suggestions
      for (int i = 0; i < words.size(); i++) {
        System.out.println(previousSentence + words.get(i));
        if (i == 4) {
          break;
        }
      }

      return true;

    } else {

      return false;
    }
  }
}
