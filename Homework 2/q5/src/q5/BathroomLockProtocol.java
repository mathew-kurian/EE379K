package q5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// TODO
// Use locks and condition variables to implement the bathroom protocol
public class BathroomLockProtocol implements Protocol {
	// declare the lock and conditions here

	private Lock m_lock = new ReentrantLock();
	private Condition m_maleWait = m_lock.newCondition();
	private Condition m_femaleWait = m_lock.newCondition();

	public volatile int m_male = 0;
	public volatile int m_female = 0;

	public void enterMale() {
		m_lock.lock();

		try {
			while (m_female > 0) {
				m_maleWait.await();
			}

			m_male++;

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			m_lock.unlock();
		}
	}

	public void leaveMale() {
		m_lock.lock();

		try {
			m_male--;

			if (m_male == 0) {
				m_femaleWait.signalAll();
			}

		} finally {
			m_lock.unlock();
		}
	}

	public void enterFemale() {
		m_lock.lock();

		try {
			while (m_male > 0) {
				m_femaleWait.await();
			}

			m_female++;

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			m_lock.unlock();
		}

	}

	public void leaveFemale() {
		m_lock.lock();

		try {
			m_female--;

			if (m_female == 0) {
				m_maleWait.signalAll();
			}
		} finally {
			m_lock.unlock();
		}

	}
}
