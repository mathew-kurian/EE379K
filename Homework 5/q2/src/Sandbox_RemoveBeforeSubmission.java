import static org.junit.Assert.*;

import org.junit.Test;

import queue.LockBasedQueue;
import stack.LockBasedStack;

public class Sandbox_RemoveBeforeSubmission {

	@Test
	public void lockBasedStackSimpleTest() {
		LockBasedStack<Integer> lbs = new LockBasedStack<Integer>();

		assertTrue(lbs.pop() == null);

		lbs.push(10);
		lbs.push(9);
		lbs.push(8);
		lbs.push(7);

		assertTrue(lbs.pop().equals(7));
		assertTrue(lbs.pop().equals(8));
		assertTrue(lbs.pop().equals(9));
		assertTrue(lbs.pop().equals(10));
		assertTrue(lbs.pop() == null);
	}

	@Test
	public void lockBasedQueueSimpleTest() {
		LockBasedQueue<Integer> lbs = new LockBasedQueue<Integer>();

		assertTrue(lbs.dequeue() == null);

		lbs.enqueue(10);
		lbs.enqueue(9);
		lbs.enqueue(8);
		lbs.enqueue(7);

		assertTrue(lbs.dequeue().equals(10));
		assertTrue(lbs.dequeue().equals(9));
		assertTrue(lbs.dequeue().equals(8));
		assertTrue(lbs.dequeue().equals(7));
		assertTrue(lbs.dequeue() == null);
	}

	@Test
	public void myTest() {
		Driver.main(new String[] { "stack", "lock-free-contention-managed",
				"1", "12000" });
	}
}
