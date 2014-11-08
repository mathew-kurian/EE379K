package com.computation.algo;

import com.computation.common.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class QuickHull extends ConvexHull {

    private ExecutorService executorService;
    private AtomicInteger subsetStartCount;
    private AtomicInteger subsetFinishCount;

    public QuickHull(int points, int width, int height, int threads) {
        super(points, width, height, threads);
    }

    @Override
    protected void findHull(int threads) {

        // Start
        this.executorService = Executors.newFixedThreadPool(threads);
        this.subsetStartCount = new AtomicInteger(0);
        this.subsetFinishCount = new AtomicInteger(0);

        // Set some fields
        pointCloud.setField("ThreadPool", true);

        // Get points
        List<Point2D> point2Ds = pointCloud.getPoints();

        Point2D p1 = Utils.findMax(point2Ds, Utils.Direction.NORTH);
        Point2D p2 = Utils.findMax(point2Ds, Utils.Direction.SOUTH);

        p1.setColor(Point2D.VISITED);
        p2.setColor(Point2D.VISITED);

        // Handle left side
        HashSet<Point2D> left = new HashSet<Point2D>();
        HashSet<Point2D> right = new HashSet<Point2D>();

        point2Ds.remove(p1);
        point2Ds.remove(p2);

        pointCloud.addEdge(new Edge(p1, p2));

        for (Point2D point2D : point2Ds) {
            if (Utils.isPointLeftOf(p1, p2, point2D)) {
                left.add(point2D);
            } else {
                right.add(point2D);
            }
        }

        try {
            executorService.execute(new Subset(left, p1, p2));
            executorService.execute(new Subset(right, p2, p1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Subset implements Runnable {

        private final Point2D a;
        private final Point2D b;
        private final Set<Point2D> point2Ds;

        public Subset(Set<Point2D> point2Ds, Point2D a, Point2D b) {
            this.a = a;
            this.b = b;
            this.point2Ds = point2Ds;

            int subsets = subsetStartCount.incrementAndGet();
            pointCloud.setField("Subsets", subsets);
        }

        @Override
        public void run() {
            Point2D max = null;
            int dist = Integer.MIN_VALUE;

            // Thread this later
            for (Point2D p : point2Ds) {
                int d = Utils.distance(a, b, p);
                if (d > dist) {
                    dist = d;
                    max = p;
                }
            }

            if (max == null) {
                if (subsetStartCount.get() == subsetFinishCount.incrementAndGet()) {
                    finish();
                }
                return;
            }

            point2Ds.remove(max);

            max.setColor(Point2D.VISITED);

            if (debug) {
                pointCloud.draw();
                try {
                    Thread.sleep(debugFrameDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            pointCloud.removeEdge(new Edge(a, b));
            pointCloud.addEdge(new Edge(max, a));
            pointCloud.addEdge(new Edge(max, b));

            HashSet<Point2D> left = new HashSet<Point2D>();
            HashSet<Point2D> right = new HashSet<Point2D>();

            for (Point2D point2D : point2Ds) {
                if (Utils.isPointLeftOf(a, max, point2D)) {
                    left.add(point2D);
                } else if (!Utils.isPointLeftOf(b, max, point2D)) {
                    right.add(point2D);
                }
            }

            try {
                executorService.execute(new Subset(left, a, max));
                executorService.execute(new Subset(right, max, b));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                subsetFinishCount.incrementAndGet();
            }
        }
    }
}
