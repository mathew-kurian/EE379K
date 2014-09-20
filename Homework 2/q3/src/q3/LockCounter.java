package q3;

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
		m_lock.lock(id);
		count++;
		m_lock.unlock(id);

	}

	@Override
	public int getCount() {
		return count;
	}
}
