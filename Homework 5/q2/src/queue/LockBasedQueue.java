package queue;

import java.util.concurrent.locks.ReentrantLock;

//single consumer lock based unbounded queue 
public class LockBasedQueue<T> extends Queue<T> {
	
	private final ReentrantLock enLock = new ReentrantLock();
	private final ReentrantLock deLock = new ReentrantLock();
	
	@Override
	public boolean enqueue(T t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public T dequeue() {
		// TODO Auto-generated method stub
		return null;
	}

}
