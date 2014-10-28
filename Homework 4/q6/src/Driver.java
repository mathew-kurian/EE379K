import q6.MCSLock;

public class Driver {

	public static void main(String[] args) {
		Test test = new Test(new MCSLock(), 2, 1000);
		test.start();
	}

}
