package com.computation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PointCloud {

    private JPanel panel = new PointPanel();
    ;
    private JFrame frame = new JFrame();
    private List<Point> points = Common.generateRandomPoints(50);
    private List<Point> polygon = new ArrayList<Point>();

    public PointCloud() {

        panel.setPreferredSize(new Dimension(1000, 1000));

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public List<Point> getPoints() {
        return points;
    }

    public void draw() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PointCloud.this.panel.repaint();
            }
        });
    }

    public void updateHullAndDraw(List<Point> polygon) {

        // Thread-safe
        final List<Point> polygonCpy = polygon == null ? new ArrayList<Point>(0) : new ArrayList<Point>(polygon);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PointCloud.this.polygon = polygonCpy;
                PointCloud.this.panel.repaint();
            }
        });
    }

    private class PointPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            if(points == null || points.size() == 0){
                return;
            }

            g2d.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            g2d.setColor(Color.BLACK);
            g2d.setBackground(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(Color.WHITE);

            for (Point p : points) {
                g2d.setColor(p.getColor());
                g2d.fillOval(p.x, p.y, 10, 10);
            }

            g2d.setColor(Color.RED);

            if(polygon == null || polygon.size() == 0){
                return;
            }

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
