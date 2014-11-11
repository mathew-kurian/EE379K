import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class SequentialCollectionTests {

	private static final String OPERATIONS = "2500000";

    @Rule
    public Timeout TIMEOUT = new Timeout(12000);

	@Test
	public void lockFreeQueueTest() {
		for (int i = 1; i <= 6; i++) {
			Driver.main(new String[] { "queue", "lock-free",
					Integer.toString(i), OPERATIONS });
		}

		assertTrue(true);
	}

	@Test
	public void lockBasedQueueTest() {
		for (int i = 1; i <= 6; i++) {
			Driver.main(new String[] { "queue", "lock-based",
					Integer.toString(i), OPERATIONS });
		}

		assertTrue(true);
	}

	@Test
	public void lockFreeStackTest() {
		for (int i = 1; i <= 6; i++) {
			Driver.main(new String[] { "stack", "lock-free",
					Integer.toString(i), OPERATIONS });
		}

		assertTrue(true);
	}

	@Test
	public void lockFreeEliminationBackOffStackTest() {
		for (int i = 1; i <= 6; i++) {
			Driver.main(new String[] { "stack", "lock-free-elimination-backoff",
					Integer.toString(i), OPERATIONS });
		}

		assertTrue(true);
	}

	@Test
	public void lockBasedStackTest() {
		for (int i = 1; i <= 6; i++) {
			Driver.main(new String[] { "stack", "lock-based",
					Integer.toString(i), OPERATIONS });
		}

		assertTrue(true);
	}

}
