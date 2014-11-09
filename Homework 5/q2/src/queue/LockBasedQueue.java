package queue;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

//single consumer lock based unbounded queue 
public class LockBasedQueue<T> extends Queue<T> {

	final AtomicReferenceArray<T> array;
	final AtomicLong head;
	final AtomicLong tail;
	Node<T> headNode;
	final AtomicReference<Node<T>> tailNode;
	final int mask;
	
	//constructor
	//parameter is the initial estimated capacity for the Queue
	public LockBasedQueue(int capacity){
		if(capacity <= 0){
			throw new IllegalArgumentException();
		}
		array = new AtomicReferenceArray<T>(capacity);
		mask = array.length() -1;
		head = new AtomicLong();
		tail = new AtomicLong();
		headNode = new Node<T>(null);
		tailNode = new AtomicReference<Node<T>>(headNode);
		
	}
	//similar to offer
	@Override
	public void enqueue(T t) {
		if (t == null){
			throw new NullPointerException();
		}
		
		long t1 = tail.getAndIncrement();
		long h = head.get();
		if ((t1-h) < array.length()){
			int index = (int) (t1 & mask);
			array.lazySet(index, t);
		} else {
			Node<T> node = new Node<T> (t);
			tailNode.getAndSet(node).lazySet(node);
		}
	}

	//similar to poll
	@Override
	public T dequeue() {
		long h = head.get();
		long t = tail.get();
		if (h == t){
			return null;
		}
		
		int index = (int) (h & mask);
		T t1 = array.get(index);
		if (t1 == null){
			Node<T> next = headNode.get();
			headNode = next;
			t1 = next.value;
		} else {
			array.lazySet(index, null);
		}
		
		head.lazySet(h + 1);
		return t1;
	}
	
	static final class Node<T> extends AtomicReference<Node<T>> {
	    private static final long serialVersionUID = 1L;

	    final T value;

	    Node(T value) {
	      this.value = value;
	    }
	  }

}
