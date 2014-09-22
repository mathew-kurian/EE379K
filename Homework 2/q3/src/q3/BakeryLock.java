package q3;

// TODO
// Implement the bakery algorithm

public class BakeryLock implements MyLock {
	
	private volatile boolean[] flag;
	private volatile Integer[] label;
	private volatile Integer numThread;

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
		
        for (int j = 0; j < numThread; j++){
            if (label[j] > label[myId]){
            	label[myId] = label[j];
            }
        }
        
        label[myId]++;
		flag[myId] = false;
				
		System.out.println(label[myId]);
		
		for (int k = 0; k < numThread; k++){
	         if(k != myId){
				while(flag[k]);
				while((label[k] != 0) 
						&& ((label[k]) < label[myId]) 
						|| ((label[k] == label[myId]) && k < myId));
			};
		}
	}
	
	@Override
	public void unlock(int myId) {
		label[myId] = 0;
	}
}