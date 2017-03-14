package edu.brown.cs.jfacey.algorithms;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.jfacey.bacon.ActorNode;
import edu.brown.cs.jfacey.bacon.MovieEdge;
import edu.brown.cs.jfacey.bacon.SQLDatabase;
import edu.brown.cs.jfacey.database.GraphDatabase;
import edu.brown.cs.jfacey.datastructs.PathInfo;

/**
 * This is a class that tests the dynamic djikstras implementation, as well as
 * the comparator for path info.
 *
 * @author jfacey
 *
 */
public class DynamicDjikstrasTest {

  @Test
  public void testDjikstrasSmall() {
    // djikstras
    DynamicDjikstra<ActorNode, MovieEdge> djikstra = new DynamicDjikstra<>();

    GraphDatabase<ActorNode, MovieEdge> database = new SQLDatabase();
    database.setDatabase("data/bacon/smallBacon.sqlite3");

    List<String> movies1 = new ArrayList<>();
    movies1.add("Men in Black");
    movies1.add("Primary Colors");
    List<String> movies2 = new ArrayList<>();
    movies2.add("Men in Black");
    movies2.add("Lovers and Other Strangers");
    ActorNode node1 = new ActorNode("/m/02661h", "Tony Shalhoub", movies1,
        database);
    ActorNode node2 = new ActorNode("/m/0gn30", "Sylvester Stallone",
        movies2, database);

    PathInfo<ActorNode, MovieEdge> path = djikstra.djikstras(node1, node2);
    assertEquals(path.getNode(), node2);
    assertEquals(path.getPrevious().getNode(), node1);

  }

  @Test
  public void testDjikstrasNoPath() {
    // djikstras
    DynamicDjikstra<ActorNode, MovieEdge> djikstra = new DynamicDjikstra<>();

    GraphDatabase<ActorNode, MovieEdge> database = new SQLDatabase();
    database.setDatabase("data/bacon/smallBacon.sqlite3");

    List<String> movies1 = new ArrayList<>();
    List<String> movies2 = new ArrayList<>();
    movies2.add("The Lorax");
    ActorNode node1 = new ActorNode("/m/05w1vf", "Stephen Tobolowsky",
        movies1, database);
    ActorNode node2 = new ActorNode("/m/03gq433", "Willow Smith", movies2,
        database);

    PathInfo<ActorNode, MovieEdge> path = djikstra.djikstras(node1, node2);
    assertEquals(path, null);
  }
}
