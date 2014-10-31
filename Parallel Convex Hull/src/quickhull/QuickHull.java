package quickhull;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

public class QuickHull {
		
	Point [] points;
	List<Point> polygon;
	
	public QuickHull(Point [] points){
		this.points = points;
		this.polygon = new LinkedList<Point>();
	}
	
	public void run(){
		Point p1 = findMax(Direction.EAST);
		Point p2 = findMax(Direction.WEST);
		
		polygon.add(p1);
		polygon.add(p2);
		
		for(Point point : points){
			if(pointInPolygon(point)){
				continue;
			}
			
			
		}
	}
	
	boolean pointInPolygon(Point point) {
			
		int polySides = this.points.length;
		
		   int   i, j=polySides-1 ;
		   boolean  oddNodes= false      ;

		   for (i=0; i<polySides; i++) {
		     if ((points[i].y < point.y && points[j].y>=point.y ||   points[j].y< point.y && points[i].y>=point.y)
		     &&  (points[i].x<=point.x || points[j].x<=point.x)) {
		       if (points[i].x+(point.y-points[i].y)/(points[j].y-points[i].y)
		    		   *(points[j].x-points[i].x)<point.x) {
		         oddNodes=!oddNodes; }}
		     j=i; 
		  }

		   return oddNodes; 
	} 
	
	enum Direction {
		NORTH, SOUTH, EAST, WEST
	}
	
	public Point findMax(Direction dir){
		Point p = null;
		int val;
		
		switch(dir){
		case NORTH:
		case SOUTH:
		case EAST:
			val = Integer.MIN_VALUE;
			for(Point point : points){
				if(point.x > val){
					p = point;
				}
			}
			break;
		case WEST:
			val = Integer.MAX_VALUE;
			for(Point point : points){
				if(point.x < val){
					p = point;
				}
			}
			break;
		}
		
		return p;
	}
}
