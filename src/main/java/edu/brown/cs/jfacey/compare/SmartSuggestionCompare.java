package edu.brown.cs.jfacey.compare;

import java.util.Comparator;

import edu.brown.cs.jfacey.autocorrect.Suggestion;

/**
 * This class defines a smart suggestion comparator that is used to sort
 * suggestions based on my ranking method.
 *
 * @author jfacey
 *
 */
public class SmartSuggestionCompare implements Comparator<Suggestion> {

  private final String iWord;

  /**
   * This constructor sets the initial word to be compared to.
   *
   * @param word
   *          the autocorrect compare word
   */
  public SmartSuggestionCompare(String word) {
    iWord = word;

  }

  @Override
  public int compare(Suggestion s1, Suggestion s2) {

    String s1Word = s1.getWord();
    String s2Word = s2.getWord();

    // checks for if either suggestions word is equal to the autocorrect word
    if (s1Word.equals(iWord) && s2Word.equals(iWord)) {

      return 0;

    } else if (s1Word.equals(iWord) && !s2Word.equals(iWord)) {

      return -1;

    } else if (!s1Word.equals(iWord) && s2Word.equals(iWord)) {

      return 1;

    } else {
      // if a suggestion is a whitespace, that word should be first
      if (s1.getType().equals("whitespace")
          && !s2.getType().equals("whitespace")) {

        return -1;

      } else if (!s1.getType().equals("whitespace")
          && s2.getType().equals("whitespace")) {

        return 1;
        // if both are, alphabetize
      } else if (s1.getType().equals("whitespace")
          && s2.getType().equals("whitespace")) {

        String[] s1split = s1Word.split("\\s+");
        String[] s2split = s2Word.split("\\s+");

        if (s1split[0].length() < s2split[0].length()) {

          return -1;

        } else {

          return 1;

        }
        // order by distance
      } else {

        if (s1.getDistance() < s2.getDistance()) {

          return -1;

        } else if (s1.getDistance() > s2.getDistance()) {

          return 1;

        } else {
          // if equal distance, prefix comes first.
          if (s1.getType().equals("prefix")
              && !s2.getType().equals("prefix")) {

            return -1;

          } else if (!s1.getType().equals("prefix")
              && s2.getType().equals("prefix")) {

            return 1;

            // if same type, order alphabetically
          } else {

            int alphaOrder = Math.min(s1Word.length(), s2Word.length());

            for (int i = 0; i < alphaOrder; i++) {

              if (s1Word.charAt(i) < s2Word.charAt(i)) {

                return -1;

              } else if (s1Word.charAt(i) > s2Word.charAt(i)) {

                return 1;

              }
            }
            // if we get to end of check for characters based on shorter string,
            // return the shorter string
            if (s1Word.length() < s2Word.length()) {

              return -1;

            } else if (s1Word.length() > s2Word.length()) {

              return 1;

            } else {
              // this means the strings are identical
              return 0;

            }
          }
        }
      }
    }
  }
}
