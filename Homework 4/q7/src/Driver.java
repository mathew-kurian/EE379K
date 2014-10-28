import q7.*;

public class Driver {
    
	public static void main(String args[]) {
        Test test = new Test();
        test.start(new LinkedList[] { new LockBased(), new LockFreeList() }, 8);
    }
}