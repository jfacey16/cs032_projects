package edu.brown.cs.jfacey.stars;

import java.util.Comparator;

public class DistanceCompare implements Comparator<Kdpoint> {
	
	private final Kdpoint _point;
	
	public DistanceCompare(Kdpoint point) {
		_point = point;
	}
	@Override
	public int compare(Kdpoint o1, Kdpoint o2) {
		return Double.compare(this.squareDistance(_point, o1), this.squareDistance(_point, o2));
		
	}
	//using squared distances as it is easier computationally, and functionally the same
	public double squareDistance(Kdpoint p1, Kdpoint p2) {
		return Math.pow((p1.getX() - p2.getX()), 2) + Math.pow((p1.getY() - p2.getY()), 2) + 
				Math.pow((p1.getZ() - p2.getZ()), 2);
		
	}
	
	
}