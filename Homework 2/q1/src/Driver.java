import lamport.Rename;


public class Driver {

	/**
	 * Issues: Not calling "DOWN"
	 */
	public static void main(String[] args) {
		
		Rename rename = new Rename(5);
		
		for(int i = 0; i < 10; i++){
			new AutoReleaseThread(rename).start();
		}
	}

}
