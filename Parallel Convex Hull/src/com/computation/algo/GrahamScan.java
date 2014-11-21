package com.computation.algo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import com.computation.common.ConvexHull;
import com.computation.common.Edge;
import com.computation.common.Point2D;

public class GrahamScan extends ConvexHull {
	public GrahamScan(int points, int width, int height, int threads,
			boolean debug, int animationDelay) {
		super(points, width, height, threads, debug, animationDelay);
	}

	int ccw(Point2D p1, Point2D p2, Point2D p3) {
		return (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x);
	}

	@Override
	protected void findHull() {
		// TODO Auto-generated method stub
		Stack<Point2D> stack = new Stack<Point2D>();
		pointCloud.setField("ThreadPool", true);

		// Get points
		List<Point2D> point2Ds = pointCloud.getPoints(); // defensive copy
		List<Point2D> sortedpoints = point2Ds;

		// preprocess so that points[0] has lowest y-coordinate; break ties by
		// x-coordinate
		// points[0] is an extreme point of the convex hull
		// (alternatively, could do easily in linear time)
		Collections.sort(sortedpoints, new Comparator<Point2D>() {
			public int compare(Point2D p1, Point2D p2) {
				if (p2.y == p1.y) {
					return p2.x - p1.x;
				}

				return p2.y - p1.y;
			}
		});

	
		
		
		final Point2D firstpoint = sortedpoints.get(0);
		
		Point2D lastpoint = firstpoint;
		stack.push(firstpoint);
		sortedpoints.remove(0);
		
		final List<Point2D> anglesortedpoints = sortedpoints;
		// sort by polar angle with respect to base point points[0],
		// breaking ties by distance to points[0]
		Collections.sort(anglesortedpoints, new Comparator<Point2D>() {
			public int compare(Point2D q1, Point2D q2) {
				double dx1 = q1.x - firstpoint.x;
				double dy1 = q1.y - firstpoint.y;
				double dx2 = q2.x - firstpoint.x;
				double dy2 = q2.y - firstpoint.y;
				if (dy1 >= 0 && dy2 < 0)
					return -1; // q1 above; q2 below
				else if (dy2 >= 0 && dy1 < 0)
					return +1; // q1 below; q2 above
				else if (dy1 == 0 && dy2 == 0) // 3-collinear and horizontal
				{
					if (dx1 >= 0 && dx2 < 0)
						return -1;
					else if (dx2 >= 0 && dx1 < 0)
						return +1;
					else
						return 0;
				} else
					return -ccw(firstpoint, q1, q2); // both above or below
				// Note: ccw() recomputes dx1, dy1, dx2, and dy2
			}
		});
		
		int x = 0;
		for(Point2D p2d : anglesortedpoints){
			p2d.text = Integer.toString(x++); 
		}


		pointCloud.draw();
		
		stack.push(anglesortedpoints.get(0)); // p[0] is first extreme point
		stack.push(anglesortedpoints.get(1));
//		
//		for (int i = 2; i < anglesortedpoints.size() - 2; i++) {
//			Point2D top = stack.pop();
//			
//			while (ccw(stack.peek(), top, anglesortedpoints.get(i)) <= 0) {
//				top = stack.pop();
//			}
//			
//			stack.push(top);
//			stack.push(anglesortedpoints.get(i));
//			// pointCloud.addEdge(new Edge(top,anglesortedpoints.get(i)));
//		}
//		
		int k1;
        for (k1 = 1; k1 < sortedpoints.size(); k1++)
            if (!anglesortedpoints.get(0).equals(anglesortedpoints.get(k1))) break;
        if (k1 == sortedpoints.size()) return;        // all points equal

        // find index k2 of first point not collinear with points[0] and points[k1]
        int k2;
        for (k2 = k1 + 1; k2 < sortedpoints.size(); k2++)
            if (ccw(anglesortedpoints.get(0), anglesortedpoints.get(k1),anglesortedpoints.get(k2)) != 0) break;
        stack.push(anglesortedpoints.get(k2-1));    // points[k2-1] is second extreme point

        // Graham scan; note that points[N-1] is extreme point different from points[0]
        for (int i = k2; i < anglesortedpoints.size(); i++) {
            Point2D top = stack.pop();
            while (ccw(stack.peek(), top, anglesortedpoints.get(i)) <= 0) {
                top = stack.pop();
            }
            stack.push(top);
            stack.push(anglesortedpoints.get(i));
        }

		lastpoint = stack.peek();
		Point2D a = lastpoint;
		while (stack.size() != 1) {
			a = stack.pop();
			Point2D b = stack.pop();
			pointCloud.addEdge(new Edge(a, b));
			stack.push(b);
		}
		pointCloud.addEdge(new Edge(firstpoint, sortedpoints.get(sortedpoints.size()-1)));
		System.out.println(stack);
		//pointCloud.addEdge(new Edge(a, lastpoint));
	}
}

