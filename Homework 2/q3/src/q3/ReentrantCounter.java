package q3;

import java.util.concurrent.locks.ReentrantLock;

// TODO
// Use ReentrantLock to protect the count
public class ReentrantCounter extends Counter {
	
	private ReentrantLock m_lock;
	
	public ReentrantCounter(){
		super();
		m_lock = new ReentrantLock();		
	}
	
	@Override
	public void increment() {
		m_lock.lock();
		count++;
		m_lock.unlock();
	}

	@Override
	public int getCount() {
		return count;
	}
}
