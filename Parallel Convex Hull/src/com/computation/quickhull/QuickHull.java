package com.computation.quickhull;

import com.computation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuickHull implements ConvexHull {

    private PointCloud pointCloud;
    private ExecutorService executorService;

    public void findConvexHull(PointCloud pointCloud, int threads) {

        // Start
        this.executorService = Executors.newFixedThreadPool(threads);
        this.pointCloud = pointCloud;

        // Set basic information
        pointCloud.setThreadCount(threads);

        // Pause and ask to continue
        pointCloud.toast("Press OK to start");

        // Get points
        List<Point> points = pointCloud.getPoints();

        Point p1 = Common.findMax(points, Common.Direction.NORTH);
        Point p2 = Common.findMax(points, Common.Direction.SOUTH);

        p1.setColor(Point.VISITED);
        p2.setColor(Point.VISITED);

        // Handle left side
        HashSet<Point> left = new HashSet<Point>();
        HashSet<Point> right = new HashSet<Point>();

        points.remove(p1);
        points.remove(p2);

        pointCloud.addEdge(new Edge(p1, p2));

        for (Point point : points) {
            if (Common.isPointLeftOf(p1, p2, point)) {
                left.add(point);
            } else {
                right.add(point);
            }
        }

        try {
            executorService.execute(new Subset(left, p1, p2));
            executorService.execute(new Subset(right, p2, p1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Subset implements Runnable {

        private final Point a;
        private final Point b;
        private final Set<Point> points;

        public Subset(Set<Point> points, Point a, Point b) {
            this.a = a;
            this.b = b;
            this.points = points;
        }

        @Override
        public void run() {
            Point max = null;
            int dist = Integer.MIN_VALUE;

            // Thread this later
            for (Point p : points) {
                int d = Common.distance(a, b, p);
                if (d > dist) {
                    dist = d;
                    max = p;
                }
            }

            if (max == null) {
                return;
            }

            points.remove(max);

            max.setColor(Point.VISITED);

            if (pointCloud.DEBUG) {
                pointCloud.draw();
                try {
                    Thread.sleep(pointCloud.DEBUG_ANIMATION_TIME_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            pointCloud.removeEdge(new Edge(a, b));
            pointCloud.addEdge(new Edge(max, a));
            pointCloud.addEdge(new Edge(max, b));

            HashSet<Point> left = new HashSet<Point>();
            HashSet<Point> right = new HashSet<Point>();

            for (Point point : points) {
                if (Common.isPointLeftOf(a, max, point)) {
                    left.add(point);
                } else if (!Common.isPointLeftOf(b, max, point)) {
                    right.add(point);
                }
            }

            try {
                executorService.execute(new Subset(left, a, max));
                executorService.execute(new Subset(right, max, b));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }
    }
}
