package com.computation.common.concurrent;

import com.computation.common.Point2D;
import com.computation.common.Utils;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * Created by mwkurian on 11/19/2014.
 */
public class PointLeftOf extends Search<Point2D, List<Point2D>> {

    protected List<Point2D> left;
    protected List<Point2D> right;

    protected Point2D a;
    protected Point2D b;

    public PointLeftOf(ExecutorService executorService, int availableThreads, List<Point2D> data,
                       List<Point2D> left, List<Point2D> right, Point2D a, Point2D b) {
        super(executorService, availableThreads, data);
        this.left = left;
        this.right = right;
        this.a = a;
        this.b = b;
    }

    @Override
    protected Reference<Point2D> getReferenceInstance() {
        return new Reference<Point2D>(null);
    }

    @Override
    protected void onSearch(int start, int end, Reference<Point2D> ref) {

        List<Point2D> localLeft = new ArrayList<Point2D>();
        List<Point2D> localRight = new ArrayList<Point2D>();

        for (int i = start; i < end; i++) {
            Point2D point = data.get(i);
            if (Utils.isPointLeftOf(a, b, point)) {
                localLeft.add(point);
            } else {
                localRight.add(point);
            }
        }

        synchronized (left) {
            left.addAll(localLeft);
        }

        synchronized (right) {
            right.addAll(localRight);
        }
    }
}
