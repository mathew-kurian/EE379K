package q6;

public abstract class Counter {
	public Counter() {
		count = 0;
	}

	protected volatile int count;

	public abstract void increment();

	public abstract int getCount();
}
