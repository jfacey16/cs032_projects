package edu.brown.cs.jfacey.stars;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.jfacey.datastructs.Kdtree;
import freemarker.template.Configuration;

/**
 * The Main class of our project. This is where execution begins.
 *
 * @author jj
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private Kdtree<Star> kdInstance;
  /**
   * The initial method called when execution begins.
   *
   * @param args
   *          An array of command line arguments
   */
  public static void main(String[] args) {
  	try {
    	new Main(args).run();
  	} catch (IOException e) {

  		System.out.println("ERROR: " + e.getMessage());
  	}
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() throws IOException {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
    .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    //beginning of repl
    try (BufferedReader br = new BufferedReader(
    		new InputStreamReader(System.in, "UTF8"))) {
			String input;
			// REPL infinite loop reading inputs
			while ((input = br.readLine()) != null) {
				//parse input
	      String[] inputs = input.split("\\s+");
	      //stars command check
	      if (Objects.equals(inputs[0], "stars")) {
	      	//make sure there are enough inputs
	      	if (inputs.length != 2) {
	      		System.out.println("ERROR: Incorrect input format. "
	      				+ "Format should be stars <filepath>");
	      		continue;
	      	}
	      	//instantiate csvparser which parses the list of stars
	      	//to add to the tree
	      	CSVParser csvParser = new CSVParser(inputs[1]);

	      	List<Star> starList;
	      	//prints error message if csvfile can't be parsed
	      	try {

	      		starList = csvParser.parseFile();

	      	} catch (RuntimeException e) {

	      		System.out.println("ERROR: " + e.getMessage());
	      		continue;
	      	}
	      	//instantiates kdtree with given star list
		    	kdInstance = new Kdtree<Star>(starList);
		    	System.out.println("Read " + starList.size()
		    			+ " stars from " + inputs[1]);
	    	//neighbors position command check
	      } else if (Objects.equals(inputs[0], "neighbors")) {
	      	//check enough inputs for neighbors search (no name)
	      	if (inputs.length == 5) {
	      		//error if kdtree has not yet been loaded
	  	      if (kdInstance == null) {

	  	      	System.out.println("ERROR: Kdtree not yet loaded");
	  	      	continue;
	  	      }
	  	      //ready parameters for neighbors search
	  	      List<Star> bestNeighbors = new ArrayList<>();

	  	      int numNeighbors;
	  	      double posX;
	  	      double posY;
	  	      double posZ;
	  	      //makes sure input values are numbers
	  	      try {

	  	      	numNeighbors = Integer.parseInt(inputs[1]);

	  	      	posX = Double.parseDouble(inputs[2]);
	  	      	posY = Double.parseDouble(inputs[3]);
	  	      	posZ = Double.parseDouble(inputs[4]);

	  	      } catch (NumberFormatException e) {

	  	      	System.out.println("ERROR: Number of neighbors "
	  	      			+ "must be a "
	  	      			+ "valid int and X positon, "
	  	      			+ "Y position, and Z position "
	  	      			+ "must all be valid doubles");
	  	      	continue;
	  	      }
	  	      //instantiate star
	  	      Star searchStar = new Star(0, "", posX, posY, posZ);
	  	      //call the search
	  	      kdInstance.neighbors(bestNeighbors, numNeighbors,
	  	      		searchStar, null);
	  	      //prints out the ids of the found points
	  	      for (int i = 0; i < bestNeighbors.size(); i++) {

	  	      	System.out.println(bestNeighbors.get(i).getID());
	  	      }
	      	//check enough inputs for neighbors search (name)
	      	} else if (inputs.length == 3) {
	      		//check name in quotes
	      		if ((inputs[2].charAt(0) == '"')
	      				&& (inputs[2].charAt(
	      				inputs[2].length() - 1) == '"')) {

	      			//error if kdtree has not yet been loaded
	  	      	if (kdInstance == null) {

	  	      		System.out.println("ERROR: Kdtree not "
	  	      				+ "yet loaded");
	  	      		continue;
	  	      	}
	  	      	//ready parameters for neighbors search
	  	      	List<Star> bestNeighbors = new ArrayList<>();
	  	      	int numNeighbors;
	  	      	//makes sure input value is a number
	  	      	try {

	  	      		numNeighbors = Integer.parseInt(inputs[1]);

	  	      	} catch (NumberFormatException e) {

	  	      		System.out.println("ERROR: Number of neighbors"
	  	      				+ " must be a valid integer");
	  	      		continue;
	  	      	}

	  	      	String name = inputs[2].substring(1,
	  	      			inputs[2].length() - 1);
	  	      	//call the search
	  	      	kdInstance.neighbors(bestNeighbors, numNeighbors,
	  	      			null, name);
	  	      	//prints out the ids of the found points
	  	      	for (int i = 0; i < bestNeighbors.size(); i++) {

	  	      		System.out.println(bestNeighbors.
	  	      				get(i).getID());
	  	      	}
	      		} else {

	      			System.out.println("ERROR: Incorrect input "
	      					+ "format. "
	      					+ "Name of star must be "
	      					+ "given in quotes");
	      			continue;
	      		}
	      	} else {

	      		System.out.println("ERROR: Incorrect input "
	      				+ "format. Format should be "
	      				+ "neighbors <number of neighbors "
	      				+ "to find> <x coordinate> "
	      				+ "<y coordinate> <z coordinate> "
	      				+ "or format should be neighbors "
	      				+ "<number of neighbors to find> "
	      				+ "<name of star>");
	      		continue;
	      	}
	    	//radius position command check
	      } else if (Objects.equals(inputs[0], "radius")) {
	      	if (inputs.length == 5) {
	      	//error if kdtree has not yet been loaded
		      	if (kdInstance == null) {

		      		System.out.println("ERROR: Kdtree "
		      				+ "not yet loaded");
		      		continue;
		      	}
		      	//make sure there are enough inputs
		      	if (inputs.length != 5) {
		      		System.out.println("ERROR: Incorrect "
		      				+ "input format. Format should "
		      				+ "be radius <radius value> "
		      				+ "<x coordinate> "
		      				+ "<y coordinate> "
		      				+ "<z coordinate>");
		      		continue;
		      	}
		      	//ready parameters for radius search
		      	List<Star> bestRadius = new ArrayList<>();
		      	double radius;
		      	double posX;
		      	double posY;
		      	double posZ;
		      	//makes sure input values are numbers
		      	try {
		      		radius = Double.parseDouble(inputs[1]);

		      		posX = Double.parseDouble(inputs[2]);
		      		posY = Double.parseDouble(inputs[3]);
		     			posZ = Double.parseDouble(inputs[4]);

		     		} catch (NumberFormatException e) {

		     			System.out.println("ERROR: Radius, "
		     					+ "X positon"
		     					+ ", Y position, "
		     					+ "and Z position "
		     					+ "must all "
		     					+ "be valid doubles");
		     			continue;
		     		}
		      	//instantiate star
		     		Star searchStar = new Star(0, "", posX,
		     				posY, posZ);
		      	//call the search
		      	kdInstance.radius(bestRadius, radius,
		      			searchStar, null);
		      	//prints out the ids of the found points
		      	for (int i = 0; i < bestRadius.size();
		      			i++) {

		      		System.out.println(bestRadius.get(i)
		      				.getID());
		      	}
	      	} else if (inputs.length == 3) {
	      		if ((inputs[2].charAt(0) == '"')
	      				&& (inputs[2].charAt(inputs[2].
	      				length() - 1) == '"')) {
	      			//error if kdtree has not yet
	      			//been loaded
	  	      	if (kdInstance == null) {

	  	      		System.out.println("ERROR: Kdtree "
	  	      				+ "not yet loaded");
	  	      		continue;
	  	      	}
	  	      	//make sure there are enough inputs
	  	      	if (inputs.length != 3) {
	  	      		System.out.println("ERROR: Incorrect "
	  	      				+ "input format. "
	  	      				+ "Format should be radius "
	  	      				+ "<radius value> "
	  	      				+ "<star name>");
	  	      		continue;
	  	      	}
	  	      	//ready parameters for radius search
	  	      	List<Star> bestRadius = new ArrayList<>();

	  	      	double radius;
	  	      	//makes sure input value is a number
	  	      	try {

	  	      		radius = Double.parseDouble(inputs[1]);

	  	      	} catch (NumberFormatException e) {

	  	      		System.out.println("ERROR: Radius must "
	  	      				+ "be a valid double");
	  	      		continue;
	  	      	}

	  	      	String name = inputs[2].substring(1,
	  	      			inputs[2].length() - 1);
	  	      	//call the search
	  	      	kdInstance.radius(bestRadius, radius, null, name);
	  	      	//prints out the ids of the found points
	  	      	for (int i = 0; i < bestRadius.size(); i++) {

	  	      		System.out.println(bestRadius.get(i).getID());
	  	      	}
	      		} else {

	      			System.out.println("ERROR: Incorrect input "
	      					+ "format. "
	      					+ "Name of star must be "
	      					+ "given in quotes");
	      			continue;
	      		}
	      	} else {

	      		System.out.println("ERROR: Incorrect input format. "
	      				+ "Format should be radius "
	      				+ "<radius value> "
	      				+ "<x coordinate> <y coordinate> "
	      				+ "<z coordinate> or "
	      				+ "format should be radius "
	      				+ "<radius value "
	      				+ "to find> <name of star>");
	      		continue;
	      	}
	      }  else {
	      	//if no command can be read
	      	System.out.println("ERROR: Invalid command given");
	      }
			}
		} catch (IOException e) {

			System.out.println("ERROR: " + e.getMessage());
		}
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
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
