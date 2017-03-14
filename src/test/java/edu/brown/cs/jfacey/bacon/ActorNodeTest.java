package edu.brown.cs.jfacey.bacon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.jfacey.database.GraphDatabase;
import edu.brown.cs.jfacey.datastructs.GraphNode;

/**
 * This is a class to test the ActorNode class which is an implementation of the
 * GraphNode class. It only needs to test the override of the toString method,
 * as well as the generate children method. The other methods are all accessor
 * methods.
 *
 * @author jfacey
 *
 */
public class ActorNodeTest {

  @Test
  public void testToString() {
    // fix setting new database thing
    GraphNode<ActorNode, MovieEdge> node = new ActorNode("/a/123",
        "John Travolta", new ArrayList<String>(), null);
    assertEquals(node.toString(),
        "ID: /a/123, Actor: John Travolta, Movies: ");

    List<String> movies = new ArrayList<String>();
    movies.add("movie1");

    node = new ActorNode("/a/122", "John Travolt", movies, null);
    assertEquals(node.toString(),
        "ID: /a/122, Actor: John Travolt, Movies: movie1");

    movies.add("movie2");
    node = new ActorNode("/a/122", "John Travolt", movies, null);
    assertEquals(node.toString(),
        "ID: /a/122, Actor: John Travolt, Movies: movie1, movie2");
  }

  @Test
  public void testGenerateChildrenSmall() {

    GraphDatabase<ActorNode, MovieEdge> database = new SQLDatabase();
    database.setDatabase("data/bacon/smallBacon.sqlite3");
    // test for a known edge
    List<String> moviesList = new ArrayList<>();
    moviesList.add("Primary Colors");
    moviesList.add("Pulp Fiction");

    GraphNode<ActorNode, MovieEdge> node = new ActorNode("/m/0f502",
        "John Travolta", moviesList, database);
    node.generateChildren();

    List<MovieEdge> edges = node.getChildren();
    assertEquals(edges.size(), 1);
    assertEquals(edges.get(0).getCost(), 0.5, .001);
    assertEquals(edges.get(0).getId(), "Primary Colors");
    assertEquals(edges.get(0).getGraphNode().getActor(), "Tony Shalhoub");
    // test for no edges with an actor that has multiple movies with other
    // actors
    // but no valid edge
    moviesList = new ArrayList<>();
    moviesList.add("Iron Man");

    node = new ActorNode("/m/0flw6", "Jeff Bridges", moviesList, database);
    node.generateChildren();

    edges = node.getChildren();
    assertEquals(edges.size(), 0);

    moviesList = new ArrayList<>();
    // test with three names actor and no movies
    node = new ActorNode("/m/0gm18k2", "Susan Shalhoub Larkin",
        moviesList, database);
    node.generateChildren();

    edges = node.getChildren();
    assertEquals(edges.size(), 0);
  }

  @Test
  public void testEquals() {
    GraphNode<ActorNode, MovieEdge> node = new ActorNode("/m/02661h",
        "Tony Shalhoub", null, null);
    GraphNode<ActorNode, MovieEdge> node1 = new ActorNode("/m/02661h",
        "Tony Shalh", null, null);

    assertEquals(node, node1);

    node1 = new ActorNode("/m/02661", "Tony Shalhoub", null, null);
    assertFalse(node.equals(node1));
  }
}
