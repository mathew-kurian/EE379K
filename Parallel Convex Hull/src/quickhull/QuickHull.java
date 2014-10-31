package quickhull;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QuickHull {

	Point[] points;
	List<Point> polygon;

	class Edge {
		Point p1;
		Point p2;

		public Edge(Point p1, Point p2) {
			this.p1 = p1;
			this.p2 = p2;
		}
	}

	public QuickHull(Point[] points) {
		this.points = points;
		this.polygon = new ArrayList<Point>();
	}

	public List<Point> run() {
		List<Edge> edges = new LinkedList<Edge>();
		Point p1 = findMax(Direction.EAST);
		Point p2 = findMax(Direction.WEST);

		polygon.add(p1);
		polygon.add(p2);

		edges.add(new Edge(p1, p2));
		
		while (edges.size() > 0) {

			Edge edge = edges.remove(0);
			int minArea = Integer.MAX_VALUE;
			Point minPoint = null;

			for (Point point : points) {
				if (polygon.size() > 2) {
					if(pointInPolygon(point)){
						continue;
					}
				}

				int area = getArea(edge, point);

				if (area < minArea) {
					minArea = area;
					minPoint = point;
				}
			}

			if (minPoint == null) {
				return polygon;
			}

			polygon.add(minPoint);

			edges.add(new Edge(edge.p1, minPoint));
			edges.add(new Edge(edge.p2, minPoint));
		}

		return new LinkedList<Point>();
	}

	int getArea(Edge edge, Point p) {
		return Math.abs((edge.p1.x - p.x) * (edge.p2.y - edge.p1.y) - (edge.p1.x - edge.p2.x) * (p.y - edge.p1.y));
	}

	boolean pointInPolygon(Point point) {

		int polySides = this.polygon.size();

		int i, j = polySides - 1;
		boolean oddNodes = false;

		for (i = 0; i < polySides; i++) {
			if ((polygon.get(i).y < point.y && points[j].y >= point.y 
					|| polygon.get(j).y < point.y && polygon.get(i).y >= point.y)
					&& (polygon.get(i).x <= point.x || polygon.get(j).x <= point.x)) {
				if (polygon.get(i).x + (point.y - polygon.get(i).y) / (polygon.get(j).y - polygon.get(i).y)
						* (polygon.get(j).x - polygon.get(i).x) < point.x) {
					oddNodes = !oddNodes;
				}
			}
			j = i;
		}
		
		return oddNodes;
	}

	enum Direction {
		NORTH, SOUTH, EAST, WEST
	}

	public Point findMax(Direction dir) {
		Point p = null;
		int val;

		switch (dir) {
		case NORTH:
		case SOUTH:
		case EAST:
			val = Integer.MIN_VALUE;
			for (Point point : points) {
				if (point.x > val) {
					p = point;
				}
			}
			break;
		case WEST:
			val = Integer.MAX_VALUE;
			for (Point point : points) {
				if (point.x < val) {
					p = point;
				}
			}
			break;
		}

		return p;
	}
}
