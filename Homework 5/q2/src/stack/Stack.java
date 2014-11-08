package stack;

import common.SequentialCollection;

public abstract class Stack<T> implements SequentialCollection<T> {
	@Override
	public final void add(T t) {
		push(t);
	}

	@Override
	public final T get() {
		return pop();
	}
	public abstract void push(T t);
	public abstract T pop();
}
