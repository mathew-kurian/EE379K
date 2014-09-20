package lamport;

class Splitter {
	
	// private id
	private volatile long m_pid;
	
	// stopped a thread
	private volatile boolean m_stopped;

	enum Direction {
		RIGHT, DOWN, STOP
	}
	
	public Splitter(){
		m_stopped = false;
		m_pid = -1;
	}
	
	public Direction getDirection(long pid){
		
		m_pid = pid;
		
		if(m_stopped) {
			return Direction.RIGHT;
		} else {
			m_stopped = true;
			if(pid == m_pid){
				return Direction.STOP;
			} else {
				System.out.println("HERER");
				return Direction.DOWN;
			}
		}
	}
	
	public void release(){
		m_stopped = false;
	}
}
