package upo.graph.implementation;

public class Edge {
	
	private int Index;
	private double Weight;
	

	public Edge(int index, double weight) {
		Index = index;
		Weight = weight;
	}
	
	public int getIndex() {
		return Index;
	}
	public void setIndex(int index) {
		Index = index;
	}
	public double getWeight() {
		return Weight;
	}
	public void setWeight(double weight) {
		Weight = weight;
		System.out.println("Weight : " + weight);
	}
		
}
