package q7;

public abstract class LinkedList {

	Entry head = null;

	public abstract boolean add(Integer x);

	public abstract boolean remove(Integer x);

	public abstract boolean contains(Integer x);

	public String toString() {
		
		if(this.head == null){
			return "[]";
		}
		
		StringBuilder smb = new StringBuilder();
		Entry next = this.head;
		while (next != null) {
			smb.append(next.value + ",");
			next = next.next;
		}

		return smb.substring(0, smb.length() - 1).toString();
	}

}
