package q5;

// TODO
// Use synchronized, wait(), notify(), and notifyAll() to implement the bathroom
// protocol
public class BathroomSynProtocol implements Protocol {

	public volatile Integer m_male = 0;
	public volatile Integer m_female = 0;

	public void enterMale() {

		try {
			synchronized (m_male) {

				while (m_female > 0) {
					m_male.wait();
				}

				m_male++;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void leaveMale() {
		synchronized (m_male) {
			m_male--;

			if (m_male == 0) {
				m_female.notify();
			}
		}
	}

	public void enterFemale() {
		try {
			synchronized (m_female) {

				while (m_male > 0) {
					m_female.wait();
				}
				
				m_female++;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void leaveFemale() {
		synchronized (m_female) {

			m_female--;

			if (m_female == 0) {
				m_male.notify();
			}
		}
	}

}
