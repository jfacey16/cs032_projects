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
public class AutoCorrectProject {

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
   */
  public AutoCorrectProject(boolean prefix, boolean whitespace,
      boolean smart, int led) {
    iPrefix = prefix;
    iWhitespace = whitespace;
    iSmart = smart;
    iLed = led;
    iTrie = new Trie();
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
   * Getter for the trie value.
   *
   * @return the trie value
   */
  public Trie getTrie() {
    return iTrie;
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
   * the last word. It stores the previous sentence fully corrected in the first
   * input. This method essentially parses the inputs correctly and then calls
   * two helpers, one of which gets suggestions from the input word, and the
   * other which sorts and gets the suggestions ready as a list of ordered
   * words.
   *
   * @param inputs
   *          the list of input words, including ac
   * @return the list of autocorrected word options
   */
  public List<String> autoCorrectWord(String[] inputs) {
    List<String> words = new ArrayList<>();
    // if last input is whitespace, just return
    if (inputs[inputs.length - 1].equals("")) {
      return words;
    }
    // instantiate sentence for printing outputs and suggestions list
    String previousSentence = "";
    // instantiate for previous word and ac word
    String previous;
    String word;

    if (inputs.length == 1) {
      // if ac is only input, print error
      System.out.println("ERROR: ac must take argument <input text>");
      return words;

    } else if (inputs.length == 2) {
      // remake full string to print
      String queryPrintString = inputs[0] + " " + inputs[1];
      System.out.println(queryPrintString);
      // reparse on whitespace, numbers, and punctuation
      inputs = queryPrintString.split("[\\p{Punct}\\d\\s]+");
      // must recheck as number of inputs may have changed on new parsing
      if (inputs.length == 2) {
        // if only two inputs, run autocorrect without bigram
        previous = null;
        word = inputs[1].toLowerCase();
      } else {
        // reassemble string for printing suggestions
        for (int i = 1; i < inputs.length - 1; i++) {
          previousSentence.concat(inputs[i].toLowerCase() + " ");
        }
        // run normal autocorrect with bigram
        previous = inputs[inputs.length - 2].toLowerCase();
        word = inputs[inputs.length - 1].toLowerCase();
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
        previousSentence = previousSentence.concat(inputs[i].toLowerCase()
            + " ");
      }
      // run normal autocorrect with bigram
      previous = inputs[inputs.length - 2].toLowerCase();
      word = inputs[inputs.length - 1].toLowerCase();
    }
    // get suggestions list
    List<Suggestion> suggestions = getSuggestions(word);
    // sort suggestions
    return sortSuggestions(suggestions, previous, word, previousSentence);
  }

  /**
   * This method returns a list of autocorrect suggestions based on an input
   * word. It also checks to see what the options are set to, to give
   * suggestions based on those options.
   *
   * @param word
   *          the input word to ac on
   * @return the list of ac suggestions
   */
  public List<Suggestion> getSuggestions(String word) {
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
    return suggestions;
  }

  /**
   * This method sorts suggestions and returns the full list of suggestions as
   * an ordered list of strings. They are sorted based on which ranking method
   * was turned on.
   *
   * @param suggestions
   *          the suggestions to sort
   * @param previous
   *          the previous word to the autocorrect word
   * @param word
   *          the word autocorrected on
   * @param previousSentence
   *          the full previous sentence to the ac word
   * @return the ordered list of suggested autocorrected strings
   */
  public List<String> sortSuggestions(List<Suggestion> suggestions,
      String previous, String word, String previousSentence) {

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
    // add initally previous sentence
    words.add(previousSentence);
    // add suggested words to words list and remove duplicates
    for (int i = 0; i < suggestions.size(); i++) {
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

  /**
   * This method is used to print out the suggestions, as only the top 5 should
   * be printed out. The first index of words should store the previous
   * sentence.
   *
   * @param words
   *          the list of words to print
   */
  public void printSuggestions(List<String> words) {
    // if no suggestions given, as size one means just previous sentence.
    // size 1 should never happen, but it is safe to check
    if (words.size() == 0 || words.size() == 1) {
      return;
    }
    // print out ordered suggestions
    for (int i = 1; i < words.size(); i++) {
      System.out.println(words.get(0) + words.get(i));
      if (i == 5) {
        break;
      }
    }
  }
}
