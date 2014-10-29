import static org.junit.Assert.*;

import org.junit.Test;


public class ListTests {

	@Test
	public void testLockBased() {
		Driver.main(new String[] { "lock-based", "8" });
		assertTrue(true);
	}

	@Test
	public void testLockFree() {
		Driver.main(new String[] { "lock-free", "8" });
		assertTrue(true);
	}
}
