package com.computation.algo;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import com.computation.common.ConvexHull;
import com.computation.common.Edge;
import com.computation.common.Point2D;
import com.computation.common.Utils;

public class GrahamScan extends ConvexHull
{
    public GrahamScan(int points, int width, int height, int threads, boolean debug) {
        super(points, width, height, threads, debug);
    }

	public GrahamScan(int points, int width, int height, int threads, boolean debug, int animationDelay) 
	{
		super(points, width, height, threads, debug, animationDelay);
	}

	protected void findHull() 
	{
		Stack stack = new Stack();
		// Set some fields
        pointCloud.setField("ThreadPool", true);
        
        // Get points
        List<Point2D> point2Ds = pointCloud.getPoints();
        final List<Point2D> sortedpoints = point2Ds;
        int numofPoints = point2Ds.size();
        Collections.sort(sortedpoints, new Comparator<Point2D>() 
        { 	
        	public int compare(Point2D p1, Point2D p2) 
        	{
        			if(p2.y == p1.y)
        			{
        				return p1.x - p2.x;
        			}
                	return p2.y - p1.y; 
           }
        });	
        final Point2D firstpoint = sortedpoints.get(0);
        stack.push(firstpoint);
        sortedpoints.remove(0);
        List<Point2D> anglesortedpoints = sortedpoints;
        System.out.println(anglesortedpoints);
        Collections.sort(anglesortedpoints, new Comparator<Point2D> ()
        {
            public int compare(Point2D q1, Point2D q2) {
                double dx1 = q1.x - firstpoint.x;
                double dy1 = q1.y - firstpoint.y;
                double dx2 = q2.x - firstpoint.x;
                double dy2 = q2.y - firstpoint.y;

                if      (dy1 >= 0 && dy2 < 0) return -1;    // q1 above; q2 below
                else if (dy2 >= 0 && dy1 < 0) return +1;    // q1 below; q2 above
                else if (dy1 == 0 && dy2 == 0) // 3-collinear and horizontal
                {            
                    if      (dx1 >= 0 && dx2 < 0) return -1;
                    else if (dx2 >= 0 && dx1 < 0) return +1;
                    else                          return  0;
                }
                else return -ccw(firstpoint, q1, q2);     // both above or below
                // Note: ccw() recomputes dx1, dy1, dx2, and dy2
            }
        });
        stack.push(anglesortedpoints.get(0));
        
	}
	
//	Three points are a counter-clockwise turn if ccw > 0, clockwise if
//	ccw < 0, and collinear if ccw = 0 because ccw is a determinant that
//	gives twice the signed  area of the triangle formed by p1, p2 and p3.
	int ccw(Point2D p1, Point2D p2, Point2D p3)
	{
		return (p2.x - p1.x)*(p3.y - p1.y) - (p2.y - p1.y)*(p3.x - p1.x);
	}
}
