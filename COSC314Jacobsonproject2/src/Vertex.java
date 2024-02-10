// This file defines a Vertex, representing an airport.
public class Vertex {
	
	private String code;
	private int[] distances; // distances to each airport
	private int distFromStart;
	private Vertex predecessor;
	private boolean visited;
	
	public Vertex(String c, int[] d) {
		code = c;
		distances = new int[d.length];
		for(int i = 0; i < d.length; i++) {
			distances[i] = d[i];
		}
		distFromStart = 99999;
	}
	
	public String getCode() {
		return code;
	}
	public int[] getDistances() {
		return distances;
	}
	public int getDistFromStart() {
		return distFromStart;
	}
	public void setDistFromStart(int d) {
		distFromStart = d;
	}
	public Vertex getPredecessor(){
		return predecessor;
	}
	public void setPredecessor(Vertex v) {
		predecessor = v;
	}
	public boolean wasVisited() {
		return visited;
	}
	public void setVisited(boolean v) {
		visited = v;
	}
}
