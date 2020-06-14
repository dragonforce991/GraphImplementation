package upo.graph.implementation;
import java.lang.Comparable;
import java.util.ArrayList;
import java.util.Collections;
/**
 * @author Davide D'Angelo 20035563
 */
public class PriorityQueueDouble implements upo.additionalstructures.PriorityQueueDouble {

	public class Node implements Comparable<Node>{
		int el;
		double priority;
		public Node(int el, double priority) {
			this.el = el;
			this.priority = priority;
		}
		void setEl(int el) {
			this.el = el;
		}
		void setPriority(double priority) {
			this.priority= priority;
		}
		double getPriority() {
			return this.priority;
		}
		int getEl() {
			return this.el;
		}
		public int compareTo(Node o) {
			if(this.priority> o.priority)
				return 1;
			if(this.priority == o.priority)
				return 0;
			return -1;
						
		}	
	}
	private ArrayList<Node> queue;
	public PriorityQueueDouble() {
		queue = new ArrayList<Node>();
	}
	@Override
	public void enqueue(int el, double priority) {
		queue.add(new Node(el,priority));
		Collections.sort(queue);
	}

	@Override
	public int dequeue() {
		int ret = queue.get(0).getEl();
		queue.remove(0);
		return ret;
	}

	@Override
	public void modify_priority(int el, double newPriority) {
		for(Node n : queue)
			if(n.getEl() == el )
				n.setPriority(newPriority);
		
		Collections.sort(queue);
	}
	
	public ArrayList<Node> getQueue(){
		return queue;
	}
	
	public int size() {
		return queue.size();
	}
	public boolean isEmpty() {
		return size()==0;
	}
	

}
