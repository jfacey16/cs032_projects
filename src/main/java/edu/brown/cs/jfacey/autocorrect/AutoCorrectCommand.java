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

  private AutoCorrectProject iACProj;

  /**
   * This constructor just sets the value for the current autocorrect project.
   *
   * @param acProj
   *          The autocorrect project stored
   */
  public AutoCorrectCommand(AutoCorrectProject acProj) {
    iACProj = acProj;
  }

  @Override
  public boolean execute(String[] inputs) {
    // ac command check
    if (Objects.equals(inputs[0], "ac")) {

      List<String> words = iACProj.autoCorrectWord(inputs);
      iACProj.printSuggestions(words);
      return true;

    } else {

      return false;
    }
  }
}
