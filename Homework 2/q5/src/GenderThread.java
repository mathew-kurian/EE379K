import q5.Protocol;

public abstract class GenderThread extends Thread {

	public enum Gender {
		MALE, FEMALE
	}

	Protocol m_protocol;
	Gender m_gender;

	public GenderThread(Protocol protocol, Gender gender) {
		m_protocol = protocol;
		m_gender = gender;
	}

	public void run() {
		while (true) {

			if (m_gender == Gender.MALE) {
				m_protocol.enterMale();
			} else {
				m_protocol.enterFemale();
			}
					
			onEnter(m_gender);

			try {
				Thread.sleep((long) (Math.random() * 5000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (m_gender == Gender.MALE) {
				m_protocol.leaveMale();
			} else {
				m_protocol.leaveFemale();
			}

			onLeave(m_gender);

			try {
				Thread.sleep((long) (Math.random() * 5000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public abstract void onEnter(Gender gender);

	public abstract void onLeave(Gender gender);
}
