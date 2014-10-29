package q6;

// TODO
// Use MyLock to protect the count

public class LockCounter extends Counter {

	private MyLock m_lock;

	public LockCounter(MyLock lock) {
		m_lock = lock;
	}

	@Override
	public void increment() {
		int id = (int) Thread.currentThread().getId();
		m_lock.lock();
		count++;
		m_lock.unlock();

	}

	@Override
	public int getCount() {
		return count;
	}
}
