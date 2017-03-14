package edu.brown.cs.jfacey.bacon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.brown.cs.jfacey.database.GraphDatabase;
import edu.brown.cs.jfacey.datastructs.GraphNode;

/**
 * This class implements the graph node interface. Its stores an actor's id as
 * the main id, as well as the actor name and movies associated with this actor.
 * Its generate children method involves pulling all other actors from the
 * database who are in one of the movies with this actor, and also the same
 * first letter in their first name as this nodes first letter in the last name.
 * The id of this node is used for equality, so a graphs nodes must have unique
 * ids. It also holds a reference to the database for generating new children
 * from.
 *
 * @author jfacey
 *
 */
public class ActorNode implements GraphNode<ActorNode, MovieEdge> {

  private List<MovieEdge> iEdges;
  private String iID;
  private String iActor;
  private List<String> iMovies;
  private boolean iChildrenGenerated;
  private GraphDatabase<ActorNode, MovieEdge> iDatabase;

  /**
   * This constructor instantiates a blank edge list, and sets the initial id,
   * name, movies, and database values.
   *
   * @param id
   *          the id of the node
   * @param actorName
   *          the actor name at this node
   * @param movies
   *          the movies this actor is in
   * @param database
   *          the database for generating the graph
   */
  public ActorNode(String id, String actorName, List<String> movies,
      GraphDatabase<ActorNode, MovieEdge> database) {
    iEdges = new ArrayList<>();
    iID = id;
    iActor = actorName;
    iMovies = movies;
    iChildrenGenerated = false;
    iDatabase = database;
  }

  @Override
  public String toString() {
    String returnString = "ID: " + iID + ", Actor: " + iActor
        + ", Movies: ";
    if (iMovies.size() >= 2) {
      for (int i = 0; i < iMovies.size() - 1; i++) {
        returnString = returnString.concat(iMovies.get(i) + ", ");
      }
      returnString = returnString.concat(iMovies.get(iMovies.size() - 1));
    } else if (iMovies.size() == 1) {
      returnString = returnString.concat(iMovies.get(0));
    }
    return returnString;
  }

  @Override
  public boolean generateChildren() {
    return iDatabase.generateChildren(iMovies, iEdges, iActor);
  }

  @Override
  public boolean isChildren() {
    return iChildrenGenerated;
  }

  @Override
  public List<MovieEdge> getChildren() {
    return iEdges;
  }

  @Override
  public MovieEdge getEdge(ActorNode endNode) {
    for (int i = 0; i < iEdges.size(); i++) {
      if (iEdges.get(i).getGraphNode().equals(endNode)) {
        return iEdges.get(i);
      }
    }
    return null;
  }

  @Override
  public String getID() {
    return iID;
  }

  /**
   * This getter method returns the actor name.
   *
   * @return the actor name
   */
  public String getActor() {
    return iActor;
  }

  /**
   * This getter method returns the movies for this actor.
   *
   * @return the movies
   */
  public List<String> getMovies() {
    return iMovies;
  }

  @Override
  public boolean equals(Object object) {
    return iID.equals(((ActorNode) object).getID());
  }

  @Override
  public int hashCode() {
    return Objects.hash(iID);
  }
}
