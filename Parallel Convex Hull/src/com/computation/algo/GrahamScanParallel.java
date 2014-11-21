package com.computation.algo;

import com.computation.common.ConvexHull;
import com.computation.common.Edge;
import com.computation.common.Point2D;
import com.computation.common.Utils;
import com.computation.common.concurrent.search.ForkedMaxBottomLeft;

import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public class GrahamScanParallel extends ConvexHull {

    public GrahamScanParallel(int points, int width, int height, int threads,
                              boolean debug, int animationDelay) {
        super(points, width, height, threads, debug, animationDelay);
    }

    @Override
    protected void findHull() {
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        Stack<Point2D> stack = new Stack<Point2D>();
        pointCloud.setField("ThreadPool", true);

        ForkedMaxBottomLeft.Reference ref = ForkedMaxBottomLeft.find(executorService, threads, points);
        final Point2D firstPoint = ref.get();

        stack.push(ref.get());
        points.remove(ref.getIndex());



    }
}
