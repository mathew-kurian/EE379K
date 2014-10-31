import java.awt.Point;
import java.util.Random;

import quickhull.QuickHull;

public class Driver {

	public static void main(String[] args) {
		QuickHull quickHull = new QuickHull(generateRandomPoints(20));
		quickHull.run();

	}

	public static Point [] generateRandomPoints(int length){
		Random rand = new Random();
		Point [] points = new Point [length];
		
		for(int i = 0; i < points.length; i++){
			points[i] = new Point(rand.nextInt(100), rand.nextInt(100));
		}
		
		return points;
	}
}
