package com.computation.quickhull;

/**
 * Created by Mathew on 10/31/2014.
 */
public class Point extends java.awt.Point{
    public Point next;
    public boolean skip = false;

    public Point(int a, int i) {
        super(a, i);
    }
}
