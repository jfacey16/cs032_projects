package edu.brown.cs.jfacey.datastructs;

/**
 * This is the class that defines a node for the trie data structure. It keeps
 * track of its parent, as well as up to 26 children associated with the letters
 * that could come after the node. It also stores whether this is the last
 * letter in a word, and the character value associated with the node itself.
 *
 * @author jfacey
 *
 */
public class TrieNode {

  private final TrieNode iParent;
  private final TrieNode[] iChildren;
  private boolean iIsWord;
  private final char iLetter;
  static final int CHILD_SIZE = 26;

  /**
   * This is a constructor that is for a blank node for no input character.
   * Useful for making the root node.
   */
  public TrieNode() {
    iParent = null;
    iChildren = new TrieNode[CHILD_SIZE];
    iIsWord = false;
    iLetter = '\0';
  }

  /**
   * This is a constructor for all nodes who are not the root, as they should
   * have a character associated with them.
   *
   * @param parent
   *          the parent node of the node
   * @param letter
   *          the character associated with the node
   */
  public TrieNode(TrieNode parent, char letter) {
    iParent = parent;
    iChildren = new TrieNode[CHILD_SIZE];
    iIsWord = false;
    iLetter = letter;
  }

  /**
   * This returns the parent value of the node.
   *
   * @return the parent node
   */
  public TrieNode getParent() {
    return iParent;

  }

  /**
   * This returns an array of all children of this node.
   *
   * @return the children of the node
   */
  public TrieNode[] getChildren() {
    return iChildren;

  }

  /**
   * This method returns the boolean associated with whether a word ends here or
   * not.
   *
   * @return the boolean value
   */
  public boolean getIsWord() {
    return iIsWord;

  }

  /**
   * This is a setter method to set whether a word ends here or not.
   *
   * @param isWord
   *          the boolean value of whether it is the end of a word or not
   */
  public void setIsWord(boolean isWord) {

    iIsWord = isWord;
  }

  /**
   * This method returns the letter associated with this node.
   *
   * @return the character value
   */
  public char getCharacter() {
    return iLetter;
  }

  /**
   * This method returns the word associated with this node, whether the node
   * has a word associated with it or not. It should only be used on nodes with
   * words associated with them, but must work on all nodes, as it is called
   * recursively to work correctly.
   *
   * @return the word associated with this node
   */
  public String getWord() {
    // checks if no parent and returns a blank string if so
    if (iParent == null) {
      return "";
      // if parent is not null, we are not at root yet, so return string +
      // current letter
    } else {
      return iParent.getWord() + Character.toString(iLetter);
    }
  }

  /**
   * This method adds a word to the current node, and is called recursively to
   * fully add a word to the trie.
   *
   * @param word
   *          the word to add to the trie
   *
   */
  public void addWord(String word) {
    if (word.length() == 0) {
      return;
    }
    // find index to insert character at
    int charValue = word.charAt(0) - 'a';
    // if this node doesn't have that child yet, add it
    if (iChildren[charValue] == null) {

      iChildren[charValue] = new TrieNode(this, word.charAt(0));

    }
    // if this isnt the last character, call recursively on substring
    if (word.length() > 1) {

      iChildren[charValue].addWord(word.substring(1));

    } else {
      // if this is the last character, set this child node to be a word
      // end.
      iChildren[charValue].setIsWord(true);

    }
  }

  /**
   * This method returns a boolean based on whether the input word is actually a
   * word or not.
   *
   * @param word
   *          the input word
   * @return boolean on whether the input is a word or not
   */
  public boolean isWord(String word) {
    if (word.length() == 0) {
      return false;
    }
    // check at first child value
    int charValue = word.charAt(0) - 'a';
    TrieNode curNode = iChildren[charValue];
    // loop through children til you find the end node or children don't
    // exist
    while (curNode != null) {
      // if you are at the last node, check if it is a valid word
      if (word.length() == 1) {

        return curNode.getIsWord();

      }
      // if not at last node yet, set curNode to be next child in the
      // string
      word = word.substring(1);
      charValue = word.charAt(0) - 'a';
      curNode = curNode.getChildren()[charValue];
    }
    return false;
  }
}
