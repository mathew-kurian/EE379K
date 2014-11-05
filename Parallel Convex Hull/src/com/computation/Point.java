package com.computation;

import java.awt.*;

/**
 * Created by Mathew on 10/31/2014.
 */
public class Point extends java.awt.Point {

    public static final Color VISITED = Color.GREEN;
    public static final Color UNVISITED = Color.WHITE;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private Color color = UNVISITED;

    public Point(int a, int i) {
        super(a, i);
    }
}
