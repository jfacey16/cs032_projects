package edu.brown.cs.jfacey.compare;

import java.util.Comparator;

import edu.brown.cs.jfacey.datastructs.Kdpoint;
/**
 * This class defines a distance comparator
 * that compares two points distance to a
 * reference point.
 * @author jfacey
 *
 */
public class DistanceCompare implements Comparator<Kdpoint> {

	private final Kdpoint pointInstance;
	/**
	 * This constructor just sets the reference
	 * point instance variable.
	 *
	 * @param point the reference point
	 */
	public DistanceCompare(Kdpoint point) {
		pointInstance = point;
	}
	/**
	 * This method overrides the standard comparator compare method
	 * to return based on comparing the distances of two points
	 * to a reference point.
	 *
	 * @param o1 point one being compared
	 * @param o2 point two being compared
	 * @return integer value of compared doubles
	 */
	@Override
	public int compare(Kdpoint o1, Kdpoint o2) {
		return Double.compare(this.squareDistance(pointInstance, o1),
				this.squareDistance(pointInstance, o2));

	}
	/**
	 * This method finds the square distance between two points.
	 * This method uses squared distances as it is easier
	 * computationally, and functionally the same.
	 *
	 * @param p1 point 1 for distance
	 * @param p2 point 2 for distance
	 * @return double of distance between two point
	 */
	public double squareDistance(Kdpoint p1, Kdpoint p2) {
		return Math.pow((p1.getX() - p2.getX()), 2) + Math.pow(
				(p1.getY() - p2.getY()), 2) + Math.pow(
				(p1.getZ() - p2.getZ()), 2);

	}
}
