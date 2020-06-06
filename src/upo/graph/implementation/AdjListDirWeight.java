package upo.graph.implementation;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;
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
		}
			
	}
	public double[] distanza;
	private HashMap<Integer,ArrayList<Edge>> graph;
	private Integer n;
	int counter;
	private int t;
	public AdjListDirWeight () {
		this.graph = new HashMap<Integer, ArrayList<Edge>>();
		this.n=0;
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
			
			//aggiorno tutti i riferimenti al vertice (se il vertice puntato è maggiore dell'indice, lo faccio puntare all'indice precedente
			// se il vertice puntato è il vertice da eliminare, lo elimino) per ogni vertice
			for(Integer i : this.graph.keySet()) {
				if(containsEdge(i,index))
					this.removeEdge(i, index);
				ArrayList<Edge> app = this.graph.get(i);
				for (Edge e : app) {
					if(e.getIndex()>index)
						e.setIndex(e.getIndex()-1);
				}
			}
			this.graph.remove(index);
			//aggiorno gli indici dei vertici
			for(Integer i = index +1; i< this.n-1; i++ ) {
				this.graph.put(i-1, this.graph.get(i));
			}
			//Elimino l'ultimo vertice, che sarà una copia del penultimo, dovuto all'eliminazione dell'ultimo nodo.
			this.graph.remove(n-1);
			//decremento la dimensione(adesso l'ultima chiave valida sarà n-2);
			this.n= n-1;
				
		}else {
			throw new NoSuchElementException("Indice di partenza non esiste");
		}
		
	}

	@Override
	public void addEdge(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException {
	
		ArrayList<Edge> listToUpdate;
		if(containsVertex(sourceVertexIndex) && containsVertex(targetVertexIndex)) {
			listToUpdate =this.graph.get(sourceVertexIndex);
			listToUpdate.add(new Edge(targetVertexIndex,WeightedGraph.defaultEdgeWeight));
			this.graph.put(sourceVertexIndex,listToUpdate);
		}
		else {
			throw new IllegalArgumentException("Il vertice sorgente o di destinazione non è contenuto nel grafo");
		}
	}

	@Override
	public boolean containsEdge(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException {
		if(containsVertex(sourceVertexIndex) && containsVertex(targetVertexIndex)) {
			for (Edge e: this.graph.get(sourceVertexIndex))
				if(e.getIndex() == targetVertexIndex)
					return true;
			return false;
		}else
			throw new IllegalArgumentException("Il vertice sorgente o di destinazione non è contenuto nel grafo");
	}

	@Override
	public void removeEdge(int sourceVertexIndex, int targetVertexIndex)
			throws IllegalArgumentException, NoSuchElementException {
		if(containsVertex(sourceVertexIndex) && containsVertex(targetVertexIndex) ) {
			if(!containsEdge(sourceVertexIndex,targetVertexIndex))
				throw new NoSuchElementException("L'Arco non esiste");
			for (Edge e : this.graph.get(sourceVertexIndex)){
				if(e.getIndex() == targetVertexIndex) {
					this.graph.get(sourceVertexIndex).remove(e);
					break; //se trovo l'elemento evito di continuare il ciclo
				}
			}
		}else {
			throw new IllegalArgumentException("Il vertice sorgente o di destinazione non è contenuto nel grafo");
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
		if(!containsVertex(sourceVertexIndex) || !containsVertex(targetVertexIndex) )
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
		if(!containsVertex(startingVertex)) {
			throw new IllegalArgumentException("Vertice di partenza non valido");
		}
		
		Stack<Integer> d = new Stack<Integer>();
		VisitForest ret = new VisitForest(this,VisitForest.VisitType.DFS);
		ret.setColor(startingVertex, VisitForest.Color.GRAY);		
		ret.setStartTime(startingVertex, counter);
		d.push(startingVertex);
		while(!d.isEmpty()) {
			int u = d.pop();
			ret.setStartTime(u, counter);
			for(Integer v: getAdjacent(u)) {
				if(ret.getColor(v) == VisitForest.Color.WHITE) {
					ret.setColor(v, VisitForest.Color.GRAY);
					ret.setParent(v,u);
					d.push(v);
				}
			}
			ret.setEndTime(u, counter);
			ret.setColor(u, VisitForest.Color.BLACK);
			counter++;
		}
		return ret;
		
		
	}
	@Override
	public VisitForest getDFSTOTForest(int startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		counter = 0;
		VisitForest ret = new VisitForest(this, VisitForest.VisitType.DFS_TOT);
		if(this.containsVertex(startingVertex)) {
			for(int i : this.graph.keySet()) {
				if(ret.getColor(i) == VisitForest.Color.WHITE) {
					Utils.addVisitForest(ret, getDFSTree(i), size());
				}
			}
		counter = 0;
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
		
	}

	@Override
	public Set<Set<Integer>> connectedComponents() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Impossibile calcolare le componenti connesse in un grafo orientato. Prova con stronglyConnectedComponents");
	}

	@Override
	public double getEdgeWeight(int sourceVertexIndex, int targetVertexIndex)
			throws IllegalArgumentException, NoSuchElementException {
		if(!containsVertex(targetVertexIndex) || !containsVertex(sourceVertexIndex))
			throw new IllegalArgumentException("Il vertice di arrivo o di partenza non esiste");
		if(!containsEdge(sourceVertexIndex,targetVertexIndex))
			throw new NoSuchElementException("L'arco non esiste");
		double d = 0.0;
		for (Edge e: this.graph.get(sourceVertexIndex)) {
			if(e.getIndex()==targetVertexIndex)
				d= e.getWeight();		
		}	
		return d; //evito errore in fase di compilazione, non necessario.
	}

	@Override
	public void setEdgeWeight(int sourceVertexIndex, int targetVertexIndex, double weight)
			throws IllegalArgumentException, NoSuchElementException {
		
		if(!containsVertex(targetVertexIndex) || !containsVertex(sourceVertexIndex))
			throw new IllegalArgumentException("Il vertice di arrivo o di partenza non esiste");
		if(!containsEdge(sourceVertexIndex,targetVertexIndex))
			throw new NoSuchElementException("L'arco non esiste");
		for (Edge e: this.graph.get(sourceVertexIndex)) {
			if(e.getIndex()==targetVertexIndex) {
				e.setWeight(weight);
			}
			
		}
		
	}

	@Override
	public WeightedGraph getBellmanFordShortestPaths(int startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("Non implementata");
	}
	
	/*
	 * Al posto di questo controllo si potrebbe pensare in fase di inserimento degli archi
	 * di valorizzare un attributo inizializzato a 0 di default nel costruttore 
	 * della classe che funzioni da contatore degli archi con peso negativo.
	 * Se viene inserito un arco con peso negativo count++
	 * Se viene rimosso un arco con peso negativo count--
	 * Se viene modificato il peso di un arco che prima era negativo con uno positivo count --
	 * La funzione hasNegativeWeigth diventerebbe return count==0
	 */
	/*
	 * La funzione serve a verificare che non esistano archi con pesi negativi all'interno del grafo
	 */
	private Boolean hasNegativeWeigth() {
		for(Integer x : this.graph.keySet())
			for(Edge e: this.graph.get(x))
				if(e.getWeight()<0)
					return false;
		return true;
	}
	@Override
	public WeightedGraph getDijkstraShortestPaths(int startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		if(!hasNegativeWeigth())
			throw new UnsupportedOperationException("Impossibile eseguire Dijkstra con archi negativi");
		
		if(this.containsVertex(startingVertex)){
			//start inizializzazione
			PriorityQueueDouble pqd = new PriorityQueueDouble();
			pqd.enqueue(startingVertex, 0);
			//double[] distanza = new double[n];
			distanza = new double[n];
			boolean[] def = new boolean[n];
			AdjListDirWeight ritorno = new AdjListDirWeight();
			for(Integer i : this.graph.keySet()) {
				ritorno.addVertex();
				def[i] = false;
				if(containsEdge(startingVertex,i)) {
					pqd.enqueue(i, getEdgeWeight(startingVertex,i));
				}
				else {
					pqd.enqueue(i, Integer.MAX_VALUE);
				}
				distanza[i] = Integer.MAX_VALUE;
				}
			distanza[startingVertex] = 0;
			//end inizializzazione
			while (!pqd.isEmpty()) {
				int u = pqd.dequeue();
				def[u] = true;
				for(int v : getAdjacent(u)) {
					if(def[v] == false && distanza[v] > distanza[u] + getEdgeWeight(u,v)) {
						distanza[v] = distanza[u] + getEdgeWeight(u,v);
						if(!ritorno.containsEdge(u,v))
							ritorno.addEdge(u, v);
						ritorno.setEdgeWeight(u, v, getEdgeWeight(u,v));
						pqd.modify_priority(v, distanza[v]);
					}
				//	actualNode = u;
				}	
			}
			return ritorno;
		}
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
