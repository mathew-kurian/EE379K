package q7;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FineList implements LinkedList {
	
	private class Node {
		
		Lock lock;
		int key;
		public Node next;
		
		public Node(int key) {
			this.lock = new ReentrantLock();
			this.key = key;
		}

		void lock() {
			lock.lock();
		}

		void unlock() {
			lock.unlock();
		}
	}

	protected Node head;
	
	public FineList() {
		head = new Node(Integer.MIN_VALUE);
		head.next = new Node(Integer.MAX_VALUE);
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
	
	public boolean add(Integer item) {
		int key = item.hashCode();
		head.lock();
		Node pred = head;
		try {
			Node curr = pred.next;
			curr.lock();
			try {
				while (curr.key < key) {
					pred.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock();
				}
				if (curr.key == key) {
					return false;
				}
				Node newNode = new Node(item);
				newNode.next = curr;
				pred.next = newNode;
				return true;
			} finally {
				curr.unlock();
			}
		} finally {
			pred.unlock();
		}
	}

	public boolean remove(Integer key) {
		Node pred = null, curr = null;
		head.lock();
		try {
			pred = head;
			curr = (Node) pred.next;
			curr.lock();
			try {
				while (curr.key < key) {
					pred.unlock();
					pred = curr;
					curr = (Node) curr.next;
					curr.lock();
				}
				if (curr.key == key) {
					pred.next = curr.next;
					return true;
				}
				return false;
			} finally {
				curr.unlock();
			}
		} finally {
			pred.unlock();
		}
	}

	public boolean contains(Integer key) {
		Node pred = null, curr = null;
		head.lock();
		try {
			pred = head;
			curr = (Node) pred.next;
			curr.lock();
			try {
				while (curr.key < key) {
					pred.unlock();
					pred = curr;
					curr = (Node) curr.next;
					curr.lock();
				}
				return (curr.key == key);
			} finally {
				curr.unlock();
			}
		} finally {
			pred.unlock();
		}
	}

}
