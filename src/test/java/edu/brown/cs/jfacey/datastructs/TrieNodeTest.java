package edu.brown.cs.jfacey.datastructs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * This class tests the trienode structure, which is the class inserted into
 * tries. It tests in particular the add word and get word methods, as well as
 * the isWord method in a similar way to the Trie test class. As explained there
 * as well, it is impossible to test all of these methods without assuming that
 * one works, in this case isWord, and so I must use one test to test isWord and
 * addWord and if it fails, assume there could be something wrong with either
 * method. The test is comprehensive enough I should be able to figure out which
 * one.
 *
 * @author jfacey
 *
 */
public class TrieNodeTest {

  @Test
  public void testAddWordAndIsWord() {
    Trie trie = new Trie();

    trie.getRoot().addWord("");

    trie.getRoot().addWord("word");
    assertTrue(trie.isWord("word"));
    assertEquals(false, trie.isWord("wor"));
    assertEquals(false, trie.isWord("work"));

    trie.getRoot().addWord("wor");
    assertTrue(trie.isWord("wor"));
    assertTrue(trie.isWord("word"));
    assertEquals(false, trie.isWord("work"));

    trie.getRoot().addWord("dog");
    assertTrue(trie.isWord("dog"));
    assertTrue(trie.isWord("wor"));
    assertTrue(trie.isWord("word"));
    assertEquals(false, trie.isWord("work"));

    trie.getRoot().addWord("dogs");
    assertTrue(trie.isWord("dog"));
    assertTrue(trie.isWord("wor"));
    assertTrue(trie.isWord("word"));
    assertTrue(trie.isWord("dogs"));
    assertEquals(false, trie.isWord("work"));
  }

  @Test
  public void testGetWord() {

    Trie trie = new Trie();

    trie.getRoot().addWord("word");
    String word = trie.getRoot().getChildren()[22].getChildren()[14]
        .getChildren()[17].getChildren()[3].getWord();
    assertEquals(word, "word");
    assertEquals(trie.getRoot().getChildren()[22].getWord(), "w");

    trie.getRoot().addWord("abc");
    assertEquals(
        trie.getRoot().getChildren()[0].getChildren()[1].getWord(), "ab");
  }
}
