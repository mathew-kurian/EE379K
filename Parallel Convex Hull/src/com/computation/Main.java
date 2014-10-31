package com.computation;

import com.computation.quickhull.Point;
import com.computation.quickhull.QuickHull;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        QuickHull quickHull = new QuickHull(generateRandomPoints(150));

    }

    public static Point[] generateRandomPoints(int length){
        Random rand = new Random();
        Point [] points = new Point [length];

        for(int i = 0; i < points.length; i++){
            points[i] = new Point(rand.nextInt(700) + 100, rand.nextInt(700) + 100);
        }

        return points;
    }
}
