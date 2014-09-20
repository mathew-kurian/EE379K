package q4;

import java.util.concurrent.Callable;

public class PSearch implements Callable<Integer> {

	// Declare variables or constructors here;
	// however, they will not be access by TA's test drvier.

	private static class PSearchResult {

		public volatile boolean m_search;
		public int m_index;

		public PSearchResult() {
			m_search = true;
			m_index = -1;
		}
	}

	private int[] m_a;
	private int m_x;
	private int m_start;
	private int m_end;
	private PSearchResult m_result;

	public PSearch(int[] A, int x, int start, int end, PSearchResult result) {
		m_a = A;
		m_x = x;
		m_start = start;
		m_end = end;
		m_result = result;
	}

	public static int parallelSearch(int x, int[] A, int numThreads) {

		if (A.length == 0) {
			return -1;
		}

		numThreads = Math.max(A.length, numThreads);

		final int[] arr = A;
		final int val = x;
		final Thread[] threads = new Thread[numThreads];
		final PSearchResult result = new PSearchResult();

		int index = 0;
		int delta = A.length / numThreads;

		for (int i = 0; i < numThreads; i++) {

			final int startIndex = index;
			final int endIndex = i + 1 == numThreads ? A.length : startIndex
					+ delta;
			final PSearch searcher = new PSearch(arr, val, startIndex,
					endIndex, result);

			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						searcher.call();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			thread.start();
			threads[i] = thread;

			index += delta;
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return result.m_index; // return -1 if the target is not found
	}

	public Integer call() throws Exception {

		// System.out.printf("%d -> %d\n", m_start, m_end);

		for (int i = m_start; i < m_end && m_result.m_search; i++) {
			if (m_a[i] == m_x) {
				m_result.m_search = false;
				m_result.m_index = i;
				return i;
			}
		}

		return -1;
	}
}
