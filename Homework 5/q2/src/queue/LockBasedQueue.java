package queue;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

//single consumer lock based unbounded queue 
public class LockBasedQueue<T> extends Queue<T> {

	private static class Node<T> {
		T value;
		Node<T> next;

		public Node(T value, Node<T> next) {
			this.next = next;
			this.value = value;
		}
	}

	/*
	 * @Kapil: The goal is to always have an empty Node at the head so we can
	 * synchronize() with a @nonNull object
	 */

	@NotNull
	private final Node<T> head;

	@Nullable
	private Node<T> tail;

	public LockBasedQueue() {
		this.head = new Node<T>(null, null);
		this.tail = this.head;
	}

	@Override
	public boolean enqueue(T t) {
		Node<T> newNode = new Node<T>(t, null);
		synchronized (tail) {
			tail.next = newNode;
			tail = tail.next;
		}
		return true;
	}

	@Override
	public T dequeue() {
		T t = null;
		synchronized (head) {
			Node<T> curr = head.next;
			if (curr != null) {
				t = curr.value;
				head.next = curr.next;
			}
		}
		return t;
	}

}
