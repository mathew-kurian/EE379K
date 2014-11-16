package com.computation;

import com.computation.algo.GiftWrapping;
import com.computation.algo.QuickHull;
import com.computation.common.ConvexHull;
import com.computation.common.Point2DCloud;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException,
            InstantiationException, IllegalAccessException, InvocationTargetException, InterruptedException {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        Point2DCloud.DPI_SCALING = 1; /* Set display scaling */

       // ConvexHull convexHull = new QuickHull(/* points */ 31, /* width */ 800, /* height */ 600, /* threadCount */ 10);
       // convexHull.show();

        ConvexHull convexHull = new GiftWrapping(20, 800, 600, 1);
        convexHull.show();
    }
}
