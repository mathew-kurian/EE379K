import q7.CoarseList;
import q7.FineList;
import q7.LinkedList;
import q7.LockFreeList;
import q7.OptimisticList;


public class Driver {
	
	public static void main(String [] args){
		test(new FineList());
		test(new CoarseList());
		test(new OptimisticList());
		test(new LockFreeList());
	}
	
	static void test(LinkedList list){
		list.add(10);
		list.add(12);
		list.add(9);
		list.add(12);
		list.add(13);
		list.add(8);
		list.remove(9);
		System.out.println(list.toString());
	}
}
