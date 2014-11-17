package com.computation.algo;

import com.computation.common.ConvexHull;
import com.computation.common.Edge;
import com.computation.common.Point2D;
import com.computation.common.Utils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created by kgowru on 11/12/14.
 */
public class GiftWrapping extends ConvexHull {

    private ExecutorService executorService;
    private AtomicIntegerArray hullArray;
    private volatile int leftMostPoint = -1;
    private volatile int rightMostPoint = -1;
    private volatile int topMostPoint = -1;
    private volatile int bottomMostPoint = -1;

    public GiftWrapping(int points, int width, int height, int threads) {
        super(points, width, height, threads);
    }

    @Override
    protected void findHull() {
        this.executorService = Executors.newFixedThreadPool(threads);

        //initalize array
        int[] next = new int[points];
        Arrays.fill(next, -1);
        this.hullArray = new AtomicIntegerArray(next);

        // Set thread pool field in JPanel
        pointCloud.setField("ThreadPool", true);

        // Get points
        List<Point2D> point2Ds = pointCloud.getPoints();

        //Split threads depending on what edge to start on
        Point2D edgePoint;
        for (int i = 0; i < threads && i < 4; i++) {
            if (i == 0) {
                edgePoint = Utils.findMax(point2Ds, Utils.Direction.WEST);
                leftMostPoint = point2Ds.indexOf(edgePoint);
                executorService.execute(new DirectionSet(point2Ds, leftMostPoint, Color.RED));
            } else if (i == 1) {
                edgePoint = Utils.findMax(point2Ds, Utils.Direction.EAST);
                rightMostPoint = point2Ds.indexOf(edgePoint);
                executorService.execute(new DirectionSet(point2Ds, rightMostPoint, Color.CYAN));
            } else if (i == 2) {
                edgePoint = Utils.findMax(point2Ds, Utils.Direction.NORTH);
                topMostPoint = point2Ds.indexOf(edgePoint);
                executorService.execute(new DirectionSet(point2Ds, topMostPoint, Color.GREEN));
            } else {
                edgePoint = Utils.findMax(point2Ds, Utils.Direction.SOUTH);
                bottomMostPoint = point2Ds.indexOf(edgePoint);
                executorService.execute(new DirectionSet(point2Ds, bottomMostPoint, Color.MAGENTA));
            }
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

        public DirectionSet(List<Point2D> point2Ds, int edge, Color color) {
            this.edge = edge;
            this.point2Ds = point2Ds;
            this.color = color;
        }

        @Override
        public void run() {
            int p, q;
            p = edge;

            do {
                q = (p + 1) % points; //search for q such that it is CCW for all other i
                for (int i = 0; i < points; i++) {
                    if (Utils.CCW(point2Ds.get(p), point2Ds.get(i), point2Ds.get(q)) == 2)
                        q = i;
                }
                hullArray.getAndSet(p, q); //add q as the next point from p
                pointCloud.addEdge(new Edge(point2Ds.get(p), point2Ds.get(q), color));
                
                // @Kapil: Just call delay()
                //wait a while so you can see it
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                p = q; //start from q next time

            }
            while (p != leftMostPoint || p != rightMostPoint || p != topMostPoint || p != bottomMostPoint); //keep going till you make full circle
            finish();
        }


    }


}
