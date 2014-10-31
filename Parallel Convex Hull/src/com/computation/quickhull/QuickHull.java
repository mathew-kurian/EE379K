package com.computation.quickhull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QuickHull {

    Point[] points;
    List<Point> polygon;
    Panel panel;
    List<Edge> edges;

    public QuickHull(Point[] points) {
        this.points = points;
        this.polygon = new ArrayList<Point>();
        this.panel = new Panel();
        this.edges = new LinkedList<Edge>();

        JFrame jmf = new JFrame();
        final JButton button = new JButton("Next");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(QuickHull.this.next().size() == 0){
                        button.setEnabled(false);
                    }
                } catch (Exception e1) {
                }
            }
        });

        panel.setPreferredSize(new Dimension(1000, 1000));

        jmf.getContentPane().setLayout(new BorderLayout());
        jmf.getContentPane().add(panel, BorderLayout.CENTER);
        jmf.getContentPane().add(button, BorderLayout.SOUTH);
        jmf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jmf.pack();
        jmf.setLocationRelativeTo(null);
        jmf.setVisible(true);

        Point p1 = findMax(Direction.EAST);
        Point p2 = findMax(Direction.WEST);

        polygon.add(p2);
        polygon.add(p1);

        edges.add(new Edge(p1, p2));

        panel.repaint();
    }

    public List<Edge> next() throws InvocationTargetException, InterruptedException {

        if (edges.size() > 0) {

            Edge edge = edges.remove(0);
            int maxArea = Integer.MIN_VALUE;
            Point maxPoint = null;

            for (Point point : points) {
                if (edge.contains(point) || polygon.contains(point) || point.skip) {
                    continue;
                }
                if (contains(point)) {
                    point.skip = true;
                    continue;
                }

                int area = getArea(edge, point);

                if (area > maxArea) {
                    maxArea = area;
                    maxPoint = point;
                }
            }

            if (maxPoint == null) {
                return edges;
            }

            maxPoint.skip = true;

            polygon.add(maxPoint);
            convex();

            panel.repaint();

            edges.add(new Edge(edge.p1, maxPoint));
            edges.add(new Edge(edge.p2, maxPoint));
        }

        return edges;
    }

    int getArea(Edge edge, Point p) {
        return Math.abs((edge.p1.x - p.x) * (edge.p2.y - edge.p1.y) - (edge.p1.x - edge.p2.x) * (p.y - edge.p1.y));
    }

    private double angleBetween(Point center, Point current, Point previous) {
        return Math.toDegrees(Math.atan2(current.x - center.x,current.y - center.y)-
                Math.atan2(previous.x- center.x,previous.y- center.y));
    }

    public void convex() {

        for (Point p : polygon) {
            p.next = null;
        }

        ArrayList<Point> newList = new ArrayList<Point>();
        Point start = polygon.get(0);

        newList.add(start);

        while (start != null && start.next == null) {

            Point end = null;
            double dist = Double.MAX_VALUE;

            for (Point other : polygon) {

                if (other.next != null || other.equals(start)) {
                    continue;
                }

                if(newList.size() > 3){
                    int i = newList.size() - 1;
                    Point center = newList.get(i);
                    Point previous = newList.get(i - 1);
                    double angle = angleBetween(center, previous, other);

                    if(angle > 180){
                        continue;
                    }
                }

                double newDist = Math.pow(start.x - other.x, 2) + Math.pow(start.y - other.y, 2);

                if (newDist < dist) {
                    dist = newDist;
                    end = other;
                }
            }

            if (end != null) {
                System.out.printf("Point (%d, %d) found (%d, %d)\n", start.x, start.y, end.x, end.y);
                newList.add(end);
            }

            start.next = end;
            start = end;
        }

        polygon = newList;
    }

    public boolean contains(Point test) {
        Point[] points = this.polygon.toArray(new Point[0]);
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = points.length - 1; i < points.length; j = i++) {
            if ((points[i].y > test.y) != (points[j].y > test.y) &&
                    (test.x < (points[j].x - points[i].x) * (test.y - points[i].y) / (points[j].y - points[i].y) + points[i].x)) {
                result = !result;
            }
        }
        return result;
    }

    public Point findMax(Direction dir) {
        Point p = null;
        int val;

        switch (dir) {
            case NORTH:
            case SOUTH:
            case EAST:
                val = Integer.MIN_VALUE;
                for (Point point : points) {
                    if (point.x > val) {
                        p = point;
                        val = point.x;
                    }
                }
                break;
            case WEST:
                val = Integer.MAX_VALUE;
                for (Point point : points) {
                    if (point.x < val) {
                        p = point;
                        val = point.x;
                    }
                }
                break;
        }

        return p;
    }


    enum Direction {
        NORTH, SOUTH, EAST, WEST
    }

    class Edge {
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

    class Panel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            g2d.setColor(Color.BLACK);
            g2d.setBackground(Color.BLACK);

            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(Color.WHITE);

            for (Point p : points) {
                if(p.skip) {
                    g2d.setColor(Color.BLUE);
                } else {
                    g2d.setColor(Color.WHITE);
                }
                g2d.fillOval(p.x, p.y, 10, 10);
            }

            g2d.setColor(Color.RED);

            Point start = polygon.get(0);

            for (Point p : polygon) {
                g2d.fillOval(p.x - 5, p.y - 5, 20, 20);
            }

            Font font = g2d.getFont();
            g2d.setFont(new Font(font.getName(), font.getStyle(), 20));

            for (int i = 1; i < polygon.size(); i++) {
                Point next = polygon.get(i);
                g2d.drawLine(start.x, start.y, next.x, next.y);
                g2d.drawString(i + "", next.x + 25, next.y);
                start = next;
            }

            g2d.drawLine(start.x, start.y, polygon.get(0).x, polygon.get(0).y);

            g2d.setColor(Color.GREEN);

            start = polygon.get(0);

            g2d.fillOval(start.x - 5, start.y - 5, 20, 20);
            g2d.drawString("0", start.x + 20, start.y);
        }
    }
}
