package com.computation.common;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PointCloud {

    public static final boolean DEBUG = true;
    public static final long DEBUG_ANIMATION_TIME_MS = 1000;

    private JPanel panel = new PointPanel();
    private JFrame frame;
    private JLabel threadCountLabel;
    private List<Point> points;
    private Set<Edge> polygon;

    public PointCloud(int count, int width, int height) {
        frame = new JFrame();
        polygon = new HashSet<Edge>();
        threadCountLabel = new JLabel();
        points = Utils.generateRandomPoints(count, width, height, 50);

        panel.setPreferredSize(new Dimension(width, height));

        threadCountLabel.setBackground(Color.BLACK);
        threadCountLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        threadCountLabel.setText("Running threads: Not set");

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(threadCountLabel, BorderLayout.SOUTH);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void setThreadCount(final int count) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                threadCountLabel.setText("Running threads: " + count + " threads");
            }
        });

    }

    public void toast(String msg) {
        JOptionPane.showMessageDialog(frame, msg);
    }

    public List<Point> getPoints() {
        return new ArrayList<Point>(points);
    }

    public void draw() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PointCloud.this.panel.repaint();
            }
        });
    }

    public void addEdge(Edge edge) {

        // Thread-safe
        final Edge edgeCpy = new Edge(edge);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PointCloud.this.polygon.add(edgeCpy);
                PointCloud.this.panel.repaint();
            }
        });
    }

    public void removeEdge(Edge edge) {

        // Thread-safe
        final Edge edgeCpy = new Edge(edge);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PointCloud.this.polygon.remove(edgeCpy);
                PointCloud.this.panel.repaint();
            }
        });
    }

    private class PointPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            if (points == null || points.size() == 0) {
                return;
            }

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            g2d.setColor(Color.BLACK);
            g2d.setBackground(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2));

            for (Edge edge : polygon) {
                g2d.drawLine(edge.p1.x, edge.p1.y, edge.p2.x, edge.p2.y);
            }

            g2d.setStroke(new BasicStroke(1));
            g2d.setColor(Color.WHITE);

            for (Point p : points) {
                g2d.setColor(p.getColor());
                g2d.fillOval(p.x - 3, p.y - 3, 6, 6);
            }

        }
    }

}
