package q3;

import java.util.concurrent.atomic.AtomicIntegerArray;

// TODO
// Implement the bakery algorithm

public class BakeryLock implements MyLock {
	
	private volatile boolean[] flag;
	private volatile AtomicIntegerArray label;
	private volatile Integer numThread;

	public BakeryLock(int numThread) {
		flag = new boolean[numThread];
		label = new AtomicIntegerArray(numThread);
		this.numThread = numThread;
		for (int i = 0; i < numThread; i++) {
			flag[i] = false;
			label.set(i, 0);
		}
	}

	@Override
	public void lock(int myId) {
		
		flag[myId] = true;
		
        for (int j = 0; j < numThread; j++){
            if (label.get(j) > label.get(myId)){
            	label.set(myId, label.get(j));
            }
        }
        
        label.set(myId, label.get(myId) + 1);
		flag[myId] = false;
						
		for (int k = 0; k < numThread; k++){
	         if(k != myId){
				while(flag[k]);
				while((label.get(myId) != 0) 
						&& ((label.get(myId)) < label.get(myId)) 
						|| ((label.get(myId) == label.get(myId)) && k < myId));
			};
		}
	}
	
	@Override
	public void unlock(int myId) {
		label.set(myId, 0);
	}
}