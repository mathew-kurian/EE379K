package com.computation.algo;

import com.computation.common.ConvexHull;
import com.computation.common.Edge;
import com.computation.common.Point2D;
import com.computation.common.Utils;
import com.computation.common.concurrent.Extrema;
import com.computation.common.concurrent.PointLeftOf;
import com.computation.common.concurrent.Reference;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public class QuickHull extends ConvexHull {

    private ExecutorService executorService;
    private Reference<Integer> subsetStartCount;
    private Reference<Integer> subsetFinishCount;

    private final Object lock = new Object();

    public QuickHull(int points, int width, int height, int threads) {
        super(points, width, height, threads);
    }
    public QuickHull(int points, int width, int height, int threads, boolean debug) {
        super(points, width, height, threads, debug);
    }
    public QuickHull(int points, int width, int height, int threads, boolean debug, int animationDelay) {
        super(points, width, height, threads, debug, animationDelay);
    }

    @Override
    protected void findHull() {

        // Start
        this.executorService = Executors.newFixedThreadPool(threads);
        this.subsetStartCount = new Reference<Integer>(0);
        this.subsetFinishCount = new Reference<Integer>(0);

        // Find start points
        Extrema extrema = new Extrema(executorService, threads, points, null, 0);

        // Set some fields
        pointCloud.setField("ThreadPool", true);

        // Find the endpoints
        extrema.setDirection(Utils.Direction.NORTH);
        Point2D p1 = extrema.find().get();
        extrema.setDirection(Utils.Direction.SOUTH);
        Point2D p2 = extrema.find().get();

        p1.setColor(Point2D.VISITED);
        p2.setColor(Point2D.VISITED);

        points.remove(p1);
        points.remove(p2);

        pointCloud.addEdge(new Edge(p1, p2));

        // Handle left side
        List<Point2D> left = new ArrayList<Point2D>();
        List<Point2D> right = new ArrayList<Point2D>();

        // Create multithreaded searcher
        PointLeftOf pointLeftOf = new PointLeftOf(executorService, threads, points, left, right, p1, p2);

        // Search
        pointLeftOf.find();

        try {
            executorService.execute(new Subset(left, p1, p2));
            executorService.execute(new Subset(right, p2, p1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        synchronized (lock){
            if(!isFinished()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        executorService.shutdown();
    }

    private boolean isFinished(){
        return subsetStartCount.get().equals(subsetFinishCount.get());
    }

    private class Subset implements Runnable {

        private final Point2D a;
        private final Point2D b;
        private final List<Point2D> points;

        public Subset(List<Point2D> points, Point2D a, Point2D b) {
            this.a = a;
            this.b = b;
            this.points = points;

            int subsets;

            synchronized (lock){
                subsets = subsetStartCount.get() + 1;
                subsetStartCount.update(subsets);
            }

            pointCloud.setField("Subsets", subsets);
        }

        @Override
        public void run() {
            Point2D max = null;
            int dist = Integer.MIN_VALUE;

            // Thread this later
            for (Point2D p : points) {
                int d = Utils.distance(a, b, p);
                if (d > dist) {
                    dist = d;
                    max = p;
                }
            }

            if (max == null) {
                synchronized (lock){
                    if(isFinished()) {
                        lock.notify();
                    }
                }
                return;
            }

            points.remove(max);

            max.setColor(Point2D.VISITED);

            // animation delay
            delay();

            pointCloud.removeEdge(new Edge(a, b));
            pointCloud.addEdge(new Edge(max, a));
            pointCloud.addEdge(new Edge(max, b));

            List<Point2D> left = new ArrayList<Point2D>();
            List<Point2D> right = new ArrayList<Point2D>();

            for (Point2D point2D : points) {
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
                synchronized (lock) {
                    subsetFinishCount.update(subsetFinishCount.get() + 1);
                }
            }
        }
    }
}
