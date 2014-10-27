package q7;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoarseList implements LinkedList {
	
	private class Node {
		int key;
		public Node next;

		Node(int key) {
			this.key = key;
		}
	}
	
	private Lock lock = new ReentrantLock();

	protected Node head;

	public CoarseList() {
		head = new Node(Integer.MIN_VALUE);
		head.next = new Node(Integer.MAX_VALUE);
	}

	public String toString() {

		if (this.head == null) {
			return "[]";
		}

		StringBuilder smb = new StringBuilder();
		Node next = this.head;
		while (next != null) {
			smb.append(next.key + ",");
			next = next.next;
		}

		return smb.substring(0, smb.length() - 1).toString();
	}

	public boolean add(Integer key) {
		Node pred, curr;
		lock.lock();
		try {
			pred = head;
			curr = pred.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			if (key == curr.key) {
				return false;
			} else {
				Node node = new Node(key);
				node.next = curr;
				pred.next = node;
				return true;
			}
		} finally {
			lock.unlock();
		}
	}

	public boolean remove(Integer key) {
		Node pred, curr;
		lock.lock();
		try {
			pred = this.head;
			curr = pred.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			if (key == curr.key) {
				pred.next = curr.next;
				return true;
			} else {
				return false;
			}
		} finally {
			lock.unlock();
		}
	}

	public boolean contains(Integer key) {
		Node pred, curr;
		lock.lock();
		try {
			pred = head;
			curr = pred.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			return (key == curr.key);
		} finally {
			lock.unlock();
		}
	}
}