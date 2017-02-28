package edu.brown.cs.jfacey.readers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * This class tests the corpus parser. As my implementation had to allow for
 * building a tree over time, the corpus parser actually only has a method to
 * read a line at a time, not parse out an entire file. So this parser is still
 * somewhat tested within the AutoCorrectData test file as well.
 *
 * @author jfacey
 *
 */
public class CorpusParserTest {

  @Test
  public void testReadOneWord() {
    CorpusParser corpusParser = new CorpusParser(
        "data/autocorrect/norton.txt");
    String[] parsedLine = corpusParser.parseLine();
    assertEquals("norton", parsedLine[0]);
  }

  @Test
  public void testReadMultipleLines() {
    CorpusParser corpusParser = new CorpusParser(
        "data/autocorrect/split.txt");

    String[] parsedLine = corpusParser.parseLine();
    assertEquals("split", parsedLine[0]);

    parsedLine = corpusParser.parseLine();
    assertEquals("words", parsedLine[0]);
  }

  @Test
  public void testReadFullLine() {
    CorpusParser corpusParser = new CorpusParser(
        "data/autocorrect/testline.txt");

    String[] parsedLine = corpusParser.parseLine();
    assertEquals("Hello", parsedLine[0]);
    assertEquals("my", parsedLine[1]);
    assertEquals("name", parsedLine[2]);
    assertEquals("is", parsedLine[3]);
    assertEquals("jack", parsedLine[4]);
  }

  @Test
  public void testParseWithSymbols() {
    CorpusParser corpusParser = new CorpusParser(
        "data/autocorrect/testsymbols.txt");

    String[] parsedLine = corpusParser.parseLine();
    assertEquals("numbers", parsedLine[0]);
    assertEquals("and", parsedLine[1]);
    assertEquals("punctuation", parsedLine[2]);
    assertEquals("should", parsedLine[3]);
    assertEquals("be", parsedLine[4]);
    assertEquals("gone", parsedLine[5]);
  }
}
