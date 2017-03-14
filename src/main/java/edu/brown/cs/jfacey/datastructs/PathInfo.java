package edu.brown.cs.jfacey.datastructs;

import java.util.Objects;

/**
 * This class represents path info, which is effectively a wrapper of a node
 * class used for djikstras. It contatins the current node, the previous nodes
 * path info, and the cost so far on the path to get to this node.
 *
 * @author jfacey
 *
 * @param <V>
 *          The type of graphnode being used
 * @param <E>
 *          The type of graphedge being used
 */
public class PathInfo<V extends GraphNode<V, E>, E extends GraphEdge<V, E>> {

  private V iNode;
  private PathInfo<V, E> iPrevious;
  private double iCost;
  private String iMovie;

  /**
   * This constructor just initially sets the node value, previous value, and
   * cost value.
   *
   * @param node
   *          the current node
   * @param previous
   *          the previous nodes path info
   * @param cost
   *          the cost
   * @param movie
   *          the movie associated with this path info
   */
  public PathInfo(V node, PathInfo<V, E> previous, double cost,
      String movie) {
    iNode = node;
    iPrevious = previous;
    iCost = cost;
    iMovie = movie;
  }

  /**
   * This is a mutator method for the cost value.
   *
   * @param cost
   *          the new cost value to set
   */
  public void setCost(double cost) {
    iCost = cost;
  }

  /**
   *
   * This is an accessor method for the node value.
   *
   * @return the node
   */
  public V getNode() {
    return iNode;
  }

  /**
   *
   * This is an accessor method for the previous value.
   *
   * @return the previous path info
   */
  public PathInfo<V, E> getPrevious() {
    return iPrevious;
  }

  /**
   *
   * This is an accessor method for the cost.
   *
   * @return the cost
   */
  public double getCost() {
    return iCost;
  }

  /**
   *
   * This is an accessor method for the movie value.
   *
   * @return the movie value
   */
  public String getMovie() {
    return iMovie;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(Object object) {
    return iNode.equals(((PathInfo<V, E>) object).getNode());
  }

  @Override
  public int hashCode() {
    return Objects.hash(iNode);
  }
}
