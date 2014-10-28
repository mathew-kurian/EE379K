import q7.CoarseList;
import q7.FineList;
import q7.LinkedList;
import q7.LockFreeList;
import q7.OptimisticList;

public class Driver {
    
	public static void main(String args[]) {
        Test test = new Test();
        test.start(new LinkedList[] { new CoarseList(), new FineList(), 
        		new OptimisticList(), new LockFreeList() }, 8);
    }
}