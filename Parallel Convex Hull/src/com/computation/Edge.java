package com.computation;

/**
 * Created by Mathew on 11/4/2014.
 */
public class Edge {

    Point p1;
    Point p2;

    public Edge(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public boolean contains(Point p) {
        return (p.x == p1.x && p.y == p1.y) ||
                (p.x == p2.x && p.y == p2.y);
    }
}
