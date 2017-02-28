package edu.brown.cs.jfacey.compare;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import edu.brown.cs.jfacey.autocorrect.Suggestion;
import edu.brown.cs.jfacey.datastructs.Trie;

/**
 * This class is for testing the smart suggestion comparator class.
 *
 * @author jfacey
 *
 */
public class SmartSuggestionCompareTest {

  @Test
  public void testCompare() {
    Trie trie = new Trie();

    trie.insert("p");
    trie.insert("a");
    trie.insert("pa");
    trie.insert("pat");
    trie.insert("pack");
    trie.insert("packes");
    trie.insert("pic");
    trie.insert("packer");

    SmartSuggestionCompare sugCompare = new SmartSuggestionCompare("pa");
    List<Suggestion> sugList = trie.getPrefixSuggestions("pa");
    sugList.addAll(trie.getLedSuggestions("pa", 2));
    sugList.addAll(trie.getWhiteSpaceSuggestions("pa"));

    sugList.sort(sugCompare);

    assertEquals(sugList.get(0).getWord(), "pa");
    assertEquals(sugList.get(1).getWord(), "pa");
    assertEquals(sugList.get(2).getWord(), "p a");
    // prefix pat
    assertEquals(sugList.get(3).getWord(), "pat");
    assertEquals(sugList.get(4).getWord(), "a");
    assertEquals(sugList.get(5).getWord(), "p");
    // led pat
    assertEquals(sugList.get(6).getWord(), "pat");
    // prefix pack
    assertEquals(sugList.get(7).getWord(), "pack");
    // led pack
    assertEquals(sugList.get(8).getWord(), "pack");
    assertEquals(sugList.get(9).getWord(), "pic");
    assertEquals(sugList.get(10).getWord(), "packer");
    assertEquals(sugList.get(11).getWord(), "packes");
    assertEquals(sugList.size(), 12);
  }
}