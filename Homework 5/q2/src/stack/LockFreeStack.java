package stack;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

// http://users.ece.utexas.edu/~garg/dist/jbkv2Code/LockFree/LockFreeStack.java.html

public class LockFreeStack<T> extends Stack<T> {

	private boolean blocking = false;
	
	AtomicReference<Node> top = new AtomicReference<Node>(null);

	// Use backoff, tweak these numbers to improve perf
	static final int MIN_DELAY = 1;
	static final int MAX_DELAY = 100;
	Backoff backoff = new Backoff(MIN_DELAY, MAX_DELAY);

	public LockFreeStack(boolean blocking){
		this.blocking = blocking;
	}
	
	// Push logic
	protected boolean tryPush(Node node) {
		Node oldTop = top.get();
		node.next = oldTop;
		return (top.compareAndSet(oldTop, node));
	}

	public boolean push(T value) {
		Node node = new Node(value);
		while (true) {
			if (tryPush(node)) {
				return true;
			} else {
				try {
					backoff.backoff();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Pop logic
	protected Node tryPop() {
		Node oldTop = top.get();
		if (oldTop == null) {
			return null;
		}

		Node newTop = oldTop.next;
		if (top.compareAndSet(oldTop, newTop)) {
			return oldTop;
		} else {
			return null;
		}
	}

	public T pop() {
		while (true){
			Node returnNode = tryPop();
			if (returnNode != null)
				return returnNode.value;
			else if (!blocking)
				return null;
			else
				try {
					backoff.backoff();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}

	}
	
	// Node class
	public class Node {

		public T value;
		public Node next;

		public Node(T value) {
			this.value = value;
			next = null;
		}
	}

	// Backoff class
	private class Backoff {
		final int minDelay, maxDelay;
		int limit;
		final Random random;

		public Backoff(int min, int max) {
			minDelay = min;
			maxDelay = max;
			limit = minDelay;
			random = new Random();
		}

		public void backoff() throws InterruptedException {
			int delay = random.nextInt(limit);
			limit = Math.min(maxDelay, 2 * limit);
			Thread.sleep(delay);
		}
	}
}
