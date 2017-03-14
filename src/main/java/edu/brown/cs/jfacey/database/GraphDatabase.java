package edu.brown.cs.jfacey.database;

import java.util.List;
import java.util.Map;

import edu.brown.cs.jfacey.datastructs.GraphEdge;
import edu.brown.cs.jfacey.datastructs.GraphNode;

/**
 * This class essentially implements a unit to store a database, and do all
 * commands necessary on the database. This inlcudes setting the databasse,
 * returning if it exists or not, generating a nodes children, and doing set up
 * for a connection based on two strings passed in.
 *
 * @author jfacey
 *
 * @param <V>
 *          the type of graph node used
 * @param <E>
 *          the type of graph edge used
 */
public interface GraphDatabase<V extends GraphNode<V, E>,
    E extends GraphEdge<V, E>> {

  /**
   * This sets up the database for use, based on a string to the filepath passed
   * in.
   *
   * @param dbPath
   *          filepath string
   */
  void setDatabase(String dbPath);

  /**
   *
   * This returns a boolean representing if a database has been set yet.
   *
   * @return whether a database has been set or not
   */
  boolean isDatabase();

  /**
   *
   * This generates the children for a node based on the passed in values of
   * strings, to use the database and queries. The edges generated are stored in
   * the passed in list. A boolean is returned based on the success of the
   * generation.
   *
   * @param queryStrings
   *          the list of strings used in the query for generating each child
   * @param edges
   *          the edges list to add children too
   * @param valueString
   *          the string that holds a value also used for querying
   * @return a boolean of if it was successful or not
   */
  boolean generateChildren(List<String> queryStrings, List<E> edges,
      String valueString);

  /**
   *
   * This sets up for getting the connect method ready to work. It will take in
   * a graph and two strings representing values of vertexs to search on, and it
   * will return the actual set vertexs, if they exist in the graph or the
   * database.
   *
   * @param one
   *          the first value of vertex
   * @param two
   *          the second value of vertex
   * @param graph
   *          the graph that has already been generated
   * @return the list, which should be length two, of the vertexs to connect
   */
  List<V> setUpConnect(String one, String two, Map<String, V> graph);
}
