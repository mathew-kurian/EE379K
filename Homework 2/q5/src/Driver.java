import javax.swing.SwingUtilities;

import q5.BathroomSynProtocol;
import q5.BathroomLockProtocol;

public class Driver {

	public static void main(String[] args) {
		test(new BathroomSynProtocol(), 5);
	}

	public static GenderThread createGenderThread(final BathroomSynProtocol protocol,
			GenderThread.Gender gender, final Bathroom bathroom) {
		return new GenderThread(protocol, gender) {
			@Override
			public void onEnter(Gender gender) {
				bathroom.onEnter(gender, gender == Gender.MALE ? protocol.m_male : protocol.m_female);

			}

			@Override
			public void onLeave(Gender gender) {
				bathroom.onLeave(gender, gender == Gender.MALE ? protocol.m_male : protocol.m_female);
			}
		};
	}

	public static void test(final BathroomSynProtocol protocol,
			final int numThreads) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				// Show UI
				Bathroom bathroom = new Bathroom();				
				bathroom.createAndShowGUI();
				
				// Create genders
				for (int i = 0; i < numThreads; i++) {
					createGenderThread(
							protocol,
							i % 2 == 1 ? GenderThread.Gender.MALE
									: GenderThread.Gender.FEMALE, bathroom)
							.start();
				}
			}
		});
	}

}
