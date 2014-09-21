package q5;

// TODO
// Use synchronized, wait(), notify(), and notifyAll() to implement the bathroom
// protocol
public class BathroomSynProtocol implements Protocol {

	public volatile Integer m_male = 0;
	public volatile Integer m_female = 0;

	public Object m_maleWait = new Object();
	public Object m_femaleWait = new Object();

	public void enterMale() {
		synchronized (this) {

			try {
				while (m_female > 0) {
					m_maleWait.wait();
				}

				m_male++;

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void leaveMale() {
		synchronized (this) {

			m_male--;

			if (m_male == 0) {
				m_femaleWait.notify();
			}
		}
	}

	public void enterFemale() {
		synchronized (this) {

			try {
				while (m_male > 0) {
					m_femaleWait.wait();
				}

				m_female++;

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void leaveFemale() {
		synchronized (this) {

			m_female--;

			if (m_female == 0) {
				m_maleWait.notify();
			}
		}

	}
}
