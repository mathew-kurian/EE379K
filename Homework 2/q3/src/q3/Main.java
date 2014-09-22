package q3;

public class Main {
	public static void main(String[] args) {
		Counter counter = null;
		MyLock lock;
		long executeTimeMS = 0;
		int numThread = 6;
		int numTotalInc = 1200000;

		/*
		 * ---- TESTING ---- ---- REMOVE BEFORE SUBMISSION ----
		 * 
		 * 0: type of algorithm - "fast/bakery/synchronized/reentrant" 1: # of
		 * threads 2: # of increments
		 * 
		 * ---- start ----
		 */
		args = new String[] { "bakery", "10", "1200000" };
		/*
		 * ---- end ----
		 */

		if (args.length < 3) {
			System.err.println("Provide 3 arguments");
			System.err.println("\t(1) <algorithm>: fast/bakery/synchronized/"
					+ "reentrant");
			System.err.println("\t(2) <numThread>: the number of test thread");
			System.err.println("\t(3) <numTotalInc>: the total number of "
					+ "increment operations performed");
			System.exit(-1);
		}

		numThread = Integer.parseInt(args[1]);
		numTotalInc = Integer.parseInt(args[2]);

		if (args[0].equals("fast")) {
			lock = new FastMutexLock(numThread);
			counter = new LockCounter(lock);
		} else if (args[0].equals("bakery")) {
			lock = new BakeryLock(numThread);
			counter = new LockCounter(lock);
		} else if (args[0].equals("synchronized")) {
			counter = new SynchronizedCounter();
		} else if (args[0].equals("reentrant")) {
			counter = new ReentrantCounter();
		} else {
			System.err.println("ERROR: no such algorithm implemented");
			System.exit(-1);
		}

		// TODO
		// Please create numThread threads to increment the counter
		// Each thread executes numTotalInc/numThread increments
		// Please calculate the total execute time in millisecond and store the
		// result in executeTimeMS

		// Create Threads
		Thread[] threads = new Thread[numThread];

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
