package queue;

import java.util.concurrent.atomic.AtomicReference;

//single consumer lock free unbounded queue
public class LockFreeQueue<T> extends Queue<T> {

	private static class Node<T> {
		final T item;
		final AtomicReference<Node<T>> next;

		public Node(T item, Node<T> next) {
			this.item = item;
			this.next = new AtomicReference<Node<T>>(next);
		}
	}

	private final Node<T> temp;
	private final AtomicReference<Node<T>> head;
	private final AtomicReference<Node<T>> tail;

	public LockFreeQueue() {
		temp = new Node<T>(null, null);
		head = new AtomicReference<Node<T>>(temp);
		tail = new AtomicReference<Node<T>>(temp);
	}

	// puts t into the queue, returns true when successful
	@Override
	public boolean enqueue(T t) {
		Node<T> newNode = new Node<T>(t, null);
		while (true) {
			Node<T> currTail = tail.get();
			Node<T> nextTail = currTail.next.get();
			if (currTail == tail.get()) {
				if (nextTail != null) {
					tail.compareAndSet(currTail, nextTail);
				} else {
					if (currTail.next.compareAndSet(null, newNode)) {
						tail.compareAndSet(currTail, newNode);
						return true;
					}
				}
			}

		}
	}

	// takes T out of the queue
	@Override
	public T dequeue() {
		while (true) {
			Node<T> oldHead = head.get();
			Node<T> oldTail = tail.get();
			Node<T> oldNextHead = oldHead.next.get();
			if (oldHead == head.get()) {
				if (oldHead == oldTail) {
					if (oldNextHead == null)
						return null;
					tail.compareAndSet(oldTail, oldNextHead);
				} else {
					if (head.compareAndSet(oldTail, oldNextHead))
						return oldNextHead.item;
				}
			}
		}
	}
}
