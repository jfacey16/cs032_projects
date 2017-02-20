package edu.brown.cs.jfacey.readers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class defines a csvparser. This parses out csv files into a usable list
 * of set points.
 *
 * @author jfacey
 *
 */
public class CSVParser {

  private String iFilePath;

  /**
   * Constructor for the csvparser. It just sets the value of the input file
   * path for parsing. This class has just one method to parse the input file.
   *
   * @param filePath
   *          filepath of the file for the csvparser to parse
   */
  public CSVParser(String filePath) {

    iFilePath = filePath;

  }

  /**
   * Returns a list of stars, as parsed out from an input csv file. This
   * function uses a buffered reader to read inputs from the given file, line by
   * line. At each line, the string is split by commas. The strings in the array
   * from the split string are used to fill in the values of a new star, after
   * being converted to the correct types. The star is then added to a list to
   * return, and a new line is read until all lines have been read.
   *
   * @throws RuntimeException
   *           if file can not be parsed
   *
   * @return the list of stars parsed by the csvreader
   */
  public List<String[]> parseFile() throws RuntimeException {
    // Instantiate stars array
    List<String[]> points = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(iFilePath))) {

      String input = br.readLine();
      String[] header = input.split(",");
      // checks for a valid header
      if (!Objects.equals(header[0], "StarID")
          || !Objects.equals(header[1], "ProperName")
          || !Objects.equals(header[2], "X") || !Objects.equals(header[3], "Y")
          || !Objects.equals(header[4], "Z")) {

        throw new RuntimeException(
            "ERROR: Header not formatted correctly. Should be StarID,"
                + "ProperName,X,Y,Z");
      }
      // loop reading inputs until no lines left
      while ((input = br.readLine()) != null) {
        // parse input
        String[] inputs = input.split(",");
        // load points into point list array
        points.add(inputs);
      }
    } catch (IOException e) {

      throw new RuntimeException(e.getMessage());
    }

    return points;
  }
}
