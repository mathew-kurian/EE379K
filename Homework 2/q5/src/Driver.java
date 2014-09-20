import javax.swing.SwingUtilities;

import q5.BathroomLockProtocol;
import q5.Protocol;

public class Driver {

	public static void main(String[] args) {
		test(BathroomLockProtocol.class, 5);
	}

	public static <T extends Protocol> void test(final Class<T> t,
			final int numThreads) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				Protocol protocol = null;

				try {
					protocol = t.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
				
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

	public static GenderThread createGenderThread(Protocol protocol,
			GenderThread.Gender gender, final Bathroom bathroom) {
		return new GenderThread(protocol, gender) {
			@Override
			public void onEnter(Gender gender) {
				bathroom.onEnter(gender);

			}

			@Override
			public void onLeave(Gender gender) {
				bathroom.onLeave(gender);
			}
		};
	}

}
