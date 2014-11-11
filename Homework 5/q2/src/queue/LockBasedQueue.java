package queue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


//single consumer lock based unbounded queue 
public class LockBasedQueue<T> extends Queue<T> {
	
	private static class Node<T> {
		T value;
		Node<T> next;
		//Lock lock;

		public Node(T value,Node<T> next) {
			this.next = next;
			this.value = value;
			//this.lock = new ReentrantLock();
		}

		/*void lock() {
			lock.lock();
		}

		void unlock() {
			lock.unlock();
		}*/
	}
	private Node<T> head;
	private Node<T> tail;
	private final Lock headerLock;
	private final Lock tailLock;
	
	public LockBasedQueue(){
		this.head = new Node<T>(null,null);
		this.tail = this.head;
		//this.headerLock = new Object();
		//this.tailLock = new Object();
		this.headerLock = new ReentrantLock();
		this.tailLock = new ReentrantLock();
	}
	
	@Override
	public void enqueue(T t) {
		Node<T> newNode = new Node<T>(t,null);
		try{
			tailLock.lock();
			tail.next = newNode;
			tail = newNode;
		} finally{
			tailLock.unlock();
		}
	}

	@Override
	public T dequeue() {
		T t = null;
		try{
			headerLock.lock();
			Node<T> node = this.head;
			Node<T> newHead = node.next;
			if(newHead != null){
				t = newHead.value;
				head = newHead;
				node = null;
			}
		} finally{
			headerLock.unlock();
		}
		return t;
	}

}
