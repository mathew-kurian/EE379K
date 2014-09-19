package lamport;

class Splitter {

	// global id
	public int m_id;
	
	// private id
	private volatile int m_pid;
	
	// stopped a thread
	private volatile boolean m_stopped;

	enum Direction {
		RIGHT, DOWN, STOP
	}
	
	public Splitter(){
		m_stopped = false;
		m_id = -1;
		m_id = -1;
	}
	
	public Direction createName(int pid){
		
		m_pid = pid;
		
		if(m_stopped) {
			return Direction.RIGHT;
		} else {
			m_stopped = true;
			if(pid == m_pid){
				return Direction.STOP;
			} else {
				return Direction.DOWN;
			}
		}
	}
	
	public void releaseName(){
		m_id = -1;
		m_stopped = false;
	}
}
