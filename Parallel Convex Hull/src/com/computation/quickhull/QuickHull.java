package com.computation.quickhull;

import com.computation.*;
import com.computation.Point;

import java.awt.*;
import java.util.List;

public class QuickHull implements ConvexHull {

    private PointCloud pointCloud;
    private List<Point> points;

    public void findConvexHull(PointCloud pointCloud) {

        // Start
        this.pointCloud = pointCloud;
        this.points = pointCloud.getPoints();

        Point p1 = Common.findMax(points, Common.Direction.NORTH);
        Point p2 = Common.findMax(points, Common.Direction.SOUTH);

        p1.setColor(Color.GREEN);
        p2.setColor(Color.GREEN);

        // No hull calculated
        pointCloud.updateHullAndDraw(null);


    }

}
