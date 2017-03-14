package edu.brown.cs.jfacey.bacon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.brown.cs.jfacey.database.GraphDatabase;

/**
 *
 * This class tests all the methods of a sql database, including setting the
 * database, generating children for nodes, and setting up connections for
 * paths. This is done on big and small database.
 *
 * @author jfacey
 *
 */
public class SQLDatabaseTest {

  @Test
  public void testSetDatabaseSmall() {

    GraphDatabase<ActorNode, MovieEdge> database = new SQLDatabase();
    database.setDatabase("data/bacon/smallBacon.sqlite3");
    assertTrue(database.isDatabase());
  }

  @Test
  public void testGenerateChildrenSmall() {

    GraphDatabase<ActorNode, MovieEdge> database = new SQLDatabase();
    database.setDatabase("data/bacon/smallBacon.sqlite3");
    // test for a known edge
    List<MovieEdge> edges = new ArrayList<>();
    List<String> moviesList = new ArrayList<>();
    moviesList.add("Primary Colors");
    moviesList.add("Pulp Fiction");

    database.generateChildren(moviesList, edges, "John Travolta");

    assertEquals(edges.size(), 1);
    assertEquals(edges.get(0).getCost(), 0.5, .001);
    assertEquals(edges.get(0).getId(), "Primary Colors");
    assertEquals(edges.get(0).getGraphNode().getActor(), "Tony Shalhoub");
    // test for no edges with an actor that has multiple movies with other
    // actors
    // but no valid edge
    edges = new ArrayList<>();
    moviesList = new ArrayList<>();
    moviesList.add("Iron Man");

    database.generateChildren(moviesList, edges, "Jeff Bridges");
    assertEquals(edges.size(), 0);
    // test with three names actor and no movies
    edges = new ArrayList<>();
    moviesList = new ArrayList<>();
    database.generateChildren(moviesList, edges, "Susan Shalhoub Larkin");
    assertEquals(edges.size(), 0);
  }

  @Test
  public void testSetUpConnectSmall() {
    GraphDatabase<ActorNode, MovieEdge> database = new SQLDatabase();
    database.setDatabase("data/bacon/smallBacon.sqlite3");
    Map<String, ActorNode> graph = new HashMap<>();
    // two valid actors
    List<ActorNode> nodes = database.setUpConnect("John Travolta",
        "Tony Shalhoub", graph);

    assertEquals(nodes.size(), 2);
    assertEquals(nodes.get(0).getActor(), "John Travolta");
    assertEquals(nodes.get(1).getActor(), "Tony Shalhoub");
    assertEquals(nodes.get(0).getID(), "/m/0f502");
    assertEquals(nodes.get(1).getID(), "/m/02661h");
    assertEquals(nodes.get(0).getMovies().get(0), "Pulp Fiction");
    assertEquals(nodes.get(0).getMovies().get(1), "Primary Colors");
    assertEquals(nodes.get(1).getMovies().get(0), "Men in Black");
    assertEquals(nodes.get(1).getMovies().get(1), "Primary Colors");
    assertEquals(graph.size(), 2);

    // one invalid actor
    nodes = database.setUpConnect("John Travolta", "", graph);
    assertEquals(nodes.size(), 0);
  }
}
