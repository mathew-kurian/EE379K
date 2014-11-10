package stack;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

// http://users.ece.utexas.edu/~garg/dist/jbkv2Code/LockFree/LockFreeStack.java.html

public class LockFreeContentionManagedStack<T> extends Stack<T> {

	private static boolean ENABLE_BLOCKING_POP = false;
	
	AtomicReference<Node> top = new AtomicReference<Node>(null);

	// Use backoff, tweak these numbers to improve perf
	static final int MIN_DELAY = 1;
	static final int MAX_DELAY = 100;
	Backoff backoff = new Backoff(MIN_DELAY, MAX_DELAY);

	// Push logic
	private boolean tryPush(Node node) {
		Node oldTop = top.get();
		node.next = oldTop;
		return (top.compareAndSet(oldTop, node));
	}

	public void push(T value) {
		Node node = new Node(value);
		while (true) {
			if (tryPush(node)) {
				return;
			} else {
				try {
					backoff.backoff();
				} catch (InterruptedException e) {
					// do nothing
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
		if(LockFreeContentionManagedStack.ENABLE_BLOCKING_POP){
			return blockingPop();
		}
		
		return nonBlockingPop();
	}

	
	public T blockingPop() {
		while (true) {
			Node returnNode = tryPop();
			if (returnNode != null) {
				return returnNode.value;
			} else {
				try {
					backoff.backoff();
				} catch (InterruptedException e) {
					// do nothing
				}
			}
		}
	}
	
	public T nonBlockingPop() {
		Node node = tryPop();
		return node != null ? node.value : null;
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
	public class Backoff {
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
