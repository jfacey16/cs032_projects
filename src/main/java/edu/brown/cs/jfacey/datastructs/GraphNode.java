package edu.brown.cs.jfacey.datastructs;

import java.util.List;

/**
 * This is my implementation of a graph node. It has methods to return an id
 * associated with it, which is unique, return its children, and generate its
 * children in some way.
 *
 * @author jfacey
 *
 * @param <V>
 *          The type of node being stored, which should be itself
 * @param <E>
 *          The type of edge being stored
 */
public interface GraphNode<V extends GraphNode<V, E>,
    E extends GraphEdge<V, E>> {
  /**
   * This method will differ per implementation, but will generate children for
   * the node. It will return a list of graph nodes to add, even though children
   * are stored in each node as edges. This is for ease of adding to the graph.
   * In this way, the nodes edges will be set, and the node will know that it is
   * set.
   *
   * @return boolean of whether children were successfully generated or not.
   */
  boolean generateChildren();

  /**
   * This method returns the list of children generated in the generateChildren
   * method.
   *
   * @return the list of edges associated with this node
   */
  List<E> getChildren();

  /**
   * This method returns the edge for the input end node, if it exists. Null is
   * returned if it doesn't.
   *
   * @param endNode
   *          the end node associated with an edge
   *
   * @return the list of edges
   */
  E getEdge(V endNode);

  /**
   * This returns the unique id of the node.
   *
   * @return the id value
   */
  String getID();

  /**
   * This returns a boolean value on whether the children have been generated
   * for this node yet.
   *
   * @return the boolean of children generated
   */
  boolean isChildren();

}
