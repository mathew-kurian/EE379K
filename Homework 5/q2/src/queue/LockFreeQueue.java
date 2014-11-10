package queue;

import java.util.concurrent.atomic.AtomicReference;

//single consumer lock free unbounded queue
public class LockFreeQueue<T> extends Queue<T> {
	
	private static class Node<T> {
		final T item;
		final AtomicReference<Node<T>> next;
		public Node(T item, Node<T> next){
			this.item = item;
			this.next = new AtomicReference<Node<T>> (next);
		}
	}
	
	private final Node<T> temp;
	private final AtomicReference<Node<T>> head;
	private final AtomicReference<Node<T>> tail;

	public LockFreeQueue(){
		temp = new Node<T>(null,null);
		head = new AtomicReference<Node<T>>(temp);
		tail = new AtomicReference<Node<T>>(temp);
	}
	
	//puts t into the queue, returns true when successful
	@Override
	public boolean enqueue(T t) {
		Node<T> newNode = new Node<T> (t,null);
		while(true){
			Node<T> currTail = tail.get();
			Node<T> nextTail = currTail.next.get();
			if(currTail == tail.get()){
				if(nextTail!=null){
					tail.compareAndSet(currTail, nextTail);
				} else {
					if(currTail.next.compareAndSet(null, newNode)){
						tail.compareAndSet(currTail, newNode);
						return true;
					}
				}
			}
		
		}
	}

	//takes T out of the queue
	@Override
	public T dequeue() {
		for(;;){
			Node<T> oldHead = head.get();
			Node<T> oldTail = tail.get();
			Node<T> oldNextHead = oldHead.next.get();
			if(oldHead == head.get()){
				if(oldHead == oldTail){
					if(oldNextHead == null) return null;
					tail.compareAndSet(oldTail, oldNextHead);
				} else {
					if(head.compareAndSet(oldTail, oldNextHead))
						return oldNextHead.item;
				}
			}
		}
	}

	
	/*
	final AtomicReferenceArray<T> array;
	final AtomicLong head;
	final AtomicLong tail;
	Node<T> headNode;
	final AtomicReference<Node<T>> tailNode;
	final int mask;
	
	//constructor
	//parameter is the initial estimated capacity for the Queue
	public LockFreeQueue(int capacity){
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
	  */
}
