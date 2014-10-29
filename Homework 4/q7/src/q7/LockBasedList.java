package q7;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Code from book
 */

public class LockBasedList implements LinkedList {

	Node head;

	public LockBasedList() {
		this.head = new Node(Integer.MIN_VALUE);
		this.head.next = new Node(Integer.MAX_VALUE);
	}

	private boolean validate(Node pred, Node curr) {
		return !pred.marked && !curr.marked && pred.next == curr;
	}

	public boolean add(Integer key) {
		while (true) {
			Node pred = this.head;
			Node curr = head.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			pred.lock();
			try {
				curr.lock();
				try {
					if (validate(pred, curr)) {
						if (curr.key == key) {
							return false;
						} else {
							Node Node = new Node(key);
							Node.next = curr;
							pred.next = Node;
							return true;
						}
					}
				} finally {
					curr.unlock();
				}
			} finally {
				pred.unlock();
			}
		}
	}

	public boolean remove(Integer key) {
		while (true) {
			Node pred = this.head;
			Node curr = head.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			pred.lock();
			try {
				curr.lock();
				try {
					if (validate(pred, curr)) {
						if (curr.key != key) {
							return false;
						} else {
							curr.marked = true;
							pred.next = curr.next;
							return true;
						}
					}
				} finally {
					curr.unlock();
				}
			} finally {
				pred.unlock();
			}
		}
	}

	public boolean contains(Integer key) {
		Node curr = this.head;
		while (curr.key < key)
			curr = curr.next;
		return curr.key == key && !curr.marked;
	}

	public class Node {
		int key;
		Node next;
		boolean marked;
		Lock lock;

		Node(int item) {
			this.key = item;
			this.next = null;
			this.marked = false;
			this.lock = new ReentrantLock();
		}

		void lock() {
			lock.lock();
		}

		void unlock() {
			lock.unlock();
		}
	}
}
