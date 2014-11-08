package com.computation.common;

public abstract class ConvexHull implements Runnable {

    protected final Point2DCloud pointCloud;
    protected final int threads;
    protected boolean debug = true;
    protected long debugFrameDelay = 1000;

    private boolean active = false;

    public ConvexHull(int points, int width, int height, int threads) {
        this(points, width, height, threads, true);
    }

    public ConvexHull(int points, int width, int height, int threads, boolean debug) {
        this(points, width, height, threads, debug, 1000);
    }

    public ConvexHull(int points, int width, int height, int threads, boolean debug, int animationDelay) {
        this.pointCloud = new Point2DCloud(points, width, height);
        this.threads = threads;
        this.debug = debug;
        this.debugFrameDelay = animationDelay;
    }

    public void show() {
        // Get algo name
        String algo = ConvexHull.this.getClass().getSimpleName();

        // Add a button
        pointCloud.addButton("Start", this);

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
            findHull();
        }
    }

    protected void finish() {
        pointCloud.toast("Completed!");
        active = false;
    }

    protected abstract void findHull();
}
