import static org.junit.Assert.*;

import org.junit.Test;

public class SequentialCollectionTests {

	private static final String OPERATIONS = "12000";

	@Test
	public void lockFreeQueueTest() {
		for (int i = 1; i <= 6; i++) {
			Driver.main(new String[] { "queue", "lock-free", Integer.toString(i), OPERATIONS });
		}

		assertTrue(true);
	}

	@Test
	public void lockBasedQueueTest() {
		for (int i = 1; i <= 6; i++) {
			Driver.main(new String[] { "queue", "lock-based", Integer.toString(i), OPERATIONS });
		}

		assertTrue(true);
	}

	@Test
	public void lockFreeStackTest() {
		for (int i = 1; i <= 6; i++) {
			Driver.main(new String[] { "stack", "lock-free", Integer.toString(i), OPERATIONS });
		}

		assertTrue(true);
	}

	@Test
	public void lockFreeContentionManagedStackTest() {
		for (int i = 1; i <= 6; i++) {
			Driver.main(new String[] { "stack", "lock-free-contention-managed", Integer.toString(i), OPERATIONS });
		}

		assertTrue(true);
	}

	@Test
	public void lockBasedStackTest() {
		for (int i = 1; i <= 6; i++) {
			Driver.main(new String[] { "stack", "lock-based", Integer.toString(i), OPERATIONS });
		}

		assertTrue(true);
	}

}