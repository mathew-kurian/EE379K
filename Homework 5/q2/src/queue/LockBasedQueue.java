package queue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


//single consumer lock based unbounded queue 
public class LockBasedQueue<T> extends Queue<T> {
	
	private static class Node<T> {
		T value;
		Node<T> next;
		Lock lock;

		public Node(T value,Node<T> next) {
			this.next = next;
			this.value = value;
			this.lock = new ReentrantLock();
		}

		void lock() {
			lock.lock();
		}

		void unlock() {
			lock.unlock();
		}
	}
	private Node<T> head;
	private Node<T> tail;
	private final Lock headerLock;
	private final Lock tailLock;
	
	public LockBasedQueue(){
		this.head = new Node<T>(null,null);
		this.tail = this.head;
		this.headerLock = new ReentrantLock();
		this.tailLock = new ReentrantLock();
	}
	
	@Override
	public boolean enqueue(T t) {
		// TODO Auto-generated method stub
		Node<T> newNode = new Node<T>(t,null);
		synchronized(tailLock){
			tail.next = newNode;
			tail = newNode;
		}
		return true;
	}

	@Override
	public T dequeue() {
		// TODO Auto-generated method stub
		T t = null;
		synchronized(headerLock){
			Node<T> node = this.head;
			Node<T> newHead = node.next;
			if(newHead != null){
				t = newHead.value;
				head = newHead;
				node = null;
			}
		}
		return t;
	}

}
