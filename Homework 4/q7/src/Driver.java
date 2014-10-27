import q7.CoarseList;
import q7.LinkedList;

public class Driver {
    
	public static void main(String args[]) {
		LinkedList list = new CoarseList();
        Test test = new Test();
        test.testAdd(8, 5000, list);
        test.testRemove(8, 5000, list);
    }
}