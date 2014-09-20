package q3;

// TODO 
// Implement Fast Mutex Algorithm
public class FastMutexLock implements MyLock {

	private boolean[] m_flags;
	private volatile int m_x = -1;
	private volatile int m_y = -1;

	public FastMutexLock(int numThread) {
		m_flags = new boolean[numThread];
	}

	@Override
	public void lock(int i) {
		while (true) {
			m_flags[i] = true;
			m_x = i;
			if (m_y != -1) {
				m_flags[i] = false;
				while (m_y != -1) {
				}
				continue;
			} else {
				m_y = i;
				if (m_x == i) {
					return;
				} else {
					m_flags[i] = false;
					boolean search = true;
					while (search) {
						boolean res = false;
						for (boolean flag : m_flags) {
							res |= flag;
						}
						search = res;
					}
					if (m_y == i) {
						return;
					} else {
						while (m_y != -1) {
						}
						continue;
					}
				}
			}
		}
	}

	@Override
	public void unlock(int i) {
		m_y = -1;
		m_flags[i] = false;
	}
}
