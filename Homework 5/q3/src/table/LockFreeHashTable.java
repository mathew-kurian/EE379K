package table;

/*
 * BucketList.java
 *
 * Created on December 30, 2005, 3:24 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicMarkableReference;

/*
 * @param <T> type 
 * @author Maurice Herlihy
 */

/*
 * Modified by Mathew Kurian, Kapil Gowru
 */

public class LockFreeHashTable<K, T> implements Map<K, T> {
	static final int WORD_SIZE = 24;
	static final int LO_MASK = 0x00000001;
	static final int HI_MASK = 0x00800000;
	static final int MASK = 0x00FFFFFF;
	Node head;

	/**
	 * Constructor
	 */
	public LockFreeHashTable() {
		this.head = new Node(0);
		this.head.next = new AtomicMarkableReference<Node>(new Node(
				Integer.MAX_VALUE), false);
	}

	private LockFreeHashTable(Node e) {
		this.head = e;
	}

	/**
	 * Restricted-size hash code
	 * 
	 * @param x
	 *            object to hash
	 * @return hash code
	 */
	public static int hashCode(Object x) {
		return x.hashCode() & MASK;
	}

	public T put(K k, T t) {
		int key = makeRegularKey(k);
		boolean splice;
		while (true) {
			// find predecessor and current entries
			Window window = find(head, key);
			Node pred = window.pred;
			Node curr = window.curr;
			// is the key present?
			if (curr.key == key) {
				curr.value = t;
				return t;
			} else {
				// splice in new entry
				Node entry = new Node(key, t);
				entry.next.set(curr, false);
				splice = pred.next.compareAndSet(curr, entry, false, false);
				if (splice)
					return t;
				else
					continue;
			}
		}
	}

	public T remove(K k) {
		int key = makeRegularKey(k);
		boolean snip;
		while (true) {
			// find predecessor and current entries
			Window window = find(head, key);
			Node pred = window.pred;
			Node curr = window.curr;
			// is the key present?
			if (curr.key != key) {
				return null;
			} else {
				// snip out matching entry
				snip = pred.next.attemptMark(curr, true);
				if (snip)
					return curr.value;
				else
					continue;
			}
		}
	}

	@SuppressWarnings("unused")
	public boolean contains(K k) {
		int key = makeRegularKey(k);
		Window window = find(head, key);
		Node pred = window.pred;
		Node curr = window.curr;
		return (curr.key == key);
	}

	public LockFreeHashTable<K, T> getSentinel(int index) {
		int key = makeSentinelKey(index);
		boolean splice;
		while (true) {
			// find predecessor and current entries
			Window window = find(head, key);
			Node pred = window.pred;
			Node curr = window.curr;
			// is the key present?
			if (curr.key == key) {
				return new LockFreeHashTable<K, T>(curr);
			} else {
				// splice in new entry
				Node entry = new Node(key);
				entry.next.set(pred.next.getReference(), false);
				splice = pred.next.compareAndSet(curr, entry, false, false);
				if (splice)
					return new LockFreeHashTable<K, T>(entry);
				else
					continue;
			}
		}
	}

	public static int reverse(int key) {
		int loMask = LO_MASK;
		int hiMask = HI_MASK;
		int result = 0;
		for (int i = 0; i < WORD_SIZE; i++) {
			if ((key & loMask) != 0) { // bit set
				result |= hiMask;
			}
			loMask <<= 1;
			hiMask >>>= 1; // fill with 0 from left
		}
		return result;
	}

	public int makeRegularKey(K k) {
		int code = k.hashCode() & MASK; // take 3 lowest bytes
		return reverse(code | HI_MASK);
	}

	private int makeSentinelKey(int key) {
		return reverse(key & MASK);
	}

	// iterate over Set elements
	public Iterator<T> iterator() {
		throw new UnsupportedOperationException();
	}

	private class Node {
		int key;
		T value;
		AtomicMarkableReference<Node> next;

		Node(int key, T object) { // usual constructor
			this.key = key;
			this.value = object;
			this.next = new AtomicMarkableReference<Node>(null, false);
		}

		Node(int key) { // sentinel constructor
			this.key = key;
			this.next = new AtomicMarkableReference<Node>(null, false);
		}

		Node getNext() {
			boolean[] cMarked = { false }; // is curr marked?
			boolean[] sMarked = { false }; // is succ marked?
			Node entry = this.next.get(cMarked);
			while (cMarked[0]) {
				Node succ = entry.next.get(sMarked);
				this.next.compareAndSet(entry, succ, true, sMarked[0]);
				entry = this.next.get(cMarked);
			}
			return entry;
		}
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
		Node pred = head;
		Node curr = head.getNext();
		while (curr.key < key) {
			pred = curr;
			curr = pred.getNext();
		}
		return new Window(pred, curr);
	}
}
