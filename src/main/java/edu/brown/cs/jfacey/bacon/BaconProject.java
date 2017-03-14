package edu.brown.cs.jfacey.bacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.jfacey.algorithms.DynamicDjikstra;
import edu.brown.cs.jfacey.autocorrect.AutoCorrectProject;
import edu.brown.cs.jfacey.autocorrect.Suggestion;
import edu.brown.cs.jfacey.database.GraphDatabase;
import edu.brown.cs.jfacey.datastructs.PathInfo;

/**
 * This is the class that stores all data for bacon and has the main methods for
 * functionality. It stores an autocorrector for autocorrecting actor names, a
 * connection for database queries, and a graph for storing actornodes. It can
 * set its current database, autocorrect input actor names, and can return a
 * path for connecting actors.
 *
 * @author jfacey
 *
 */
public class BaconProject {

  private AutoCorrectProject iACProj;
  private Map<String, ActorNode> iGraph;
  private DynamicDjikstra<ActorNode, MovieEdge> iDjikstra;
  private GraphDatabase<ActorNode, MovieEdge> iDatabase;

  /**
   * This is the initial constructor to instantiate the autocorrector,
   * connection for queries, and the graph, as well as the database.
   */
  public BaconProject() {
    iACProj = new AutoCorrectProject(false, false, false, 0);
    iGraph = new HashMap<>();
    iDjikstra = new DynamicDjikstra<>();
    iDatabase = new SQLDatabase();
  }

  /**
   * This sets the database given by the input filepath, and sets up the
   * connection to this database.
   *
   * @param dbPath
   *          the path to the database
   */
  public void setDatabase(String dbPath) {
    iDatabase.setDatabase(dbPath);
  }

  /**
   * This method sets up for finding the connection between two actors. It will
   * first look for the actors and will add nodes associated with them to graph
   * if they do not already exist. It will then call dijikstras on these two
   * nodes to find the shortest path between them.
   *
   * @param nameOne
   *          first actor to connect on
   * @param nameTwo
   *          second actor to connect on
   * @return the list of actornodes representing a path
   */
  public PathInfo<ActorNode, MovieEdge> connect(String nameOne,
      String nameTwo) {
    PathInfo<ActorNode, MovieEdge> path = new PathInfo<>(null, null, 0, "");
    // if no database has been set, throw error
    if (!iDatabase.isDatabase()) {
      System.out.println("ERROR: no database set");
      return path;
    }
    List<ActorNode> list = iDatabase
        .setUpConnect(nameOne, nameTwo, iGraph);
    // call dijikstras
    if (list.size() == 0) {
      return path;
    }

    path = iDjikstra.djikstras(list.get(0), list.get(1));
    return path;
  }

  /**
   * This method prints the path found based on the input actor names.
   *
   * @param nameOne
   *          the first actor name
   * @param nameTwo
   *          the second actor name
   * @param path
   *          the end path for the current bacon
   */
  public void printConnections(String nameOne, String nameTwo,
      PathInfo<ActorNode, MovieEdge> path) {
    if (path == null) {
      System.out.println(nameOne + " -/- " + nameTwo);
      return;
    }
    List<String> printStrings = new ArrayList<>();
    // assemble print strings in reverse order and push to stack
    while (path.getPrevious() != null) {
      String printString = new String(path.getPrevious().getNode()
          .getActor()
          + " -> " + path.getNode().getActor() + " : " + path.getMovie());
      printStrings.add(printString);
      path = path.getPrevious();
    }
    // print all strings in correct order
    for (int i = 0; i < printStrings.size(); i++) {
      String print = printStrings.get(printStrings.size() - i - 1);
      System.out.println(print);
    }
  }

  /**
   * This method sets autocorrecting based on the actors name.
   *
   * @param name
   *          the name to autocorrect on
   * @param previous
   *          the previous string before the autocorrect one
   * @param previousSentence
   *          the entire previousSentence to the word
   * @return the list of strings for autocorrecting the actor
   */
  public List<String> autoCorrectActor(String name, String previous,
      String previousSentence) {

    List<Suggestion> suggestions = iACProj.getSuggestions(name);
    return iACProj.sortSuggestions(suggestions, previous, name,
        previousSentence);
  }
}
