package queue;

import java.util.concurrent.atomic.AtomicReference;

//single consumer lock free unbounded queue
public class LockFreeQueue<T> extends Queue<T> {

	private static class Node<T> {
		final T item;
		final AtomicReference<Node<T>> next;

		public Node(T item, Node<T> next) {
			this.item = item;
			this.next = new AtomicReference<Node<T>>(next);
		}
	}

	private final Node<T> temp;
	private final AtomicReference<Node<T>> head;
	private final AtomicReference<Node<T>> tail;

	public LockFreeQueue() {
		temp = new Node<T>(null, null);
		head = new AtomicReference<Node<T>>(temp);
		tail = new AtomicReference<Node<T>>(temp);
	}

	// puts t into the queue, returns true when successful
	@Override
<<<<<<< HEAD
	public void enqueue(T t) {
		Node<T> currTail;
		Node<T> nextTail;
		System.out.println("working enque");
		while(true){
			currTail = tail.get();
			nextTail = currTail.next.get();
			//trying something different
			/*if(currTail == tail.get()){
				if(nextTail!=null){
=======
	public boolean enqueue(T t) {
		Node<T> newNode = new Node<T>(t, null);
		while (true) {
			Node<T> currTail = tail.get();
			Node<T> nextTail = currTail.next.get();
			if (currTail == tail.get()) {
				if (nextTail != null) {
>>>>>>> b4911791f26d731df7fe9f2a6db4f6babfe02522
					tail.compareAndSet(currTail, nextTail);
				} else {
					if (currTail.next.compareAndSet(null, newNode)) {
						tail.compareAndSet(currTail, newNode);
						return true;
					}
				}
			}*/
			
			if(currTail == tail.get()){
				if (nextTail == null){
					if(tail.compareAndSet(currTail.next.get(), nextTail)){
						break;
					}
				} else {
					tail.compareAndSet(tail.get(), currTail);
				}
			}
<<<<<<< HEAD
=======

>>>>>>> b4911791f26d731df7fe9f2a6db4f6babfe02522
		}
		tail.compareAndSet(tail.get(), currTail);
	}

	// takes T out of the queue
	@Override
	public T dequeue() {
<<<<<<< HEAD
		//System.out.println("working deque");
		for(;;){
=======
		while (true) {
>>>>>>> b4911791f26d731df7fe9f2a6db4f6babfe02522
			Node<T> oldHead = head.get();
			Node<T> oldTail = tail.get();
			Node<T> oldNextHead = oldHead.next.get();
			if (oldHead == head.get()) {
				if (oldHead == oldTail) {
					if (oldNextHead == null)
						return null;
					tail.compareAndSet(oldTail, oldNextHead);
				} else {
					if (head.compareAndSet(oldTail, oldNextHead))
						return oldNextHead.item;
				}
			}
		}
	}

	
}
