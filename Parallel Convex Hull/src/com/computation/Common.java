package com.computation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Common {

    public static List<Point> generateRandomPoints(int length, int width, int height, int inset) {
        Random rand = new Random();
        List<Point> points = new ArrayList<Point>();

        for (int i = 0; i < length; i++) {
            points.add(new Point(rand.nextInt(width - inset * 2) + inset, rand.nextInt(height - inset * 2) + inset));
        }

        return points;
    }

    public static Point findMax(List<Point> points, Direction dir) {
        Point p = null;
        int val;

        switch (dir) {
            case NORTH:
                val = Integer.MAX_VALUE;
                for (Point point : points) {
                    if (point.y < val) {
                        p = point;
                        val = point.y;
                    }
                }
                break;
            case SOUTH:
                val = Integer.MIN_VALUE;
                for (Point point : points) {
                    if (point.y > val) {
                        p = point;
                        val = point.y;
                    }
                }
                break;
            case EAST:
                val = Integer.MIN_VALUE;
                for (Point point : points) {
                    if (point.x > val) {
                        p = point;
                        val = point.x;
                    }
                }
                break;
            case WEST:
                val = Integer.MAX_VALUE;
                for (Point point : points) {
                    if (point.x < val) {
                        p = point;
                        val = point.x;
                    }
                }
                break;
        }

        return p;
    }

    public static boolean isPointLeftOf(Point A, Point B, Point P) {
        return (((B.x - A.x) * (P.y - A.y) - (B.y - A.y) * (P.x - A.x)) > 0) ? true : false;
    }

    public static double angleBetween(Point center, Point current, Point previous) {
        return Math.toDegrees(Math.atan2(current.x - center.x, current.y - center.y) -
                Math.atan2(previous.x - center.x, previous.y - center.y));
    }

    public static int distance(Point line1, Point line2, Point p) {
        int ABx = line2.x - line1.x;
        int ABy = line2.y - line1.y;
        int num = ABx * (line1.y - p.y) - ABy * (line1.x - p.x);
        if (num < 0) num = -num;
        return num;
    }

    public static enum Direction {
        NORTH, SOUTH, EAST, WEST
    }
}
