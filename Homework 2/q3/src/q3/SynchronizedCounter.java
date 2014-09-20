package q3;

// TODO 
// Use synchronized to protect count
public class SynchronizedCounter extends Counter {
	
	private Object mutex = new Object();
	
	@Override
	public void increment() {
		synchronized(mutex){
			count++; 
		}
	}

	@Override
	public int getCount() {
		return count;
	}
}
