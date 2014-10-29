import static org.junit.Assert.*;

import org.junit.Test;

public class LockTests {
	
	@Test
	public void testReentrantLock() {
		Driver.main(new String[] { "reentrant", "8", "1200000" });
		assertTrue(true);
	}

	@Test
	public void testCLHLock() {
		Driver.main(new String[] { "clh", "8", "1200000" });
		assertTrue(true);
	}

	@Test
	public void testMCSLock() {
		Driver.main(new String[] { "mcs", "8", "1200000" });
		assertTrue(true);
	}
}
