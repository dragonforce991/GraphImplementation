package upo.graph.implementation;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.LinkedList; 
import java.util.Queue;
import upo.graph.base.*;
/**
 * Implementazione mediante <strong>liste di adiacenza</strong> di un grafo <strong>orientato pesato</strong>.
 * 
 * @author Davide D'Angelo 20035563
 *
 */
public class AdjListDirWeight implements WeightedGraph{

	private HashMap<Integer,ArrayList<Edge>> graph;
	private Integer n;
	private int t;
	public AdjListDirWeight () {
		this.graph = new HashMap<Integer, ArrayList<Edge>>();
		this.n=0;
	}
	public AdjListDirWeight (HashMap<Integer,ArrayList<Edge>> g) {
		this();
		for (Integer i: g.keySet()) {
			this.addVertex();
			for (Edge e : g.get(i)) {
				this.addEdge(i, e.getIndex());
				this.setEdgeWeight(i,e.getIndex(),e.getWeight());
			}
		}
		
	}
	
	@Override
	public int addVertex() {
		this.graph.put(this.n, new ArrayList<Edge>());
		return this.n++;
	}
	@Override
	public boolean containsVertex(int index) {
		return this.graph.containsKey(index);
	}

	@Override
	public void removeVertex(int index) throws NoSuchElementException {
		//verifico l'esistenza del vertice da eliminare
		if(this.containsVertex(index)) {
			//elimino il vertice
			this.graph.remove(index);
			//aggiorno tutti i riferimenti al vertice (se il vertice puntato è maggiore dell'indice, lo faccio puntare all'indice precedente
			// se il vertice puntato è il vertice da eliminare, lo elimino) per ogni vertice
			for(Integer i : this.graph.keySet()) {
				this.removeEdge(i, index);
				ArrayList<Edge> app = this.graph.get(i);
				for (Edge e : app) {
					if(e.getIndex()>index)
						e.setIndex(e.getIndex()-1);
				}
			}
			//aggiorno gli indici dei vertici
			for(Integer i = index +1; i< this.n-1; i++ ) {
				this.graph.put(i-1, this.graph.get(i));
			}
			//Elimino l'ultimo vertice, che sarà una copia del penultimo, dovuto all'eliminazione dell'ultimo nodo.
			this.graph.remove(n-1);
			//decremento la dimensione(adesso l'ultima chiave valida sarà n-2);
			this.n= n-1;
				
		}
		
	}

	@Override
	public void addEdge(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException {
	
		ArrayList<Edge> listToUpdate;
		if(this.graph.containsKey(sourceVertexIndex)) {
			listToUpdate =this.graph.get(sourceVertexIndex);
			if (listToUpdate == null)
				listToUpdate = new ArrayList<Edge>();
			else {
				for (Edge e : listToUpdate) {
					if(e.getIndex()== targetVertexIndex)
						throw new IllegalArgumentException("Il vertice target è già contenuto nel vertice sorgente");						
				}
			}
			listToUpdate.add(new Edge(targetVertexIndex,WeightedGraph.defaultEdgeWeight));
			this.graph.put(sourceVertexIndex,listToUpdate);
		}
	}

	@Override
	public boolean containsEdge(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException {
		if(this.containsVertex(sourceVertexIndex)) {
			for (Edge e: this.graph.get(sourceVertexIndex))
				if(e.getIndex() == targetVertexIndex)
					return true;
			return false;
		}
		throw new IllegalArgumentException("il vertice source non esiste");
	}

	@Override
	public void removeEdge(int sourceVertexIndex, int targetVertexIndex)
			throws IllegalArgumentException, NoSuchElementException {
		if(this.containsVertex(sourceVertexIndex)) {
			for (Edge e : this.graph.get(sourceVertexIndex)){
				if(e.getIndex() == targetVertexIndex) {
					this.graph.get(sourceVertexIndex).remove(e);
					break; //se trovo l'elemento evito di continuare il ciclo
				}
			}
		}
	}

	@Override
	public Set<Integer> getAdjacent(int vertexIndex) throws NoSuchElementException {
		HashSet<Integer> ret = new HashSet<Integer>();
		if(containsVertex(vertexIndex)) {
			for (Edge e: this.graph.get(vertexIndex))
				ret.add(e.getIndex());
		}else {
			throw new NoSuchElementException("L'elemento non esiste");
		}
		return ret;
	}

	@Override
	public boolean isAdjacent(int targetVertexIndex, int sourceVertexIndex) throws IllegalArgumentException {
		if(!containsVertex(sourceVertexIndex) || !containsVertex(sourceVertexIndex) )
			throw new IllegalArgumentException("Elemento non presente");
		return getAdjacent(sourceVertexIndex).contains(targetVertexIndex);
	}

	@Override
	public int size() {
		return n;
	}

	@Override
	public boolean isDirected() {
		return true;
	}

	@Override
	public boolean isCyclic() {
		int color[];
		int r[];
		color = new int[n];
		r = new int[n];
		for (int i = 0; i<n; i++) {
			r[i] = -1;
			color[i] = 0; //color White
		}
		for(int i : this.graph.keySet())
			if(color[i] == 0 &&  VisitaRic(i, color,r))
				return true;
		return false;
		

	}
	private boolean VisitaRic(int i, int [] color, int[] r) {
		color[i] = 1; //color gray
		for (int j : getAdjacent(i)) {
			if(color[j] == 0) {
				r[j] = i;
				if (VisitaRic(j,color,r)) return true;
			}
			else if(color[j] == 1) return true;
		}
		color[i] = 2;
		return false;
		
				
	}

	@Override
	public boolean isDAG() {
		return !isCyclic();
	}

	@Override
	public VisitForest getBFSTree(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		VisitForest ret = new VisitForest(this,VisitForest.VisitType.BFS);
		if(this.containsVertex(startingVertex)){
			Queue<Integer> D = new LinkedList<>();
			D.add(startingVertex);
			ret.setColor(startingVertex, VisitForest.Color.GRAY);
			while(D.size()>0) {
				int u = D.peek();
				for (int v : getAdjacent(u))
					if (ret.getColor(v) == VisitForest.Color.WHITE) {
						ret.setColor(v,VisitForest.Color.WHITE );
						ret.setParent(v, u);
						D.add(v);
					}
				ret.setColor(u, VisitForest.Color.BLACK);
				D.remove();
			}
			return ret;
		}	
		else 
			throw new IllegalArgumentException("Vertice di partenza non valido");
	}

	@Override
	public VisitForest getDFSTree(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		
		if(this.containsVertex(startingVertex)) {
			VisitForest ret = new VisitForest(this,VisitForest.VisitType.DFS);
			ret.setColor(startingVertex, VisitForest.Color.GRAY);		
			ret.setStartTime(startingVertex, Utils.getDate());
			for (int v : getAdjacent(startingVertex)) {
				if (ret.getColor(v) == VisitForest.Color.WHITE) {
					ret.setParent(v, startingVertex);
					getDFSTree(v);
				}
				ret.setColor(startingVertex, VisitForest.Color.BLACK);
				ret.setEndTime(startingVertex, Utils.getDate());
			}
			return ret;
		}
		else 
			throw new IllegalArgumentException("Vertice di partenza non valido");
	}

	@Override
	public VisitForest getDFSTOTForest(int startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		VisitForest ret = new VisitForest(this, VisitForest.VisitType.DFS_TOT);
		if(this.containsVertex(startingVertex)) {
			for(int i : this.graph.keySet()) {
				if(ret.getColor(i) == VisitForest.Color.WHITE) {
					Utils.addVisitForest(ret, getDFSTree(i), size());
				}
			}
			return ret;
		}
		else 
			throw new IllegalArgumentException("Vertice di partenza non valido");
	}

	@Override
	public VisitForest getDFSTOTForest(int[] vertexOrdering)
			throws UnsupportedOperationException, IllegalArgumentException {
		for (int startingVertex: vertexOrdering)
			if(!containsVertex(startingVertex)) 
				throw new IllegalArgumentException("Vertice di partenza non valido");
		VisitForest ret = new VisitForest(this, VisitForest.VisitType.DFS_TOT);
		for(int startingVertex : vertexOrdering)
			if(ret.getColor(startingVertex) == VisitForest.Color.WHITE)
					Utils.addVisitForest(ret,getDFSTree(startingVertex), size());
				
		
		return ret;
	}

	@Override
	public int[] topologicalSort() throws UnsupportedOperationException {
		if(!isDAG())
			throw new UnsupportedOperationException("Impossibile eseguire ordinamento topologico su un grafo non DAG");
		int ord[] = new int[n];
		t = n-1;
		
		VisitForest app = new VisitForest(this,VisitForest.VisitType.DFS);
		for (Integer i: this.graph.keySet()) {
			if(app.getColor(i) == VisitForest.Color.WHITE) {
				DFS_TOPOLOGICAL(i,ord,app);
			}		
		}
		return ord;
	}
	
	private void DFS_TOPOLOGICAL(int u, int[] ord, VisitForest app) {
		app.setColor(u, VisitForest.Color.GRAY);
		
		for (Integer j: getAdjacent(u)) {
			if (app.getColor(j) == VisitForest.Color.WHITE)
				DFS_TOPOLOGICAL(j,ord,app);
		}
		app.setColor(u, VisitForest.Color.BLACK);
		ord[t] = u;
		t = t-1;
	}

	@Override
	public Set<Set<Integer>> stronglyConnectedComponents() throws UnsupportedOperationException {
		VisitForest ret = getDFSTOTForest(0);
		int dim[] = new int[n];
		ArrayList<Integer> l = new ArrayList<Integer>();
		for(int i = 0; i<n; i++)
			l.add(ret.getEndTime(i));
		for (int i = 0; i<n; i++)
			dim[i] = Utils.getMax(l);
		AdjListDirWeight trasposto = Utils.createT(this);
		ret = trasposto.getDFSTOTForest(dim);
		
		Set<Set<Integer>> scc = new HashSet<Set<Integer>>();
		for(Integer x : ret.getRoots()) {
			scc.add(Utils.getTree(ret, x, n));
		}
		return scc;
		//throw new UnsupportedOperationException("Ancora da implementare");
	}

	@Override
	public Set<Set<Integer>> connectedComponents() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Impossibile calcolare le componenti connesse in un grafo orientato. Prova con stronglyConnectedComponents");
	}

	@Override
	public double getEdgeWeight(int sourceVertexIndex, int targetVertexIndex)
			throws IllegalArgumentException, NoSuchElementException {
		if(containsVertex(sourceVertexIndex)){
			for (Edge e: this.graph.get(sourceVertexIndex)) {
				if(e.getIndex()==targetVertexIndex)
					return e.getWeight();		
			}
			throw new IllegalArgumentException("Da implementare il vertice di arrivo non esiste(forse meglio lanciare l'exception prima, eseguendo una containsVertex?)");
		}else {
			throw new NoSuchElementException("Da Implementare il vertice di partenza non esiste");
		}	
	}

	@Override
	public void setEdgeWeight(int sourceVertexIndex, int targetVertexIndex, double weight)
			throws IllegalArgumentException, NoSuchElementException {
		if(!containsVertex(targetVertexIndex))
			throw new IllegalArgumentException("Il vertice di arrivo non esiste");
		if(containsVertex(sourceVertexIndex)){
			for (Edge e: this.graph.get(sourceVertexIndex)) {
				if(e.getIndex()==targetVertexIndex) {
					e.setWeight(weight);
					return; //se lancio l'eccezione prima rimuovere
				}
			}
		}else {
			throw new NoSuchElementException("Il vertice di partenza non esiste");
		}	
	}

	@Override
	public WeightedGraph getBellmanFordShortestPaths(int startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("Non implementata");
	}

	@Override
	public WeightedGraph getDijkstraShortestPaths(int startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		if(this.containsVertex(startingVertex))
			throw new UnsupportedOperationException("Ancora da implementare");
		else 
			throw new IllegalArgumentException("Vertice di partenza non valido");
	}

	@Override
	public WeightedGraph getPrimMST(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("Non implementata");
	}

	@Override
	public WeightedGraph getKruskalMST() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Non implementata");
	}

	@Override
	public WeightedGraph getFloydWarshallShortestPaths() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Non implementata");
	}

}
