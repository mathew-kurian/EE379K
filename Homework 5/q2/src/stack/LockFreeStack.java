package stack;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack<E> extends Stack<E> {

	private class Node {
		Node next;
		final E item;

		public Node(E item) {
			this.item = item;
		}

	}

	AtomicReference<Node> head = new AtomicReference<Node>();

	@Override
	public boolean push(E t) {
		Node newHead = new Node(t);
		Node oldHead = head.get();
		newHead.next = oldHead;
		while (!head.compareAndSet(oldHead, newHead)) {
			oldHead = head.get();
			newHead.next = oldHead;
		}

		return true;

	}

	@Override
	public E pop() {
		Node oldHead = head.get();
		if (oldHead == null)
			return null;
		Node newHead = oldHead.next;
		while (!head.compareAndSet(oldHead, newHead)) {
			oldHead = head.get();
			if (oldHead == null)
				return null;
			newHead = oldHead.next;
		}
		
		return oldHead.item;
	}

}
