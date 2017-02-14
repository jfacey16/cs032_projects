package edu.brown.cs.jfacey.datastructs;

import java.util.Hashtable;
import java.util.List;

import edu.brown.cs.jfacey.compare.DistanceCompare;
import edu.brown.cs.jfacey.compare.PointCompare;
/**
 *
 * @author jfacey
 *
 * @param <T>
 */
public class Kdtree<T extends Kdpoint> {

	private T rootInstance;
	private Hashtable<String, T> pointsTableInstance;
	/**
	 *
	 * Builds the kdtree out of a list of points passed in.
	 * It chooses the point to add by sorting the list on
	 * the x plane and choosing the median. The left and right
	 * children of the node are then set by calling a build
	 * tree helper method on the sub lists split at the median
	 * point, which will be called recursively. The points are
	 * also added to hash table for easy retrieval when a
	 * node must be accessed by name. This class has methods
	 * to build a tree, as well as do radius or nearest
	 * neighbors searches.
	 *
	 * @param points The list of points used to build the tree
	 */
	public Kdtree(List<T> points) {
		//instantiate hashtable for storing points by name
		pointsTableInstance = new Hashtable<>();
		//sort points by splitting axis and select median for root
		points.sort(new PointCompare(1));
		rootInstance = points.get(points.size() / 2);
		//add point to list
		pointsTableInstance.put(rootInstance.getName(), rootInstance);
		//call build tree to set children based on size of list
		if (points.size() > 2) {

			rootInstance.setLeftChild(buildTree(points.subList(
					0, points.size() / 2), 2));
			rootInstance.setRightChild(buildTree(points.subList(
					points.size() / 2 + 1,
					points.size()), 2));

		} else if (points.size() == 2) {

			rootInstance.setLeftChild(buildTree(points.subList(
					0, points.size() / 2), 2));
		}
	}
	/**
	 *
	 * Returns and sets the children of the current node
	 * being added to the kdtree. This method takes in
	 * the sublist of points being added to this branch of the
	 * tree. It is essentially identical to the constructor
	 * method, and just sorts the sublists based on the
	 * current depth, either x, y, or z axis.
	 *
	 * @param points A sublist of all points that are being
	 * added to this branch of the kd tree
	 * @param depth The current depth of the tree at this
	 * point in order to split on the correct axis
	 * @return The current node that got added to the tree
	 */
	public T buildTree(List<T> points, int depth) {
		//sort points by splitting axis and select
		//median for node
		points.sort(new PointCompare(depth));

		T node = points.get(points.size() / 2);
		//add point to list
		pointsTableInstance.put(node.getName(), node);
		//call build tree recursively to set children
		//based on size of list
		if (points.size() > 2) {

			node.setLeftChild(buildTree(points.subList(
					0, points.size() / 2), depth + 1));
			node.setRightChild(buildTree(points.subList(
					points.size() / 2 + 1,
					points.size()), depth + 1));

		}	else if (points.size() == 2) {

			node.setLeftChild(buildTree(points.subList(
					0, points.size() / 2), depth + 1));
		}

		return node;
	}
	/**
	 * Stores the list of the k nearest neighbors to
	 * the inputs search point or point named
	 * in the list bestNeighbors. This function first
	 * just checks whether name is null or not
	 * to know if it needs to find the search point,
	 * or if the point was inserted in the initial
	 * command. The method then will call the function
	 * that actually finds the k nearest neighbors
	 * passing in the new parameters. If the name is null,
	 * this means a point is already given. If it is not,
	 * the name should be of a point in the kdtree.
	 *
	 * @param bestNeighbors list where the k nearest neighbors
	 * are stored
	 * @param numNeighbors number of nearest neighbors to find
	 * @param searchPoint the point the search is based off of,
	 * may be null
	 * @param name the name of the point to search off,
	 * may be null
	 */
	public void neighbors(List<T> bestNeighbors, int numNeighbors,
			T searchPoint, String name) {
		//check to make sure number of neighbors to search on
		//is greater than zero
		if (numNeighbors <= 0) {
			System.out.println("ERROR: Number of neighbors "
					+ "to find must "
					+ "be greater than zero");
			return;
		}
		//if the name is given, find the point to search on
		if (name != null) {

			searchPoint = pointsTableInstance.get(name);
			//if the point can not be found, an invalid
			//name was given
			if (searchPoint == null) {

				System.out.println("ERROR: Name is of a point "
						+ "that "
						+ "does not exist");
				return;
			}
		}
		//call the helper method to run the algorithm
		this.neighborsHelper(bestNeighbors, numNeighbors, rootInstance,
				searchPoint, 1);
	}
	/**
	 * Recursively called to find k nearest neighbors
	 * to the search point. Stores the current best list
	 * under bestNeighbors. The algorithm fundamentally
	 * works by first checking on the current splitting
	 * plane of the tree the difference of the coordinates
	 * on that plane of the search point and the
	 * current node. It then recursively calls itself on
	 * the left or right child based on whether the search
	 * points value was smaller or larger. At each node,
	 * if the bestNeighbors list is not yet full, the node
	 * gets added automatically. If it is, distance from
	 * the worst node in the list to the search point is
	 * compared to the distance for the current node from
	 * the search point. If it is closer, it gets added
	 * to the list. Then, the algorithm checks if the other
	 * subtree must be checked. This occurs if the
	 * overall distance between search point and the worst
	 * of my current nearest neighbors is greater
	 * than the distance between the search point and current
	 * point along the current axis or if I don't
	 * currently have a full nearest neighbors list. If a node
	 * is a leaf, then the check to add to the list
	 * is run. If the node has only one child, that path
	 * is always followed, and the normal add to the
	 * list code is run.
	 *
	 * @param bestNeighbors the current list of nearest neighbors
	 * @param numNeighbors the number of nearest neighbors to find
	 * @param cur the current node
	 * @param searchPoint the point we are searching based on
	 * @param depth the current depth of the tree
	 */
	@SuppressWarnings("unchecked")
	public void neighborsHelper(List<T> bestNeighbors, int numNeighbors,
			T cur, T searchPoint, int depth) {

		PointCompare pointComparator = new PointCompare(depth);
		DistanceCompare dComparator =
				new DistanceCompare(searchPoint);

		//node with two children
		if (cur.getLeftChild() != null && cur.getRightChild() != null) {

			//compare to pick path to follow
			if (pointComparator.compare(searchPoint, cur) <= 0) {
				//follow left path
				this.neighborsHelper(bestNeighbors,
						numNeighbors,
						(T) cur.getLeftChild(),
						searchPoint, depth + 1);

				//add current node to best list if applicable
				if (bestNeighbors.size() < numNeighbors) {
					//if list not yet at size, add and sort
					bestNeighbors.add(cur);
					bestNeighbors.sort(dComparator);

				} else {

					if (dComparator.compare(
							bestNeighbors.get(
							numNeighbors - 1),
							cur) > 0) {
						//if last node in sorted list
						//not as
						//close as current node,
						//replace it and sort
						bestNeighbors.remove(
								numNeighbors
								- 1);
						bestNeighbors.add(
								cur);
						bestNeighbors.sort(
								dComparator);
					}
				}
				//calculate split coordinate distance between
				//current node and search node
				double dist;

				int dimension = (depth % 3);

				if (dimension == 1) {

					dist = Math.pow(cur.getX()
							- searchPoint.getX(),
							2);

				} else if (dimension == 2) {

					dist = Math.pow(cur.getY()
							- searchPoint.getY(),
							2);

				} else {

					dist = Math.pow(cur.getZ()
							- searchPoint.getZ(),
							2);

				}
				//if overall distance between search point
				//and the worst of my current nearest
				//neighbors is greater than the distance
				//between the search point and current point
				//along the current axis or if I don't
				//currently have a full nearest neighbors
				//list, then I search along other branches
				if (bestNeighbors.size() < numNeighbors) {

					this.neighborsHelper(
							bestNeighbors,
							numNeighbors,
							(T)
							cur.getRightChild(
							),
							searchPoint,
							depth + 1);

				} else {

					if (dComparator.squareDistance(
							bestNeighbors.get(
							numNeighbors
							- 1),
							searchPoint) > dist) {

						this.neighborsHelper(
								bestNeighbors,
								numNeighbors,
								(T)
								cur
								.getRightChild(
								),
								searchPoint,
								depth + 1);
					}
				}
			} else {
				//follow right path
				this.neighborsHelper(bestNeighbors,
						numNeighbors,
						(T)
						cur.getRightChild(
						),
						searchPoint,
						depth + 1);

				//add current node to best list if applicable
				if (bestNeighbors.size() < numNeighbors) {
					//if list not yet at size, add and sort
					bestNeighbors.add(cur);
					bestNeighbors.sort(dComparator);

				} else {

					if (dComparator.compare(
							bestNeighbors.get(
							numNeighbors
							- 1), cur) > 0) {
						//if last node in sorted list
						//not as
						//close as current node,
						//replace it and sort
						bestNeighbors.remove(
								numNeighbors
								- 1);
						bestNeighbors.add(cur);
						bestNeighbors.sort(dComparator);
					}
				}
				//calculate split coordinate distance between
				//current node and search node
				double dist;

				int dimension = (depth % 3);

				if (dimension == 1) {

					dist = Math.pow(cur.getX()
							- searchPoint.getX(),
							2);

				} else if (dimension == 2) {

					dist = Math.pow(cur.getY()
							- searchPoint.getY(),
							2);

				} else {

					dist = Math.pow(cur.getZ()
							- searchPoint.getZ(),
							2);
				}
				//if overall distance between search point and
				//the worst of my current nearest
				//neighbors is greater
				//than the distance between the search point and
				//current point along the current axis
				//or if I don't
				//currently have a full nearest neighbors list,
				//then I search along other branches
				if (bestNeighbors.size() < numNeighbors) {

					this.neighborsHelper(bestNeighbors,
							numNeighbors,
							(T) cur.getLeftChild(),
							searchPoint,
							depth + 1);

				} else {

					if (dComparator.squareDistance(
							bestNeighbors.get(
									numNeighbors
									- 1),
							searchPoint) > dist) {

						this.neighborsHelper(
								bestNeighbors,
								numNeighbors,
								(T)
								cur
								.getLeftChild(
								),
								searchPoint,
								depth + 1);
					}
				}
			}
		//only left child just go left
		} else if (cur.getLeftChild() != null
				&& cur.getRightChild() == null) {

			this.neighborsHelper(bestNeighbors,
					numNeighbors, (T) cur.getLeftChild(),
					searchPoint, depth + 1);

			//add current node to best list if applicable
			if (bestNeighbors.size() < numNeighbors) {
				//if list not yet at size, add and sort
				bestNeighbors.add(cur);
				bestNeighbors.sort(dComparator);

			} else if (dComparator.compare(bestNeighbors.get(
					numNeighbors - 1), cur) > 0) {
				//if last node in sorted list not as
				//close as current node,
				//replace it and sort
				bestNeighbors.remove(numNeighbors - 1);
				bestNeighbors.add(cur);
				bestNeighbors.sort(dComparator);
			}
		//leaf node
		} else if (cur.getLeftChild() == null
				&& cur.getRightChild() == null) {

			//add current node to best list if applicable
			if (bestNeighbors.size() < numNeighbors) {
				//if list not yet at size, add and sort
				bestNeighbors.add(cur);
				bestNeighbors.sort(dComparator);

			} else if (dComparator.compare(bestNeighbors.get(
					numNeighbors - 1), cur) > 0) {
				//if last node in sorted list not as
				//close as current node,
				//replace it and sort
				bestNeighbors.remove(numNeighbors - 1);
				bestNeighbors.add(cur);
				bestNeighbors.sort(dComparator);
			}
		}
	}
	/**
	 * Stores the list of the nodes found in the
	 * radius search based on the inputs search point
	 * or point named in the list bestRadius.
	 * This function first just checks whether name is
	 * null or not to know if it needs to find the
	 * search point, or if the point was inserted
	 * in the initial command. The method then will
	 * call the function that actually runs the radius
	 * search passing in the new parameters. If the
	 * name is null, this means a point is already
	 * given. If it is not, the name should be of a
	 * point in the kdtree.
	 *
	 * @param bestRadius list where the k nearest neighbors are stored
	 * @param radiusValue number of nearest neighbors to find
	 * @param searchPoint the point the search is based off of, may be null
	 * @param name the name of the point to search off, may be null
	 */
	public void radius(List<T> bestRadius, double radiusValue,
			T searchPoint, String name) {
			//check to make sure radius value is greater than zero
		if (radiusValue <= 0) {
			System.out.println("ERROR: Radius must "
					+ "be greater than zero");
			return;
		}
		//if name is given, find the point to search on
		if (name != null) {

			searchPoint = pointsTableInstance.get(name);
			//if the point can not be found, an invalid
			//name was given
			if (searchPoint == null) {

				System.out.println("ERROR: Name is of a "
						+ "point that does not exist");
				return;
			}
		}
		//call the helper method to run the algorithm
		this.radiusHelper(bestRadius, radiusValue, rootInstance,
				searchPoint, 1);

		DistanceCompare distanceComparator =
				new DistanceCompare(searchPoint);
		bestRadius.sort(distanceComparator);
	}
	/**
	 * Recursively called to find all points that are within
	 * a radius of the search point. Stores the current
	 * best list under bestRadius. The algorithm fundamentally
	 * works by first checking on the current splitting
	 * plane of the tree the difference of the coordinates
	 * on that plane of the search point and the
	 * current node. It then recursively calls itself on
	 * the left or right child based on whether the search
	 * points value was smaller or larger. At each node,
	 * the algorithm just checks if the distance between the
	 * current node and the search point is less than the radius.
	 * Then, the algorithm checks if the other subtree
	 * must be checked. This occurs if the radius is greater
	 * than the distance along the splitting plane between
	 * the current point and the point we are searching for.
	 * If a node is a leaf, then the check to add to the list
	 * is run. If the node has only one child, that path
	 * is always followed, and the normal add to the list code
   * is run.
	 *
	 * @param bestRadius the current list of the radius
	 * search points
	 * @param radiusValue the radius to search on
	 * @param cur the current node
	 * @param searchPoint the point we are searching based on
	 * @param depth the current depth of the tree
	 */
	@SuppressWarnings("unchecked")
	public void radiusHelper(List<T> bestRadius, double radiusValue, T cur,
			T searchPoint, int depth) {

		PointCompare pointComparator = new PointCompare(depth);
		DistanceCompare distanceComparator =
				new DistanceCompare(searchPoint);

		//node with two children
		if (cur.getLeftChild() != null && cur.getRightChild() != null) {

			//compare to pick path to follow
			if (pointComparator.compare(searchPoint, cur) <= 0) {
				//follow left path
				this.radiusHelper(bestRadius, radiusValue,
						(T) cur.getLeftChild(),
						searchPoint, depth + 1);

				//if node is within distance add to list
				if (distanceComparator.squareDistance(
						cur, searchPoint) < Math.pow(
						radiusValue, 2)) {

					bestRadius.add(cur);
				}
				//calculate split coordinate distance between
				//current node and search node
				double dist;

				int dimension = depth % 3;

				if (dimension == 1) {

					dist = Math.pow(cur.getX()
							- searchPoint.getX(),
							2);

				} else if (dimension == 2) {

					dist = Math.pow(cur.getY()
							- searchPoint.getY(),
							2);

				} else {

					dist = Math.pow(cur.getZ()
							- searchPoint.getZ(),
							2);
				}
				//if overall distance between
				//search point and the
				//worst of my current nearest
				//neighbors is greater
				//than the distance between
				//the search point and
				//current point along the current
				//axis or if I don't
				//currently have a full nearest
				//neighbors list,
				//then I search along other branches
				if (radiusValue > dist) {

					this.radiusHelper(bestRadius,
							radiusValue,
							(T) cur.getRightChild(),
							searchPoint, depth + 1);

				}
			} else {
				//follow right path
				this.radiusHelper(bestRadius,
						radiusValue,
						(T) cur.getRightChild(),
						searchPoint, depth + 1);

				//if node is within distance add to list
				if (distanceComparator.squareDistance(
						cur, searchPoint) < Math.pow(
						radiusValue, 2)) {

					bestRadius.add(cur);
				}
				//calculate split coordinate distance between
				//current node and search node
				double dist;

				int dimension = depth % 3;

				if (dimension == 1) {

					dist = Math.pow(cur.getX()
							- searchPoint.getX(),
							2);

				} else if (dimension == 2) {

					dist = Math.pow(cur.getY()
							- searchPoint.getY(),
							2);

				} else {

					dist = Math.pow(cur.getZ()
							- searchPoint.getZ(),
							2);
				}
				//if the radius is greater than
				//the distance along
				//the splitting plane between the current
				//point and the point we are searching for,
				//then we follow the other subtree
				if (radiusValue > dist) {

					this.radiusHelper(bestRadius,
							radiusValue,
							(T) cur.getLeftChild(),
							searchPoint,
							depth + 1);
				}
			}
		//only left child just go left
		} else if (cur.getLeftChild() != null
				&& cur.getRightChild() == null) {

			this.radiusHelper(bestRadius,
					radiusValue,
					(T) cur.getLeftChild(),
					searchPoint,
					depth + 1);

			//if node is within distance add to list
			if (distanceComparator.squareDistance(
					cur, searchPoint) < Math.pow(
					radiusValue, 2)) {

				bestRadius.add(cur);
			}
		//leaf node
		} else if (cur.getLeftChild() == null
				&& cur.getRightChild() == null) {

			//if node is within distance add to list
			if (distanceComparator.squareDistance(
					cur, searchPoint) < Math.pow(
							radiusValue, 2)) {

				bestRadius.add(cur);
			}
		}
	}
}
