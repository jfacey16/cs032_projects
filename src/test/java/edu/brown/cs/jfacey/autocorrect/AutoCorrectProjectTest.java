package edu.brown.cs.jfacey.autocorrect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

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
public class AutoCorrectProjectTest {

  @Test
  public void testBuildTrieOneWord() {
    AutoCorrectProject acData = new AutoCorrectProject(false, false,
        false, 0);

    acData.buildTrie("data/autocorrect/norton.txt");
    assertTrue(acData.getTrie().isWord("norton"));
    assertEquals(false, acData.getTrie().isWord("hi"));
    assertEquals(false, acData.getTrie().isWord("nort"));
  }

  @Test
  public void testBuildTrieManyWords() {
    AutoCorrectProject acData = new AutoCorrectProject(false, false,
        false, 0);

    acData.buildTrie("data/autocorrect/testmultiline.txt");
    assertTrue(acData.getTrie().isWord("there"));
    assertEquals(false, acData.getTrie().isWord("hi"));
    assertTrue(acData.getTrie().isWord("more"));
    assertTrue(acData.getTrie().isWord("one"));
    assertTrue(acData.getTrie().isWord("line"));
    assertTrue(acData.getTrie().isWord("liner"));
    assertEquals(false, acData.getTrie().isWord("lin"));
  }

  @Test
  public void testBuildTrieManyFiles() {
    AutoCorrectProject acData = new AutoCorrectProject(false, false,
        false, 0);

    acData.buildTrie("data/autocorrect/testmultiline.txt");
    assertTrue(acData.getTrie().isWord("there"));
    assertEquals(false, acData.getTrie().isWord("hi"));
    assertTrue(acData.getTrie().isWord("more"));
    assertTrue(acData.getTrie().isWord("one"));
    assertTrue(acData.getTrie().isWord("line"));
    assertTrue(acData.getTrie().isWord("liner"));
    assertEquals(false, acData.getTrie().isWord("lin"));

    acData.buildTrie("data/autocorrect/norton.txt");
    assertTrue(acData.getTrie().isWord("norton"));
    assertTrue(acData.getTrie().isWord("there"));
    assertEquals(false, acData.getTrie().isWord("hi"));
    assertTrue(acData.getTrie().isWord("more"));
    assertTrue(acData.getTrie().isWord("one"));
    assertTrue(acData.getTrie().isWord("line"));
    assertTrue(acData.getTrie().isWord("liner"));
    assertEquals(false, acData.getTrie().isWord("lin"));
  }

  @Test
  public void testAutoCorrectJustEquals() {

    AutoCorrectProject acData = new AutoCorrectProject(false, false,
        false, 0);
    acData.buildTrie("data/autocorrect/bigram_unigram.txt");
    String[] input = new String[] { "ac", "apple", "pineapple" };
    List<String> acWords = acData.autoCorrectWord(input);
    assertEquals(acWords.size(), 2);

    assertEquals(acWords.get(0), "apple ");
    assertEquals(acWords.get(1), "pineapple");
  }

  @Test
  public void testAutoCorrectPrefix() {

    AutoCorrectProject acData = new AutoCorrectProject(true, false, false,
        0);
    acData.buildTrie("data/autocorrect/bigram_unigram.txt");

    String[] input = new String[] { "ac", "apple", "pineapple" };
    List<String> acWords = acData.autoCorrectWord(input);

    assertEquals(acWords.get(0), "apple ");
    assertEquals(acWords.get(1), "pineapple");

    input = new String[] { "ac", "apple", "p" };
    acWords = acData.autoCorrectWord(input);

    assertEquals(acWords.get(0), "apple ");
    assertEquals(acWords.get(1), "pineapple");
    assertEquals(acWords.get(2), "peach");
    assertEquals(acWords.get(3), "pack");
    assertEquals(acWords.get(4), "pear");
    assertEquals(acWords.get(5), "pine");
  }

  @Test
  public void testAutoCorrectLed() {

    AutoCorrectProject acData = new AutoCorrectProject(false, false,
        false, 2);
    acData.buildTrie("data/autocorrect/bigram_unigram.txt");

    String[] input = new String[] { "ac", "apple", "pineapple" };
    List<String> acWords = acData.autoCorrectWord(input);

    assertEquals(acWords.get(0), "apple ");
    assertEquals(acWords.get(1), "pineapple");

    input = new String[] { "ac", "apple", "p" };
    acWords = acData.autoCorrectWord(input);

    assertEquals(acWords.get(0), "apple ");
    assertEquals(acWords.get(1), "of");
    assertEquals(acWords.get(2), "at");
    assertEquals(acWords.get(3), "al");
    assertEquals(acWords.get(4), "ol");
  }

  @Test
  public void testAutoCorrectWhitespace() {

    AutoCorrectProject acData = new AutoCorrectProject(false, true, false,
        0);
    acData.buildTrie("data/autocorrect/bigram_unigram.txt");

    String[] input = new String[] { "ac", "apple", "pineapplen" };
    List<String> acWords = acData.autoCorrectWord(input);
    assertEquals(acWords.size(), 0);

    input = new String[] { "ac", "apple", "pineapplepeach" };
    acWords = acData.autoCorrectWord(input);

    assertEquals(acWords.get(0), "apple ");
    assertEquals(acWords.get(1), "pineapple peach");
  }

  @Test
  public void testAutoCorrectAll() {

    AutoCorrectProject acData = new AutoCorrectProject(true, true, false,
        2);
    acData.buildTrie("data/autocorrect/bigram_unigram.txt");

    String[] input = new String[] { "ac", "apple", "pineapple" };
    List<String> acWords = acData.autoCorrectWord(input);

    assertEquals(acWords.get(0), "apple ");
    assertEquals(acWords.get(1), "pineapple");
    assertEquals(acWords.get(2), "pine apple");

    input = new String[] { "ac", "apple", "p" };
    acWords = acData.autoCorrectWord(input);

    assertEquals(acWords.get(0), "apple ");
    assertEquals(acWords.get(1), "pineapple");
    assertEquals(acWords.get(2), "peach");
    assertEquals(acWords.get(3), "of");
    assertEquals(acWords.get(4), "pack");
    assertEquals(acWords.get(5), "pear");
    assertEquals(acWords.get(6), "at");
    assertEquals(acWords.get(7), "al");
    assertEquals(acWords.get(8), "ol");
    assertEquals(acWords.get(9), "pine");
  }

  @Test
  public void testMyRanking() {

    AutoCorrectProject acData = new AutoCorrectProject(true, true, true, 2);
    acData.buildTrie("data/autocorrect/bigram_unigram.txt");

    String[] input = new String[] { "ac", "apple", "p" };
    List<String> acWords = acData.autoCorrectWord(input);

    assertEquals(acWords.get(0), "apple ");
    assertEquals(acWords.get(1), "al");
    assertEquals(acWords.get(2), "at");
    assertEquals(acWords.get(3), "of");
    assertEquals(acWords.get(4), "ol");
    assertEquals(acWords.get(5), "pack");
    assertEquals(acWords.get(6), "pear");
    assertEquals(acWords.get(7), "pine");
    assertEquals(acWords.get(8), "peach");
    assertEquals(acWords.get(9), "pineapple");
  }
}
