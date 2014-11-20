package com.computation.common.concurrent;

import com.computation.common.Point2D;
import com.computation.common.Utils;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by mwkurian on 11/19/2014.
 */
public class CCW extends Search<Point2D> {

    private volatile int pivot;
    private volatile int next;

    public CCW(ExecutorService executorService, int availableThreads, List<Point2D> data) {
        super(executorService, availableThreads, data);
    }

    @Override
    protected Reference<Point2D> getReferenceInstance() {
        CCWReference cf = new CCWReference(null);
        cf.setIndex(Integer.MIN_VALUE);
        return cf;
    }

    public void setPivot(int pivot) {
        this.pivot = pivot;
    }

    public void setNext(int next) {
        this.next = next;
    }

    @Override
    protected void onSearch(int start, int end, Reference ref) {
        CCWReference exRef = (CCWReference) ref;
        int q = Integer.MIN_VALUE;

        for (int i = end - 1; i >= start; i--) {
            if (Utils.ccw(data.get(pivot), data.get(i), data.get(next)) == 2) {
                q = i;
                break;
            }
        }

        synchronized (exRef){
            if(q > exRef.getIndex()){
                exRef.setIndex(q);
            }
        }
    }

    public class CCWReference extends Reference<Point2D> {

        private int index = -1;

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
