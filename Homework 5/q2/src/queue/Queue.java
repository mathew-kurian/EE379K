package queue;

import common.SequentialCollection;

public abstract class Queue<T> implements SequentialCollection<T> {
	@Override
	public final void add(T t) {
		enqueue(t);
	}

	@Override
	public final T get() {
		return dequeue();
	}

	public abstract boolean enqueue(T t);

	public abstract T dequeue();
}
