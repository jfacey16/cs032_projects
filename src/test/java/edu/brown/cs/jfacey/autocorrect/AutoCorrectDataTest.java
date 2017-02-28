package edu.brown.cs.jfacey.autocorrect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import edu.brown.cs.jfacey.datastructs.Trie;

/**
 * This is the class to test the autocorrect data class. It mainly tests
 * building the tree, and that the autocorrector is correctly being run on the
 * options given. The trie tests will actually test the different options for
 * led and prefix searching more in depth, while this will especially focus on
 * the smart ranking and whitespace, as they are not dealt with in the trie.
 *
 * @author jfacey
 *
 */
public class AutoCorrectDataTest {

  @Test
  public void testBuildTrieOneWord() {
    Trie trie = new Trie();
    AutoCorrectData acData = new AutoCorrectData(false, false, false, 0,
        trie);

    acData.buildTrie("data/autocorrect/norton.txt");
    assertTrue(trie.isWord("norton"));
    assertEquals(false, trie.isWord("hi"));
    assertEquals(false, trie.isWord("nort"));
  }

  @Test
  public void testBuildTrieManyWords() {
    Trie trie = new Trie();
    AutoCorrectData acData = new AutoCorrectData(false, false, false, 0,
        trie);

    acData.buildTrie("data/autocorrect/testmultiline.txt");
    assertTrue(trie.isWord("there"));
    assertEquals(false, trie.isWord("hi"));
    assertTrue(trie.isWord("more"));
    assertTrue(trie.isWord("one"));
    assertTrue(trie.isWord("line"));
    assertTrue(trie.isWord("liner"));
    assertEquals(false, trie.isWord("lin"));
  }

  @Test
  public void testBuildTrieManyFiles() {
    Trie trie = new Trie();
    AutoCorrectData acData = new AutoCorrectData(false, false, false, 0,
        trie);

    acData.buildTrie("data/autocorrect/testmultiline.txt");
    assertTrue(trie.isWord("there"));
    assertEquals(false, trie.isWord("hi"));
    assertTrue(trie.isWord("more"));
    assertTrue(trie.isWord("one"));
    assertTrue(trie.isWord("line"));
    assertTrue(trie.isWord("liner"));
    assertEquals(false, trie.isWord("lin"));

    acData.buildTrie("data/autocorrect/norton.txt");
    assertTrue(trie.isWord("norton"));
    assertTrue(trie.isWord("there"));
    assertEquals(false, trie.isWord("hi"));
    assertTrue(trie.isWord("more"));
    assertTrue(trie.isWord("one"));
    assertTrue(trie.isWord("line"));
    assertTrue(trie.isWord("liner"));
    assertEquals(false, trie.isWord("lin"));
  }

  @Test
  public void testAutoCorrectJustEquals() {
    Trie trie = new Trie();

    AutoCorrectData acData = new AutoCorrectData(false, false, false, 0,
        trie);
    acData.buildTrie("data/autocorrect/bigram_unigram.txt");

    List<String> acWords = acData.autoCorrectWord("apple", "pineapple");
    assertEquals(acWords.size(), 1);
    assertEquals(acWords.get(0), "pineapple");
  }

  @Test
  public void testAutoCorrectPrefix() {
    Trie trie = new Trie();

    AutoCorrectData acData = new AutoCorrectData(true, false, false, 0,
        trie);
    acData.buildTrie("data/autocorrect/bigram_unigram.txt");

    List<String> acWords = acData.autoCorrectWord("apple", "pineapple");
    assertEquals(acWords.get(0), "pineapple");

    acWords = acData.autoCorrectWord("apple", "p");
    assertEquals(acWords.get(0), "pineapple");
    assertEquals(acWords.get(1), "peach");
    assertEquals(acWords.get(2), "pack");
    assertEquals(acWords.get(3), "pear");
    assertEquals(acWords.get(4), "pine");
  }

  @Test
  public void testAutoCorrectLed() {
    Trie trie = new Trie();

    AutoCorrectData acData = new AutoCorrectData(false, false, false, 2,
        trie);
    acData.buildTrie("data/autocorrect/bigram_unigram.txt");

    List<String> acWords = acData.autoCorrectWord("apple", "pineapple");
    assertEquals(acWords.get(0), "pineapple");

    acWords = acData.autoCorrectWord("apple", "p");
    assertEquals(acWords.get(0), "of");
    assertEquals(acWords.get(1), "at");
    assertEquals(acWords.get(2), "al");
    assertEquals(acWords.get(3), "ol");
  }

  @Test
  public void testAutoCorrectWhitespace() {
    Trie trie = new Trie();

    AutoCorrectData acData = new AutoCorrectData(false, true, false, 0,
        trie);
    acData.buildTrie("data/autocorrect/bigram_unigram.txt");

    List<String> acWords = acData.autoCorrectWord("apple", "pineapplen");
    assertEquals(acWords.size(), 0);

    acWords = acData.autoCorrectWord("apple", "pineapplepeach");
    assertEquals(acWords.get(0), "pineapple peach");
  }

  @Test
  public void testAutoCorrectAll() {
    Trie trie = new Trie();

    AutoCorrectData acData = new AutoCorrectData(true, true, false, 2,
        trie);
    acData.buildTrie("data/autocorrect/bigram_unigram.txt");

    List<String> acWords = acData.autoCorrectWord("apple", "pineapple");

    assertEquals(acWords.get(0), "pineapple");
    assertEquals(acWords.get(1), "pine apple");

    acWords = acData.autoCorrectWord("apple", "p");

    assertEquals(acWords.get(0), "pineapple");
    assertEquals(acWords.get(1), "peach");
    assertEquals(acWords.get(2), "of");
    assertEquals(acWords.get(3), "pack");
    assertEquals(acWords.get(4), "pear");
    assertEquals(acWords.get(5), "at");
    assertEquals(acWords.get(6), "al");
    assertEquals(acWords.get(7), "ol");
    assertEquals(acWords.get(8), "pine");
  }

  @Test
  public void testMyRanking() {

    Trie trie = new Trie();

    AutoCorrectData acData = new AutoCorrectData(true, true, true, 2, trie);
    acData.buildTrie("data/autocorrect/bigram_unigram.txt");

    List<String> acWords = acData.autoCorrectWord("apple", "p");

    assertEquals(acWords.get(0), "al");
    assertEquals(acWords.get(1), "at");
    assertEquals(acWords.get(2), "of");
    assertEquals(acWords.get(3), "ol");
    assertEquals(acWords.get(4), "pack");
    assertEquals(acWords.get(5), "pear");
    assertEquals(acWords.get(6), "pine");
    assertEquals(acWords.get(7), "peach");
    assertEquals(acWords.get(8), "pineapple");
  }
}
