package com.computation;

import com.computation.algo.QuickHull;
import com.computation.common.VisualConvexHull;
import com.computation.common.Point2DCloud;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException,
            InstantiationException, IllegalAccessException {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        VisualConvexHull visualConvexHull = new QuickHull(new Point2DCloud(/* points */ 31, /* width */ 800, /* height */ 600));
        visualConvexHull.start(/* threadCount */ 10);
    }
}
