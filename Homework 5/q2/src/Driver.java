import java.util.Random;

import common.SequentialCollection;
import queue.LockBasedQueue;
import queue.LockFreeQueue;
import stack.LockBasedStack;
import stack.LockFreeStack;

public class Driver {

	public static void main(String[] args) {
		SequentialCollection<Object> collection = null;
		double executeTimeMS = 0;
		int numThread;
		int numTotalOps;

		if (args.length < 4) {
			System.err.println("Provide 4 arguments");
			System.err.println("\t(1) <structure>: queue/stack");
			System.err.println("\t(1) <algorithm>: lock-free/lock-based/"
					+ "reentrant");
			System.err.println("\t(2) <numThread>: the number of test thread");
			System.err.println("\t(3) <numTotalOps>: the total number of "
					+ "operations performed");
			System.exit(-1);
		}

		numThread = Integer.parseInt(args[2]);
		numTotalOps = Integer.parseInt(args[3]);
		
		args[0] = args[0] + "-" + args[1];
		
		if (args[0].equals("queue-lock-free")) {
			collection = new LockFreeQueue<Object>();
		} else if (args[0].equals("queue-lock-based")) {
			collection = new LockBasedQueue<Object>();
		} else if (args[0].equals("stack-lock-free")) {
			collection = new LockFreeStack<Object>();
		} else if (args[0].equals("stack-lock-based")) {
			collection = new LockBasedStack<Object>();
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
			threads[i] = new Thread(createRunnable(collection,
					(numTotalOps + numThread) / numThread));
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

		System.out.printf("%-20s (threads: %d) (time: %fms)\n", args[0], numThread, executeTimeMS);

		System.gc();
	}

	public static Runnable createRunnable(final SequentialCollection<Object> collection,
			final int increments) {
		return new Runnable() {
			@Override
			public void run() {
				Random rand = new Random();
				for (int i = 0; i < increments; i++) {
					double op = rand.nextDouble();
					if (op <= 0.6) {
						collection.add(new Object());
					} else {
						collection.get();
					}
				}
			}
		};
	}
}
