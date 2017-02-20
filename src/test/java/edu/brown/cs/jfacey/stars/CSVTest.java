package edu.brown.cs.jfacey.stars;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import edu.brown.cs.jfacey.readers.CSVParser;

/**
 * This class is for testing the csvparser class.
 *
 * @author jfacey
 *
 */
public class CSVTest {

  @Test
  public void testParse() {
    CSVParser csvParser = new CSVParser("data/stars/ten-star.csv");
    StarConverter starConverter = new StarConverter(csvParser.parseFile());
    List<Star> starList = starConverter.convertStars();
    assertEquals(starList.get(0).getX(), 0.0, .1);
    assertEquals(starList.get(1).getY(), 0.00449, .1);
    assertEquals(starList.get(2).getZ(), -15.24144, .1);
    assertEquals(starList.get(3).getID(), 3);
    assertEquals(starList.get(5).getName(), "Proxima Centauri");
  }
}
