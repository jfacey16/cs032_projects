package edu.brown.cs.jfacey.compare;

import java.util.Comparator;

import edu.brown.cs.jfacey.autocorrect.Suggestion;
import edu.brown.cs.jfacey.datastructs.BigramPair;
import edu.brown.cs.jfacey.datastructs.Trie;

/**
 * This class defines a smart suggestion comparator that is used to sort
 * suggestions based on the given ranking method.
 *
 * @author jfacey
 *
 */
public class SuggestionCompare implements Comparator<Suggestion> {

  private final String iPrevious;
  private final String iWord;
  private final Trie iTrie;

  /**
   * This constructor sets the previous word, autocorrect word, and trie values.
   *
   * @param previous
   *          the previous word in the input string
   * @param word
   *          the autocorrect word in the input string
   * @param trie
   *          the trie
   */
  public SuggestionCompare(String previous, String word, Trie trie) {
    iPrevious = previous;
    iWord = word;
    iTrie = trie;

  }

  @Override
  public int compare(Suggestion s1, Suggestion s2) {

    String s1Word = s1.getWord();
    String s2Word = s2.getWord();
    // if a suggestion is a whitespace suggestion, we want to use the first word
    // in it
    if (s1.getType().equals("whitespace")) {
      String[] split1 = s1Word.split("\\s+");
      s1Word = split1[0];
    }

    if (s2.getType().equals("whitespace")) {
      String[] split2 = s2Word.split("\\s+");
      s2Word = split2[0];
    }
    // checks for if either suggestions word is equal to the autocorrect word
    if (s1Word.equals(iWord) && s2Word.equals(iWord)) {

      return 0;

    } else if (s1Word.equals(iWord) && !s2Word.equals(iWord)) {

      return -1;

    } else if (!s1Word.equals(iWord) && s2Word.equals(iWord)) {

      return 1;

    } else {

      BigramPair s1Bigram;
      BigramPair s2Bigram;
      int s1BigramVal;
      int s2BigramVal;
      // if no previous word
      if (iPrevious == null) {
        s1BigramVal = 0;
        s2BigramVal = 0;

      } else {
        s1Bigram = new BigramPair(iPrevious, s1Word);
        s2Bigram = new BigramPair(iPrevious, s2Word);
        // bigram check
        if (iTrie.getBigram().get(s1Bigram) == null) {
          s1BigramVal = 0;
        } else {
          s1BigramVal = iTrie.getBigram().get(s1Bigram);
        }

        if (iTrie.getBigram().get(s2Bigram) == null) {
          s2BigramVal = 0;
        } else {
          s2BigramVal = iTrie.getBigram().get(s2Bigram);
        }
      }

      if (s1BigramVal > s2BigramVal) {

        return -1;

      } else if (s1BigramVal < s2BigramVal) {

        return 1;

      } else {
        // unigram check
        int s1UnigramVal;
        int s2UnigramVal;

        if (iTrie.getUnigram().get(s1Word) == null) {
          s1UnigramVal = 0;
        } else {
          s1UnigramVal = iTrie.getUnigram().get(s1Word);
        }

        if (iTrie.getUnigram().get(s2Word) == null) {
          s2UnigramVal = 0;
        } else {
          s2UnigramVal = iTrie.getUnigram().get(s2Word);
        }

        if (s1UnigramVal > s2UnigramVal) {

          return -1;

        } else if (s1UnigramVal < s2UnigramVal) {

          return 1;

        } else {
          // check alphabetical ordering
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
