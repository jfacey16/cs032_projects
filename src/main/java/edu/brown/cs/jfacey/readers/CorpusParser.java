package edu.brown.cs.jfacey.readers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class defines a corpus parser used to read in a list of valid words from
 * an input corpus file.
 *
 * @author jfacey
 *
 */
public class CorpusParser {

  private String iFilePath;
  private BufferedReader iBr;

  /**
   * This constructor sets the value of the filepath, and attempts to load the
   * filepath into the buffered reader. If the filepath is invalid, an error
   * will be thrown.
   *
   * @param filepath
   *          the filepath to the file to parse
   * @throws RuntimeException
   *           if filereader can't be made, throwexception
   */
  public CorpusParser(String filepath) throws RuntimeException {

    iFilePath = filepath;
    // if filepath is invalid, throw runtime exception to be eventually caught
    // in the command for loading a corpus
    try {
      iBr = new BufferedReader(new FileReader(iFilePath));
    } catch (FileNotFoundException e) {

      throw new RuntimeException();
    }
  }

  /**
   * This method is used to parse a corpus file into a list of strings
   * representing words.
   *
   * @return parsed words
   * @throws RuntimeException
   *           throw exception if invalid filepath
   */
  public String[] parseLine() throws RuntimeException {

    // instantiate array to store words
    String[] words = null;
    // make buffered reader for reading corpus file

    String input;
    // read lines from corpus file
    try {

      input = iBr.readLine();

      if (input != null) {
        // parse input
        words = input.split("[\\p{Punct}\\d\\s]+");
      }
    } catch (IOException e) {

      throw new RuntimeException(e.getMessage());
    }

    return words;
  }
}
