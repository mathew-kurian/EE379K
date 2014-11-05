package com.computation.common;

import java.awt.*;

public class Point extends java.awt.Point {

    public static final Color VISITED = Color.GREEN;
    public static final Color UNVISITED = Color.WHITE;

    private Color color = UNVISITED;

    public Point(int a, int i) {
        super(a, i);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
