import lamport.Rename;


public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Rename rename = new Rename(5, 10);
		
		System.out.println(rename.reserveId());
		System.out.println(rename.reserveId());
		System.out.println(rename.reserveId());
		System.out.println(rename.reserveId());
		System.out.println(rename.reserveId());
		
		rename.releaseId(2);
		rename.releaseId(4);
		
		System.out.println(rename.reserveId());
		System.out.println(rename.reserveId());

	}

}
