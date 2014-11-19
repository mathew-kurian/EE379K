package com.computation.algo;

import com.computation.common.ConvexHull;
import com.computation.common.Edge;
import com.computation.common.Point2D;
import com.computation.common.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created by kgowru on 11/12/14.
 */
public class GiftWrapping extends ConvexHull {

    private ExecutorService executorService;
    private AtomicIntegerArray hullArray;

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
    protected void findHull(int threads) {
        this.executorService = Executors.newFixedThreadPool(threads);

        // Initialize array
        int[] next = new int[pointCount];
        Arrays.fill(next, -1);
        this.hullArray = new AtomicIntegerArray(next);

        //List of threads
        ArrayList<DirectionSet> allDirectionSets = new ArrayList<DirectionSet>();

        // Searching threads
        int searchThreads = 0;

        // Set thread pool field in JPanel
        pointCloud.setField("ThreadPool", true);

        // Get pointCount
        List<Point2D> point2Ds = pointCloud.getPoints();

        //Split threads depending on what edge to start on
        Point2D edgePoint;
        double degree = 0;

        /** Get this offset by doing it for 5 threads.
         * Suppose we have 5 threads
         *  1. We do the normal 4 threads which is NORTH, SOUTH, EAST, WEST
         *      which results in 4 start points.
         *  2. We do 90.0 / (threads - 4) = 90 / ( 5 - 4 ) = 90
         *  3. But degreeOffset needs to 45 not 90, so we add 1
         *  4. degreeOffset = 90.0 / (threads - 4 + 1) =
         *                    90.0 / (threads - 4 + 1) =
         *                    90.0 / (threads - 3) =
         *                    45.0
         */
        double degreeOffset = 90.0 / (threads - 3);

        /**
         * Reduce this value each time a thread finishes. Until it
         * reaches zero
         */
        AtomicInteger threadCount = new AtomicInteger(threads);

        /**
         * Get a good looking set of colors
         */
        Color[] palette = Utils.getColorPalette(threads);
        int paletteIndex = 0;

        for (int i = 0; i < threads; i++) {
            int dir = i % 4;
            int index;
            Color color = palette[paletteIndex++];

            switch (dir) {
                case 0:
                    edgePoint = Utils.findMax(point2Ds, Utils.Direction.WEST, degree);
                    /** not sure why we are looking for it again here. maybe some other way? */
                    index = point2Ds.indexOf(edgePoint);
                    break;
                case 1:
                    edgePoint = Utils.findMax(point2Ds, Utils.Direction.EAST, degree);
                    index = point2Ds.indexOf(edgePoint);
                    break;
                case 2:
                    edgePoint = Utils.findMax(point2Ds, Utils.Direction.NORTH, degree);
                    index = point2Ds.indexOf(edgePoint);
                    break;
                default:
                case 3:
                    edgePoint = Utils.findMax(point2Ds, Utils.Direction.SOUTH, degree);
                    index = point2Ds.indexOf(edgePoint);
                    break;
            }

            if (edgePoint.getColor() != Point2D.VISITED) {
                edgePoint.setColor(Point2D.VISITED);

                //executorService.execute(new DirectionSet(point2Ds, index, color, threadCount));

                //String threadName = "Thread " + i;
                //allThreads.add(new Thread(new DirectionSet(point2Ds, index, color, threadCount),threadName));

                allDirectionSets.add(new DirectionSet(point2Ds,index,color,threadCount));

            } else {
                /**
                 * @Kapil. We reduce the extra threads here. Use the extra
                 * threads to optimize the searching
                 */
                int currThreadCount = threadCount.decrementAndGet();

                // Increment search threads; use this later
                searchThreads++;

                // Update threads count
                pointCloud.setField("Wrap threads", currThreadCount);

                /*if(currThreadCount == 0){
                    pointCloud.toast("@Kapil, the threads that started already finished doing the whole thing!");
                    finish();
                }*/
            }

            if (dir == 3) {
                // increment degree offset
                degree += degreeOffset;
            }
        }

        //initialize cyclic barrier, used so all threads can start once initialized
        CyclicBarrier cyclicBarrier = new CyclicBarrier(allDirectionSets.size(), new Runnable(){
            @Override
            public void run(){
                System.out.println("all threads have reached the barrier, start the algorithm");
            }
        });


        //System.out.println(allThreads.size() + " threads are about to start");
        for(DirectionSet ds: allDirectionSets){
            //System.out.println("Starting " + t.getName());
            ds.setCyclicBarrier(cyclicBarrier);
            Thread t = new Thread(ds);
            t.start();
        }


        //displaying results
//        int a = leftMost;
//        for (int i = 0 ; i < count; i ++){
//            pointCloud.addEdge(new Edge(point2Ds.get(a),point2Ds.get(next[a])));
//            a = next[a];
//        }

    }

    private class DirectionSet implements Runnable {

        private int edge;
        private List<Point2D> point2Ds;
        private Color color;
        private AtomicInteger threads;
        private CyclicBarrier cyclicBarrier;

        public DirectionSet(List<Point2D> point2Ds, int edge, Color color, AtomicInteger threads) {
            this.edge = edge;
            this.point2Ds = point2Ds;
            this.color = color;
            this.threads = threads;
            this.cyclicBarrier = new CyclicBarrier(1);
        }

        public void setCyclicBarrier(CyclicBarrier cyclicBarrier){
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName()+"waiting on other threads");
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName()+"has crossed the barrier");

                int p, q;
                p = edge;

                do {

                    /**
                     * Mark the point as visited; if we see this again in
                     * another thread, that thread knows they are done
                     */
                    point2Ds.get(p).setColor(Point2D.VISITED);

                    //search for q such that it is CCW for all other i
                    q = (p + 1) % pointCount;
                    for (int i = 0; i < pointCount; i++) {
                        if (Utils.CCW(point2Ds.get(p), point2Ds.get(i), point2Ds.get(q)) == 2) {
                            q = i;
                        }
                    }

                    // Add q as the next point from p
                    hullArray.getAndSet(p, q);

                    // Add edge
                    pointCloud.addEdge(new Edge(point2Ds.get(p), point2Ds.get(q), color));

                    // Wait a while so you can see it
                    delay();

                    // Start from q next time
                    p = q;

                }
                /**
                 * Keep going until you hit a point which has been visited
                 */
                while (point2Ds.get(p).getColor() == Point2D.UNVISITED);

                if (threads.decrementAndGet() == 0) {
                    finish();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }


    }


}