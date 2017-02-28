package edu.brown.cs.jfacey.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
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
import edu.brown.cs.jfacey.autocorrect.AutoCorrectData;
import edu.brown.cs.jfacey.autocorrect.CorpusCommand;
import edu.brown.cs.jfacey.autocorrect.OptionsCommand;
import edu.brown.cs.jfacey.datastructs.Kdtree;
import edu.brown.cs.jfacey.datastructs.Trie;
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
  private AutoCorrectData iACData;
  private Trie iTrie;
  private AutoCorrectCommand iACCommand;

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
    iTrie = new Trie();
    iACData = new AutoCorrectData(false, false, false, 0, iTrie);
    // pass in data to commands and put commands in list
    iACCommand = new AutoCorrectCommand(iACData);
    List<Command> commands = ImmutableList.of(new StarsCommand(iKd),
        new NeighborsCommand(iKd), new RadiusCommand(iKd),
        new CorpusCommand(iACData), new OptionsCommand(iACData),
        iACCommand);
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
    Spark.get("/autocorrect", new AutoFrontHandler(), freeMarker);
    Spark.post("/autoback", new AutoBackHandler());
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

  /**
   * Initially sets up webpage for autocorrect.
   *
   * @author jfacey
   */
  private static class AutoFrontHandler implements TemplateViewRoute {
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
  private static class AutoBackHandler implements Route {
    @Override
    public String handle(Request req, Response res) {

      QueryParamsMap qm = req.queryMap();
      String autocorrectInput = qm.value("input");

      // what I would try to do here is take the input,
      // and run the auto correct command on it. I will
      // then return the 5 results given. If it is at the
      // point where the input is not ready yet, 5 empty
      // strings would be returned.
      // my javascript code then will set the value of
      // the five options in the options list to the
      // autocorrect values
      Map<String, Object> variables = ImmutableMap.of();
      return GSON.toJson(variables);
    }
  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   * @author jj
   */
  private static class ExceptionPrinter implements ExceptionHandler {
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
