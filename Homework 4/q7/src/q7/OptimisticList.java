package q7;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OptimisticList implements LinkedList {

	Node head;

	private class Node {
		int key;
		Node next;
		Lock lock;

		Node(int key) {
			this.key = key;
			lock = new ReentrantLock();
		}
		
		void lock() {
			lock.lock();
		}

		void unlock() {
			lock.unlock();
		}
	}

	public OptimisticList() {
		this.head = new Node(Integer.MIN_VALUE);
		this.head.next = new Node(Integer.MAX_VALUE);
	}


	public String toString() {
		
		if(this.head == null){
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
		while (true) {
			Node pred = this.head;
			Node curr = pred.next;
			while (curr.key <= key) {
				pred = curr;
				curr = curr.next;
			}
			pred.lock();
			curr.lock();
			try {
				if (validate(pred, curr)) {

					if (curr.key == key || pred.key == key) {
						return false;
					} else {
						Node entry = new Node(key);
						entry.next = curr;
						pred.next = entry;
						return true;
					}
				}
			} finally {
				pred.unlock();
				curr.unlock();
			}
		}
	}

	public boolean remove(Integer key) {
		while (true) {
			Node pred = this.head;
			Node curr = pred.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			pred.lock();
			curr.lock();
			try {
				if (validate(pred, curr)) {
					if (curr.key == key) {
						pred.next = curr.next;
						return true;
					} else {
						return false;
					}
				}
			} finally {
				pred.unlock();
				curr.unlock();
			}
		}
	}

	public boolean contains(Integer key) {
		while (true) {
			Node pred = this.head;
			Node curr = pred.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			try {
				pred.lock();
				curr.lock();
				if (validate(pred, curr)) {
					return (curr.key == key);
				}
			} finally {
				pred.unlock();
				curr.unlock();
			}
		}
	}

	private boolean validate(Node pred, Node curr) {
		Node entry = head;
		while (entry.key <= pred.key) {
			if (entry == pred)
				return pred.next == curr;
			entry = entry.next;
		}
		return false;
	}
}