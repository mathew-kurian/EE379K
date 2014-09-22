package q3;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

// TODO
// Implement the bakery algorithm

public class BakeryLock implements MyLock {
	
	private volatile AtomicReferenceArray<Boolean> flag;
	private volatile AtomicIntegerArray label;
	private volatile Integer numThread;

	public BakeryLock(int numThread) {
//		flag = new boolean[numThread];
		flag = new AtomicReferenceArray<Boolean> (numThread);
		label = new AtomicIntegerArray(numThread);
		this.numThread = numThread;
		for (int i = 0; i < numThread; i++) {
			flag.set(i, false);
			label.set(i, 0);
		}
	}

	@Override
	public void lock(int myId) {
		
//		flag[myId] = true;
		flag.set(myId, true);
		
        for (int j = 0; j < numThread; j++){
            if (label.get(j) > label.get(myId)){
            	label.set(myId, label.get(j));
            }
        }
        
        label.set(myId, label.get(myId) + 1);
//		flag[myId] = false;
		flag.set(myId, false);
		
		System.out.println(label.get(myId));
		
		for (int k = 0; k < numThread; k++){
	         if(k != myId){
				while(flag.get(k));
				while ((label.get(k) != 0) &&
                        ((label.get(k) < label.get(myId)) ||
                        ((label.get(k) == label.get(myId)) && k < myId)));
			};
		}
	}
	
	@Override
	public void unlock(int myId) {
		label.set(myId, 0);
	}
}