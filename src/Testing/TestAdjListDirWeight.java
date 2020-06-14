package Testing;

import org.junit.Assert;

import static org.junit.Assert.assertThrows;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import org.junit.jupiter.api.Test;
//import junit.framework.TestCase;
import upo.graph.base.VisitForest;
import upo.graph.implementation.AdjListDirWeight;

/*
 * @Author Davide D'Angelo 20035563
 */
public class TestAdjListDirWeight {
    @Test
    public void baseImplementationTest() {	
    	AdjListDirWeight g = new AdjListDirWeight();
		for(int i = 0; i<10; i++)
			g.addVertex();
		g.addEdge(0, 1);
		g.addEdge(0, 2);
		g.addEdge(0, 3);
		g.addEdge(0, 4);
		g.addEdge(0, 5);
		g.addEdge(1,6);
		g.addEdge(7, 6);  
		HashSet<Integer> adj = new HashSet<Integer>();
		adj.add(1);
		adj.add(2);
		adj.add(3);
		adj.add(4);
		adj.add(5);
		HashSet<Integer> adj2 = new HashSet<Integer>();
		adj2.add(6);
		
		Assert.assertEquals(adj,g.getAdjacent(0));
		Assert.assertEquals(adj2,g.getAdjacent(1));
		Assert.assertEquals(adj2,g.getAdjacent(7));
		
		g.removeVertex(5);
		
		adj.remove(5); // prima puntava direttamente a 5. Adesso che il vertice è stato eliminato va rimosso.
		adj2.clear();
		adj2.add(5); // prima conteneva 6. rimuovendo il vertice 5, tutti i vertici >5 sono stati decrementati. Adesso conterrà 5.
		
		Assert.assertEquals(adj,g.getAdjacent(0));
		Assert.assertEquals(adj2,g.getAdjacent(1));
		Assert.assertEquals(adj2,g.getAdjacent(6)); //Devo verificare con 6, e non più 7, perchè i vertici sono decrementati
		Assert.assertEquals(new HashSet<Integer>(),g.getAdjacent(7));

		g.removeEdge(0, 4);
		adj.remove(4);
		
		Assert.assertEquals(adj,g.getAdjacent(0));
		Assert.assertEquals(adj2,g.getAdjacent(1));
		Assert.assertTrue(g.containsEdge(0,1));
		Assert.assertFalse(g.containsEdge(0,6));
		Assert.assertEquals(1.0,g.getEdgeWeight(0, 1),0);
		g.setEdgeWeight(0,1, 15);
		Assert.assertEquals(15.0,g.getEdgeWeight(0, 1),0);
		
		//Exception Test
		Exception exception = assertThrows(NoSuchElementException.class , ()->{g.removeVertex(15);} );
		Assert.assertTrue(exception.getMessage().contains("Indice di partenza non esiste") );
		Exception ex = assertThrows(IllegalArgumentException.class,()->{g.addEdge(15, 16);});
		Assert.assertTrue(ex.getMessage().contains("Il vertice sorgente o di destinazione non è contenuto nel grafo") );
		ex = assertThrows(IllegalArgumentException.class,()->{g.addEdge(7, 15);});
		Assert.assertTrue(ex.getMessage().contains("Il vertice sorgente o di destinazione non è contenuto nel grafo") );
		ex = assertThrows(IllegalArgumentException.class,()->{g.addEdge(15,6);});
		Assert.assertTrue(ex.getMessage().contains("Il vertice sorgente o di destinazione non è contenuto nel grafo") );
		ex = assertThrows(IllegalArgumentException.class,()->{g.containsEdge(15,6);});
		Assert.assertTrue(ex.getMessage().contains("Il vertice sorgente o di destinazione non è contenuto nel grafo") );
		ex = assertThrows(IllegalArgumentException.class,()->{g.containsEdge(6,15);});
		Assert.assertTrue(ex.getMessage().contains("Il vertice sorgente o di destinazione non è contenuto nel grafo") );
		ex = assertThrows(IllegalArgumentException.class,()->{g.removeEdge(6,15);});
		Assert.assertTrue(ex.getMessage().contains("Il vertice sorgente o di destinazione non è contenuto nel grafo") );
		ex = assertThrows(IllegalArgumentException.class,()->{g.removeEdge(15,6);});
		Assert.assertTrue(ex.getMessage().contains("Il vertice sorgente o di destinazione non è contenuto nel grafo") );
		ex = assertThrows(IllegalArgumentException.class,()->{g.removeEdge(15,16);});
		Assert.assertTrue(ex.getMessage().contains("Il vertice sorgente o di destinazione non è contenuto nel grafo") );
		ex = assertThrows(NoSuchElementException.class,()->{g.removeEdge(6,3);});
		Assert.assertTrue(ex.getMessage().contains("L'Arco non esiste") );
    }
    
    @Test
    public void getAdjacentTest() {
    	AdjListDirWeight g = new AdjListDirWeight();
		for(int i = 0; i<10; i++)
			g.addVertex();
		g.addEdge(0, 1);
		g.addEdge(0, 2);
		g.addEdge(0, 3);
		g.addEdge(0, 4);
		g.addEdge(0, 5);
		HashSet<Integer> ret = new HashSet<Integer>();
		ret.add(1);
		ret.add(2);
		ret.add(3);
		ret.add(4);
		ret.add(5);
		Assert.assertEquals(ret ,g.getAdjacent(0));
		Exception ex = assertThrows(NoSuchElementException.class,()->{g.getAdjacent(11);});
		Assert.assertTrue(ex.getMessage().contains("L'elemento non esiste") );
    }
    @Test
    public void isAdjacentTest() {
    	AdjListDirWeight g = new AdjListDirWeight();
		for(int i = 0; i<4; i++)
			g.addVertex();
		g.addEdge(0, 1);
		Assert.assertTrue(g.isAdjacent(1, 0));
		Exception ex =  assertThrows(IllegalArgumentException.class,()->{g.isAdjacent(1,11);});
		Assert.assertTrue(ex.getMessage().contains("Elemento non presente") );
		ex =  assertThrows(IllegalArgumentException.class,()->{g.isAdjacent(11,1);});
		Assert.assertTrue(ex.getMessage().contains("Elemento non presente") );
    }
    @Test
    public void isDAGTest() {
    	AdjListDirWeight g = new AdjListDirWeight();
    	Assert.assertTrue(g.isDAG());
		for(int i = 0; i<10; i++)
			g.addVertex();
		g.addEdge(0, 1);
	    g.addEdge(1, 2);
		g.addEdge(2, 3);
		g.addEdge(3, 4);
		g.addEdge(4, 5);
		g.addEdge(5, 6);
		g.addEdge(6, 7);
		g.addEdge(7, 8);
		g.addEdge(8, 9);
		Assert.assertTrue(g.isDAG());
		g.addEdge(9,0);
		Assert.assertFalse(g.isDAG());
    	
    	
    }
    @Test
    public void isDirectedTest() {
    	AdjListDirWeight g = new AdjListDirWeight();
    	Assert.assertTrue(g.isDirected());
    }
    @Test
    public void isCyclicTest() {
		AdjListDirWeight g = new AdjListDirWeight();
		for(int i = 0; i<10; i++)
			g.addVertex();
		g.addEdge(0, 1);
	    g.addEdge(1, 2);
		g.addEdge(2, 3);
		g.addEdge(3, 4);
		g.addEdge(4, 5);
		g.addEdge(5, 6);
		g.addEdge(6, 7);
		g.addEdge(7, 8);
		g.addEdge(8, 9);
		Assert.assertFalse(g.isCyclic());
		g.addEdge(9,0);
		Assert.assertTrue(g.isCyclic());
    }
    
    
    @Test
    public void topologicalSortTest() {
    	AdjListDirWeight g = new AdjListDirWeight();
		for (int i = 0; i<5; i++)
			g.addVertex();
		g.addEdge(0, 1);
		g.addEdge(0, 2);
		g.addEdge(3, 1);
		g.addEdge(4, 2);
		
		int[] result = new int[5];
		result[0] = 4; 
		result[1] = 3;
		result[2] = 0;
		result[3] = 2;
		result[4] = 1;
		int [] topSort = g.topologicalSort();
		for (int i = 0; i<g.size(); i++)
			Assert.assertEquals(result[i],topSort[i]);

		g.addEdge(1, 0);
		Exception ex = assertThrows(UnsupportedOperationException.class, () -> {g.topologicalSort();});
		Assert.assertTrue(ex.getMessage().contains("Impossibile eseguire ordinamento topologico su un grafo non DAG"));
    }

    @Test
    public void getBFSTreeTest() {
		AdjListDirWeight g = new AdjListDirWeight();
		for(int i = 0; i<4; i++)
			g.addVertex();
		g.addEdge(0, 1);
		g.addEdge(0, 3);
		g.addEdge(2, 0);
		g.addEdge(3, 2);
        int [] res = new int[4];
		int [] visita = new int[g.size()];
		VisitForest f = g.getBFSTree(0);
		for (int i = 0; i< g.size(); i++)
			visita[i] = f.getPartent(i) == null ? -1 : f.getPartent(i);
		res [0] = -1;
		res [1] = 0;
		res [2] = 3;
		res [3] = 0;
		for (int i = 0; i< g.size(); i++)
			Assert.assertEquals(res[i],visita[i]);
		
		Exception ex =  assertThrows(IllegalArgumentException.class,()->{g.getBFSTree(10);});
		Assert.assertTrue(ex.getMessage().contains("Vertice di partenza non valido") );
    }
    @Test
    public void getDFSTreeTest() {
		AdjListDirWeight g = new AdjListDirWeight();
		for(int i = 0; i<5; i++)
			g.addVertex();
		g.addEdge(0, 1);
		g.addEdge(1, 2);
		g.addEdge(2, 0);
		
		int[] equals = new int[g.size()];
		equals[0] = -1;
		equals[1] = 0;
		equals[2] = 1;
		equals[3] = -1;
		equals[4] = -1;
		VisitForest res = g.getDFSTree(0);
			
		for(int i = 0; i<g.size(); i++)
			Assert.assertEquals(equals[i] , (res.getPartent(i) == null ? -1 : res.getPartent(i)));
		
		Exception ex =  assertThrows(IllegalArgumentException.class,()->{g.getDFSTree(10);});
		Assert.assertTrue(ex.getMessage().contains("Vertice di partenza non valido") );
    }
    @Test
    public void getDFSTOTForestTest() {
		AdjListDirWeight g = new AdjListDirWeight();
		for(int i = 0; i<5; i++)
			g.addVertex();
		g.addEdge(0, 1);
		g.addEdge(1, 2);
		g.addEdge(2, 0);
		g.addEdge(3, 4);
		
		int[] equals = new int[g.size()];
		equals[0] = -1;
		equals[1] = 0;
		equals[2] = 1;
		equals[3] = -1;
		equals[4] = 3;
		VisitForest res = g.getDFSTOTForest(0);
			
		for(int i = 0; i<g.size(); i++) {
			Assert.assertEquals(equals[i], (res.getPartent(i) == null ? -1 : res.getPartent(i)));
		}
		
		Exception ex = assertThrows(IllegalArgumentException.class,()->{g.getDFSTOTForest(10);});
		Assert.assertTrue(ex.getMessage().contains("Vertice di partenza non valido") );
		
    }
    @Test
    public void getDFSTOTForest2Test() {
		AdjListDirWeight g = new AdjListDirWeight();
		for(int i = 0; i<5; i++)
			g.addVertex();
		g.addEdge(0, 1);
		g.addEdge(1, 2);
		g.addEdge(2, 0);
		g.addEdge(3, 4);
		
		int[] equals = new int[g.size()];
		equals[0] = -1;
		equals[1] = 0;
		equals[2] = 1;
		equals[3] = -1;
		equals[4] = 3;
		int [] vertexOrder = new int[g.size()];
		for(int i = 0; i<g.size();i++)
			vertexOrder[i] = i;
		VisitForest res = g.getDFSTOTForest(vertexOrder);
			
		for(int i = 0; i<g.size(); i++) {
			Assert.assertEquals(equals[i], (res.getPartent(i) == null ? -1 : res.getPartent(i)));
		}
		int [] vertexOrder2 = new int[g.size()+1];
		for(int i = 0; i<g.size()+1;i++)
			vertexOrder2[i] = i;
		Exception ex = assertThrows(IllegalArgumentException.class,()->{g.getDFSTOTForest(vertexOrder2);});
		Assert.assertTrue(ex.getMessage().contains("Vertice di partenza non valido") );
		
    }
    
    @Test
    public void stronglyConnectedComponentTest() {
	   	AdjListDirWeight g = new AdjListDirWeight();	
	   	for(int i = 0; i<3; i++)
			g.addVertex();
		g.addEdge(1, 2);
		g.addEdge(2, 0);
		g.addEdge(2, 2);
	   	
		Set<Set<Integer>> res = g.stronglyConnectedComponents();
		HashSet<Integer> r = new HashSet<Integer>();
		r.add(0);
		r.add(1);
		r.add(2);
		HashSet<Set<Integer>> expected = new HashSet<Set<Integer>>(); 
		expected.add(r);
		
		Assert.assertTrue(res.equals(expected));
		for(Set<Integer> s: res)
			Assert.assertTrue(s.equals(r));
		
		g = new AdjListDirWeight();
	   	for(int i = 0; i<4; i++)
			g.addVertex();
		g.addEdge(1, 2);
		g.addEdge(2, 3);
		g.addEdge(3, 1);
		
		r.remove(0);
		r.add(3);
		HashSet<Integer> r2 = new HashSet<Integer>();
		r2.add(0);
		expected = new HashSet<Set<Integer>>();
		expected.add(r2);
		expected.add(r);
		boolean first = false;
		res = g.stronglyConnectedComponents();
		Assert.assertTrue(res.equals(expected));
		for(Set<Integer> set : res) {
			if(first)
				Assert.assertTrue(set.equals(r));
			else 
				Assert.assertTrue(set.equals(r2));
			first = true;
		}
		//result (0) (1,2,3)
		
    }
    @Test
    public void EdgeWeigthTest() {
		AdjListDirWeight g = new AdjListDirWeight();
		for(int i = 0; i<4; i++)
			g.addVertex();
		g.addEdge(0, 1);
		g.addEdge(0, 3);
		g.addEdge(2, 0);
		g.addEdge(3, 2);
		
		g.setEdgeWeight(0, 1, 24);
		Assert.assertEquals(24.0,g.getEdgeWeight(0, 1),0);
		
		Exception ex =  assertThrows(IllegalArgumentException.class,()->{g.setEdgeWeight(10,5,1);});
		Assert.assertTrue(ex.getMessage().contains("Il vertice di arrivo o di partenza non esiste") );
		ex =  assertThrows(IllegalArgumentException.class,()->{g.setEdgeWeight(3,10,1);});
		Assert.assertTrue(ex.getMessage().contains("Il vertice di arrivo o di partenza non esiste") );
		ex =  assertThrows(IllegalArgumentException.class,()->{g.setEdgeWeight(10,3,1);});
		Assert.assertTrue(ex.getMessage().contains("Il vertice di arrivo o di partenza non esiste") );
		ex =  assertThrows(NoSuchElementException.class,()->{g.setEdgeWeight(2,1,1);});
		Assert.assertTrue(ex.getMessage().contains("L'arco non esiste") );
		ex =  assertThrows(IllegalArgumentException.class,()->{g.getEdgeWeight(10,5);});
		Assert.assertTrue(ex.getMessage().contains("Il vertice di arrivo o di partenza non esiste") );
		ex =  assertThrows(IllegalArgumentException.class,()->{g.getEdgeWeight(3,10);});
		Assert.assertTrue(ex.getMessage().contains("Il vertice di arrivo o di partenza non esiste") );
		ex =  assertThrows(IllegalArgumentException.class,()->{g.getEdgeWeight(10,3);});
		Assert.assertTrue(ex.getMessage().contains("Il vertice di arrivo o di partenza non esiste") );
		ex =  assertThrows(NoSuchElementException.class,()->{g.getEdgeWeight(2,1);});
		Assert.assertTrue(ex.getMessage().contains("L'arco non esiste") );
		
    }
    
    @Test
    public void DijkstraTest() {
    	AdjListDirWeight g = new AdjListDirWeight();
		for(int i = 0; i<4; i++)
			g.addVertex();
		g.addEdge(0, 1);
		g.addEdge(0, 3);
		g.addEdge(2, 0);
		g.addEdge(3, 2);
		g.addEdge(1,0);
		g.addEdge(3,0);
		g.addEdge(0,2);
		g.addEdge(2,3);
		g.setEdgeWeight(0, 1,24);
		g.setEdgeWeight(0, 3,20);
		g.setEdgeWeight(2, 0,3);
		g.setEdgeWeight(3, 2, 12);
		g.setEdgeWeight(1,0,24);
		g.setEdgeWeight(3,0,20);
		g.setEdgeWeight(0,2,3);
		g.setEdgeWeight(2,3,12);
		
		g.getDijkstraShortestPaths(0);
		Assert.assertEquals(0.0,g.distanza[0],0);
		Assert.assertEquals(24.0,g.distanza[1],0);
		Assert.assertEquals(3.0,g.distanza[2],0);
		Assert.assertEquals(15.0,g.distanza[3],0);	
		
		Exception ex =  assertThrows(IllegalArgumentException.class,()->{g.getDijkstraShortestPaths(10);});
		Assert.assertTrue(ex.getMessage().contains("Vertice di partenza non valido") );
		g.setEdgeWeight(1, 0, -1);
		ex =  assertThrows(UnsupportedOperationException.class,()->{g.getDijkstraShortestPaths(0);});
		Assert.assertTrue(ex.getMessage().contains("Impossibile eseguire Dijkstra con archi negativi") );
		
    }
    
    @Test
    public void getPrimMSTTest() {
    	AdjListDirWeight g = new AdjListDirWeight();
    	Exception ex =  assertThrows(UnsupportedOperationException.class,()->{g.getPrimMST(10);});
		Assert.assertTrue(ex.getMessage().contains("Non implementata") );
    }
    
    @Test
    public void getKruskalMSTTest() {
    	AdjListDirWeight g = new AdjListDirWeight();
    	Exception ex =  assertThrows(UnsupportedOperationException.class,()->{g.getKruskalMST();});
		Assert.assertTrue(ex.getMessage().contains("Non implementata") );
    }
    
    @Test
    public void getFloydWarshallShortestPathsTest() {
    	AdjListDirWeight g = new AdjListDirWeight();
    	Exception ex =  assertThrows(UnsupportedOperationException.class,()->{g.getFloydWarshallShortestPaths();});
		Assert.assertTrue(ex.getMessage().contains("Non implementata") );
    }
    
    @Test
    public void getBellmanFordShortestPathsTest() {
    	AdjListDirWeight g = new AdjListDirWeight();
    	Exception ex =  assertThrows(UnsupportedOperationException.class,()->{g.getBellmanFordShortestPaths(1);});
		Assert.assertTrue(ex.getMessage().contains("Non implementata") );
    }
    @Test
    public void connectedComponentsTest() {
    	AdjListDirWeight g = new AdjListDirWeight();
    	Exception ex =  assertThrows(UnsupportedOperationException.class,()->{g.connectedComponents();});
		Assert.assertTrue(ex.getMessage().contains("Impossibile calcolare le componenti connesse in un grafo orientato. Prova con stronglyConnectedComponents") );
    }
    
}
