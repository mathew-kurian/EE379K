package com.computation;

import java.lang.reflect.InvocationTargetException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.computation.algo.GrahamScan;
import com.computation.common.ConvexHull;
import com.computation.common.Point2DCloud;
import com.computation.experimental.OptimalThreadCountFinder;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException,
            InstantiationException, IllegalAccessException, InvocationTargetException, InterruptedException {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        OptimalThreadCountFinder.DPI_SCALING =
                Point2DCloud.DPI_SCALING = 1; /* Set display scaling */

//        OptimalThreadCountFinder optimalThreadCountFinder = new OptimalThreadCountFinder();
//        optimalThreadCountFinder.find();

        //Mathew's Concurrent Quick Hull Implementation
//        ConvexHull convexHull = new QuickHull(/* pointCount */ 100, /* width */ 800, /* height */ 600, /* availableThreads */ 10);
//        convexHull.show(\);

        //Kapil's Concurrent Gift Wrapping Implemntation (max at 4 availableThreads)

//        ConvexHull convexHull = new QuickHull(1000, 1000, 1000, 16, true, 100);
//        convexHull.show();

//        ConvexHull convexHull2 = new QuickHull(10000000, 1000, 1000, 16, false, 0);
//        convexHull2.show();

        ConvexHull convexHull = new GrahamScan(100, 500, 500, 1, true, 0);
        convexHull.show();
    }
}
