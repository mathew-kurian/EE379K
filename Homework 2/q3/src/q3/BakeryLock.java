package q3;

import java.util.Arrays;
import java.util.Collections;

// TODO
// Implement the bakery algorithm

public class BakeryLock implements MyLock {
	private boolean[] flag;
	private Integer[] label;
	private Integer numThread;


	public BakeryLock(int numThread) {
		flag = new boolean[numThread];
		label = new Integer[numThread];
		this.numThread = numThread;
		for (int i = 0; i < numThread; i++) {
			flag[i] = false;
			label[i] = 0;
		}
	}

	@Override
	public void lock(int myId) {
		flag[myId] = true;
		label[myId] = Collections.max(Arrays.asList(label)) + 1;
		flag[myId] = false;
		for (int k = 0; k < numThread; ++k){
			if( k != myId){
				while(flag[k]){};
				while((label[k] != 0) && ((label[k]) < label[myId]) || ((label[k] == label[myId]) && k < myId)) {};

			};
		};
	}

	@Override
	public void unlock(int myId) {
		label[myId] = 0;
		flag[myId] = false;
	}
}