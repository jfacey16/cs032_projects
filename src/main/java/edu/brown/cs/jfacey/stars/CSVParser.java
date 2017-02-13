package edu.brown.cs.jfacey.stars;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {
	
	private String _filePath;
	
	public CSVParser(String filePath) {
		
		_filePath = filePath;
		
	}
	
	public List<Star> parseFile() {
		//Instantiate stars array
		List<Star> points = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(_filePath))) {
			
			//TODO test for worng header
			String input = br.readLine();
			//loop reading inputs until no lines left
			while ((input = br.readLine()) != null) {
				//parse input
	      String[] inputs = input.split(",");
	      //load input into new star
	      Star curStar = new Star(Integer.parseInt(inputs[0]), inputs[1], Double.parseDouble(inputs[2]), 
	      		Double.parseDouble(inputs[3]), Double.parseDouble(inputs[4]));
	  		//load star into stars arraylist
	      points.add(curStar);
	      
			}
		} catch (IOException e) {
			//To do: fill in error message
			System.out.println("ERROR: " + e.getMessage());
		}	
		return points;
		
	}
	
	
}