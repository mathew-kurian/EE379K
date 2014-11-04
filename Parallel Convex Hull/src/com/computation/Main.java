package com.computation;

import com.computation.quickhull.QuickHull;

public class Main {

    public static void main(String[] args) {
        ConvexHull convexHull = new QuickHull();
        convexHull.findConvexHull(new PointCloud());
    }
}
