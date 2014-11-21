package com.computation.algo;

import com.computation.common.*;
import com.computation.common.concurrent.AngleBetween;
import com.computation.common.concurrent.Extrema;
import com.computation.common.concurrent.Reference;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by kgowru on 11/12/14.
 */
public class GiftWrapping extends ConvexHull {

    private static final Console console = Console.getInstance(GiftWrapping.class);
    private ExecutorService executorService;
    private Extrema extrema;
    private AngleBetween angleBetween;
    private volatile int searchCount;
    private Lock debugStep;
    private Condition debugStepCondition;
    private Reference<Integer> threadCount;

    private final Object lock = new Object();

    public GiftWrapping(int points, int width, int height, int threads) {
        super(points, width, height, threads);
    }

    public GiftWrapping(int points, int width, int height, int threads, boolean debug) {
        super(points, width, height, threads, debug);
    }

    public GiftWrapping(int points, int width, int height, int threads, boolean debug, int animationDelay) {
        super(points, width, height, threads, debug, animationDelay);
    }

    @Override
    protected void findHull() {

        // Set all the global variables you need
        this.executorService = Executors.newFixedThreadPool(threads);

        // Set thread pool field in JPanel
        this.pointCloud.setField("ThreadPool", true);

        // Set search count
        this.searchCount = 0;

        if (debugStepThrough) {
            this.debugStep = new ReentrantLock();
            this.debugStepCondition = debugStep.newCondition();
        }

        // Thread count
        this.threadCount = new Reference<Integer>(threads);

        // Concurrent CCW
        angleBetween = new AngleBetween(executorService, 0, points);

        double radOffset = (Math.PI / 2) / (threads - 3);
        int index = 0;
        int rad = 0;
        List<Integer> extremas = new ArrayList<Integer>();

        // Concurrent Extrema finder
        this.extrema = new Extrema(executorService, threads, points,
                Utils.Direction.NORTH, 0);

        for (; index < threads; index++) {

            extremas.add(((Extrema.ExtremaReference) extrema.find()).getIndex());

            int dir = index % threads;

            switch (dir) {
                case 1: {
                    extrema.setDirection(Utils.Direction.SOUTH);
                    extrema.setRadians(rad);
                    break;
                }
                case 2: {
                    extrema.setDirection(Utils.Direction.EAST);
                    extrema.setRadians(rad);
                    break;
                }
                case 3: {
                    extrema.setDirection(Utils.Direction.WEST);
                    extrema.setRadians(rad);
                    break;
                }
                case 0: {
                    rad += radOffset;

                    extrema.setDirection(Utils.Direction.NORTH);
                    extrema.setRadians(rad);
                    break;
                }
            }
        }

        int paletteIndex = 0;
        int maxSubsetCount = extremas.size();
        Color[] palette = Utils.getColorPalette(maxSubsetCount);

        pointCloud.setField("Wrap Threads", maxSubsetCount);
        pointCloud.setField("Search Threads", 0);

        for (int pointIndex : extremas) {
            Point2D point = points.get(pointIndex);
            if (point.getColor() != Point2D.VISITED) {
                point.setColor(Point2D.VISITED);
                executorService.execute(new Subset(pointIndex, palette[paletteIndex++]));
            } else {

                int currThreadCount = 0;

                synchronized (threadCount) {
                    currThreadCount = threadCount.get() - 1;
                    threadCount.update(currThreadCount);
                }

                if (debug) {
                    console.log("Adding search thread");
                }

                // Increase search active
                searchCount++;

                // Update availableThreads count
                pointCloud.setField("Wrap Threads", currThreadCount);
                pointCloud.setField("Search Threads", searchCount);
            }
        }

        if (debugStepThrough) {
            pointCloud.addButton("Step", new Runnable() {
                @Override
                public void run() {
                    debugStep.lock();
                    debugStepCondition.signal();
                    debugStep.unlock();
                }
            });
        }

        pointCloud.draw();

        synchronized (threadCount) {
            if (threadCount.get() > 0) {
                try {
                    threadCount.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        executorService.shutdown();
    }

    private class Subset implements Runnable {

        private Color color;
        private int edge;
        private boolean firstSearch;

        public Subset(int edge, Color color) {
            this.edge = edge;
            this.color = color;
            this.firstSearch = true;
        }

        @Override
        public void run() {

            int pivPointIndex = edge;
            int refPointIndex;
            int lastPivPointIndex = 0;
            boolean performLinear;
            Point2D pivPoint, refPoint = null;

            do {

                if (debugStepThrough) {
                    debugStep.lock();

                    try {
                        debugStepCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                performLinear = true;
                pivPoint = points.get(pivPointIndex);

                /**
                 * Mark the point as visited; if we see this again in
                 * another thread, that thread knows they are done
                 */
                pivPoint.setColor(Point2D.VISITED);

                //search for q such that it is ccwQuant for all other i
                refPointIndex = (pivPointIndex + 1) % pointCount;

                if (!firstSearch && searchCount > 1) {

                    Lock lock = angleBetween.getLock();

                    if (lock.tryLock()) {

                        try {
                            if (debug) {
                                console.log("Performing concurrent search");
                            }

                            angleBetween.setAvailableThreads(searchCount);
                            angleBetween.setCenter(pivPointIndex);
                            angleBetween.setPrevious(lastPivPointIndex);

                            // Get next
                            refPointIndex = ((AngleBetween.CCWReference) angleBetween.find()).getIndex();
                            refPoint = points.get(refPointIndex);

                            // Skip linear
                            performLinear = false;

                        } finally {
                            lock.unlock();
                        }
                    }
                }

                if (performLinear) {
                    if (debug) {
                        console.log("Performing linear search");
                    }

                    pivPoint = points.get(pivPointIndex);
                    refPoint = points.get(refPointIndex);

                    for (int currPointIndex = 0; currPointIndex < pointCount; currPointIndex++) {
                        Point2D currPoint = points.get(currPointIndex);
                        if (Utils.ccw(pivPoint, currPoint, refPoint) == 2) {
                            refPoint = currPoint;
                            refPointIndex = currPointIndex;
                        }
                    }

                    firstSearch = false;
                }

                // Add edge
                pointCloud.addEdge(new Edge(pivPoint, refPoint, color));

                // Wait a while so you can see it
                delay();

                // Last pivot
                lastPivPointIndex = pivPointIndex;

                // Start from q next time
                pivPointIndex = refPointIndex;

                if (debugStepThrough) {
                    debugStep.unlock();
                }
            }
            while (points.get(pivPointIndex).getColor() == Point2D.UNVISITED);

            int currThreadCount = 0;

            synchronized (threadCount) {
                currThreadCount = threadCount.get() - 1;
                threadCount.update(currThreadCount);
                if (currThreadCount == 0) {
                    threadCount.notify();
                }
            }

            // Update availableThreads count
            if(debug) {
                pointCloud.setField("Wrap Threads", currThreadCount);
                pointCloud.setField("Search Threads", searchCount + 1);
            }
            
            searchCount++;
        }
    }
}
