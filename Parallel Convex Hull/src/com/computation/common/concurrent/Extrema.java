package com.computation.common.concurrent;

import com.computation.common.Console;
import com.computation.common.Point2D;
import com.computation.common.Utils;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by mwkurian on 11/19/2014.
 */

public class Extrema extends Search<Point2D, List<Point2D>> {

    public class ExtremaReference extends Reference<Point2D> {

        private volatile int index = -1;

        public ExtremaReference(Point2D point2D) {
            super(point2D);
        }

        private void setIndex(int index){
            this.index = index;
        }

        public int getIndex(){
            return index;
        }
    }

    private static Console console = Console.getInstance(Extrema.class);
    private Utils.Direction dir;
    private double rad;

    public Extrema(ExecutorService executorService, int availableThreads, List<Point2D> data,
                   Utils.Direction dir, double rad) {
        super(executorService, availableThreads, data);
        this.dir = dir;
        this.rad = rad;
    }

    public void setDirection(Utils.Direction dir){
        this.dir = dir;
    }

    public void setRadians(double rad){
        this.rad = rad;
    }

    @Override
    protected Reference<Point2D> getReferenceInstance() {
        return new ExtremaReference(null);
    }

    @Override
    protected void onSearch(int start, int end, Reference<Point2D> ref) {
        Point2D max = null;
        int maxIndex = -1;

        double maxY = Integer.MIN_VALUE;
        double rad = this.rad + dir.getRadianOffset();

        for (int i = start; i < end; i++) {
            Point2D point = data.get(i);
            double y = Utils.rotateY(point, rad);
            if (y > maxY) {
                max = point;
                maxY = y;
                maxIndex = i;
            }
        }

        if(max == null){
            console.err("Max is empty. List empty?");
            return;
        }

        ExtremaReference exRef = (ExtremaReference) ref;

        synchronized (exRef){
            Point2D lastPoint = exRef.get();

            if(lastPoint == null ||
                    Utils.rotateY(lastPoint, rad) < maxY){
                exRef.update(max);
                exRef.setIndex(maxIndex);
            }
        }
    }
}
