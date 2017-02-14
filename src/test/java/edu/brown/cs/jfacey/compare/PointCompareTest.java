package edu.brown.cs.jfacey.compare;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.brown.cs.jfacey.stars.Star;

/**
 * This class is for testing the
 * point comparator class.
 *
 * @author jfacey
 *
 */
public class PointCompareTest {

	@Test
	public void testCompare() {
		Star referencePoint = new Star(0, "", 3, 0, 5);
		Star testPoint1 = new Star(1, "", 2, 3, 4);

		PointCompare pComparator1 = new PointCompare(1);
		PointCompare pComparator2 = new PointCompare(2);
		PointCompare pComparator3 = new PointCompare(3);

		assertTrue(pComparator1.compare(testPoint1, referencePoint) < 0);
		assertTrue(pComparator2.compare(testPoint1, referencePoint) > 0);
		assertTrue(pComparator3.compare(testPoint1, referencePoint) < 0);
	}
}
