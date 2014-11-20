package com.computation.common.concurrent;

import com.computation.common.Point2D;
import com.computation.common.Utils;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by mwkurian on 11/19/2014.
 */
public class AngleBetween extends Search<Point2D, List<Point2D>> {

    private volatile int pivot;
    private volatile int next;

    public AngleBetween(ExecutorService executorService, int availableThreads, List<Point2D> data) {
        super(executorService, availableThreads, data);
    }

    @Override
    protected Reference<Point2D> getReferenceInstance() {
        CCWReference cf = new CCWReference(null);
        cf.setIndex(Integer.MIN_VALUE);
        return cf;
    }

    public void setCenter(int pivot) {
        this.pivot = pivot;
    }

    public void setPrevious(int next) {
        this.next = next;
    }

    @Override
    protected void onSearch(int start, int end, Reference ref) {
        CCWReference exRef = (CCWReference) ref;
        int q = Integer.MIN_VALUE;
        double maxAngle = Double.MIN_VALUE;

        Point2D pivPoint = data.get(pivot);
        Point2D nexPoint = data.get(next);

        for (int i = start;i != pivot && i != next && i < end; i++) {
            Point2D currPoint = data.get(i);
            double currAngle = Utils.angleBetween(pivPoint, nexPoint, currPoint);
            //double currAngle = pivPoint.
            if (currAngle > maxAngle) {
                maxAngle = currAngle;
                q = i;
            }
        }

        synchronized (exRef){
            if(maxAngle > exRef.getAngle()){
                exRef.setAngle(maxAngle);
                exRef.setIndex(q);
            }
        }
    }

    public class CCWReference extends Reference<Point2D> {

        private int index = -1;

        public double getAngle() {
            return angle;
        }

        public void setAngle(double angle) {
            this.angle = angle;
        }

        private double angle = Double.MIN_VALUE;

        public CCWReference(Point2D point2D) {
            super(point2D);
        }

        public int getIndex() {
            return index;
        }

        private void setIndex(int index) {
            this.index = index;
        }
    }
}
