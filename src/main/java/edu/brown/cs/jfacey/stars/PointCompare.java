package edu.brown.cs.jfacey.stars;

import java.util.Comparator;

public class PointCompare implements Comparator<Kdpoint> {
	
	private final int _depth;
	
	public PointCompare(int depth) {
		_depth = depth;
	}
	@Override
	public int compare(Kdpoint o1, Kdpoint o2) {
		
		int dimension = (_depth % 3);
		
		if (dimension == 1) {
			
			return Double.compare(o1.getX(), o2.getX());
					
		} else if (dimension == 2) {
			
			return Double.compare(o1.getY(), o2.getY());
			
		} else {
			
			return Double.compare(o1.getZ(), o2.getZ());
			
		} 
	}
	
	
	
}