package q4;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PSearch implements Callable<Integer> {

	// Declare variables or constructors here;
	// however, they will not be access by TA's test drvier.

	private static class PSearchFlag {

		public volatile boolean m_search;

		public PSearchFlag() {
			m_search = true;
		}
	}

	private int[] m_a;
	private int m_x;
	private int m_start;
	private int m_end;
	private PSearchFlag m_result;

	public PSearch(int[] A, int x, int start, int end, PSearchFlag result) {
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

		ExecutorService pool = Executors.newFixedThreadPool(numThreads);
		Set<Future<Integer>> set = new HashSet<Future<Integer>>();
		PSearchFlag flag = new PSearchFlag();

		int val = x;
		int index = 0;
		int delta = A.length / numThreads;

		for (int i = 0; i < numThreads; i++) {

			int startIndex = index;
			int endIndex = i + 1 == numThreads ? A.length : startIndex + delta;
			PSearch searcher = new PSearch(A, val, startIndex, endIndex, flag);
			set.add(pool.submit(searcher));

			index += delta;
		}

		int res = -1;
				
		for (Future<Integer> future : set) {
			try {

				res = future.get();
				
				if(res > -1){
					break;
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		pool.shutdown();
		
		return res; // return -1 if the target is not found
	}

	public Integer call() throws Exception {

		for (int i = m_start; i < m_end && m_result.m_search; i++) {
			if (m_a[i] == m_x) {
				m_result.m_search = false;
				return i;
			}
		}

		return -1;
	}
}
