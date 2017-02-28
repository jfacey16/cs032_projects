package edu.brown.cs.jfacey.datastructs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.brown.cs.jfacey.autocorrect.Suggestion;

/**
 * This class defines the implementation of a trie data structure.
 *
 * @author jfacey
 *
 */
public class Trie {

  private TrieNode iRoot;
  private String iPrevious;
  private HashMap<BigramPair, Integer> iBigram;
  private HashMap<String, Integer> iUnigram;

  /**
   * This is the constructor for a trie, which just initially sets the root
   * node.
   */
  public Trie() {

    iRoot = new TrieNode();
    iPrevious = null;
    iBigram = new HashMap<>();
    iUnigram = new HashMap<>();
  }

  /**
   * This is a getter method for a root node.
   *
   * @return root node value
   */
  public TrieNode getRoot() {
    return iRoot;
  }

  /**
   * This is a getter method for the bigram hash.
   *
   * @return the bigram hash
   */
  public HashMap<BigramPair, Integer> getBigram() {
    return iBigram;
  }

  /**
   * This is a getter method for the unigram hash.
   *
   * @return the unigram hash
   */
  public HashMap<String, Integer> getUnigram() {
    return iUnigram;
  }

  /**
   * This method is used to insert a word into the trie.
   *
   * @param word
   *          the word to insert into the trie
   */
  public void insert(String word) {
    // if not first word
    if (iPrevious != null) {
      // instantiate new bigram pair
      BigramPair bigram = new BigramPair(iPrevious, word);
      // if the bigram and unigram exist for this word with the previous,
      // increment values
      if (iUnigram.containsKey(word) && iBigram.containsKey(bigram)) {

        Integer intu = new Integer(iUnigram.get(word).intValue() + 1);
        iUnigram.put(word, intu);

        Integer intb = new Integer(iBigram.get(bigram).intValue() + 1);
        iBigram.put(bigram, intb);
        iPrevious = word;
        return;
        // if only word exists, add unigram
      } else if (iUnigram.containsKey(word)) {

        Integer intu = new Integer(iUnigram.get(word).intValue() + 1);
        iUnigram.put(word, intu);

        Integer one = new Integer(1);
        iBigram.put(bigram, one);

        iPrevious = word;
        return;
      }
      // if word not in trie yet, add to bigram and unigram and add to
      // trie

      Integer one = new Integer(1);
      iBigram.put(bigram, one);
    }

    Integer oneT = new Integer(1);
    iUnigram.put(word, oneT);
    // actually add word to trie
    iPrevious = word;
    iRoot.addWord(word);
  }

  /**
   * This method is used to return whether a word is in the trie or not.
   *
   * @param word
   *          the word to look for
   * @return boolean on whether the word is in the trie or not
   */
  public boolean isWord(String word) {
    return iRoot.isWord(word);

  }

  /**
   * This method returns a list of all words currently in the trie.
   *
   * @return list of words in the trie
   */
  public List<String> getWords() {
    List<String> words = new ArrayList<>();
    return this.getWordsHelper(iRoot, words);
  }

  /**
   * This method iterates through the tree using a pre-order depth first search
   * to get every word in the trie. It is called recursively.
   *
   * @param curNode
   *          the cur node in the trie
   * @param words
   *          the list of words to return
   * @return the list of words in the trie
   */
  public List<String> getWordsHelper(TrieNode curNode, List<String> words) {
    // if the node has a word associated with it, add it
    if (curNode.getIsWord()) {
      words.add(curNode.getWord());
    }
    // call method recursively on all children that exist
    for (int i = 0; i < curNode.getChildren().length; i++) {

      if (curNode.getChildren()[i] != null) {
        this.getWordsHelper(curNode.getChildren()[i], words);
      }
    }

    return words;
  }

  /**
   * This method returns all suggestions for prefix matching. It calls a method
   * that finds the end of the prefix, which will then call a method to get all
   * words under the prefix.
   *
   * @param inputWord
   *          the input word to autocorrect
   * @return the list of possible suggestions
   */
  public List<Suggestion> getPrefixSuggestions(String inputWord) {
    List<Suggestion> suggestions = new ArrayList<>();

    return this.findEndPrefix(inputWord, suggestions);
  }

  /**
   * This method is very similar to the isWord method in the trie node class. It
   * will find the end of the input prefix and then call the method to find all
   * words in the subtree from the end of this prefix. This will end up
   * returning a list of possible suggestions for the input word. If the prefix
   * does not exist in the trie, null is returned.
   *
   * @param inputWord
   *          the input word to autocorrect
   * @param suggestions
   *          the list to fill in with suggestions
   * @return the suggestions list
   */
  public List<Suggestion> findEndPrefix(String inputWord,
      List<Suggestion> suggestions) {
    // instantiate list for suggestions
    List<Suggestion> words = new ArrayList<>();
    // check first child value
    int charValue = inputWord.charAt(0) - 'a';
    TrieNode curNode = iRoot.getChildren()[charValue];
    // keep iterating through children for the string
    while (curNode != null) {
      // when end of prefix is found, call helper method on subtree
      if (inputWord.length() == 1) {
        return this.getSuggestionsSubtree(curNode, words, 0);

      }
      // if not at last node yet, set curNode to be next child in the
      // string
      inputWord = inputWord.substring(1);
      charValue = inputWord.charAt(0) - 'a';
      curNode = curNode.getChildren()[charValue];
    }
    // if prefix is not found return empty list
    return words;
  }

  /**
   * This method is called once the prefix helper finds the end of the prefix.
   * It is very similar to calling the getWords method on the sublist, but is
   * different because it loads the words directly into a suggestion list as
   * suggestions and keeps track of distance from the prefix, which is necessary
   * for my smart rank.
   *
   * @param curNode
   *          the current node
   * @param suggestions
   *          the list of suggestions to add to
   * @param depth
   *          current depth in the subtree
   * @return the list of suggestions for the prefix search
   */
  public List<Suggestion> getSuggestionsSubtree(TrieNode curNode,
      List<Suggestion> suggestions, int depth) {
    // checks if there is a word at this node
    if (curNode.getIsWord()) {
      Suggestion suggestion = new Suggestion(depth, "prefix",
          curNode.getWord());
      suggestions.add(suggestion);
    }
    // call recursively on all children, if not null
    for (int i = 0; i < curNode.getChildren().length; i++) {

      if (curNode.getChildren()[i] != null) {
        this.getSuggestionsSubtree(curNode.getChildren()[i], suggestions,
            depth + 1);
      }
    }
    // returns the list
    return suggestions;
  }

  /**
   * This method returns all suggestions using the led algorithm on the input
   * word.
   *
   * @param inputWord
   *          the input word to autocorrect
   * @param led
   *          the led int value to use
   * @return the list of possible suggestions
   */
  public List<Suggestion> getLedSuggestions(String inputWord, int led) {
    // instantiate suggestions list and get all words in trie
    List<String> words = this.getWords();
    List<Suggestion> suggestions = new ArrayList<>();
    // extra check if led is too low
    if (led < 0) {
      return suggestions;
    }
    // for all words in trie, calculate led on given word
    // if low enough add to suggestions
    Suggestion suggestion;
    int foundLed;

    for (int i = 0; i < words.size(); i++) {

      foundLed = this.calculateLed(inputWord, words.get(i));

      if (foundLed <= led) {
        suggestion = new Suggestion(foundLed, "led", words.get(i));
        suggestions.add(suggestion);
      }
    }

    return suggestions;
  }

  /**
   * This is a helper method that calculates the led between two words. It is
   * used to compare the input word with a word in the trie. It is constantly
   * updated, so there is only a need to make two
   *
   * @param autoCorrectWord
   *          the input autocorrect word
   * @param compareWord
   *          the word from the trie to compare to
   * @return the led between the two words
   */
  public int calculateLed(String autoCorrectWord, String compareWord) {
    // check if words are equal
    if (autoCorrectWord.equals(compareWord)) {
      return 0;
    }
    // unnecessary check for the way it is used, but still correct
    // implementation of the algorithm
    if (autoCorrectWord.length() == 0) {
      return compareWord.length();
    }

    if (compareWord.length() == 0) {
      return autoCorrectWord.length();
    }
    // initialize the vectors for the matrix on either side
    int[] v0 = new int[compareWord.length() + 1];
    int[] v1 = new int[compareWord.length() + 1];

    // set the initial edit distances just based on string size for each point
    for (int i = 0; i < v0.length; i++) {
      v0[i] = i;
    }

    for (int i = 0; i < autoCorrectWord.length(); i++) {

      v1[0] = i + 1;

      for (int j = 0; j < compareWord.length(); j++) {

        int cost = (autoCorrectWord.charAt(i) == compareWord.charAt(j)) ? 0
            : 1;
        int minm = Math.min(v1[j] + 1, v0[j + 1] + 1);
        v1[j + 1] = Math.min(minm, v0[j] + cost);
      }

      for (int j = 0; j < v0.length; j++) {

        v0[j] = v1[j];
      }
    }
    return v1[compareWord.length()];
  }

  /**
   * This method returns all suggestions using whitespace splitting on the input
   * word.
   *
   * @param inputWord
   *          the input word to autocorrect
   * @return the list of possible suggestions
   */
  public List<Suggestion> getWhiteSpaceSuggestions(String inputWord) {

    List<Suggestion> suggestions = new ArrayList<>();
    // for every possible pair of substrings, check if both are valid words.
    // If so, add to suggestions list
    for (int i = 1; i < inputWord.length(); i++) {

      if (this.isWord(inputWord.substring(0, i))
          && this.isWord(inputWord.substring(i, inputWord.length()))) {

        Suggestion suggestion = new Suggestion(0, "whitespace",
            inputWord.substring(0, i) + " "
                + inputWord.substring(i, inputWord.length()));

        suggestions.add(suggestion);
      }
    }

    return suggestions;
  }
}
