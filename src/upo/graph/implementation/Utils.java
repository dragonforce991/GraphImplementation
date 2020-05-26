package upo.graph.implementation;

import java.util.ArrayList;
import java.util.HashSet;

import upo.graph.base.VisitForest;

public class Utils {
	public static int getDate() {
		return (int) System.currentTimeMillis();
		
	}
	public static int getMax(ArrayList<Integer> list) {
		int max= Integer.MIN_VALUE;
		int index = -1;
		for (int i = 0; i<list.size(); i++) {
			if (max < list.get(i)) {
				max = list.get(i);
				index = i;
			}
				
		}
		list.remove(index);
		return index;
	}
	public static AdjListDirWeight createT(AdjListDirWeight original) {
		AdjListDirWeight t = new AdjListDirWeight();
		for (int i = 0 ; i<original.size(); i++) {
			t.addVertex();
		}
		for (int i = 0 ; i<original.size(); i++) {
			for(Integer e : original.getAdjacent(i)) {
				t.addEdge(e, i);
			}
		}
		return t;
		
	}
	public static void addVisitForest(VisitForest ret, VisitForest a, int n) {
		for (int i = 0; i<n; i++) {
			if(a.getColor(i) == VisitForest.Color.BLACK) {
				ret.setColor(i, VisitForest.Color.BLACK);
				ret.setParent(i, a.getPartent(i));
				ret.setDistance(i, a.getDistance(i));
				ret.setEndTime(i, a.getEndTime(i));
				ret.setStartTime(i, a.getStartTime(i));
			}				
		}
	}
	
	public static HashSet<Integer> getTree(VisitForest vf, int root, int n) {
		HashSet<Integer> ret = new HashSet<Integer>();
		getNextElement(vf,root,n,ret);
		return ret;
		

	}
	
	private static void getNextElement(VisitForest vf, int root, int n, HashSet<Integer> s) {
		if(root != -1) {
			s.add(root);
			for(int i = 0; i<n ; i++ ) {
				if(vf.getPartent(i) != null)
					if(vf.getPartent(i) == root)
						getNextElement(vf,i, n, s);
			}
			//getNextElement(vf,-1,n,s);
		}
	}
	
	
}
