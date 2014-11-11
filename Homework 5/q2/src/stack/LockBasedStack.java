package stack;

import com.sun.istack.internal.NotNull;
 
public class LockBasedStack<T> extends Stack<T> {

	private static class Node<T> {
		T value;
		Node<T> next;

		public Node(T value, Node<T> next) {
			this.next = next;
			this.value = value;
		}
	}
	
	@NotNull
	private final Node<T> head;
	
	public LockBasedStack() {
		this.head = new Node<T>(null, null);
	}

	@Override
	public boolean push(T t) {
		Node<T> newNode = new Node<T>(t, null);
		synchronized (head) {
			newNode.next = head.next;
			head.next = newNode;
		}
		return true;
	}

	@Override
	public T pop() {
		T t = null;
		synchronized (head) {
			Node<T> curr = head.next;
			if (curr != null) {
				t = curr.value;
				head.next = curr.next;
			}
		}
		return t;
	}

}
