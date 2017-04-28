package edu.brown.cs.jfacey.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.eclipse.jetty.util.ArrayUtil;

import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.jfacey.autocorrect.AutoCorrectCommand;
import edu.brown.cs.jfacey.autocorrect.AutoCorrectProject;
import edu.brown.cs.jfacey.autocorrect.CorpusCommand;
import edu.brown.cs.jfacey.autocorrect.OptionsCommand;
import edu.brown.cs.jfacey.bacon.ActorNode;
import edu.brown.cs.jfacey.bacon.BaconCommand;
import edu.brown.cs.jfacey.bacon.BaconProject;
import edu.brown.cs.jfacey.bacon.DatabaseCommand;
import edu.brown.cs.jfacey.bacon.MovieEdge;
import edu.brown.cs.jfacey.datastructs.Kdtree;
import edu.brown.cs.jfacey.datastructs.PathInfo;
import edu.brown.cs.jfacey.readers.Command;
import edu.brown.cs.jfacey.readers.REPL;
import edu.brown.cs.jfacey.stars.NeighborsCommand;
import edu.brown.cs.jfacey.stars.RadiusCommand;
import edu.brown.cs.jfacey.stars.Star;
import edu.brown.cs.jfacey.stars.StarsCommand;
import freemarker.template.Configuration;

/**
 * The Main class of our project. This is where execution begins.
 *
 * @author jj
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private static final Gson GSON = new Gson();
  private Kdtree<Star> iKd;
  private AutoCorrectProject iACData;
  private AutoCorrectCommand iACCommand;
  private BaconProject iBacon;

  /**
   * The initial method called when execution begins.
   *
   * @param args
   *          An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }
    // instantiate data for stars and autocorrect
    iKd = new Kdtree<Star>();
    iACData = new AutoCorrectProject(false, false, false, 0);
    iBacon = new BaconProject();
    // pass in data to commands and put commands in list
    iACCommand = new AutoCorrectCommand(iACData);
    List<Command> commands = ImmutableList.of(new StarsCommand(iKd),
        new NeighborsCommand(iKd), new RadiusCommand(iKd),
        new CorpusCommand(iACData), new OptionsCommand(iACData),
        iACCommand, new DatabaseCommand(iBacon), new BaconCommand(iBacon));
    // pass commands into new repl
    REPL repl = new REPL(commands);
    // run repl
    repl.execute();
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File(
        "src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/stars", new FrontHandler(), freeMarker);
    Spark.post("/neighbors", new NeighborsHandler(), freeMarker);
    Spark.post("/radius", new RadiusHandler(), freeMarker);
    Spark.get("/autocorrect", new AutoFrontHandler(), freeMarker);
    Spark.post("/autoback", new AutoBackHandler());
    Spark.get("/bacon", new BaconFrontHandler(), freeMarker);
    Spark.post("/baconback", new BaconBackHandler());
  }

  /**
   * Handle requests to the front page of our Stars website.
   *
   * @author jj
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Stars: Query the database");
      return new ModelAndView(variables, "query.ftl");
    }
  }

  private class NeighborsHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      QueryParamsMap qm = req.queryMap();
      String neighborsInput = qm.value("neighbortext");
      String type = qm.value("neighbor");
      List<String> stars = new ArrayList<>();

      String[] inputs = neighborsInput.split(
          "[ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
      // if trailing whitespace, parse out
      if (inputs[inputs.length - 1].equals("")) {
        String[] temp = new String[inputs.length];
        temp = ArrayUtil.removeFromArray(inputs, inputs.length - 1);
        inputs = new String[inputs.length - 1];
        // copy new array over
        for (int i = 0; i < inputs.length; i++) {
          inputs[i] = temp[i];
        }
      }
      if (type.equals("point")) {

        // check enough inputs for neighbors search (no name)
        if (inputs.length == 4) {
          // error if kdtree has not yet been loaded
          if (iKd.getRoot() == null) {

            stars.add("The stars have not yet been loaded");
            Map<String, Object> variables = ImmutableMap.of("type", type,
                "stars", stars, "title", "Neighbors Point Results");
            return new ModelAndView(variables, "neighbor.ftl");
          }
          // ready parameters for neighbors search
          List<Star> bestNeighbors = new ArrayList<>();

          int numNeighbors;
          double posX;
          double posY;
          double posZ;
          // makes sure input values are numbers
          try {

            numNeighbors = Integer.parseInt(inputs[0]);

            posX = Double.parseDouble(inputs[1]);
            posY = Double.parseDouble(inputs[2]);
            posZ = Double.parseDouble(inputs[3]);

          } catch (NumberFormatException e) {

            stars.add("Number of neighbors must be a valid "
                + "int and X positon, Y position, and Z position "
                + "must all be valid doubles");
            Map<String, Object> variables = ImmutableMap.of("type", type,
                "stars", stars, "title", "Neighbors Point Results");
            return new ModelAndView(variables, "neighbor.ftl");
          }
          // instantiate star
          Star searchStar = new Star(0, "", posX, posY, posZ);
          // call the search
          iKd.neighbors(bestNeighbors, numNeighbors, searchStar, null);

          for (int i = 0; i < bestNeighbors.size(); i++) {
            int j = i + 1;
            stars.add("Star " + j + ": "
                + String.valueOf(bestNeighbors.get(i).getID()));
          }

          Map<String, Object> variables = ImmutableMap.of("type", type,
              "stars", stars, "title", "Neighbors Point Results");
          return new ModelAndView(variables, "neighbor.ftl");
        }

        stars.add("Input type must be a valid "
            + "int and X positon, Y position, and Z position "
            + "must all be valid doubles ");
        Map<String, Object> variables = ImmutableMap.of("type", type,
            "stars", stars, "title", "Neighbors Point Results");
        return new ModelAndView(variables, "neighbor.ftl");

      } else {
        if (inputs.length == 2) {
          // check name in quotes
          if ((inputs[1].charAt(0) == '"')
              && (inputs[1].charAt(inputs[1].length() - 1) == '"')) {

            // error if kdtree has not yet been loaded
            if (iKd.getRoot() == null) {

              stars.add("The stars have not yet been loaded");
              Map<String, Object> variables = ImmutableMap.of("type",
                  type, "stars", stars, "title", "Neighbors Name Results");
              return new ModelAndView(variables, "neighbor.ftl");
            }
            // ready parameters for neighbors search
            List<Star> bestNeighbors = new ArrayList<>();

            int numNeighbors;
            // makes sure input value is a number
            try {

              numNeighbors = Integer.parseInt(inputs[0]);

            } catch (NumberFormatException e) {

              stars
              .add("Number of neighbors" + " must be a valid integer");
              Map<String, Object> variables = ImmutableMap.of("type",
                  type, "stars", stars, "title", "Neighbors Name Results");
              return new ModelAndView(variables, "neighbor.ftl");
            }

            String name = inputs[1].substring(1, inputs[1].length() - 1);
            // call the search
            iKd.neighbors(bestNeighbors, numNeighbors, null, name);
            // prints out the ids of the found points
            for (int i = 0; i < bestNeighbors.size(); i++) {
              int j = i + 1;
              stars.add("Star " + j + ": "
                  + String.valueOf(bestNeighbors.get(i).getID()));
            }

            Map<String, Object> variables = ImmutableMap.of("type", type,
                "stars", stars, "title", "Neighbors Name Results");
            return new ModelAndView(variables, "neighbor.ftl");
          } else {

            stars.add("Incorrect input format. "
                + "Name of star must be given in quotes");
            Map<String, Object> variables = ImmutableMap.of("type", type,
                "stars", stars, "title", "Neighbors Name Results");
            return new ModelAndView(variables, "neighbor.ftl");
          }
        }
        stars.add("Input type must be a valid " + "int and star name");
        Map<String, Object> variables = ImmutableMap.of("type", type,
            "stars", stars, "title", "Neighbors Name Results");
        return new ModelAndView(variables, "neighbor.ftl");
      }
    }
  }

  private class RadiusHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      QueryParamsMap qm = req.queryMap();
      String radiusInput = qm.value("radiustext");
      String type = qm.value("radius");
      List<String> stars = new ArrayList<>();

      String[] inputs = radiusInput.split(
          "[ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);

      if (inputs[inputs.length - 1].equals("")) {
        String[] temp = new String[inputs.length];
        temp = ArrayUtil.removeFromArray(inputs, inputs.length - 1);
        inputs = new String[inputs.length - 1];
        // copy new array over
        for (int i = 0; i < inputs.length; i++) {
          inputs[i] = temp[i];
        }
      }
      if (type.equals("point")) {
        // check enough inputs for neighbors search (no name)
        if (inputs.length == 4) {
          // error if kdtree has not yet been loaded
          if (iKd.getRoot() == null) {

            stars.add("The stars have not yet been loaded");
            Map<String, Object> variables = ImmutableMap.of("type", type,
                "stars", stars, "title", "Radius Point Results");
            return new ModelAndView(variables, "radius.ftl");
          }
          // ready parameters for neighbors search
          List<Star> bestRadius = new ArrayList<>();

          double radius;
          double posX;
          double posY;
          double posZ;
          // makes sure input values are numbers
          try {

            radius = Integer.parseInt(inputs[0]);

            posX = Double.parseDouble(inputs[1]);
            posY = Double.parseDouble(inputs[2]);
            posZ = Double.parseDouble(inputs[3]);

          } catch (NumberFormatException e) {

            stars.add("Radius, X positon, Y position, and Z position "
                + "must all be valid doubles");
            Map<String, Object> variables = ImmutableMap.of("type", type,
                "stars", stars, "title", "Radius Point Results");
            return new ModelAndView(variables, "radius.ftl");
          }
          // instantiate star
          Star searchStar = new Star(0, "", posX, posY, posZ);
          // call the search
          iKd.radius(bestRadius, radius, searchStar, null);

          for (int i = 0; i < bestRadius.size(); i++) {
            int j = i + 1;
            stars.add("Star " + j + ": "
                + String.valueOf(bestRadius.get(i).getID()));
          }

          Map<String, Object> variables = ImmutableMap.of("type", type,
              "stars", stars, "title", "Radius Point Results");
          return new ModelAndView(variables, "radius.ftl");
        }

        stars.add("Input type must be a valid "
            + "double and X positon, Y position, and Z position "
            + "must all be valid doubles ");
        Map<String, Object> variables = ImmutableMap.of("type", type,
            "stars", stars, "title", "Radius Point Results");
        return new ModelAndView(variables, "radius.ftl");

      } else {
        if (inputs.length == 2) {
          // check name in quotes
          if ((inputs[1].charAt(0) == '"')
              && (inputs[1].charAt(inputs[1].length() - 1) == '"')) {

            // error if kdtree has not yet been loaded
            if (iKd.getRoot() == null) {

              stars.add("The stars have not yet been loaded");
              Map<String, Object> variables = ImmutableMap.of("type",
                  type, "stars", stars, "title", "Radius Name Results");
              return new ModelAndView(variables, "radius.ftl");
            }
            // ready parameters for neighbors search
            List<Star> bestRadius = new ArrayList<>();

            double radius;
            // makes sure input value is a number
            try {

              radius = Integer.parseInt(inputs[0]);

            } catch (NumberFormatException e) {

              stars.add("Radius" + " must be a valid double");
              Map<String, Object> variables = ImmutableMap.of("type",
                  type, "stars", stars, "title", "Radius Name Results");
              return new ModelAndView(variables, "radius.ftl");
            }

            String name = inputs[1].substring(1, inputs[1].length() - 1);
            // call the search
            iKd.radius(bestRadius, radius, null, name);
            // prints out the ids of the found points
            for (int i = 0; i < bestRadius.size(); i++) {
              int j = i + 1;
              stars.add("Star " + j + ": "
                  + String.valueOf(bestRadius.get(i).getID()));
            }

            Map<String, Object> variables = ImmutableMap.of("type", type,
                "stars", stars, "title", "Radius Name Results");
            return new ModelAndView(variables, "radius.ftl");
          } else {

            stars.add("Incorrect input format. "
                + "Name of star must be given in quotes");
            Map<String, Object> variables = ImmutableMap.of("type", type,
                "stars", stars, "title", "Radius Name Results");
            return new ModelAndView(variables, "radius.ftl");
          }
        }
        stars.add("Input type must be a valid " + "double and star name");
        Map<String, Object> variables = ImmutableMap.of("type", type,
            "stars", stars, "title", "Radius Name Results");
        return new ModelAndView(variables, "radius.ftl");
      }
    }
  }

  /**
   * Initially sets up webpage for autocorrect.
   *
   * @author jfacey
   */
  private class AutoFrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Autocorrect");
      return new ModelAndView(variables, "autocorrect.ftl");
    }
  }

  /**
   * Handle autocorrect inputs on webpage.
   *
   * @author jfacey
   *
   */
  private class AutoBackHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      System.out.println("here");
      QueryParamsMap qm = req.queryMap();
      String autocorrectInput = qm.value("input");
      String[] inputs = autocorrectInput.split(
          "[ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
      List<String> words = iACData.autoCorrectWord(inputs);
      int blank = 5 - words.size();
      if (blank > 0) {
        for (int i = 5; i > 5 - blank; i = i - 1) {
          words.add("");
        }
      }
      Map<String, Object> variables = ImmutableMap
          .of("wordOne", words.get(0), "wordTwo", words.get(1),
              "wordThree", words.get(2), "wordFour", words.get(3),
              "wordFive", words.get(4));
      return GSON.toJson(variables);
    }
  }

  /**
   * Initially sets up the bacon page.
   *
   * @author jfacey
   *
   */
  private class BaconFrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "bacon");
      return new ModelAndView(variables, "bacon.ftl");
    }
  }

  /**
   * This class handles the back end for bacon on two input actor names.
   *
   * @author jfacey
   *
   */
  private class BaconBackHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      System.out.println("here");
      QueryParamsMap qm = req.queryMap();
      String[] inputs = qm.values();
      PathInfo<ActorNode, MovieEdge> path = iBacon.connect(inputs[0],
          inputs[1]);

      String returnString = "";
      if (path == null) {
        returnString = inputs[0] + " -/- " + inputs[1];
      } else {
        List<String> printStrings = new ArrayList<>();
        // assemble print strings in reverse order and push to stack
        while (path.getPrevious() != null) {
          String printString = new String(path.getPrevious().getNode()
              .getActor()
              + " -> "
              + path.getNode().getActor()
              + " : "
              + path.getMovie());
          printStrings.add(printString);
          path = path.getPrevious();
        }
        // print all strings in correct order
        for (int i = 0; i < printStrings.size(); i++) {
          String print = printStrings.get(printStrings.size() - i - 1);
          returnString = returnString.concat(print + "\n");
        }
      }
      Map<String, Object> variables = ImmutableMap
          .of("path", returnString);
      return GSON.toJson(variables);
    }
  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   * @author jj
   */
  private class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
