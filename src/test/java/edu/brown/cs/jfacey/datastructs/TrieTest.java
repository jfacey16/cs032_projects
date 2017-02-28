package edu.brown.cs.jfacey.datastructs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import edu.brown.cs.jfacey.autocorrect.Suggestion;

/**
 * This class tests the trie data structure. It focuses on testing the insert
 * and get word methods, as well as the prefix and led algorithms on the trie.
 * The isWord method is also tested in conjunction with these, but it is
 * impossible to test that all methods work from scratch, and therefore I must
 * try to test under the assumption that I implemented this method correctly.
 * This is why the first test actually is testing that both the insert and
 * isWord methods work correctly.
 *
 * @author jfacey
 *
 */
public class TrieTest {

  @Test
  public void testInsertAndIsWord() {
    Trie trie = new Trie();

    trie.insert("word");
    assertTrue(trie.isWord("word"));
    assertEquals(false, trie.isWord("wor"));
    assertEquals(false, trie.isWord("work"));

    trie.insert("wor");
    assertTrue(trie.isWord("wor"));
    assertTrue(trie.isWord("word"));
    assertFalse(trie.isWord("work"));

    trie.insert("dog");
    assertTrue(trie.isWord("dog"));
    assertTrue(trie.isWord("wor"));
    assertTrue(trie.isWord("word"));
    assertEquals(false, trie.isWord("work"));
  }

  @Test
  public void testGetWords() {
    Trie trie = new Trie();

    trie.insert("word");
    assertTrue(trie.getWords().contains("word"));
    assertFalse(trie.getWords().contains("wor"));
    assertFalse(trie.getWords().contains("work"));

    trie.insert("wor");
    assertTrue(trie.getWords().contains("wor"));
    assertTrue(trie.getWords().contains("word"));
    assertFalse(trie.getWords().contains("work"));

    trie.insert("dog");
    assertTrue(trie.getWords().contains("dog"));
    assertTrue(trie.getWords().contains("wor"));
    assertTrue(trie.getWords().contains("word"));
    assertFalse(trie.getWords().contains("work"));
  }

  @Test
  public void testUnigram() {
    Trie trie = new Trie();

    trie.insert("word");
    trie.insert("word");
    trie.insert("word");
    trie.insert("wor");
    trie.insert("wor");
    trie.insert("dog");
    trie.insert("word");
    trie.insert("dog");

    assertEquals((int) trie.getUnigram().get("word"), 4);
    assertEquals((int) trie.getUnigram().get("wor"), 2);
    assertEquals((int) trie.getUnigram().get("dog"), 2);
    assertEquals(trie.getUnigram().get("hi"), null);
  }

  @Test
  public void testBigram() {
    Trie trie = new Trie();

    trie.insert("word");
    trie.insert("hi");
    trie.insert("word");
    trie.insert("hi");
    trie.insert("at");
    trie.insert("dog");
    trie.insert("word");
    trie.insert("dog");

    BigramPair wordHi = new BigramPair("word", "hi");
    BigramPair hiWord = new BigramPair("hi", "word");
    BigramPair hiAt = new BigramPair("hi", "at");
    BigramPair atDog = new BigramPair("at", "dog");
    BigramPair dogWord = new BigramPair("dog", "word");
    BigramPair wordDog = new BigramPair("word", "dog");

    assertEquals((int) trie.getBigram().get(wordHi), 2);
    assertEquals((int) trie.getBigram().get(hiWord), 1);
    assertEquals((int) trie.getBigram().get(hiAt), 1);
    assertEquals((int) trie.getBigram().get(atDog), 1);
    assertEquals((int) trie.getBigram().get(dogWord), 1);
    assertEquals((int) trie.getBigram().get(wordDog), 1);

  }

  @Test
  public void testPrefix() {

    Trie trie = new Trie();
    // checks for blank tree
    assertEquals(trie.getPrefixSuggestions("word").size(), 0);

    trie.insert("word");
    trie.insert("dog");
    assertEquals(trie.getPrefixSuggestions("wor").get(0).getWord(), "word");
    assertEquals(trie.getPrefixSuggestions("wor").size(), 1);
    assertEquals(trie.getPrefixSuggestions("all").size(), 0);

    trie.insert("wor");
    assertEquals(trie.getPrefixSuggestions("wor").get(0).getWord(), "wor");
    assertEquals(trie.getPrefixSuggestions("wor").get(1).getWord(), "word");
    assertEquals(trie.getPrefixSuggestions("wor").size(), 2);
    assertEquals(trie.getPrefixSuggestions("dogs").size(), 0);
  }

  @Test
  public void testLed() {
    Trie trie = new Trie();
    // checks for blank tree
    assertEquals(trie.getLedSuggestions("", 1).size(), 0);
    assertEquals(trie.getLedSuggestions("word", 1).size(), 0);

    trie.insert("word");
    trie.insert("dog");
    assertEquals(trie.getLedSuggestions("wor", 1).get(0).getWord(), "word");
    assertEquals(trie.getLedSuggestions("wor", 2).get(0).getWord(), "dog");
    assertEquals(trie.getLedSuggestions("wor", 2).get(1).getWord(), "word");
    assertEquals(trie.getLedSuggestions("wor", 1).size(), 1);
    assertEquals(trie.getLedSuggestions("wor", 2).size(), 2);
    assertEquals(trie.getLedSuggestions("all", 2).size(), 0);
    assertEquals(trie.getLedSuggestions("wordsaz", 2).size(), 0);
    assertEquals(trie.getLedSuggestions("words", 2).size(), 1);
    trie.insert("wor");
    assertEquals(trie.getLedSuggestions("wor", 1).get(0).getWord(), "wor");
    assertEquals(trie.getLedSuggestions("wor", 1).get(1).getWord(), "word");
    assertEquals(trie.getLedSuggestions("wor", 1).size(), 2);
    assertEquals(trie.getPrefixSuggestions("dogs").size(), 0);
  }

  @Test
  public void testWhiteSpace() {
    Trie trie = new Trie();

    trie.insert("the");
    List<Suggestion> suggestions = trie.getWhiteSpaceSuggestions("thedog");
    assertEquals(suggestions.size(), 0);

    trie.insert("dog");
    suggestions = trie.getWhiteSpaceSuggestions("thedog");
    assertEquals(suggestions.get(0).getWord(), "the dog");

    suggestions = trie.getWhiteSpaceSuggestions("dog");
    assertEquals(suggestions.size(), 0);

    suggestions = trie.getWhiteSpaceSuggestions("abc");
    assertEquals(suggestions.size(), 0);

    suggestions = trie.getWhiteSpaceSuggestions("thedogs");
    assertEquals(suggestions.size(), 0);

    trie.insert("ad");
    trie.insert("ads");
    trie.insert("smore");
    trie.insert("more");
    suggestions = trie.getWhiteSpaceSuggestions("adsmore");

    assertEquals(suggestions.get(0).getWord(), "ad smore");
    assertEquals(suggestions.get(1).getWord(), "ads more");
  }
}
