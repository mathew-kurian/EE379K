package q3;

import java.util.Arrays;
import java.util.Collections;

// TODO
// Implement the bakery algorithm

public class BakeryLock implements MyLock {
	private boolean[] flag;
	private Integer[] label;

	public BakeryLock(int numThread) {
		flag = new boolean[numThread];
		label = new Integer[numThread];
		for (int i = 0; i < numThread; i++) {
			flag[i] = false;
			label[i] = 0;
		}
	}

	@Override
	public void lock(int myId) {
		flag[myId] = true;
		label[myId] = Collections.max(Arrays.asList(label)) + 1;
		for (Integer k : label) {
			while ((k != myId) && (flag[k]) && ((label[k]) < label[myId])
					|| ((label[k] == label[myId]) && k < myId)) {
			};
		};
	}

	@Override
	public void unlock(int myId) {
		flag[myId] = false;
	}
}
