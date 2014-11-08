import static org.junit.Assert.*;

public class HashTableTests {

	@org.junit.Test
	public void lockFree() {
		Driver.main(new String[] { "lock-free", "6", "12000" });
		assertTrue(true);
	}

	@org.junit.Test
	public void lockBased() {
		Driver.main(new String[] { "lock-based", "6", "12000" });
		assertTrue(true);
	}
}
