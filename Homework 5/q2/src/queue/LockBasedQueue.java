package queue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

	private Lock headLock;
	private Lock tailLock;
	private AtomicInteger count;

	@NotNull
	private final Node<T> head;

	@Nullable
	private Node<T> tail;

	public LockBasedQueue() {
		this.head = new Node<T>(null, null);
		this.tail = this.head;
		this.headLock = new ReentrantLock();
		this.tailLock = new ReentrantLock();
		this.count = new AtomicInteger(0);
	}

	@Override
	public boolean enqueue(T t) {
		Node<T> newNode = new Node<T>(t, null);
		try {

			tailLock.lock();
			tail.next = newNode;
			tail = tail.next;

			// Increment after
			count.incrementAndGet();

		} finally {
			tailLock.unlock();
		}

		return true;
	}

	@Override
	public T dequeue() {
		T t = null;
		try {
			headLock.lock();
			int length = count.get(); // Check length so head != tail
			if (length > 0) {
				Node<T> curr = head.next;
				if (curr != null) {
					t = curr.value;
					head.next = curr.next;
				}
			}
		} finally {
			headLock.unlock();
		}
		return t;
	}

}
