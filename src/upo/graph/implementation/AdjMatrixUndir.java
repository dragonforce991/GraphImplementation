package upo.graph.implementation;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Set;

import upo.graph.base.*;
import java.util.HashSet;


/**
 * Implementazione mediante <strong>matrice di adiacenza</strong> di un grafo <strong>non orientato non pesato</strong>.
 * 
 * @author Antonino Massarotti Matricola
 *
 */
public class AdjMatrixUndir implements Graph{

	private ArrayList <ArrayList<Integer>> AdjMatrix;
	private int nodes;

	public AdjMatrixUndir(){
		AdjMatrix = new ArrayList<>();
		nodes = 0;
	}
	
	@Override
	public int addVertex() {
		ArrayList<Integer> temp = new ArrayList<>();
		for(int i = 0; i < nodes; i++){
			temp.add(0);

		}
		temp.add(0);
		for(ArrayList<Integer> iter : AdjMatrix){
			iter.add(0);
		}

		AdjMatrix.add(temp);
		return nodes++;
	}

	@Override
	public boolean containsVertex(int index) {
		
		return index >= 0 && index < nodes;
	}

	@Override
	public void removeVertex(int index) throws NoSuchElementException {
		if(containsVertex(index)){
			for(ArrayList<Integer> i : AdjMatrix){
				i.remove(index);
				
			}
			AdjMatrix.remove(index);
			nodes--;
		}
		else{
			throw new NoSuchElementException("Indice di partenza non esiste");
		}
	}

	@Override
	public void addEdge(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException {

		if(containsVertex(sourceVertexIndex) && containsVertex(targetVertexIndex)){
			
				AdjMatrix.get(sourceVertexIndex).set(targetVertexIndex,1);
				AdjMatrix.get(targetVertexIndex).set(sourceVertexIndex,1);
			
		}
		else throw new IllegalArgumentException("Il vertice sorgente o di destinazione non è contenuto nel grafo");
		
	}

	@Override
	public boolean containsEdge(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException {
		if(containsVertex(sourceVertexIndex) && containsVertex(targetVertexIndex)){
			
				return AdjMatrix.get(sourceVertexIndex).get(targetVertexIndex) == 1;
			
		}
		else throw new IllegalArgumentException("Vertice non presente");
		
	}

	@Override
	public void removeEdge(int sourceVertexIndex, int targetVertexIndex)
			throws IllegalArgumentException, NoSuchElementException {
				
					if(containsVertex(sourceVertexIndex) && containsVertex(targetVertexIndex)){
						if(containsEdge(sourceVertexIndex, targetVertexIndex)){
							AdjMatrix.get(sourceVertexIndex).set(targetVertexIndex,0);
							AdjMatrix.get(targetVertexIndex).set(sourceVertexIndex,0);
						}
						else throw new NoSuchElementException("L'Arco non esiste");
					}
				
				else throw new IllegalArgumentException("Il vertice sorgente o di destinazione non è contenuto nel grafo");
		
	}

	@Override
	public Set<Integer> getAdjacent(int vertexIndex) throws NoSuchElementException {
		if(containsVertex(vertexIndex)){
			HashSet<Integer> Adj = new HashSet<Integer>();
			ArrayList<Integer> retrieve = AdjMatrix.get(vertexIndex);
			for(int i = 0; i<nodes; i++){
				if(retrieve.get(i) == 1){
					Adj.add(i);
				}
			}
			return Adj;
		}
		throw new NoSuchElementException("L'elemento non esiste");
	}

	@Override
	public boolean isAdjacent(int targetVertexIndex, int sourceVertexIndex) throws IllegalArgumentException {
		return containsEdge(sourceVertexIndex, targetVertexIndex);
	}

	@Override
	public int size() {
		return nodes;
	}

	@Override
	public boolean isDirected() {
		return false;
	}

	@Override
	public boolean isCyclic() {
		VisitForest VF = new VisitForest(this,VisitForest.VisitType.BFS);
		for(int i = 0; i<nodes; i++){
			if(VF.getColor(i) == VisitForest.Color.WHITE && Recursive_Visit_Cyclic_check(i, VF)){
				return true;
			}

		}
		return false;
	}

	private boolean Recursive_Visit_Cyclic_check(int index, VisitForest VF){
		VF.setColor(index, VisitForest.Color.GRAY);
		for(Integer i : getAdjacent(index)){
			if(VF.getColor(i) == VisitForest.Color.WHITE ){
				VF.setParent(i,index);
				if (Recursive_Visit_Cyclic_check(i, VF))
					return true;
			}
			else if(VF.getPartent(index) != i){
				return true;
			}
		}
		VF.setColor(index, VisitForest.Color.BLACK);
		return false;
	}

	@Override
	public boolean isDAG() {
		return false;
	}

	@Override
	public VisitForest getBFSTree(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		if(this.containsVertex(startingVertex)){
			VisitForest VF = new VisitForest(this,VisitForest.VisitType.BFS);
			ArrayList<Integer> queue = new ArrayList<Integer>();
			queue.add(startingVertex);
			VF.setColor(startingVertex, VisitForest.Color.GRAY);
			while(!queue.isEmpty()){
				int temp = queue.remove(0);
				for(int i : this.getAdjacent(temp)){
					if(VF.getColor(i) == VisitForest.Color.WHITE){
						queue.add(i);
						VF.setParent(i, temp);
						VF.setColor(i,  VisitForest.Color.GRAY);
					}
				}
				VF.setColor(temp, VisitForest.Color.BLACK);
			}
			return VF;
		}
		else throw new IllegalArgumentException("Vertice di partenza non valido");
	}

	@Override
	public VisitForest getDFSTree(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		if(this.containsVertex(startingVertex)){
			VisitForest VF = new VisitForest(this, VisitForest.VisitType.DFS);
			ArrayList<Integer> stack = new ArrayList<Integer>();
			stack.add(startingVertex);
			VF.setColor(startingVertex, VisitForest.Color.GRAY);
			while(!stack.isEmpty()){
				int actual = stack.get(0);
				int adj = getFirstWhiteAdjacent(actual, VF);
				if(adj != -1){
					stack.add(0,adj);
					VF.setColor(adj, VisitForest.Color.GRAY);
					VF.setParent(adj,actual);
				}
				else{
					VF.setColor(actual, VisitForest.Color.BLACK);
					stack.remove(0);
				}
			}
			return VF;
		}
		else throw new IllegalArgumentException("Vertice di partenza non valido");
	}

	private int getFirstWhiteAdjacent(int index, VisitForest VF) throws IllegalArgumentException{
		if(containsVertex(index)){
			for(int i = 0; i < nodes; i++){
				if(AdjMatrix.get(index).get(i) != 0 && VF.getColor(i) == VisitForest.Color.WHITE)
					return i;
			}
			return -1;
		}
		throw new IllegalArgumentException();
	}

	@Override
	public VisitForest getDFSTOTForest(int startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		
			VisitForest VF = new VisitForest(this,VisitForest.VisitType.DFS_TOT);
			for(int i = 0; i < size(); i++){
				if(VF.getColor(i) == VisitForest.Color.WHITE){
					Utils.addVisitForest(VF, getDFSTree(i), size());
				}
			}
			return VF;
		
		
		
	}

	@Override
	public VisitForest getDFSTOTForest(int[] vertexOrdering)
			throws UnsupportedOperationException, IllegalArgumentException {
		VisitForest VF = new VisitForest(this, VisitForest.VisitType.DFS_TOT);
		for(int vertex : vertexOrdering){
			if(containsVertex(vertex)){
				if(VF.getColor(vertex) == VisitForest.Color.WHITE)
					Utils.addVisitForest(VF, getDFSTree(vertex), size());
			}
			else throw new IllegalArgumentException("Vertice di partenza non valido");
		}
		return VF;
	}

	@Override
	public int[] topologicalSort() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Set<Integer>> stronglyConnectedComponents() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Set<Integer>> connectedComponents() throws UnsupportedOperationException {
		VisitForest VF = getDFSTOTForest(0);
		Set<Set<Integer>> connected = new HashSet<Set<Integer>>();
		for(Integer x : VF.getRoots()) {
			connected.add(Utils.getTree(VF, x, size()));
		}
		return connected;
	}

}
