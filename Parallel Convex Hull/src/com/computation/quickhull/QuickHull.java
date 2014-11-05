package com.computation.quickhull;

import com.computation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuickHull implements ConvexHull {

    private static final boolean DEBUG = true;
    private static final long DEBUG_ANIMATION_TIME_MS = 1000;

    private PointCloud pointCloud;
    private List<Point> points;
    private List<Point> hull;

    private ExecutorService executorService;

    public void findConvexHull(PointCloud pointCloud, int threads) {

        // Start
        this.executorService = Executors.newFixedThreadPool(threads);
        this.hull = new ArrayList<Point>();
        this.pointCloud = pointCloud;
        this.points = pointCloud.getPoints();

        Point p1 = Common.findMax(points, Common.Direction.NORTH);
        Point p2 = Common.findMax(points, Common.Direction.SOUTH);

        p1.setColor(Point.VISITED);
        p2.setColor(Point.VISITED);

        hull.add(p1);
        hull.add(p2);

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

            if (DEBUG) {
                pointCloud.draw();
                try {
                    Thread.sleep(DEBUG_ANIMATION_TIME_MS);
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
