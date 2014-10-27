package q7;

public class CoarseGrainedConcurrentLinkedList extends LinkedList {
		
	@Override
	public boolean add(Integer x) {
		
		synchronized(this) {
			
			// Head related issues
			if(this.head == null){
				this.head = new Entry(x);
			} else if(x < this.head.value){
				Entry entry = new Entry(x);
				entry.next = head;
				this.head = entry;
			}
			
			// Other
			Entry next = this.head;
			Entry last = null;
			
			while(next != null && next.value <= x){
				last = next;
				next = next.next;
			}
			
			if(last.value == x){
				return false;
			} 
			
			Entry entry = new Entry(x);

			entry.next = last.next;
			last.next = entry;
		}
		
		return true;
	}

	@Override
	public boolean remove(Integer x) {
		
		synchronized(this){

			// Head related issues
			if(this.head == null){
				return false;
			} else if(x < this.head.value){
				return false;
			} else if(x == this.head.value){
				this.head = this.head.next;
				return true;
			}
			
			Entry start = this.head;
			
			while(start.next != null){
				if(start.next.value == x){
					start.next = start.next.next;
					return true;
				}
				
				start = start.next;
			}
		}
		
		return false;
	}

	@Override
	public boolean contains(Integer x) {
		
		synchronized(this){
			Entry next = this.head;
			while(next != null){
				if(next.value == x){
					return true;
				}
				next = next.next;
			}
		}
		
		return false;
	}

}
