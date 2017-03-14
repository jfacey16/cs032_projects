package edu.brown.cs.jfacey.bacon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.jfacey.database.GraphDatabase;

/**
 * This class is an implementation of the basic graph database interface for a
 * sql database, for generating actor nodes and movie edges. It uses sequel
 * queries to generate children, as well as set up for the connect.
 *
 * @author jfacey
 *
 */
public class SQLDatabase implements GraphDatabase<ActorNode, MovieEdge> {

  private Connection iConn;
  private boolean iIsDatabase;

  /**
   * This initially sets the sql database connection to null and sets the
   * database boolean to not be loaded.
   */
  public SQLDatabase() {
    iConn = null;
    iIsDatabase = false;
  }

  @Override
  public void setDatabase(String dbPath) {
    try {
      try {
        Class.forName("org.sqlite.JDBC");
      } catch (ClassNotFoundException e) {
        // idk what to do if I get here
        e.printStackTrace();
      }
      String urlToDB = "jdbc:sqlite:" + dbPath;
      iConn = DriverManager.getConnection(urlToDB);
      Statement stat = iConn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys = ON;");

      ResultSet rs = iConn.getMetaData().getTables(null, null, null, null);

      List<String> tables = new ArrayList<>();

      while (rs.next()) {

        tables.add(rs.getString(3));
      }
      rs.close();
      // check to make sure it is a valid sql database with the correct tables
      if (tables.size() == 3) {
        if (tables.get(0).equals("actor")
            && tables.get(1).equals("actor_film")
            && tables.get(2).equals("film")) {
          iIsDatabase = true;
          System.out.println("db set to " + dbPath);
        } else {
          iIsDatabase = false;
          System.out.println("ERROR: must be valid sql database "
              + "containing the tables actor, film, and actor_film");
        }
      } else {
        iIsDatabase = false;
        System.out.println("ERROR: must be valid sql database "
            + "containing the tables actor, film, and actor_film");
      }

    } catch (SQLException e) {
      iIsDatabase = false;
      System.out.println("ERROR: invalid database path");
    }
  }

  @Override
  public boolean generateChildren(List<String> moviesList,
      List<MovieEdge> edges, String actor) {
    // actor node list
    List<ActorNode> actorNodes = new ArrayList<>();
    try {
      if (moviesList.size() == 0) {
        return true;
      }
      // query all actor ids and names that are in the same movie as this actor
      PreparedStatement prep;
      String queryString = "SELECT actor.id, actor.name, "
          + "film.id, film.name FROM actor, film, actor_film "
          + "WHERE actor.id = actor_film.actor AND film.id "
          + "= actor_film.film AND film.name IN ";
      // set up movies list for query
      for (int i = 0; i < moviesList.size(); i++) {
        if (i == 0) {

          queryString = queryString.concat("(");
        }

        if (i == moviesList.size() - 1) {
          queryString = queryString.concat("?);");
        } else {

          queryString = queryString.concat("?,");
        }
      }
      prep = iConn.prepareStatement(queryString);

      for (int i = 0; i < moviesList.size(); i++) {
        prep.setString(i + 1, moviesList.get(i));
      }
      ResultSet rs = prep.executeQuery();
      // lists to store query results for making nodes and edges
      List<String> actorIDs = new ArrayList<>();
      List<String> actors = new ArrayList<>();
      List<String> movieIDs = new ArrayList<>();
      List<String> movies = new ArrayList<>();
      // find character to check on
      String[] splitActor = actor.split("\\s+");
      char checkChar = splitActor[splitActor.length - 1].charAt(0);
      // loop through results, if first char of current nodes
      // actor last name is equal to first char of
      // selected actors first name, add to lists of potential nodes
      while (rs.next()) {

        String aID = rs.getString(1);
        String name = rs.getString(2);
        String movieID = rs.getString(3);
        String movie = rs.getString(4);
        if (name.length() > 0) {
          if (name.charAt(0) == checkChar) {
            actorIDs.add(aID);
            actors.add(name);
            movieIDs.add(movieID);
            movies.add(movie);
          }
        }
      }
      rs.close();
      // set edges
      // first make a list of unique moveies to query on
      List<String> uniqueIDs = new ArrayList<>();
      for (int i = 0; i < movieIDs.size(); i++) {
        if (!uniqueIDs.contains(movieIDs.get(i))) {
          uniqueIDs.add(movieIDs.get(i));
        }
      }

      // get number of actors in each unique movie and put in hashmap
      HashMap<String, Integer> actorsPerMovie = new HashMap<>();
      for (int i = 0; i < uniqueIDs.size(); i++) {
        queryString = "SELECT COUNT(*) FROM actor_film "
            + "WHERE actor_film.film=?;";
        prep = iConn.prepareStatement(queryString);
        prep.setString(1, uniqueIDs.get(i));
        rs = prep.executeQuery();

        while (rs.next()) {
          Integer actorCount = new Integer(rs.getInt(1));
          actorsPerMovie.put(uniqueIDs.get(i), actorCount);
        }

        rs.close();
      }

      // select all selected actors, get their movie list to make an actor node
      // for them
      for (int i = 0; i < actorIDs.size(); i++) {

        prep = iConn.prepareStatement("SELECT name FROM film, actor_film "
            + "WHERE film.id = actor_film.film "
            + "AND actor_film.actor=?;");
        prep.setString(1, actorIDs.get(i));
        rs = prep.executeQuery();
        // add movies to movie list
        List<String> moviesListNew = new ArrayList<>();

        while (rs.next()) {
          moviesListNew.add(rs.getString(1));
        }
        rs.close();
        // make actor node
        ActorNode actorNode = new ActorNode(actorIDs.get(i),
            actors.get(i), moviesListNew, this);
        actorNodes.add(actorNode);
      }

      // add edges to edge list
      double edgeWeight;
      for (int i = 0; i < actorNodes.size(); i++) {
        if ((actorsPerMovie.get(movieIDs.get(i)).intValue()) == 0) {
          edgeWeight = 0;
        } else {
          edgeWeight = (double) 1
              / (actorsPerMovie.get(movieIDs.get(i)).intValue());
        }

        MovieEdge edge = new MovieEdge(actorNodes.get(i), edgeWeight,
            movies.get(i));
        edges.add(edge);
      }
      return true;
    } catch (SQLException e) {
      System.out.println("ERROR: invalid database loadedaa");
    }
    return false;
  }

  @Override
  public boolean isDatabase() {
    return iIsDatabase;
  }

  @Override
  public List<ActorNode> setUpConnect(String nameOne, String nameTwo,
      Map<String, ActorNode> graph) {
    List<ActorNode> nodes = new ArrayList<>();
    try {
      PreparedStatement prep;
      // query to find all ids with this actor name
      prep = iConn.prepareStatement("SELECT id FROM actor WHERE name=?;");
      prep.setString(1, nameOne);

      ResultSet rs = prep.executeQuery();
      List<String> actorListOne = new ArrayList<>();

      while (rs.next()) {
        actorListOne.add(rs.getString(1));

      }
      rs.close();
      // query to find all ids with this actor name
      prep = iConn.prepareStatement("SELECT id FROM actor WHERE name=?;");
      prep.setString(1, nameTwo);

      rs = prep.executeQuery();
      List<String> actorListTwo = new ArrayList<>();

      while (rs.next()) {
        actorListTwo.add(rs.getString(1));
      }
      rs.close();
      // throw error if no ids returned
      if (actorListOne.size() == 0) {
        System.out.println("ERROR: " + nameOne + " is not a valid actor");
        return nodes;
      }

      if (actorListTwo.size() == 0) {
        System.out.println("ERROR: " + nameTwo + " is not a valid actor");
        return nodes;
      }
      // random select id from returned possible ids
      int index = (int) Math.random() * actorListOne.size();
      String actorIdOne = actorListOne.get(index);
      // random select id from returned possible ids
      index = (int) Math.random() * actorListTwo.size();
      String actorIdTwo = actorListTwo.get(index);

      ActorNode nodeOne;

      if (graph.containsKey(actorIdOne)) {

        nodeOne = graph.get(actorIdOne);

      } else {
        // query to get all movies for specific actor
        prep = iConn.prepareStatement("SELECT name FROM film, "
            + "actor_film WHERE film.id = actor_film.film AND "
            + "actor_film.actor=?;");
        prep.setString(1, actorIdOne);

        rs = prep.executeQuery();
        List<String> moviesListOne = new ArrayList<>();

        while (rs.next()) {
          moviesListOne.add(rs.getString(1));
        }
        rs.close();

        // add nodes to graph if they don't already exist
        nodeOne = new ActorNode(actorIdOne, nameOne, moviesListOne, this);
        graph.put(actorIdOne, nodeOne);
      }

      ActorNode nodeTwo;

      if (graph.containsKey(actorIdTwo)) {

        nodeTwo = graph.get(actorIdTwo);

      } else {
        // query to get all movies for specific actor
        prep = iConn.prepareStatement("SELECT name FROM film, "
            + "actor_film WHERE film.id = "
            + "actor_film.film AND actor_film.actor=?;");
        prep.setString(1, actorIdTwo);

        rs = prep.executeQuery();
        List<String> moviesListTwo = new ArrayList<>();

        while (rs.next()) {
          moviesListTwo.add(rs.getString(1));
        }
        rs.close();

        nodeTwo = new ActorNode(actorIdTwo, nameTwo, moviesListTwo, this);
        graph.put(actorIdTwo, nodeTwo);

      }
      nodes.add(nodeOne);
      nodes.add(nodeTwo);
    } catch (SQLException e) {
      System.out.println("ERROR: invalid database loaded");
    }
    return nodes;
  }
}
