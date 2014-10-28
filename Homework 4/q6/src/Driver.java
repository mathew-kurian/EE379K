import q6.*;

public class Driver {

	public static void main(String[] args) {
		new Test(new MCSLock(), 8, 1000).start();
		new Test(new CLHLock(), 8, 1000).start();
	}

}
