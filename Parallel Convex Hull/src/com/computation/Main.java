package com.computation;

import com.computation.quickhull.QuickHull;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException,
            InstantiationException, IllegalAccessException {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        ConvexHull convexHull = new QuickHull();
        convexHull.findConvexHull(new PointCloud(/* points */ 23, /* width */ 1000, /* height */ 1000), /* threadCount */ 10);
    }
}
