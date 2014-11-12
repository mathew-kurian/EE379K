package table;

import java.util.concurrent.atomic.AtomicInteger;

/*
 * LockFreeHashTable.java
 *
 * Created on December 30, 2005, 12:48 AM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

/**
 * @param <T>
 *            type
 * @author Maurice Herlihy
 */
public class LockFreeHashTable<K, V> extends BucketList<K, V> implements
		Table<K, V> {
	protected BucketList<K, V>[] bucket;
	protected AtomicInteger bucketSize;
	protected AtomicInteger mapSize;
	private int policyFactor;

	/**
	 * Constructor
	 * 
	 * @param capacity
	 *            max number of buckets
	 */
	public LockFreeHashTable(int capacity, int policyFactor) {
		bucket = (BucketList<K, V>[]) new BucketList[capacity];
		bucket[0] = new BucketList<K, V>();
		bucketSize = new AtomicInteger(2);
		mapSize = new AtomicInteger(0);
		this.policyFactor = policyFactor;
	}

	/**
	 * Add item to set
	 * 
	 * @param x
	 *            item to add
	 * @return <code>true</code> iff set changed.
	 */
	public V put(K key, V value) {
		V oldValue;
		int myBucket = Math.abs(BucketList.hashCode(key) % bucketSize.get());
		BucketList<K, V> b = getBucketList(myBucket);
		if ((oldValue = b.add(key, value)) != null)
			return oldValue;
		int mapSizeNow = mapSize.getAndIncrement();
		int bucketSizeNow = bucketSize.get();
		if (mapSizeNow / (double) bucketSizeNow > policyFactor)
			bucketSize.compareAndSet(bucketSizeNow, 2 * bucketSizeNow);
		return null;
	}

	/**
	 * Remove item from set
	 * 
	 * @param x
	 *            item to remove
	 * @return <code>true</code> iff set changed.
	 */
	public V remove(K key) {
		V oldValue;
		int myBucket = Math.abs(BucketList.hashCode(key) % bucketSize.get());
		BucketList<K, V> b = getBucketList(myBucket);
		if ((oldValue = b.remove(key)) == null) {
			return null; // she's not there
		}
		mapSize.getAndDecrement();
		return oldValue;
	}

	public boolean contains(K key) {
		int myBucket = Math.abs(BucketList.hashCode(key) % bucketSize.get());
		BucketList<K, V> b = getBucketList(myBucket);
		return b.contains(key);
	}

	private BucketList<K, V> getBucketList(int myBucket) {
		if (bucket[myBucket] == null)
			initializeBucket(myBucket);
		return bucket[myBucket];
	}

	private void initializeBucket(int myBucket) {
		int parent = getParent(myBucket);
		if (bucket[parent] == null)
			initializeBucket(parent);
		BucketList<K, V> b = bucket[parent].getSentinel(myBucket);
		if (b != null)
			bucket[myBucket] = b;
	}

	private int getParent(int myBucket) {
		int parent = bucketSize.get();
		do {
			parent = parent >> 1;
		} while (parent > myBucket);
		parent = myBucket - parent;
		return parent;
	}

	public int size() {
		return mapSize.get();
	}

}
