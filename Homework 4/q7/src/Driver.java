import q7.CoarseGrainedConcurrentLinkedList;
import q7.LinkedList;


public class Driver {
	
	public static void main(String [] args){
		LinkedList list = new CoarseGrainedConcurrentLinkedList();
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
