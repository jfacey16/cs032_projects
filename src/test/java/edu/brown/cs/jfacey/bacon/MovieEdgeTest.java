package edu.brown.cs.jfacey.bacon;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.brown.cs.jfacey.datastructs.GraphEdge;

/**
 * This class tests the MovieEdge class which is an implementation of the
 * GraphEdge class. It only tests the override of the toString method as the
 * other methods are all just accessors.
 *
 * @author jfacey
 *
 */
public class MovieEdgeTest {

  @Test
  public void testToString() {
    ActorNode node = new ActorNode("/m/abc/", "Actor1", null, null);
    GraphEdge<ActorNode, MovieEdge> edge = new MovieEdge(node, 5.0,
        "movie");
    assertEquals(edge.toString(),
        "ID: /m/abc/, Actor: Actor1, Cost: 5.0, Movie: movie");
  }
}
