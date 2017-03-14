package edu.brown.cs.jfacey.algorithms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import edu.brown.cs.jfacey.datastructs.GraphEdge;
import edu.brown.cs.jfacey.datastructs.GraphNode;
import edu.brown.cs.jfacey.datastructs.PathInfo;

/**
 *
 * This class holds an implementation of djikstras for a dynamic graph. This
 * means nodes may not have their edges generated yet, and could link to old
 * nodes or completely new ones. This implementation can work for any type of
 * GraphNode or GraphEdge. It utilizes a priority queue which updates to hold
 * all paths to nodes using a class called path info, which stores nodes, costs,
 * and previous path infos to previous nodes. While the priority queue is full,
 * it pops off a node, generates its children if necessary, updates new path
 * infos based on the costs, and adds them to the queue. This then loops back
 * and does this again on the highest priority node, ordered by lowest cost.
 *
 * @author jfacey
 *
 * @param <V>
 *          the type of graph node used
 * @param <E>
 *          the type of graph edge used
 */
public class DynamicDjikstra<V extends GraphNode<V, E>,
    E extends GraphEdge<V, E>> {

  /**
   * This is an implementation of djikstras algorithm.
   *
   * @param start
   *          the start node
   * @param end
   *          the end node
   * @return the list of nodes representing a path
   */
  public PathInfo<V, E> djikstras(V start, V end) {
    PathInfoComparator pathInfoCompare = new PathInfoComparator();
    // current graph and all nodes storage
    Map<PathInfo<V, E>, Double> allNodes = new HashMap<>();
    Set<PathInfo<V, E>> exploredNodes = new HashSet<>();
    Queue<PathInfo<V, E>> currentGraph = new PriorityQueue<>(
        pathInfoCompare);
    // info for the start node
    PathInfo<V, E> startInfo = new PathInfo<>(start, null, 0, "");
    // add only start node initially
    currentGraph.add(startInfo);
    allNodes.put(startInfo, new Double(startInfo.getCost()));
    // while graph is not empty
    int k = 0;
    while (currentGraph.size() != 0) {
      // remove top priority node which is current lowest cost
      PathInfo<V, E> currentNode = currentGraph.remove();
      // if this node is end return its info
      if (currentNode.getNode().equals(end)) {
        return currentNode;
      }
      exploredNodes.add(currentNode);
      // if children not generated yet, generate them
      if (!currentNode.getNode().isChildren()) {

        currentNode.getNode().generateChildren();
      }
      // get the children, represented as outgoing edges
      List<E> children = currentNode.getNode().getChildren();

      for (int i = 0; i < children.size(); i++) {
        // generate path info for each children
        PathInfo<V, E> info = new PathInfo<>(children.get(i)
            .getGraphNode(), currentNode, currentNode.getCost()
            + children.get(i).getCost(), children.get(i).getId());
        // add children to graph, while checking to see if a new cost needs to
        // be updated
        if (!exploredNodes.contains(info)) {
          if (!currentGraph.contains(info)) {
            currentGraph.add(info);
            allNodes.put(info, info.getCost());
          } else if (currentGraph.contains(info)) {

            if (allNodes.containsKey(info)) {

              if (allNodes.get(info) > info.getCost()) {
                allNodes.remove(info);
                currentGraph.remove(info);
                allNodes.put(info, info.getCost());
                currentGraph.add(info);
              }
            }
          }
        }
      }
    }
    return null;
  }

  /**
   * This class implements a comparator for the path info class. It works by
   * comparing the costs.
   *
   * @author jfacey
   *
   */
  private class PathInfoComparator implements Comparator<PathInfo<V, E>> {

    @Override
    public int compare(PathInfo<V, E> o1, PathInfo<V, E> o2) {
      return Double.compare(o1.getCost(), o2.getCost());
    }
  }
}
