package com.computation.common;

public abstract class VisualConvexHull {

    protected final Point2DCloud point2DCloud;

    public VisualConvexHull(Point2DCloud point2DCloud){
        this.point2DCloud = point2DCloud;
    }

    public final void start(int threads){

        // Set basic information
        point2DCloud.setThreadCount(threads);
        point2DCloud.setAlgorithm(getClass().getSimpleName());

        // Pause and ask to continue
        point2DCloud.toast("Press OK to start");

        findHull(threads);
    }

    protected abstract void findHull(int threads);
}
