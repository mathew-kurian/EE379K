package q5;

// TODO
// Use synchronized, wait(), notify(), and notifyAll() to implement the bathroom
// protocol
public class BathroomSynProtocol implements Protocol {

	public volatile Integer m_lock = 0;
	public volatile Integer m_male = 0;
	public volatile Integer m_female = 0;
	public volatile Integer m_maleInc = 0;
	public volatile Integer m_femaleInc = 0;

	public void enterMale() {

		try {
			synchronized (m_lock) {
				synchronized (m_male) {

					while (m_female > 0) {
						m_lock.notifyAll();
						m_male.wait();
					}

					synchronized (m_maleInc) {
						m_male++;
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void leaveMale() {
		synchronized (m_lock) {
			synchronized (m_female) {
				synchronized (m_maleInc) {
					m_male--;
				}

				if (m_male == 0) {
					m_lock.notifyAll();
					m_female.notifyAll();
				}
			}
		}
	}

	public void enterFemale() {
		try {
			synchronized (m_lock) {
				synchronized (m_female) {

					while (m_male > 0) {
						m_lock.notifyAll();
						m_female.wait();
					}

					synchronized (m_femaleInc) {
						m_female++;
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void leaveFemale() {
		synchronized (m_lock) {
			synchronized (m_female) {
				synchronized (m_femaleInc) {
					m_female--;
				}

				if (m_female == 0) {
					m_lock.notifyAll();
					m_male.notifyAll();
				}
			}
		}
	}

}
