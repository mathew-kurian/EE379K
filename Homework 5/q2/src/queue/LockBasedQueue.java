package queue;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;

//single consumer lock based unbounded queue 
public class LockBasedQueue<T> extends Queue<T> {

	final AtomicReferenceArray<T> array;
	final AtomicLong head;
	final AtomicLong tail;
	Node<T> headNode;
	final AtomicReference<Node<T>> tailNode;
	
	//constructor
	//parameter is the initial estimated capacity for the Queue
	public LockBasedQueue(int capacity){
		if(capacity <= 0){
			throw new IllegalArgumentException();
		}
		array = new AtomicReferenceArray<T>(capacity);
		mask = array.length() -1;
		head = new AtomicLong();
		
		
		
	}
	//similar to offer
	@Override
	public void enqueue(T t) {
		// TODO Auto-generated method stub

	}

	//similar to poll
	@Override
	public T dequeue() {
		// TODO Auto-generated method stub
		return null;
	}

}
