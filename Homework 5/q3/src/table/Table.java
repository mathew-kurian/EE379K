package table;

public interface Table<K, T> {

	public T put(K k, T t);

	public T remove(K k);

	public boolean contains(K k);
}
