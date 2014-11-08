package q3;

public interface Map<K, T> {

	public T put(K k, T t);

	public T remove(K k);

	public boolean contains(K k);
}
