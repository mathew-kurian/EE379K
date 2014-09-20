import q4.PSearch;
import static java.lang.System.out;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		out.println(PSearch.parallelSearch(12, new int[] { 10, 12, 13, 15 }, 2));
		out.println(PSearch.parallelSearch(9, new int[] { 10, 12, 13, 15 }, 2));
		out.println(PSearch.parallelSearch(9, new int[] { 9 }, 2));
		out.println(PSearch.parallelSearch(9, new int[] { 7 }, 50));
		out.println(PSearch.parallelSearch(9, new int[] {}, 50));
		out.println(PSearch.parallelSearch(9, new int[] { 9, 9, 9, 9 }, 50));

	}

}
