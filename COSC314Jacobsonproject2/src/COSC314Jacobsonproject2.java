// Doing extra credit!
// This program takes airport codes and a matrix representation of their flight path distances
// and calculates the shortest path and distance from each airport to each airport.

import java.io.*;
import java.util.*;

public class COSC314Jacobsonproject2 {
	
	public static void main(String[] args) {
		
	
		try {
			// get airport codes
			FileInputStream codesStream = new FileInputStream("airport_codes.txt");
			Scanner codesReader = new Scanner(codesStream); 
			int numAirports = 0;
			String[] airportCodes = new String[100];
			while(codesReader.hasNext()) {
				airportCodes[numAirports] = codesReader.next();
				numAirports++;
			}
			
			// get distances
			FileInputStream matrixStream = new FileInputStream("distance_matrix.txt");
			Scanner matrixReader = new Scanner(matrixStream);  
	        int [][] distanceMatrix = new int[numAirports][numAirports];
			//store matrix as 2D array
	   	 	for(int i = 0; i < numAirports; i++){
	   	 		for(int j = 0; j < numAirports; j++){
	   	 			distanceMatrix[i][j] = matrixReader.nextInt();
	   	 		}
	   	 	}
	   	 	
	   	 	// create an array of Vertices
	   	 	Vertex[] airportList = new Vertex[numAirports];
	   	 	
	   	 	// give each Vertex an int[] data field with its distances to each vertex
	   	 	for(int i = 0; i < numAirports; i++) {
	   	 		airportList[i] = new Vertex(airportCodes[i], distanceMatrix[i]);
	   	 	}
	   	 	
	   	 	// output all shortest distances to a file in matrix format
	   	 	FileWriter fileWriter = new FileWriter("shortest_distances.txt");
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    for(int i = 0; i < numAirports; i++){
	   	 		for(int j = 0; j < numAirports; j++){
	   	 			Vertex[] shortestPath = new Vertex[airportList.length]; // unused, needed to call dijkstra
	   	 			int shortestDistance = dijkstra(airportList, airportList[i], airportList[j], shortestPath);
	   	 			printWriter.write(shortestDistance + "\t");
	   	 		}
	   	 		printWriter.write("\n");
	   	 	}
	   	 	printWriter.close();
	   	 	System.out.println("Shortest distances between all airports are available in shortest_distances.txt\n");
	   	 	
	   	 	// allow user to find specific distances without opening file
	   	 	Scanner keyboard = new Scanner(System.in);
	   	 	boolean userIsFinished = false;
	   	 	boolean badInput = false;
	   	 	do {
		   	 	System.out.print("Enter two airports codes, separated by whitespace, to find out the shortest route between them, or enter q to quit: ");
		   	 	String startCode = keyboard.next();
		   	 	if(startCode.equalsIgnoreCase("q")) { // if user wants to end program
		   	 		userIsFinished = true;
		   	 		break;
		   	 	}
		   	 	String endCode = keyboard.next();
		   	 	Vertex startVertex = null;
		   	 	Vertex endVertex = null;
		   	 	// find the vertices associated with the input airport codes
		   	 	for(int i = 0; i < airportList.length; i++) {
		   	 		if(startCode.equals(airportList[i].getCode())) startVertex = airportList[i];
		   	 		if(endCode.equals(airportList[i].getCode())) endVertex = airportList[i];
		   	 	}
		   	 	if(startVertex == null) {
		   	 		System.out.println("First vertex is not in the airport list.");
		   	 		badInput = true;
		   	 	}
		   	 	if(endVertex == null) {
		   	 		System.out.println("Second vertex is not in the airport list.");
		   	 		badInput = true;
		   	 	}
		   	 	// if vertices have been found, execute algorithm
		   	 	if(!badInput) prepareDijkstra(airportList, startVertex, endVertex);
		   	 	
		   	 	// allow user to continue to enter more vertices
	   	 	} while(!userIsFinished);	   	 	
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void prepareDijkstra(Vertex[] airportList, Vertex start, Vertex end) {
		// dijkstra will store shortest path in shortestPath 
		Vertex[] shortestPath = new Vertex[airportList.length]; // might not use full array
   	 	for(int i = 0; i < shortestPath.length; i++) {
   	 		shortestPath[i] = null;
   	 	}
   	 	
   	 	// get shortest distance and path
   	 	int shortestDist = dijkstra(airportList, start, end, shortestPath);
   	 	System.out.println("\nShortest route from " + start.getCode() + " to " + end.getCode() + " is:");

   	 	// print out airports in order of shortest path
   	 	for(int i = 0; i < shortestPath.length; i++) {
   	 		if(shortestPath[i] != null) System.out.println(shortestPath[i].getCode());
   	 	}
   	 	System.out.println("Distance is " + shortestDist + " miles.\n");
	}
	
	// stores shortest path in Vertex[] shortestPath and returns shortest distance as int
	public static int dijkstra(Vertex[] airports, Vertex start, Vertex end, Vertex[] path) {
		// set initial values
		for(int i = 0; i < airports.length; i++) {
			airports[i].setVisited(false);
			airports[i].setDistFromStart(99999);
			airports[i].setPredecessor(null);
		}
		// start vertex
		Vertex current = start;
		start.setDistFromStart(0);
		
		// visiting a vertex
		while(true) {
			current.setVisited(true);
		
			// adjust distances and predecessors
			for(int i = 0; i < airports.length; i++) {
				if(current.getDistances()[i] != 1000000 && current.getDistances()[i] != 0) {
					if(current.getDistFromStart() + current.getDistances()[i] < airports[i].getDistFromStart()) {
						airports[i].setDistFromStart(current.getDistFromStart() + current.getDistances()[i]);
						airports[i].setPredecessor(current);
					}
				}
			}
			// if we've reached end
			if(current == end) {
				int i = airports.length - 1;
				while(current != start) {
					path[i] = current;
					current = current.getPredecessor();
					i--;
				}
				path[i] = start;
				return end.getDistFromStart();
			}
			// find next closest vertex from start
			Vertex closestUnvisited = null;
			int closestUnvisitedDist = 1000000;
			for(int i = 0; i < airports.length; i++) {
				if(!airports[i].wasVisited() && airports[i].getDistFromStart() < closestUnvisitedDist) {
					closestUnvisitedDist = airports[i].getDistFromStart();
					closestUnvisited = airports[i];
				}
			}
			// if we cannot reach the end
			if(closestUnvisited == null) {
				return -1;
			}
			// go to next vertex
			current = closestUnvisited;
		}
	}
}