package q3;

// TODO
// Use MyLock to protect the count

public class LockCounter extends Counter {
	public LockCounter(MyLock lock) {
	}

	@Override
	public void increment() {
	}

	@Override
	public int getCount() {
		return count;
	}
}
