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
    private volatile int threadTurn;

    public CCW(ExecutorService executorService, int availableThreads, List<Point2D> data) {
        super(executorService, availableThreads, data);
        this.threadTurn = 0;
    }

    @Override
    protected Reference<Point2D> getReferenceInstance() {
        CCWReference cf = new CCWReference(null);
        cf.setIndex(Integer.MAX_VALUE);
        return cf;
    }

    public void setPivot(int pivot) {
        this.pivot = pivot;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getThreadTurn(){
        return threadTurn;
    }

    public void nextThread() {
        if(availableThreads > 0) {
            threadTurn = ++threadTurn % availableThreads;
        }
    }

    @Override
    protected void onSearch(int start, int end, Reference ref) {
        CCWReference exRef = (CCWReference) ref;

        for (int i = start; i < end; i++) {

            // Some other thread result
            // solution
            if (exRef.getIndex() != Integer.MAX_VALUE) {
                break;
            }

            if (Utils.ccw(data.get(pivot), data.get(i), data.get(next)) == 2) {
                synchronized (exRef) {
                    exRef.setIndex(i);
                    break;
                }
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
