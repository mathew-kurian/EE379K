import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

import q7.LinkedList;

public class Test {

	// Runnable task for each thread
	private abstract class Task implements Runnable {

		protected LinkedList list;
		protected CyclicBarrier start, block, end;
		protected Random rand;
		protected int max;

		public Task(CyclicBarrier start, CyclicBarrier block, CyclicBarrier end, LinkedList list, int max) {
			this.start = start;
			this.block = block;
			this.end = end;
			this.list = list;
			this.rand = new Random();
			this.max = max;
		}

		@Override
		public void run() {

			try {
				start.await();
			} catch (InterruptedException | BrokenBarrierException e1) {
				e1.printStackTrace();
			}

			for (int i = 0; i < max; i++) {
				try {
					double probability = rand.nextDouble();
					block.await();
					execute(probability);
				} catch (InterruptedException | BrokenBarrierException ex) {
					ex.printStackTrace();
				}
			}

			try {
				end.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
		}

		public abstract void execute(double s);
	}

	private class AddTask extends Task {
		public AddTask(CyclicBarrier start, CyclicBarrier block, CyclicBarrier end, LinkedList list, int max) {
			super(start, block, end, list, max);
		}

		@Override
		public void execute(double s) {
			list.add(rand.nextInt());
		}
	}

	private void testAdd(final int threadCount, final int maxCount, final LinkedList list, final Runnable done) {
		final AtomicLong startTime = new AtomicLong();

		CyclicBarrier start = new CyclicBarrier(threadCount, new Runnable() {
			@Override
			public void run() {
				startTime.set(System.nanoTime());
			}
		});

		CyclicBarrier block = new CyclicBarrier(threadCount);
		CyclicBarrier end = new CyclicBarrier(threadCount, new Runnable() {
			@Override
			public void run() {
				System.out.println("- testAdd() - " + (double) (System.nanoTime() - startTime.get()) / 1000000000.0);
				done.run();
			}
		});

		for (int i = 0; i < threadCount; i++) {
			new Thread(new AddTask(start, block, end, list, (int) maxCount / threadCount), i + "").start();
		}
	}

	private class CombinationTask extends Task {
		public CombinationTask(CyclicBarrier start, CyclicBarrier block, CyclicBarrier end, LinkedList list, int max) {
			super(start, block, end, list, max);
		}

		@Override
		public void execute(double probability) {
			int value = rand.nextInt() % 100001;
			if (probability <= 0.4) {
				list.remove(value);
			} else if (probability > 0.5) {
				list.contains(value);
			} else {
				list.remove(value);
			}
		}
	}

	private void testCombination(final int threadCount, final int maxCount, final LinkedList list, final Runnable done) {
		final AtomicLong startTime = new AtomicLong();

		CyclicBarrier start = new CyclicBarrier(threadCount, new Runnable() {
			@Override
			public void run() {
				startTime.set(System.nanoTime());
			}
		});

		CyclicBarrier block = new CyclicBarrier(threadCount);
		CyclicBarrier end = new CyclicBarrier(threadCount, new Runnable() {
			@Override
			public void run() {
				System.out.println("- testCombination() - " + (double) (System.nanoTime() - startTime.get())
						/ 1000000000.0);
				done.run();
			}
		});

		for (int i = 0; i < threadCount; i++) {
			new Thread(new CombinationTask(start, block, end, list, (int) maxCount / threadCount), i + "").start();
		}
	}

	public void start(LinkedList[] lists, int N) {
		test(lists, 0, 0, Math.abs(N));
	}

	private void test(LinkedList[] lists, int index, int t, int N) {
		if (index >= lists.length) {
			return;
		}

		t = Math.max(t, 1);

		if (t > N) {
			test(lists, index + 1, 1, N);
			return;
		}

		final int threadCount = t;
		final LinkedList list = lists[index];

		System.out.printf("\n%s - %d thread(s)\n", list.getClass().getSimpleName(), t);

		testAdd(threadCount, 5000, list, new Runnable() {
			@Override
			public void run() {
				testCombination(threadCount, 25000, list, new Runnable() {
					@Override
					public void run() {
						test(lists, index, threadCount + 1, N);
					}
				});
			}
		});
	}
}
