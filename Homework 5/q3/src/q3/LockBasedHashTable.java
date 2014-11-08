package q3;

/*
 * BaseHashSet.java
 *
 * Created on November 15, 2006, 3:23 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
 * @param <T> type
 * @author Maurice Herlihy
 */

/*
 * Modified by Mathew Kurian, Kapil Gowru
 */

@SuppressWarnings("unchecked")
public class LockBasedHashTable<K, T> implements Map<K, T> {

	private class Entry {

		public K key;
		public T value;

		public Entry(K key, T value) {
			this.key = key;
			this.value = value;
		}

		public int hashCode() {
			return key.hashCode();
		}

		public boolean equals(Object o) {
			return key.equals(o);
		}

	}

	private final ReadWriteLock lock;
	protected List<Entry>[] table;
	protected int size;

	public LockBasedHashTable(int capacity) {
		lock = new ReentrantReadWriteLock();
		size = 0;
		table = (List<Entry>[]) new List[capacity];
		for (int i = 0; i < capacity; i++) {
			table[i] = new ArrayList<Entry>();
		}
	}

	/**
	 * Is item in set?
	 * 
	 * @param x
	 *            item to test
	 * @return <code>true</code> iff item present
	 */
	public boolean contains(K k) {
		lock.readLock().lock();
		try {
			int myBucket = Math.abs(k.hashCode() % table.length);
			return table[myBucket].contains(k);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Add item to set
	 * 
	 * @param x
	 *            item to add
	 * @return <code>true</code> iff set changed
	 */
	public T put(K k, T t) {
		boolean result = false;
		lock.writeLock().lock();
		try {
			int myBucket = Math.abs(k.hashCode() % table.length);
			result = table[myBucket].add(new Entry(k, t));
			size = result ? size + 1 : size;
		} finally {
			lock.writeLock().unlock();
		}

		if (policy()) {
			resize();
		}

		return result ? t : null;
	}

	/**
	 * Remove item from set
	 * 
	 * @param x
	 *            item to remove
	 * @return <code>true</code> iff set changed
	 */
	public T remove(K k) {
		lock.writeLock().lock();
		try {
			int myBucket = Math.abs(k.hashCode() % table.length);
			int index = table[myBucket].indexOf(k);
			boolean result = index > -1;
			if (result) {
				Entry entry = table[myBucket].get(index);
				table[myBucket].remove(index);
				size = result ? size - 1 : size;
				return entry.value;
			}
			return null;
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * double the set size
	 */
	public void resize() {
		int oldCapacity = table.length;
		lock.writeLock().lock();
		try {
			if (oldCapacity != table.length) {
				return; // someone beat us to it
			}
			int newCapacity = 2 * oldCapacity;
			List<Entry>[] oldTable = table;
			table = (List<Entry>[]) new List[newCapacity];
			for (int i = 0; i < newCapacity; i++)
				table[i] = new ArrayList<Entry>();
			for (List<Entry> bucket : oldTable) {
				for (Entry x : bucket) {
					int myBucket = Math.abs(x.hashCode() % table.length);
					table[myBucket].add(x);
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * decide whether to resize
	 * 
	 * @return whether to resize
	 */
	public boolean policy() {
		return size / table.length > 4;
	}

}
