import java.util.Random;

import table.LockBasedHashTable;
import table.LockFreeHashTable;
import table.Table;

public class Driver {

	private static int putOps = 0;
	private static int removeOps = 0;
	private static int containsOps = 0;

	public static void main(String[] args) {
		Table<Integer, Object> map = null;
		double executeTimeMS = 0;
		int numThread;
		int numTotalOps;

		if (args.length < 3) {
			System.err.println("Provide 3 arguments");
			System.err.println("\t(1) <algorithm>: lock-free/lock-based");
			System.err.println("\t(2) <numThread>: the number of test thread");
			System.err.println("\t(3) <numTotalOps>: the total number of "
					+ "operations performed");
			System.exit(-1);
		}

		numThread = Integer.parseInt(args[1]);
		numTotalOps = Integer.parseInt(args[2]);

		if (args[0].equals("lock-free")) {
			map = new LockFreeHashTable<Integer, Object>(100, 2);
		} else if (args[0].equals("lock-based")) {
			map = new LockBasedHashTable<Integer, Object>(100);
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
			threads[i] = new Thread(createRunnable(map,
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

		System.out.print("------------\n" + args[0] + ": ");
		System.out.printf("%fms (%d ops)\n------------\n", executeTimeMS, numTotalOps);
		System.out.printf("%-20s   %-20s    %-20s\n", "put(K,T)",
				"contains(K,T)", "remove(K,T)");
		System.out.printf("%-20d   %-20d    %-20d\n\n", putOps, containsOps,
				removeOps);

		System.gc();
	}

	public static Runnable createRunnable(final Table<Integer, Object> map,
			final int increments) {
		return new Runnable() {
			@Override
			public void run() {
				Random rand = new Random();
				int putOps = 0, removeOps = 0, containsOps = 0;

				for (int i = 0; i < increments; i++) {
					double op = rand.nextDouble();
					int key = rand.nextInt(100000);
					if (op <= 0.5) {
						map.contains(key);
						containsOps++;
					} else if (0.5 < op && op <= 0.9) {
						map.put(key, new Object());
						putOps++;
					} else {
						map.remove(key);
						removeOps++;
					}
				}

				synchronized (map) {
					Driver.putOps += putOps;
					Driver.removeOps += removeOps;
					Driver.containsOps += containsOps;
				}
			}
		};
	}
}
