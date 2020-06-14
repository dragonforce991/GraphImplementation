package Testing;


/*
 * @Author Davide D'Angelo 20035563
 */
import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import junit.framework.TestCase;
import upo.graph.base.VisitForest;
import upo.graph.implementation.AdjListDirWeight;
import upo.graph.implementation.Utils;
import org.junit.Assert;


public class UtilsTesting {

	@Test
	public void getMaxTesting() {
		ArrayList<Integer> al = new ArrayList<Integer>();
		for(int i = 0; i<10;i++)
			al.add(i);
		Assert.assertEquals(9,Utils.getMax(al));
	}
	
	@Test
	public void createTTest() {
		AdjListDirWeight g = new AdjListDirWeight();
		for(int i = 0; i<3; i++)
			g.addVertex();
		g.addEdge(0, 1);
		g.addEdge(0, 2);
		g.addEdge(1, 2);
		g = Utils.createT(g);
		Assert.assertTrue(g.containsEdge(1,0));
		Assert.assertTrue(g.containsEdge(2,0));
		Assert.assertTrue(g.containsEdge(2,1));
		Assert.assertFalse(g.containsEdge(0,1));
		Assert.assertFalse(g.containsEdge(0,2));
		Assert.assertFalse(g.containsEdge(1,2));
		
		}
	@Test
	public void addVisitForestTest() {
		AdjListDirWeight g = new AdjListDirWeight();
		for(int i = 0; i<3; i++)
			g.addVertex();
		g.addEdge(0, 1);
		g.addEdge(0, 2);
		g.addEdge(1, 2);
		VisitForest vf1 = new VisitForest(g,VisitForest.VisitType.BFS);
		VisitForest vf2 = new VisitForest(g,VisitForest.VisitType.BFS);
		vf1.setParent(0, 1);
		vf1.setColor(0, VisitForest.Color.BLACK);
		vf2.setParent(2, 1);
		vf2.setColor(2, VisitForest.Color.BLACK);
		Utils.addVisitForest(vf1, vf2, 3);
	    int res [] = new int [3];
	    res[0] = 1;
	    res[1] = -1;
	    res[2] = 1;
	    int res1 [] = new int[3];
	    for ( int i = 0; i<3; i++)
	    	res1[i] = vf1.getPartent(i) == null ? -1 : vf1.getPartent(i);
	    Assert.assertArrayEquals(res1, res);	
	}
	
	@Test
	public void getTreeTest() {
		AdjListDirWeight g = new AdjListDirWeight();	
	   	for(int i = 0; i<3; i++)
			g.addVertex();
		g.addEdge(1, 2);
		g.addEdge(2, 0);
		g.addEdge(2, 2);
	   	VisitForest vf = new VisitForest(g,VisitForest.VisitType.BFS);
	   	vf.setParent(1, 2);
	   	vf.setParent(2, 0);
	   	HashSet<Integer> res = Utils.getTree(vf, 0, 3);
	   	HashSet<Integer> expected = new HashSet<Integer>();
	   	expected.add(2);
	   	expected.add(1);
	   	expected.add(0);
	   	
	   	Assert.assertTrue(res.equals(expected));
	   	g.addVertex();
	   	vf = new VisitForest(g,VisitForest.VisitType.BFS);
	   	vf.setParent(1, 2);
	   	vf.setParent(2, 3);
	   	res = Utils.getTree(vf, 0, 3);
	   	expected.clear();
	   	expected.add(0);
	   	Assert.assertTrue(res.equals(expected)); //0
	   	res = Utils.getTree(vf, 3, 3);
	   	expected.clear();
	   	expected.add(3);
	   	expected.add(2);
	   	expected.add(1);
	   	Assert.assertTrue(res.equals(expected));//3 2 1
		
	}
}
