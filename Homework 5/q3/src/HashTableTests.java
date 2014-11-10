import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.rules.Timeout;

public class HashTableTests {

	private static final String THREAD_COUNT = "6";
	private static final String OPERATIONS = "12000";

    @Rule
    public Timeout TIMEOUT = new Timeout(4000);
    
	@org.junit.Test
	public void lockFree() {
		Driver.main(new String[] { "lock-free", THREAD_COUNT, OPERATIONS });
		assertTrue(true);
	}

	@org.junit.Test
	public void lockBased() {
		Driver.main(new String[] { "lock-based", THREAD_COUNT, OPERATIONS });
		assertTrue(true);
	}
}
