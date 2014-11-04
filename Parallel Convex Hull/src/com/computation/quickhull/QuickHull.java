package com.computation.quickhull;

import com.computation.debug.Inspector;
import com.computation.debug.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QuickHull extends Inspector {

    private List<Edge> edges;

    @Override
    public void with(Point[] points) {

        this.points = points;
        this.polygon = new ArrayList<Point>();
        this.edges = new LinkedList<Edge>();

        Point p1 = findMax(Direction.EAST);
        Point p2 = findMax(Direction.WEST);

        polygon.add(p2);
        polygon.add(p1);

        edges.add(new Edge(p1, p2));
    }

    public boolean next() {

        if (edges.size() > 0) {

            Edge edge = edges.remove(0);
            int maxArea = Integer.MIN_VALUE;
            com.computation.debug.Point maxPoint = null;

            for (com.computation.debug.Point point : points) {
                if (edge.contains(point) || polygon.contains(point) || point.skip) {
                    continue;
                }
                if (contains(point)) {
                    point.skip = true;
                    continue;
                }

                int area = getArea(edge, point);

                if (area > maxArea) {
                    maxArea = area;
                    maxPoint = point;
                }
            }

            if (maxPoint == null) {
                return false;
            }

            maxPoint.skip = true;

            polygon.add(maxPoint);
            convex();

            edges.add(new Edge(edge.p1, maxPoint));
            edges.add(new Edge(edge.p2, maxPoint));
        }

        return true;
    }

    int getArea(Edge edge, com.computation.debug.Point p) {
        return Math.abs((edge.p1.x - p.x) * (edge.p2.y - edge.p1.y) - (edge.p1.x - edge.p2.x) * (p.y - edge.p1.y));
    }

    private double angleBetween(com.computation.debug.Point center, com.computation.debug.Point current, com.computation.debug.Point previous) {
        return Math.toDegrees(Math.atan2(current.x - center.x, current.y - center.y) -
                Math.atan2(previous.x - center.x, previous.y - center.y));
    }

    public void convex() {

        for (com.computation.debug.Point p : polygon) {
            p.next = null;
        }

        ArrayList<com.computation.debug.Point> newList = new ArrayList<com.computation.debug.Point>();
        com.computation.debug.Point start = polygon.get(0);

        newList.add(start);

        while (start != null && start.next == null) {

            com.computation.debug.Point end = null;
            double dist = Double.MAX_VALUE;

            for (com.computation.debug.Point other : polygon) {

                if (other.next != null || other.equals(start)) {
                    continue;
                }

                if (newList.size() > 3) {
                    int i = newList.size() - 1;
                    com.computation.debug.Point center = newList.get(i);
                    com.computation.debug.Point previous = newList.get(i - 1);
                    double angle = angleBetween(center, previous, other);

//                    if(angle > 180){
//                        continue;
//                    }
                }

                double newDist = Math.pow(start.x - other.x, 2) + Math.pow(start.y - other.y, 2);

                if (newDist < dist) {
                    dist = newDist;
                    end = other;
                }
            }

            if (end != null) {
                System.out.printf("Point (%d, %d) found (%d, %d)\n", start.x, start.y, end.x, end.y);
                newList.add(end);
            }

            start.next = end;
            start = end;
        }

        polygon = newList;
    }

    public boolean contains(com.computation.debug.Point test) {
        com.computation.debug.Point[] points = this.polygon.toArray(new com.computation.debug.Point[0]);
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = points.length - 1; i < points.length; j = i++) {
            if ((points[i].y > test.y) != (points[j].y > test.y) &&
                    (test.x < (points[j].x - points[i].x) * (test.y - points[i].y) / (points[j].y - points[i].y) + points[i].x)) {
                result = !result;
            }
        }
        return result;
    }

    public Point findMax(Direction dir) {
        Point p = null;
        int val;

        switch (dir) {
            case NORTH:
            case SOUTH:
            case EAST:
                val = Integer.MIN_VALUE;
                for (com.computation.debug.Point point : points) {
                    if (point.x > val) {
                        p = point;
                        val = point.x;
                    }
                }
                break;
            case WEST:
                val = Integer.MAX_VALUE;
                for (com.computation.debug.Point point : points) {
                    if (point.x < val) {
                        p = point;
                        val = point.x;
                    }
                }
                break;
        }

        return p;
    }


    enum Direction {
        NORTH, SOUTH, EAST, WEST
    }
}
