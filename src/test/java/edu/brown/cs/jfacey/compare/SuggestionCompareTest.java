package edu.brown.cs.jfacey.compare;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import edu.brown.cs.jfacey.autocorrect.Suggestion;
import edu.brown.cs.jfacey.datastructs.Trie;

/**
 * This class is for testing the suggestion comparator class.
 *
 * @author jfacey
 *
 */
public class SuggestionCompareTest {

  @Test
  public void testCompare() {
    Trie trie = new Trie();

    trie.insert("hello");
    trie.insert("pa");
    trie.insert("hello");
    trie.insert("pat");
    trie.insert("hello");
    trie.insert("pat");
    trie.insert("hello");
    trie.insert("pack");
    trie.insert("hello");
    trie.insert("pal");
    trie.insert("pal");
    trie.insert("hello");
    trie.insert("pacl");
    trie.insert("hello");

    SuggestionCompare sugCompare = new SuggestionCompare("hello", "pa",
        trie);
    List<Suggestion> sugList = trie.getPrefixSuggestions("pa");

    sugList.sort(sugCompare);
    assertEquals(sugList.get(0).getWord(), "pa");
    assertEquals(sugList.get(1).getWord(), "pat");
    assertEquals(sugList.get(2).getWord(), "pal");
    assertEquals(sugList.get(3).getWord(), "pack");
    assertEquals(sugList.get(4).getWord(), "pacl");
  }
}
