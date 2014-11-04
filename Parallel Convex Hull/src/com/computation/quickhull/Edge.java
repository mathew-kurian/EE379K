package com.computation.quickhull;

/**
 * Created by Mathew on 11/4/2014.
 */
public class Edge {

    com.computation.debug.Point p1;
    com.computation.debug.Point p2;

    public Edge(com.computation.debug.Point p1, com.computation.debug.Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public boolean contains(com.computation.debug.Point p) {
        return (p.x == p1.x && p.y == p1.y) ||
                (p.x == p2.x && p.y == p2.y);
    }
}
