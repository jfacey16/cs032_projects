package edu.brown.cs.jfacey.datastructs;

import java.util.HashMap;
import java.util.List;

import edu.brown.cs.jfacey.compare.DistanceCompare;
import edu.brown.cs.jfacey.compare.PointCompare;

/**
 * This class defines an implementation of a kdtree data structure that can take
 * in kdpoints.
 *
 * @author jfacey
 *
 * @param <T>
 *          The type to be stored in the tree
 */
public class Kdtree<T extends Kdpoint> {

  private T iRoot;
  private HashMap<String, T> iPointsTable;

  /**
   * This is a constructor that builds the kdtree out of a list of points passed
   * in. The points are also added to hash table for easy retrieval when a node
   * must be accessed by name.
   *
   * @param points
   *          The list of points used to build the tree
   */
  public Kdtree(List<T> points) {
    // instantiate hashmap for storing points by name
    iPointsTable = new HashMap<>();
    iRoot = buildTree(points, 1);
  }

  /**
   * This is a blank constructor for the kdtree used for initialization when a
   * list of points is not yet given.
   */
  public Kdtree() {
    // instantiate hashmap for storing points by name
    iPointsTable = new HashMap<>();
    iRoot = null;
  }

  /**
   * Returns and sets the children of the current node being added to the
   * kdtree. This method takes in a sublist of points being added to this branch
   * of the tree. It chooses the point to add by sorting the list on the current
   * plane and choosing the median. The left and right children of the node are
   * then set by calling the method recursively on the sub lists split at the
   * median point.
   *
   * @param points
   *          A sublist of all points that are being added to this branch of the
   *          kd tree
   * @param depth
   *          The current depth of the tree at this point in order to split on
   *          the correct axis
   * @return The current node that got added to the tree
   */
  public T buildTree(List<T> points, int depth) {
    // sort points by splitting axis and select
    // median for node
    points.sort(new PointCompare(depth));

    T node = points.get(points.size() / 2);
    // add point to list
    iPointsTable.put(node.getName(), node);
    // call build tree recursively to set children
    // based on size of list
    if (points.size() > 2) {

      node.setLeftChild(buildTree(points.subList(0, points.size() / 2),
          depth + 1));
      node.setRightChild(buildTree(
          points.subList(points.size() / 2 + 1, points.size()), depth + 1));

    } else if (points.size() == 2) {

      node.setLeftChild(buildTree(points.subList(0, points.size() / 2),
          depth + 1));
    }

    return node;
  }

  /**
   * This is a getter method for the root node.
   *
   * @return the root value for the kdtree
   */
  public T getRoot() {
    return iRoot;
  }

  /**
   * This is a setter method for the root node.
   *
   * @param root
   *          the new root value for the kdtree
   */
  public void setRoot(T root) {
    iRoot = root;
  }

  /**
   * Stores the list of the k nearest neighbors to the inputs search point or
   * point named in the list bestNeighbors. This function first just checks
   * whether name is null or not to know if it needs to find the search point,
   * or if the point was inserted in the initial command. The method then will
   * call the function that actually finds the k nearest neighbors passing in
   * the new parameters. If the name is null, this means a point is already
   * given. If it is not, the name should be of a point in the kdtree.
   *
   * @param bestNeighbors
   *          list where the k nearest neighbors are stored
   * @param numNeighbors
   *          number of nearest neighbors to find
   * @param searchPoint
   *          the point the search is based off of, may be null
   * @param name
   *          the name of the point to search off, may be null
   */
  public void neighbors(List<T> bestNeighbors, int numNeighbors,
      T searchPoint, String name) {
    // check to make sure number of neighbors to search on
    // is greater than zero
    if (numNeighbors < 0) {
      System.out.println("ERROR: Number of neighbors " + "to find must "
          + "be greater than zero");
      return;
    } else if (numNeighbors == 0) {
      return;
    }
    // set initial value for if name search or not
    boolean isName = false;
    // if the name is given, find the point to search on
    if (name != null) {
      // set boolean true for name search
      isName = true;

      searchPoint = iPointsTable.get(name);
      // if the point can not be found, an invalid
      // name was given
      if (searchPoint == null) {

        System.out.println("ERROR: Name is of a point " + "that "
            + "does not exist");
        return;
      }
    }
    // call the helper method to run the algorithm
    this.neighborsHelper(bestNeighbors, numNeighbors, iRoot, searchPoint,
        1, isName);
  }

  /**
   * Recursively called to find k nearest neighbors to the search point. Stores
   * the current best list under bestNeighbors. The algorithm fundamentally
   * works by first checking on the current splitting plane of the tree the
   * difference of the coordinates on that plane of the search point and the
   * current node. It then recursively calls itself on the left or right child
   * based on whether the search points value was smaller or larger. At each
   * node, if the bestNeighbors list is not yet full, the node gets added
   * automatically. If it is, distance from the worst node in the list to the
   * search point is compared to the distance for the current node from the
   * search point. If it is closer, it gets added to the list. Then, the
   * algorithm checks if the other subtree must be checked. This occurs if the
   * overall distance between search point and the worst of my current nearest
   * neighbors is greater than the distance between the search point and current
   * point along the current axis or if I don't currently have a full nearest
   * neighbors list. If a node is a leaf, then the check to add to the list is
   * run. If the node has only one child, that path is always followed, and the
   * normal add to the list code is run.
   *
   * @param bestNeighbors
   *          the current list of nearest neighbors
   * @param numNeighbors
   *          the number of nearest neighbors to find
   * @param cur
   *          the current node
   * @param searchPoint
   *          the point we are searching based on
   * @param depth
   *          the current depth of the tree
   * @param isName
   *          name search or not
   */
  @SuppressWarnings("unchecked")
  public void neighborsHelper(List<T> bestNeighbors, int numNeighbors,
      T cur, T searchPoint, int depth, boolean isName) {
    // instantiate comparators
    PointCompare pointComparator = new PointCompare(depth);
    DistanceCompare dComparator = new DistanceCompare(searchPoint);

    // node with two children
    if (cur.getLeftChild() != null && cur.getRightChild() != null) {

      // compare to pick path to follow
      if (pointComparator.compare(searchPoint, cur) <= 0) {
        // follow left path
        this.neighborsHelper(bestNeighbors, numNeighbors,
            (T) cur.getLeftChild(), searchPoint, depth + 1, isName);

        // call check and add on current node
        this.neighborsCheckAndAdd(bestNeighbors, numNeighbors, cur,
            searchPoint, depth, isName, dComparator);
        // calculate split coordinate distance between
        // current node and search node
        double dist = this.calcSplit(depth, cur, searchPoint);
        // if there are points on the other side of the hyper plane or if I
        // don't currently have a full nearest neighbors list, then I search
        // along other branches
        if (bestNeighbors.size() < numNeighbors) {

          this.neighborsHelper(bestNeighbors, numNeighbors,
              (T) cur.getRightChild(), searchPoint, depth + 1, isName);

        } else {

          if (dComparator.squareDistance(
              bestNeighbors.get(numNeighbors - 1), searchPoint) > dist) {

            this.neighborsHelper(bestNeighbors, numNeighbors,
                (T) cur.getRightChild(), searchPoint, depth + 1, isName);
          }
        }
      } else {
        // follow right path
        this.neighborsHelper(bestNeighbors, numNeighbors,
            (T) cur.getRightChild(), searchPoint, depth + 1, isName);

        this.neighborsCheckAndAdd(bestNeighbors, numNeighbors, cur,
            searchPoint, depth, isName, dComparator);
        // calculate split coordinate distance between
        // current node and search node
        double dist = this.calcSplit(depth, cur, searchPoint);
        // if there are points on the other side of the hyper plane or if I
        // don't currently have a full nearest neighbors list, then I search
        // along other branches
        if (bestNeighbors.size() < numNeighbors) {

          this.neighborsHelper(bestNeighbors, numNeighbors,
              (T) cur.getLeftChild(), searchPoint, depth + 1, isName);

        } else {

          if (dComparator.squareDistance(
              bestNeighbors.get(numNeighbors - 1), searchPoint) > dist) {

            this.neighborsHelper(bestNeighbors, numNeighbors,
                (T) cur.getLeftChild(), searchPoint, depth + 1, isName);
          }
        }
      }
      // only left child just go left
    } else if (cur.getLeftChild() != null && cur.getRightChild() == null) {
      // call search recursively
      this.neighborsHelper(bestNeighbors, numNeighbors,
          (T) cur.getLeftChild(), searchPoint, depth + 1, isName);
      // call check and add on current node
      this.neighborsCheckAndAdd(bestNeighbors, numNeighbors, cur,
          searchPoint, depth, isName, dComparator);
      // leaf node
    } else if (cur.getLeftChild() == null && cur.getRightChild() == null) {
      // call check and add on current node
      this.neighborsCheckAndAdd(bestNeighbors, numNeighbors, cur,
          searchPoint, depth, isName, dComparator);
    }
  }

  /**
   * This helper method checks if a node should be added to the list, and does
   * so if it should be. This list is also sorted for the next use.
   *
   * @param bestNeighbors
   *          the current list of nearest neighbors
   * @param numNeighbors
   *          the number of nearest neighbors to find
   * @param cur
   *          the current node
   * @param searchPoint
   *          the point we are searching based on
   * @param depth
   *          the current depth of the tree
   * @param isName
   *          name search or not
   * @param dComparator
   *          the distance comparator
   */
  public void neighborsCheckAndAdd(List<T> bestNeighbors,
      int numNeighbors, T cur, T searchPoint, int depth, boolean isName,
      DistanceCompare dComparator) {
    // if current point is the search point and this is a name search, don't add
    // the point
    if (cur.getX() == searchPoint.getX()
        && cur.getY() == searchPoint.getY()
        && cur.getZ() == searchPoint.getZ() && isName) {
      return;
    }
    // add current node to best list if applicable
    if (bestNeighbors.size() < numNeighbors) {
      // if list not yet at size, add and sort
      bestNeighbors.add(cur);
      bestNeighbors.sort(dComparator);

    } else if (dComparator.compare(bestNeighbors.get(numNeighbors - 1),
        cur) > 0) {
      // if last node in sorted list not as
      // close as current node,
      // replace it and sort
      bestNeighbors.remove(numNeighbors - 1);
      bestNeighbors.add(cur);
      bestNeighbors.sort(dComparator);
    }
  }

  /**
   * This method just calculates the square distance between the current node's
   * and the search nodes's split coordinate.
   *
   * @param depth
   *          current depth
   * @param cur
   *          current node
   * @param searchPoint
   *          search node
   * @return double representing square distance between points on split
   *         coordinate
   */
  public double calcSplit(int depth, T cur, T searchPoint) {

    int dimension = (depth % 3);

    if (dimension == 1) {

      return Math.pow(cur.getX() - searchPoint.getX(), 2);

    } else if (dimension == 2) {

      return Math.pow(cur.getY() - searchPoint.getY(), 2);

    } else {

      return Math.pow(cur.getZ() - searchPoint.getZ(), 2);
    }
  }

  /**
   * Stores the list of the nodes found in the radius search based on the inputs
   * search point or point named in the list bestRadius. This function first
   * just checks whether name is null or not to know if it needs to find the
   * search point, or if the point was inserted in the initial command. The
   * method then will call the function that actually runs the radius search
   * passing in the new parameters. If the name is null, this means a point is
   * already given. If it is not, the name should be of a point in the kdtree.
   *
   * @param bestRadius
   *          list where the k nearest neighbors are stored
   * @param radiusValue
   *          number of nearest neighbors to find
   * @param searchPoint
   *          the point the search is based off of, may be null
   * @param name
   *          the name of the point to search off, may be null
   */
  public void radius(List<T> bestRadius, double radiusValue,
      T searchPoint, String name) {
    // check to make sure radius value is greater than zero
    if (radiusValue < 0) {
      System.out.println("ERROR: Radius must " + "be greater than zero");
      return;
    } else if (radiusValue == 0) {
      return;
    }
    // set initial value for if name search or not
    boolean isName = false;
    // if name is given, find the point to search on
    if (name != null) {
      // set boolean true for name search
      isName = true;

      searchPoint = iPointsTable.get(name);
      // if the point can not be found, an invalid
      // name was given
      if (searchPoint == null) {

        System.out.println("ERROR: Name is of a "
            + "point that does not exist");
        return;
      }
    }
    // call the helper method to run the algorithm
    this.radiusHelper(bestRadius, radiusValue, iRoot, searchPoint, 1,
        isName);
    // re-sort when final list is given
    DistanceCompare distanceComparator = new DistanceCompare(searchPoint);
    bestRadius.sort(distanceComparator);
  }

  /**
   * Recursively called to find all points that are within a radius of the
   * search point. Stores the current best list under bestRadius. The algorithm
   * fundamentally works by first checking on the current splitting plane of the
   * tree the difference of the coordinates on that plane of the search point
   * and the current node. It then recursively calls itself on the left or right
   * child based on whether the search points value was smaller or larger. At
   * each node, the algorithm just checks if the distance between the current
   * node and the search point is less than the radius. Then, the algorithm
   * checks if the other subtree must be checked. This occurs if the radius is
   * greater than the distance along the splitting plane between the current
   * point and the point we are searching for. If a node is a leaf, then the
   * check to add to the list is run. If the node has only one child, that path
   * is always followed, and the normal add to the list code is run.
   *
   * @param bestRadius
   *          the current list of the radius search points
   * @param radiusValue
   *          the radius to search on
   * @param cur
   *          the current node
   * @param searchPoint
   *          the point we are searching based on
   * @param depth
   *          the current depth of the tree
   * @param isName
   *          the isName value for this search
   */
  @SuppressWarnings("unchecked")
  public void radiusHelper(List<T> bestRadius, double radiusValue, T cur,
      T searchPoint, int depth, boolean isName) {

    PointCompare pointComparator = new PointCompare(depth);
    DistanceCompare distanceComparator = new DistanceCompare(searchPoint);

    // node with two children
    if (cur.getLeftChild() != null && cur.getRightChild() != null) {

      // compare to pick path to follow
      if (pointComparator.compare(searchPoint, cur) <= 0) {
        // follow left path
        this.radiusHelper(bestRadius, radiusValue, (T) cur.getLeftChild(),
            searchPoint, depth + 1, isName);
        // check and add node
        this.radiusCheckAndAdd(bestRadius, radiusValue, cur, searchPoint,
            isName, distanceComparator);
        // calculate split coordinate distance between
        // current node and search node
        double dist = this.calcSplit(depth, cur, searchPoint);
        // if overall distance between search point and the worst of my current
        // nearest neighbors is greater than the distance between the search
        // point and current point along the current axis or if I don't
        // currently have a full nearest neighbors list, then I search along
        // other branches
        if (radiusValue > dist) {

          this.radiusHelper(bestRadius, radiusValue,
              (T) cur.getRightChild(), searchPoint, depth + 1, isName);

        }
      } else {
        // follow right path
        this.radiusHelper(bestRadius, radiusValue,
            (T) cur.getRightChild(), searchPoint, depth + 1, isName);

        // check and add node
        this.radiusCheckAndAdd(bestRadius, radiusValue, cur, searchPoint,
            isName, distanceComparator);
        // calculate split coordinate distance between
        // current node and search node
        double dist = this.calcSplit(depth, cur, searchPoint);
        // if the radius is greater than
        // the distance along
        // the splitting plane between the current
        // point and the point we are searching for,
        // then we follow the other subtree
        if (radiusValue > dist) {

          this.radiusHelper(bestRadius, radiusValue,
              (T) cur.getLeftChild(), searchPoint, depth + 1, isName);
        }
      }
      // only left child just go left
    } else if (cur.getLeftChild() != null && cur.getRightChild() == null) {

      this.radiusHelper(bestRadius, radiusValue, (T) cur.getLeftChild(),
          searchPoint, depth + 1, isName);

      // check and add node
      this.radiusCheckAndAdd(bestRadius, radiusValue, cur, searchPoint,
          isName, distanceComparator);
      // leaf node
    } else if (cur.getLeftChild() == null && cur.getRightChild() == null) {
      // check and add node
      this.radiusCheckAndAdd(bestRadius, radiusValue, cur, searchPoint,
          isName, distanceComparator);
    }
  }

  /**
   * This helper method checks if a node should be added to the list, and does
   * so if it should be.
   *
   * @param bestRadius
   *          the current list of best nodes found
   * @param radiusValue
   *          the radius searching on
   * @param cur
   *          the current node
   * @param searchPoint
   *          the point we are searching based on
   * @param isName
   *          name search or not
   * @param distanceComparator
   *          the distance comparator
   */
  public void radiusCheckAndAdd(List<T> bestRadius, double radiusValue,
      T cur, T searchPoint, boolean isName,
      DistanceCompare distanceComparator) {

    // if current point is the search point and this is a name search, don't add
    // the point
    if (cur.getX() == searchPoint.getX()
        && cur.getY() == searchPoint.getY()
        && cur.getZ() == searchPoint.getZ() && isName) {
      return;
    }

    // if node is within distance add to list
    if (distanceComparator.squareDistance(cur, searchPoint) < Math.pow(
        radiusValue, 2)) {

      bestRadius.add(cur);
    }
  }
}
