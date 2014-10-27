package q7;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeList implements LinkedList {

	private class Node {
		int key;
		AtomicMarkableReference<Node> next;

		Node(Integer key) {
			this.key = key;
			this.next = new AtomicMarkableReference<Node>(null, false);
		}
	}

	Node head;

	public LockFreeList() {
		this.head = new Node(Integer.MIN_VALUE);
		Node tail = new Node(Integer.MAX_VALUE);
		while (!head.next.compareAndSet(null, tail, false, false))
			;
	}

	public String toString() {
		
		if(this.head == null){
			return "[]";
		}
		
		StringBuilder smb = new StringBuilder();
		Node next = this.head;
		while (next != null) {
			smb.append(next.key + ",");
			next = next.next.getReference();
		}

		return smb.substring(0, smb.length() - 1).toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean add(Integer key) {
		while (true) {
			Window window = find(head, key);
			Node pred = window.pred, curr = window.curr;
			if (curr.key == key) {
				return false;
			} else {
				Node node = new Node(key);
				node.next = new AtomicMarkableReference(curr, false);
				if (pred.next.compareAndSet(curr, node, false, false)) {
					return true;
				}
			}
		}
	}

	public boolean remove(Integer key) {
		boolean snip;
		while (true) {
			Window window = find(head, key);
			Node pred = window.pred, curr = window.curr;
			if (curr.key != key) {
				return false;
			} else {
				Node succ = curr.next.getReference();
				snip = curr.next.attemptMark(succ, true);
				if (!snip)
					continue;
				pred.next.compareAndSet(curr, succ, false, false);
				return true;
			}
		}
	}

	@SuppressWarnings("unused")
	public boolean contains(Integer key) {
		Window window = find(head, key);
		Node pred = window.pred, curr = window.curr;
		return (curr.key == key);
	}
	
	class Window {
		public Node pred;
		public Node curr;

		Window(Node pred, Node curr) {
			this.pred = pred;
			this.curr = curr;
		}
	}

	public Window find(Node head, int key) {
		Node pred = null, curr = null, succ = null;
		boolean[] marked = { false };
		boolean snip;
		retry: while (true) {
			pred = head;
			curr = pred.next.getReference();
			while (true) {
				succ = curr.next.get(marked);
				while (marked[0]) {
					snip = pred.next.compareAndSet(curr, succ, false, false);
					if (!snip)
						continue retry;
					curr = pred.next.getReference();
					succ = curr.next.get(marked);
				}
				if (curr.key >= key)
					return new Window(pred, curr);
				pred = curr;
				curr = succ;
			}
		}
	}
}