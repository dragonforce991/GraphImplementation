package upo.greedy;

import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;


import upo.graph.implementation.AdjMatrixUndir;
public class Greedy {
	
	/** Calcola una codifica di Huffman per i caratteri contenuti nel vettore characters, date le frequenze 
	 * contenute in f. f[i] contiene la frequenza (in percentuale, 0<=f[i]<=100) del carattere characters[i] 
	 * nel testo per il quale si vuole calcolare la codifica.
	 * </br>CONSIGLIO: potete estendere o usare un vostro grafo non pesato non orientato per rappresentare la 
	 * foresta di Huffman.
	 * </br>CONSIGLIO2: potete implementate una PriorityQueue dall'interfaccia in upo.additionalstructures,
	 * oppure aggiungere al grafo del primo consiglio delle priorit�.
	 * 
	 * @param characters i caratteri dell'alfabeto per i quali calcolare la codifica.
	 * @param f le frequenze dei caratteri in characters nel dato testo.
	 * @return una Map che mappa ciascun carattere in una stringa che rappresenta la sua codifica secondo 
	 * l'algoritmo visto a lezione.
	 */
	public static class wrapper{
		Character character;
		int freq;
		int index;
		public wrapper(Character character, int freq, int index){
			this.character = character;
			this.freq = freq;
			this.index = index;
		}
		public Character getCharacter(){
			return this.character;
		}
		public int getFreq(){
			return this.freq;
		}
		
		public int getIndex(){
			return this.index;
		}
	}

	private static void insertWithPriority(ArrayList<wrapper> L, wrapper newNode){
		for(int i = 0; i < L.size(); i++){
			if(L.get(i).getFreq()>=newNode.getFreq()){
				L.add(i, newNode);
				return;
			}
		}
		L.add(newNode);
	}


	public static void recursiveDFSHuffmanTreeVisit(int node, AdjMatrixUndir graph, ArrayList<wrapper> Nodes, ArrayList<Boolean> visited, Map<Character,String> res, String code){
		Set<Integer> adj = graph.getAdjacent(node);
		ArrayList<wrapper> priority = new ArrayList<>();
		if(visited == null){
			visited = new ArrayList<Boolean>();
			for(int i = 0; i<graph.size(); i++){
				visited.add(false);
			}
		}
		visited.set(node, true);
		for(Integer i : adj){
			if(!visited.get(i)){
				insertWithPriority(priority,Nodes.get(i));
			}
		}
		if(priority.isEmpty()){
			res.put(Nodes.get(node).getCharacter(),code);
		}
		else{
			recursiveDFSHuffmanTreeVisit(priority.get(0).getIndex(), graph, Nodes, visited, res, code+"0");
			recursiveDFSHuffmanTreeVisit(priority.get(1).getIndex(), graph, Nodes, visited, res, code+"1");
		}
	}

	public static Map<Character,String> getHuffmanCodes(Character[] characters, int[] f) {
		ArrayList<wrapper> priority = new ArrayList<>();
		ArrayList<wrapper> Nodes = new ArrayList<>();
		int n = characters.length;
		AdjMatrixUndir graph = new AdjMatrixUndir();
		for(int i = 0; i<characters.length; i++){
			Greedy.wrapper temp = new Greedy.wrapper(characters[i], f[i], i);
			
			insertWithPriority(priority,temp);
			graph.addVertex();
			Nodes.add(temp);
		}

		for(int i = 0; i<n-1; i++){
			wrapper x = priority.remove(0);
			wrapper y = priority.remove(0);
			wrapper z = new wrapper(null, x.getFreq()+y.getFreq(),graph.size());
			Nodes.add(z);
			Greedy.insertWithPriority(priority, z);
			graph.addVertex();
			graph.addEdge(z.getIndex(), x.getIndex());
			graph.addEdge(z.getIndex(), y.getIndex());
		}
		HashMap<Character,String> Huffman = new HashMap<>();
		recursiveDFSHuffmanTreeVisit(priority.get(0).getIndex(),graph,Nodes,null,Huffman,"");
		return Huffman;
	}

}
