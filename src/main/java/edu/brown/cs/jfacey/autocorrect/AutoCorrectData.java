package edu.brown.cs.jfacey.autocorrect;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.jfacey.compare.SmartSuggestionCompare;
import edu.brown.cs.jfacey.compare.SuggestionCompare;
import edu.brown.cs.jfacey.datastructs.Trie;
import edu.brown.cs.jfacey.readers.CorpusParser;

/**
 * This class stores essential data for the autocorrector as well as sets up
 * methods to modify the data. In this way, the Trie is only modified internally
 * in this class, and is not returned. The primitive values of the booleans are
 * able to be got and modified, but this is much easier to design for the
 * commands to work correctly, and it is ok that this data is being modified.
 *
 * @author jfacey
 *
 */
public class AutoCorrectData {

  private boolean iPrefix;
  private boolean iWhitespace;
  private boolean iSmart;
  private int iLed;
  private final Trie iTrie;

  /**
   * The constructor just sets the initial value of all variables based on the
   * passed in parameters.
   *
   * @param prefix
   *          the prefix value
   * @param whitespace
   *          the whitespace value
   * @param smart
   *          the smart value
   * @param led
   *          the led value
   * @param trie
   *          the trie value
   */
  public AutoCorrectData(boolean prefix, boolean whitespace,
      boolean smart, int led, Trie trie) {
    iPrefix = prefix;
    iWhitespace = whitespace;
    iSmart = smart;
    iLed = led;
    iTrie = trie;
  }

  /**
   * Getter for prefix value.
   *
   * @return the prefix value
   */
  public boolean getPrefix() {
    return iPrefix;
  }

  /**
   * Setter for prefix value.
   *
   * @param prefix
   *          new prefix value
   */
  public void setPrefix(boolean prefix) {
    iPrefix = prefix;
  }

  /**
   * Getter for whitespace value.
   *
   * @return the whitespace value
   */
  public boolean getWhitespace() {
    return iWhitespace;
  }

  /**
   * Setter for whitespace value.
   *
   * @param whitespace
   *          new whitespace value
   */
  public void setWhitespace(boolean whitespace) {
    iWhitespace = whitespace;
  }

  /**
   * Getter for smart value.
   *
   * @return the smart value
   */
  public boolean getSmart() {
    return iSmart;
  }

  /**
   * Setter for smart value.
   *
   * @param smart
   *          new smart value
   */
  public void setSmart(boolean smart) {
    iSmart = smart;
  }

  /**
   * Getter for led value.
   *
   * @return the led value
   */
  public int getLed() {
    return iLed;
  }

  /**
   * Setter for led value.
   *
   * @param led
   *          new led value
   */
  public void setLed(int led) {
    iLed = led;
  }

  /**
   * This method builds a trie given an input corpus file.
   *
   * @param file
   *          the filepath to the corpus file
   */
  public void buildTrie(String file) {
    // load file into corpus parser
    CorpusParser corpusParser;
    // if invalid file throw runtime exception to be caught in the actual
    // command
    try {
      corpusParser = new CorpusParser(file);
    } catch (RuntimeException e) {
      throw new RuntimeException();
    }

    String[] readLine;
    // loop through file reading and parsing lines
    while ((readLine = corpusParser.parseLine()) != null) {
      // for each parsed line, add it to the trie
      for (int i = 0; i < readLine.length; i++) {
        // insert word into trie
        iTrie.insert(readLine[i]);
      }
    }
  }

  /**
   * This method runs the autocorrector on the input string given, correcting
   * the last word.
   *
   * @param previous
   *          the previous input word if it exists
   * @param word
   *          the input string to autocorrect
   * @return the list of autocorrected word options
   */
  public List<String> autoCorrectWord(String previous, String word) {
    List<Suggestion> suggestions = new ArrayList<>();
    // if an option is on, autocorrect for that option
    if (iPrefix) {

      suggestions.addAll(iTrie.getPrefixSuggestions(word));
    }

    if (iLed >= 0) {

      suggestions.addAll(iTrie.getLedSuggestions(word, iLed));
    }

    if (iWhitespace) {
      suggestions.addAll(iTrie.getWhiteSpaceSuggestions(word));
    }

    List<String> words = new ArrayList<>();

    // if no suggestions, just return
    if (suggestions.size() == 0) {
      return words;
    }
    // rank suggestion by my ranking or normal ranking
    if (iSmart) {
      SmartSuggestionCompare smartCompare = new SmartSuggestionCompare(
          word);

      suggestions.sort(smartCompare);

    } else {
      SuggestionCompare sugCompare = new SuggestionCompare(previous, word,
          iTrie);

      suggestions.sort(sugCompare);
    }
    // add suggested words to words list and remove duplicates
    words.add(suggestions.get(0).getWord());
    // return right away if one word
    if (suggestions.size() == 1) {
      return words;
    }
    for (int i = 1; i < suggestions.size(); i++) {
      int add = 0;
      // if word is already in list, don't add
      for (int j = 0; j < words.size(); j++) {

        if (words.get(j).equals(suggestions.get(i).getWord())) {
          add = 1;
          break;
        }
      }
      // after checking word isnt in list already, add
      if (add == 0) {
        words.add(suggestions.get(i).getWord());
      }
    }

    return words;
  }
}
