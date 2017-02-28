package edu.brown.cs.jfacey.compare;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.jfacey.stars.Star;

/**
 * This class is for testing the distance comparator class.
 *
 * @author jfacey
 *
 */
public class DistanceCompareTest {

  @Test
  public void testCompare() {
    Star referencePoint = new Star(0, "", 0, 0, 0);
    Star testPoint1 = new Star(1, "", 2, 2, 2);
    Star testPoint2 = new Star(2, "", 3, 3, 3);

    DistanceCompare dComparator = new DistanceCompare(referencePoint);

    assertTrue(dComparator.compare(testPoint1, testPoint2) < 0);
    assertTrue(dComparator.compare(testPoint2, testPoint1) > 0);
  }

  @Test
  public void testsSquareDistance() {
    Star referencePoint = new Star(0, "", 0, 0, 0);
    Star testPoint1 = new Star(1, "", 2, 2, 2);
    Star testPoint2 = new Star(2, "", 3, 3, 3);

    DistanceCompare dComparator = new DistanceCompare(referencePoint);

    assertEquals(dComparator.squareDistance(testPoint1, referencePoint),
        12.0, .1);
    assertEquals(dComparator.squareDistance(testPoint2, referencePoint),
        27.0, .1);

  }
}
