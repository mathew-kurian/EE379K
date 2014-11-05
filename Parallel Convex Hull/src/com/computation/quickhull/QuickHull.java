package com.computation.quickhull;

import com.computation.*;
import com.computation.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuickHull implements ConvexHull {

    private PointCloud pointCloud;
    private List<Point> points;
    private List<Point> hull;

    public void findConvexHull(PointCloud pointCloud) {

        // Start
        this.hull = new ArrayList<Point>();
        this.pointCloud = pointCloud;
        this.points = pointCloud.getPoints();

        Point p1 = Common.findMax(points, Common.Direction.NORTH);
        Point p2 = Common.findMax(points, Common.Direction.SOUTH);

        p1.setColor(Point.VISITED);
        p2.setColor(Point.VISITED);

        hull.add(p1);
        hull.add(p2);

        // No hull calculated
        pointCloud.updateHullAndDraw(null);

        // Handle left side
        HashSet<Point> left = new HashSet<Point>();
        HashSet<Point> right = new HashSet<Point>();

        for(Point point : points){
            if(Common.isPointLeftOf(p1, p2, point) && !point.getColor().equals(Point.VISITED)){
                left.add(point);
            } else {
                right.add(point);
            }
        }

        doLeft(left, p1, p2);
        doLeft(right, p2, p1);
    }

    public void doLeft(Set<Point> points, Point a, Point b){

        Point max = null;

        int dist = Integer.MIN_VALUE;
        for(Point p : points){
            int d = Common.distance(a, b, p);
            if(d > dist){
                dist = d;
                max = p;
            }
        }

        if(max == null){
            return;
        } else {
            max.setColor(Point.VISITED);
            pointCloud.draw();
        }

        HashSet<Point> leftL1 = new HashSet<Point>();

        for(Point point : points){
            if(Common.isPointLeftOf(a, max, point) && !point.getColor().equals(Point.VISITED)){
                leftL1.add(point);
            }
        }

        doLeft(leftL1, a, max);
    }



}
