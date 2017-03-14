package edu.brown.cs.jfacey.bacon;

import edu.brown.cs.jfacey.datastructs.GraphEdge;

/**
 * This class implements the graph edge interface. The link between actor nodes
 * is by movie and last to first initial, so a movie is used as the
 * representation. The edge weight is based on the reciprocal of the amount of
 * people in the movie.
 *
 * @author jfacey
 *
 */
public class MovieEdge implements GraphEdge<ActorNode, MovieEdge> {

  private final ActorNode iActorNode;
  private final double iCost;
  private final String iMovie;

  /**
   * This constructor sets the end node, weight, and movie associated with the
   * node.
   *
   * @param graphNode
   *          the end node
   * @param c
   *          the cost
   * @param movie
   *          the movie
   */
  public MovieEdge(ActorNode graphNode, double c, String movie) {
    iActorNode = graphNode;
    iCost = c;
    iMovie = movie;
  }

  @Override
  public ActorNode getGraphNode() {
    return iActorNode;
  }

  @Override
  public double getCost() {
    return iCost;
  }

  @Override
  public String getId() {
    return iMovie;
  }

  @Override
  public String toString() {
    return "ID: " + iActorNode.getID() + ", Actor: "
        + iActorNode.getActor() + ", Cost: " + iCost + ", Movie: "
        + iMovie;

  }
}
