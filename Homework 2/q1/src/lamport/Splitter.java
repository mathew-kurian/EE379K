package lamport;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

class Splitter {

	// private id
	private AtomicLong m_pid;

	// stopped a thread
	public AtomicBoolean m_stopped;

	enum Direction {
		RIGHT, DOWN, STOP
	}

	public Splitter() {
		m_stopped = new AtomicBoolean(false);
		m_pid = new AtomicLong(-1);
	}

	public Direction getDirection(long pid) {

		m_pid.set(pid);

		if (m_stopped.get()) {
			return Direction.RIGHT;
		} else {
			m_stopped.set(true);
			if (m_pid.get() == pid) { 
				return Direction.STOP;
			} else {
				return Direction.DOWN;
			}
		}
	}

	public void release() {
		m_stopped.set(false);
	}
}
