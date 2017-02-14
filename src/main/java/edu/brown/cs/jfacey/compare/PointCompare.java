package edu.brown.cs.jfacey.compare;

import java.util.Comparator;

import edu.brown.cs.jfacey.datastructs.Kdpoint;
/**
 * This class defines a point comparator
 * that compares two points distance on a
 * certain axis.
 *
 * @author jfacey
 *
 */
public class PointCompare implements Comparator<Kdpoint> {

	private final int depthInstance;
	/**
	 * Constructor for a comparator that compares the coordinate values
	 * of two points on a certain plane.
	 *
	 * @param depth current depth in the tree for the comparing nodes
	 */
	public PointCompare(int depth) {
		depthInstance = depth;
	}
	/**
	 * This method overrides the standard comparator method. It compares
	 * using standard double comparators, but will compare the certain
	 * coordinate of each point based on the depth of the tree, which
	 * defines the splitting coordinate plane.
	 *
	 * @param o1 point one to be compared
	 * @param o2 point two to be compared
	 * @return integer value of compared doubles
	 */
	@Override
	public int compare(Kdpoint o1, Kdpoint o2) {
		//find dimension
		int dimension = (depthInstance % 3);
		//split based on dimension
		if (dimension == 1) {

			return Double.compare(o1.getX(), o2.getX());

		} else if (dimension == 2) {

			return Double.compare(o1.getY(), o2.getY());

		} else {

			return Double.compare(o1.getZ(), o2.getZ());
		}
	}
}
