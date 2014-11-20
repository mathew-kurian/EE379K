package com.computation.algo;

import com.computation.common.*;
import com.computation.common.concurrent.CCW;
import com.computation.common.concurrent.Extrema;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kgowru on 11/12/14.
 */
public class GiftWrapping extends ConvexHull {

    private static final Console console = Console.getInstance(GiftWrapping.class);
    private ExecutorService executorService;
    private Extrema extrema;
    private CCW ccw;
    private volatile int searchCount;

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

        // Concurrent CCW
        ccw = new CCW(executorService, 0, points);

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

        int priority = 0;
        int paletteIndex = 0;
        int maxSubsetCount = extremas.size();
        Color[] palette = Utils.getColorPalette(maxSubsetCount);
        AtomicInteger threadCount = new AtomicInteger(maxSubsetCount);

        pointCloud.setField("Wrap Threads", maxSubsetCount);
        pointCloud.setField("Search Threads", 0);

        for (int pointIndex : extremas) {
            Point2D point = points.get(pointIndex);
            if (point.getColor() != Point2D.VISITED) {
                point.setColor(Point2D.VISITED);
                executorService.execute(new Subset(pointIndex, palette[paletteIndex++], threadCount, priority++));
            } else {
                /**
                 * @Kapil. We reduce the extra availableThreads here. Use the extra
                 * availableThreads to optimize the searching
                 */
                int currThreadCount = threadCount.decrementAndGet();

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

        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
    }

    private class Subset implements Runnable {

        private Color color;
        private AtomicInteger active;
        private int id = 0;
        private int edge;

        public Subset(int edge, Color color, AtomicInteger active,
                      int id) {
            this.edge = edge;
            this.color = color;
            this.active = active;
            this.id = id;
        }

        @Override
        public void run() {

            int p = edge;
            int q;

            do {
                /**
                 * Mark the point as visited; if we see this again in
                 * another thread, that thread knows they are done
                 */
                points.get(p).setColor(Point2D.VISITED);

                //search for q such that it is ccw for all other i
                q = (p + 1) % pointCount;

                if (ccw.getThreadTurn() == id
                        && searchCount > 1 && false) {

                    if (debug) {
                        console.log("Thread " + id + " performing concurrent search");
                    }

                    ccw.setAvailableThreads(searchCount);
                    ccw.setPivot(p);
                    ccw.setNext(q);

                    // Get next
                    q = ((CCW.CCWReference) ccw.find()).getIndex();

                    // Give the next thread the priority
                    ccw.nextThread();

                } else {

                    if (debug) {
                        console.log("Thread " + id + " performing linear search");
                    }

                    for (int i = 0; i < pointCount; i++) {
                        if (Utils.ccw(points.get(p), points.get(i), points.get(q)) == 2) {
                            q = i;
                        }
                    }
                }

                // Add edge
                pointCloud.addEdge(new Edge(points.get(p), points.get(q), color));

                // Wait a while so you can see it
                delay();

                // Start from q next time
                p = q;

            } while (points.get(p).getColor() == Point2D.UNVISITED);

            if (active.decrementAndGet() == 0) {
                synchronized (GiftWrapping.this){
                    GiftWrapping.this.notify();
                }
            } else if (ccw.getThreadTurn() == id) {
                ccw.nextThread();
            }
        }
    }
}
