

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long executeTimeMS = 0;
		int numThread = 6;
		
		Thread[] threads = new Thread[8];

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new CountedThread(
					createRunnable(counter, numTotalInc), i);
		}

		// Start time
		executeTimeMS = System.nanoTime();

		// Start Threads
		for (Thread thread : threads) {
			thread.start();
		}

		// Wait for threads to finish
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// End time
		executeTimeMS = System.nanoTime() - executeTimeMS;

		System.out.println(executeTimeMS);

	}
	
	public static Runnable createRunnable(final Counter counter,
			final int increments) {
		return new Runnable() {
			@Override
			public void run() {
				while (counter.getCount() <= increments) {	
					counter.increment();
				}
			}
		};
	}

}
