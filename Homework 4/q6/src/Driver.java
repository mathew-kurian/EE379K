import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import q6.*;

public class Driver {
	
	public static void main(String[] args) {
		Counter counter = null;
		MyLock lock;
		double executeTimeMS = 0;
		int numThread = 6;
		int numTotalInc = 1200000;
		
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

		if (args[0].equals("clh")) {
			lock = new CLHLock();
			counter = new LockCounter(lock);
		} else if (args[0].equals("mcs")) {
			lock = new MCSLock();
			counter = new LockCounter(lock);
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
		
		// Create cyclic barrier
		CyclicBarrier cb = new CyclicBarrier(numThread);
		
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(createRunnable(counter, numTotalInc, cb));
		}
		
		// Start time
		executeTimeMS = System.nanoTime();

		// Start Threads
		for (Thread thread : threads) {
			thread.start();
		}

		// Join threads
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// End time
		executeTimeMS = (double) (System.nanoTime() - executeTimeMS) / 1000000000.0;

		System.out.printf("%-15s: %fms\n", args[0], executeTimeMS);
		
		System.gc();
	}


	public static Runnable createRunnable(final Counter counter,
			final int increments, final CyclicBarrier cb) {
		return new Runnable() {
			@Override
			public void run() {
				while (counter.getCount() <= increments) {	
					counter.increment();
				}

				// Wait for threads to finish
				try {
					cb.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
				
			}
		};
	}
}
