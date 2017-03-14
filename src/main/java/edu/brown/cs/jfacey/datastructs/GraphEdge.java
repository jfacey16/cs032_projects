package edu.brown.cs.jfacey.datastructs;

/**
 * This interface defines a graph edge, which is used in my implementation of a
 * graph. It stores the end node, as this is a directed graph, and the weight or
 * cost of the path.
 *
 * @author jfacey
 *
 * @param <V>
 *          The type of node being stored
 * @param <E>
 *          The type of edge being used, which should be itself
 */
public interface GraphEdge<V extends GraphNode<V, E>,
    E extends GraphEdge<V, E>> {
  /**
   * This is a getter method to return the end node.
   *
   * @return the end graph node
   */
  V getGraphNode();

  /**
   * This is a getter method to return the cost of the edge.
   *
   * @return the cost
   */
  double getCost();

  /**
   * This is a getter method to associate a Id with the edge, such as what it
   * would be called, as opposed to its cost.
   *
   * @return the value of the edge
   */
  String getId();
}
