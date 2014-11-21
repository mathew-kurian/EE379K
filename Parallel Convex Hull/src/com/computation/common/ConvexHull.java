package com.computation.common;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ConvexHull implements Runnable {

    private static final Console console = Console.getInstance(ConvexHull.class);
    protected final Point2DCloud pointCloud;
    protected final int pointCount;
    protected final int threads;
    protected final List<Point2D> points;
    protected boolean debug = true;
    protected boolean debugStepThrough = false;
    protected long debugFrameDelay = 1000;
    protected Lock debugStep;
    protected Condition debugStepCondition;
    private boolean active = false;
    private long startTime;

    public ConvexHull(int pointCount, int width, int height, int threads) {
        this(pointCount, width, height, threads, true);
    }

    public ConvexHull(int pointCount, int width, int height, int threads, boolean debug) {
        this(pointCount, width, height, threads, debug, 1000);
    }

    public ConvexHull(int pointCount, int width, int height, int threads, boolean debug, int animationDelay) {
        this.pointCloud = new Point2DCloud(pointCount, width, height, debug);
        this.pointCount = pointCount;
        this.threads = threads;
        this.debug = debug;
        this.debugStepThrough = animationDelay == Integer.MAX_VALUE;
        this.debugFrameDelay = debugStepThrough ? 0 : animationDelay;
        this.points = this.pointCloud.getPoints();
        this.debugStep = new ReentrantLock();
        this.debugStepCondition = debugStep.newCondition();
    }

    public void show() {
        // Get algo name
        String algo = ConvexHull.this.getClass().getSimpleName();

        // Add a button
        pointCloud.addButton("Start", this);

        // Add debug button
        if (debugStepThrough) {
            pointCloud.addButton("Step", new Runnable() {
                @Override
                public void run() {
                    debugStep.lock();
                    debugStepCondition.signal();
                    debugStep.unlock();
                }
            });
        }

        // Set basic information
        pointCloud.setName(algo);
        pointCloud.setField("Algorithm", algo);
        pointCloud.setField("Points", pointCloud.getPoints().size());
        pointCloud.setField("Threads", threads);
        pointCloud.setField("Debug", debug);
        pointCloud.setField("Frame Delay (ms)", debugFrameDelay);

        pointCloud.show();
    }

    @Override
    public void run() {
        if (!active) {
            active = true;
            pointCloud.enableButton("Start", false);
            startTime = System.nanoTime();
            findHull();
            double duration = ((double) (System.nanoTime() - startTime)) / 1000000000.0;
            duration = Math.round(duration * 10000.0) / 10000.0;
            pointCloud.setField("Duration (s)", duration);
            pointCloud.toast("Completed!");
            active = false;
        }
    }

    protected void delay() {
        if (debug) {
            pointCloud.draw();
            try {
                Thread.sleep(debugFrameDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void requestAnimationFrame() {
        if (debugStepThrough) {
            debugStep.lock();
            try {
                debugStepCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void releaseAnimationFrame() {
        if (debugStepThrough) {
            console.err("Released lock");
            debugStep.unlock();
        }
    }

    protected abstract void findHull();
}
