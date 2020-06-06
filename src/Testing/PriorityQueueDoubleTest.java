package Testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import upo.graph.implementation.PriorityQueueDouble;

public class PriorityQueueDoubleTest {
	@Test
	public void enqueueDequeueTest() {
		PriorityQueueDouble pqd = new PriorityQueueDouble();
		pqd.enqueue(1, 0);
		assertEquals(pqd.dequeue(),1);	
		for(int i = 0; i<10; i++)
			pqd.enqueue(i, i*-1);
		for(int i = 9; i>=0; i-- )
			assertEquals(i,pqd.dequeue());
		for(int i = 0; i<10; i++)
			pqd.enqueue(i, i);
		for(int i = 0; i<10; i++ )
			assertEquals(i,pqd.dequeue());
	}
	
	@Test
	public void modify_priorityTest() {
		PriorityQueueDouble pqd = new PriorityQueueDouble();
		for(int i = 0; i<10; i++)
			pqd.enqueue(i, i);
		pqd.modify_priority(9, -1);
		assertEquals(9,pqd.dequeue());
		pqd.modify_priority(8, 0);
		assertEquals(pqd.dequeue(), 0);
	}
	@Test
	public void sizeTest() {
		PriorityQueueDouble pqd = new PriorityQueueDouble();
		assertTrue(pqd.isEmpty());
		for(int i = 0; i<10; i++)
			pqd.enqueue(i, i);
		assertFalse(pqd.isEmpty());
		assertEquals(10,pqd.size());
	}
	
	
}
