import static org.junit.Assert.*;

import org.junit.Test;

import stack.LockBasedStack;

public class Sandbox_RemoveBeforeSubmission {

//	@Test
//	public void lockBasedStackSimpleTest() {
//		LockBasedStack<Integer> lbs = new LockBasedStack<Integer>();
//		lbs.push(10);
//		lbs.push(9);
//		lbs.push(8);
//		lbs.push(7);
//
//		assertTrue(lbs.pop().equals(7));
//		assertTrue(lbs.pop().equals(8));
//		assertTrue(lbs.pop().equals(9));
//		assertTrue(lbs.pop().equals(10));
//		assertTrue(lbs.pop() == null);
//		assertTrue(true);
//	}
	
	@Test
	public void myTest(){
		Driver.main(new String[] { "stack", "lock-free-contention-managed", "1", "12000" });	
	}
}
