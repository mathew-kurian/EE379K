package com.computation.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    public static List<Point2D> generateRandomPoints(int length, int width, int height, int inset) {
        Random rand = new Random();
        List<Point2D> point2Ds = new ArrayList<Point2D>();

        for (int i = 0; i < length; i++) {
            point2Ds.add(new Point2D(rand.nextInt(width - inset * 2) + inset, rand.nextInt(height - inset * 2) + inset));
        }

        return point2Ds;
    }

    public static Point2D findMax(List<Point2D> point2Ds, Direction dir) {
        Point2D p = null;
        int val;

        switch (dir) {
            case NORTH:
                val = Integer.MAX_VALUE;
                for (Point2D point2D : point2Ds) {
                    if (point2D.y < val) {
                        p = point2D;
                        val = point2D.y;
                    }
                }
                break;
            case SOUTH:
                val = Integer.MIN_VALUE;
                for (Point2D point2D : point2Ds) {
                    if (point2D.y > val) {
                        p = point2D;
                        val = point2D.y;
                    }
                }
                break;
            case EAST:
                val = Integer.MIN_VALUE;
                for (Point2D point2D : point2Ds) {
                    if (point2D.x > val) {
                        p = point2D;
                        val = point2D.x;
                    }
                }
                break;
            case WEST:
                val = Integer.MAX_VALUE;
                for (Point2D point2D : point2Ds) {
                    if (point2D.x < val) {
                        p = point2D;
                        val = point2D.x;
                    }
                }
                break;
        }

        return p;
    }

    public static boolean isPointLeftOf(Point2D line1, Point2D line2, Point2D point2D) {
        return (((line2.x - line1.x) * (point2D.y - line1.y) - (line2.y - line1.y) * (point2D.x - line1.x)) > 0);
    }

    public static double angleBetween(Point2D center, Point2D current, Point2D previous) {
        return Math.toDegrees(Math.atan2(current.x - center.x, current.y - center.y) -
                Math.atan2(previous.x - center.x, previous.y - center.y));
    }

    public static int distance(Point2D line1, Point2D line2, Point2D p) {
        int ABx = line2.x - line1.x;
        int ABy = line2.y - line1.y;
        int num = ABx * (line1.y - p.y) - ABy * (line1.x - p.x);
        if (num < 0) num = -num;
        return num;
    }

    //finds orientation of triplet (p,q,r)
    //true --> p-r is counterclockwise from p-q
    //false --> if not
    public static boolean CCW(Point2D p, Point2D q, Point2D r){
        int val = (q.y-p.y) * (r.x-q.x) - (q.x*p.x) * (r.y-q.y);

        if(val >= 0){
            return false;
        }
        return true;
    }

    public static enum Direction {
        NORTH, SOUTH, EAST, WEST
    }
}
