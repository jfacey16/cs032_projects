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

import freemarker.template.Configuration;

/**
 * The Main class of our project. This is where execution begins.
 *
 * @author jj
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private Kdtree<Star> _kd;
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
			
			e.printStackTrace();
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
    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF8"))) {
			String input;
			// REPL infinite loop reading inputs
			while ((input = br.readLine()) != null) {
				//parse input
	      String[] inputs = input.split("\\s+");
	      //stars command check
	      if (Objects.equals(inputs[0],"stars")) {
	      
	      	CSVParser csvParser = new CSVParser(inputs[1]);
		    	_kd = new Kdtree<Star>(csvParser.parseFile());
	      
	    	//neighbors position command check
	      } else if (Objects.equals(inputs[0],"neighbors") && (inputs[2].charAt(0) != '"') 
	  				&& (inputs[2].charAt(inputs[2].length() - 1) != '"')) {
	      	
	      	if (_kd == null) {
	      		
	      		System.out.println("ERROR: Kdtree not yet loaded");
	      		
	      	} else {
	      		
	      		List<Star> bestNeighbors = new ArrayList<>();
	      		int numNeighbors = Integer.parseInt(inputs[1]);
	      		
	      		double posX = Double.parseDouble(inputs[2]);
	      		double posY = Double.parseDouble(inputs[3]);
	      		double posZ = Double.parseDouble(inputs[4]);
	      		Star searchStar = new Star(0,"",posX,posY,posZ);
	      		
	      		_kd.neighbors(bestNeighbors, numNeighbors, searchStar,null);
	      		
	      		for (int i = 0; i < bestNeighbors.size(); i++) {
	      			
	      			System.out.println(bestNeighbors.get(i).getID());
	      			System.out.println(bestNeighbors.get(i).getX());
	      			System.out.println(bestNeighbors.get(i).getY());
	      			System.out.println(bestNeighbors.get(i).getZ());
	      			
	      		}
	      	}
	    		
	    	//neighbors name command check
	      } else if (Objects.equals(inputs[0],"neighbors") && (inputs[2].charAt(0) == '"') 
	  				&& (inputs[2].charAt(inputs[2].length() - 1) == '"')) {
	      	
	      	if (_kd == null) {
	      		
	      		System.out.println("ERROR: Kdtree not yet loaded");
	      		
	      	} else {
	      		
	      		List<Star> bestNeighbors = new ArrayList<>();
	      		int numNeighbors = Integer.parseInt(inputs[1]);
	      		
	      		String name = inputs[2].substring(1,inputs[2].length()-1);
	      		
	      		_kd.neighbors(bestNeighbors, numNeighbors, null,name);
	      		
	      		for (int i = 0; i < bestNeighbors.size(); i++) {
	      			
	      			System.out.println(bestNeighbors.get(i).getID());
	      		}
	      	}
	    		
	    	//radius position command check	
	      } else if (Objects.equals(inputs[0],"radius") && (inputs[2].charAt(0) != '"') 
	  				&& (inputs[2].charAt(inputs[2].length() - 1) != '"')) {
	      	
	      	if (_kd == null) {
	      		
	      		System.out.println("ERROR: Kdtree not yet loaded");
	      		
	      	} else {
	      		
	      		List<Star> bestRadius = new ArrayList<>();
	      		double radius = Double.parseDouble(inputs[1]);
	      		
	      		double posX = Double.parseDouble(inputs[2]);
	      		double posY = Double.parseDouble(inputs[3]);
	      		double posZ = Double.parseDouble(inputs[4]);
	      		Star searchStar = new Star(0,"",posX,posY,posZ);
	      		
	      		_kd.radius(bestRadius, radius, searchStar, null);
	      		
	      		for (int i = 0; i < bestRadius.size(); i++) {
	      			
	      			System.out.println(bestRadius.get(i).getID());
	      			System.out.println(bestRadius.get(i).getX());
	      			System.out.println(bestRadius.get(i).getY());
	      			System.out.println(bestRadius.get(i).getZ());
	      		}
	      	}
	      	
	    	//radius name command check	
	      } else if (Objects.equals(inputs[0],"radius") && (inputs[2].charAt(0) == '"') 
	  				&& (inputs[2].charAt(inputs[2].length() - 1) == '"')) {
	      	
	      	if (_kd == null) {
	      		
	      		System.out.println("ERROR: Kdtree not yet loaded");
	      		
	      	} else {
	      		List<Star> bestRadius = new ArrayList<>();
	      		double radius = Double.parseDouble(inputs[1]);
	      		
	      		String name = inputs[2].substring(1,inputs[2].length()-1);
	      		
	      		_kd.radius(bestRadius, radius, null, name);
	      		
	      		for (int i = 0; i < bestRadius.size(); i++) {
	      			
	      			System.out.println(bestRadius.get(i).getID());
	      		}
	      	}
	      
	      } else {
	      	
	      	
	      	
	      }
			}
		} catch (IOException e) {
			//To do: fill in error message
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
