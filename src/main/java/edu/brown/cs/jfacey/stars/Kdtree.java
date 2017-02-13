package edu.brown.cs.jfacey.stars;

import java.util.Hashtable;
import java.util.List;

public class Kdtree<T extends Kdpoint> {
	
	private T _root;
	private Hashtable<String,T> _pointsTable;
	
	public Kdtree(List<T> points) {
		_root = null;
		_pointsTable = new Hashtable<>();
		
		points.sort(new PointCompare(1));
		_root = points.get(points.size()/2);
		
		_pointsTable.put(_root.getName(), _root);
		
		_root.setLeftChild(buildTree(points.subList(0,points.size()/2),2));
		_root.setRightChild(buildTree(points.subList(points.size()/2 + 1, points.size()),2));
	}
	
	public T buildTree(List<T> points, int depth) {
		
		points.sort(new PointCompare(depth));
		
		T node = points.get(points.size()/2);
		
		_pointsTable.put(node.getName(), node);
		
		if (points.size() > 2) {
			
			node.setLeftChild(buildTree(points.subList(0,points.size()/2),depth+1));
			node.setRightChild(buildTree(points.subList(points.size()/2 + 1, points.size()),depth+1));
			
		}	else if (points.size() == 2) {
			
			node.setLeftChild(buildTree(points.subList(0,points.size()/2),depth+1));
			
		}
		
		
		return node;
	}
	
	public void neighbors(List<T> bestNeighbors, int numNeighbors, T searchPoint, String name) {
		
		if (name != null) {
			
			searchPoint = _pointsTable.get(name);
					
		}
		
		this.neighborsHelper(bestNeighbors, numNeighbors, _root,searchPoint, 1);
		
	}
	
	@SuppressWarnings("unchecked")
	public void neighborsHelper(List<T> bestNeighbors, int numNeighbors, T cur, T searchPoint, int depth) {
		
		PointCompare pointComparator = new PointCompare(depth);
		DistanceCompare distanceComparator = new DistanceCompare(searchPoint);
		
		//node with two children
		if (cur.getLeftChild() != null && cur.getRightChild() != null) {
			
			//compare to pick path to follow
			if (pointComparator.compare(searchPoint, cur) <= 0) {
				//follow left path
				this.neighborsHelper(bestNeighbors, numNeighbors, (T) cur.getLeftChild(), searchPoint, depth + 1);
				
				//add current node to best list if applicable
				if (bestNeighbors.size() < numNeighbors) {
					//if list not yet at size, add and sort
					bestNeighbors.add(cur);
					bestNeighbors.sort(distanceComparator);
					
				} else {
					
					if (distanceComparator.compare(bestNeighbors.get(numNeighbors - 1), cur) > 0) {
						//if last node in sorted list not as close as current node, replace it and sort
						bestNeighbors.remove(numNeighbors - 1);
						bestNeighbors.add(cur);
						bestNeighbors.sort(distanceComparator);
					}
				}
				//calculate split coordinate distance between current node and search node
				double dist;
				
				int dimension = (depth % 3);
				
				if (dimension == 1) {
					
					dist = Math.pow(cur.getX() - searchPoint.getX(), 2);
							
				} else if (dimension == 2) {
					
					dist = Math.pow(cur.getY() - searchPoint.getY(), 2);
					
				} else {
					
					dist = Math.pow(cur.getZ() - searchPoint.getZ(), 2);
					
				}
				//if overall distance between search point and the worst of my current nearest neighbors is greater 
				//than the distance between the search point and current point along the current axis or if I don't 
				//currently have a full nearest neighbors list, then I search along other branches
				if (bestNeighbors.size() < numNeighbors) {
					
					this.neighborsHelper(bestNeighbors, numNeighbors, (T) cur.getRightChild(), searchPoint, depth + 1);
					
				} else {
					
					if (distanceComparator.squareDistance(bestNeighbors.get(numNeighbors - 1), searchPoint) > dist) {
					
						this.neighborsHelper(bestNeighbors, numNeighbors, (T) cur.getRightChild(), searchPoint, depth + 1);
					}
				}
				
			} else {
				//follow right path
				this.neighborsHelper(bestNeighbors, numNeighbors, (T) cur.getRightChild(), searchPoint, depth + 1);
				
				//add current node to best list if applicable
				if (bestNeighbors.size() < numNeighbors) {
					//if list not yet at size, add and sort
					bestNeighbors.add(cur);
					bestNeighbors.sort(distanceComparator);
					
				} else {
					
					if (distanceComparator.compare(bestNeighbors.get(numNeighbors - 1), cur) > 0) {
						//if last node in sorted list not as close as current node, replace it and sort
						bestNeighbors.remove(numNeighbors - 1);
						bestNeighbors.add(cur);
						bestNeighbors.sort(distanceComparator);
					}
				}
				//calculate split coordinate distance between current node and search node
				double dist;
				
				int dimension = (depth % 3);
				
				if (dimension == 1) {
					
					dist = Math.pow(cur.getX() - searchPoint.getX(), 2);
							
				} else if (dimension == 2) {
					
					dist = Math.pow(cur.getY() - searchPoint.getY(), 2);
					
				} else {
					
					dist = Math.pow(cur.getZ() - searchPoint.getZ(), 2);
					
				}
				//if overall distance between search point and the worst of my current nearest neighbors is greater 
				//than the distance between the search point and current point along the current axis or if I don't 
				//currently have a full nearest neighbors list, then I search along other branches
				if (bestNeighbors.size() < numNeighbors) {
					
					this.neighborsHelper(bestNeighbors, numNeighbors, (T) cur.getLeftChild(), searchPoint, depth + 1);
					
				} else {
					
					if (distanceComparator.squareDistance(bestNeighbors.get(numNeighbors - 1), searchPoint) > dist) {
					
						this.neighborsHelper(bestNeighbors, numNeighbors, (T) cur.getLeftChild(), searchPoint, depth + 1);
					}
				}
			}
			
		//only left child just go left	
		} else if (cur.getLeftChild() != null && cur.getRightChild() == null) {
			
			this.neighborsHelper(bestNeighbors, numNeighbors, (T) cur.getLeftChild(), searchPoint, depth + 1);
			
			//add current node to best list if applicable
			if (bestNeighbors.size() < numNeighbors) {
				//if list not yet at size, add and sort
				bestNeighbors.add(cur);
				bestNeighbors.sort(distanceComparator);
				
			} else if (distanceComparator.compare(bestNeighbors.get(numNeighbors - 1), cur) > 0) {
				//if last node in sorted list not as close as current node, replace it and sort
				bestNeighbors.remove(numNeighbors - 1);
				bestNeighbors.add(cur);
				bestNeighbors.sort(distanceComparator);
			}		
		//leaf node	
		} else if (cur.getLeftChild() == null && cur.getRightChild() == null) {
			
			//add current node to best list if applicable
			if (bestNeighbors.size() < numNeighbors) {
				//if list not yet at size, add and sort
				bestNeighbors.add(cur);
				bestNeighbors.sort(distanceComparator);
				
			} else if (distanceComparator.compare(bestNeighbors.get(numNeighbors - 1), cur) > 0) {
				//if last node in sorted list not as close as current node, replace it and sort
				bestNeighbors.remove(numNeighbors - 1);
				bestNeighbors.add(cur);
				bestNeighbors.sort(distanceComparator);
			}
		}
	}
	
	public void radius(List<T> bestNeighbors, double radiusValue, T searchPoint, String name) {
			
		if (name != null) {
			
			searchPoint = _pointsTable.get(name);
			System.out.println(searchPoint.getName());				
		}
		
		this.radiusHelper(bestNeighbors, radiusValue, _root,searchPoint, 1);
	}
	
	@SuppressWarnings("unchecked")
	public void radiusHelper(List<T> bestRadius, double radiusValue, T cur, T searchPoint, int depth) {
		
		PointCompare pointComparator = new PointCompare(depth);
		DistanceCompare distanceComparator = new DistanceCompare(searchPoint);
		
		//node with two children
		if (cur.getLeftChild() != null && cur.getRightChild() != null) {
			
			//compare to pick path to follow
			if (pointComparator.compare(searchPoint, cur) <= 0) {
				//follow left path
				this.radiusHelper(bestRadius, radiusValue, (T) cur.getLeftChild(), searchPoint, depth + 1);
				
				//if node is within distance add to list
				if (distanceComparator.squareDistance(cur, searchPoint) < Math.pow(radiusValue,2)) {
					
					bestRadius.add(cur);
					
				}
				
				//calculate split coordinate distance between current node and search node
				double dist;
				
				int dimension = depth % 3;
				
				if (dimension == 1) {
					
					dist = Math.pow(cur.getX() - searchPoint.getX(), 2);
							
				} else if (dimension == 2) {
					
					dist = Math.pow(cur.getY() - searchPoint.getY(), 2);
					
				} else {
					
					dist = Math.pow(cur.getZ() - searchPoint.getZ(), 2);
					
				}
				//if overall distance between search point and the worst of my current nearest neighbors is greater 
				//than the distance between the search point and current point along the current axis or if I don't 
				//currently have a full nearest neighbors list, then I search along other branches
					
				if (radiusValue > dist) {
					
					this.radiusHelper(bestRadius, radiusValue, (T) cur.getRightChild(), searchPoint, depth + 1);
					
				}
				
			} else {
				//follow right path
				this.radiusHelper(bestRadius, radiusValue, (T) cur.getRightChild(), searchPoint, depth + 1);
				
				//if node is within distance add to list
				if (distanceComparator.squareDistance(cur, searchPoint) < Math.pow(radiusValue,2)) {
					
					bestRadius.add(cur);
					
				}
				//calculate split coordinate distance between current node and search node
				double dist;
				
				int dimension = depth % 3;
				
				if (dimension == 1) {
					
					dist = Math.pow(cur.getX() - searchPoint.getX(), 2);
							
				} else if (dimension == 2) {
					
					dist = Math.pow(cur.getY() - searchPoint.getY(), 2);
					
				} else {
					
					dist = Math.pow(cur.getZ() - searchPoint.getZ(), 2);
					
				}
				//if the radius is greater than the distance along the splitting plane between the current 
				//point and the point we are searching for, then we follow the other subtree
				if (radiusValue > dist) {
					
					this.radiusHelper(bestRadius, radiusValue, (T) cur.getLeftChild(), searchPoint, depth + 1);
					
				} 
			}
			
		//only left child just go left	
		} else if (cur.getLeftChild() != null && cur.getRightChild() == null) {
			
			this.radiusHelper(bestRadius, radiusValue, (T) cur.getLeftChild(), searchPoint, depth + 1);
			
			//if node is within distance add to list
			if (distanceComparator.squareDistance(cur, searchPoint) < Math.pow(radiusValue,2)) {
				
				bestRadius.add(cur);
				
			}
			
		//leaf node	
		} else if (cur.getLeftChild() == null && cur.getRightChild() == null) {
			
			//if node is within distance add to list
			if (distanceComparator.squareDistance(cur, searchPoint) < Math.pow(radiusValue,2)) {
				
				bestRadius.add(cur);
				
			}
		}
	}
}