package com.computation;

import com.computation.algo.QuickHull;
import com.computation.common.ConvexHull;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException,
            InstantiationException, IllegalAccessException, InvocationTargetException, InterruptedException {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        ConvexHull convexHull = new QuickHull(/* points */ 31, /* width */ 800, /* height */ 600, /* threadCount */ 10);
        convexHull.show();
    }
}
