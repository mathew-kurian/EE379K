package queue;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Lock-free queue. Based on Michael and Scott
 * http://doi.acm.org/10.1145/248052.248106
 * 
 * @author Maurice Herlihy
 */
public class LockFreeQueue<T> extends Queue<T> {
	private final AtomicReference<Node> head;
	private final AtomicReference<Node> tail;
	private final boolean blocking;
	
	/**
	 * Create a new object of this class.
	 */
	public LockFreeQueue(boolean blocking) {
		this.blocking = blocking;
		Node sentinel = new Node(null);
		head = new AtomicReference<Node>(sentinel);
		tail = new AtomicReference<Node>(sentinel);
	}

	/**
	 * Enqueue an item.
	 * 
	 * @param value
	 *            Item to enqueue.
	 */
	public boolean enqueue(T value) {
		// try to allocate new node from local pool
		Node node = new Node(value);
		while (true) { // keep trying
			Node last = tail.get(); // read tail
			Node next = last.next.get(); // read next
			// are they consistent?
			if (last == tail.get()) {
				if (next == null) { // was tail the last node?
					// try to link node to end of list
					if (last.next.compareAndSet(next, node)) {
						// enq done, try to advance tail
						tail.compareAndSet(last, node);
						return true;
					}
				} else { // tail was not the last node
					// try to swing tail to next node
					tail.compareAndSet(last, next);
				}
			}
		}
	}

	/**
	 * Dequeue an item.
	 * 
	 * @throws queue.EmptyException
	 *             The queue is empty.
	 * @return Item at the head of the queue.
	 */
	public T dequeue() {
		while (true) {
			Node first = head.get();
			Node last = tail.get();
			Node next = first.next.get();
			// are they consistent?
			if (first == head.get()) {
				if (first == last) { // is queue empty or tail falling behind?
					if (next == null) { // is queue empty?
						// enable blocking
						if(blocking){
							continue;
						}
						
						return null;
					}
					// tail is behind, try to advance
					tail.compareAndSet(last, next);
				} else {
					T value = next.value; // read value before dequeuing
					if (head.compareAndSet(first, next)) {
						return value;
					}
				}
			}
		}
	}

	/**
	 * Items are kept in a list of nodes.
	 */
	public class Node {
		/**
		 * Item kept by this node.
		 */
		public T value;
		/**
		 * Next node in the queue.
		 */
		public AtomicReference<Node> next;

		/**
		 * Create a new node.
		 */
		public Node(T value) {
			this.next = new AtomicReference<Node>(null);
			this.value = value;
		}
	}
}
