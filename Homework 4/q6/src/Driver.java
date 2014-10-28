import q6.CLHLock;

public class Driver {

	public static void main(String[] args) {
		Test test = new Test(new CLHLock(), 8, 1000);
		test.start();
	}

}
