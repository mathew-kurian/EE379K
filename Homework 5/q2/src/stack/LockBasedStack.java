package stack;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockBasedStack<T> extends Stack<T> {

	private LockBasedList list = new LockBasedList();
	
	@Override
	public void push(T t) {
		list.add(t, 0);		
	}

	@Override
	public T pop() {
		return list.remove(0);
	}
	
	private class LockBasedList {

		Node head;

		public LockBasedList() {
			this.head = new Node(Integer.MIN_VALUE);
			this.head.next = new Node(Integer.MAX_VALUE);
		}

		private boolean validate(Node pred, Node curr) {
			return !pred.marked && !curr.marked && pred.next == curr;
		}

		public void add(T value, Integer index) {			
			if(index < 0 || index == Integer.MAX_VALUE){
				// InvalidIndexException
				return;
			}
			
			while (true) {
				Node pred = this.head;
				Node curr = head.next;
				while (curr.index < index) {
					pred = curr;
					curr = curr.next;
				}
				pred.lock();
				try {
					curr.lock();
					try {
						if (validate(pred, curr)) {
							Node node = new Node(index, value);
							node.next = curr;
							pred.next = node;
							return;
						}
					} finally {
						curr.unlock();
					}
				} finally {
					pred.unlock();
				}
			}
		}

		public T remove(Integer index) {
			if(index < 0 || index == Integer.MAX_VALUE){
				// InvalidIndexException
				return null;
			}
			
			while (true) {
				Node pred = this.head;
				Node curr = head.next;
				pred.lock();
				try {
					curr.lock();
					try {
						if (validate(pred, curr)) {
							if (curr.index == Integer.MAX_VALUE) {
								// Reached the end
								return null;
							} else {
								curr.marked = true;
								pred.next = curr.next;
								return curr.value;
							}
						}
					} finally {
						curr.unlock();
					}
				} finally {
					pred.unlock();
				}
			}
		}

		public class Node {
			int index;
			T value;
			Node next;
			boolean marked;
			Lock lock;

			Node(int item) {
				this.index = item;
				this.next = null;
				this.marked = false;
				this.lock = new ReentrantLock();
			}
			
			Node(int item, T value){
				this(item);
				this.value = value;
			}

			void lock() {
				lock.lock();
			}

			void unlock() {
				lock.unlock();
			}
		}
	}

}
