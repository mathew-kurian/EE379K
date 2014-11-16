package com.computation.algo;

import com.computation.common.ConvexHull;
import com.computation.common.Point2D;
import com.computation.common.Utils;
import com.computation.common.Edge;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kgowru on 11/12/14.
 */
public class GiftWrapping extends ConvexHull{

    public GiftWrapping(int points, int width, int height, int threads) {
        super(points, width, height, threads);
    }

    @Override
    protected void findHull(){

        // Set some fields
        pointCloud.setField("ThreadPool", true);

        // Get points
        List<Point2D> point2Ds = pointCloud.getPoints();

        //S is our set of points
        int numOfPoints = point2Ds.size(); //total number of points in S


        int[] next = new int[numOfPoints];
        Arrays.fill(next, -1);

        //find the leftmost point in S
        int leftMost = 0;
        for (int i = 0; i < numOfPoints; i++){
            if(point2Ds.get(i).x < point2Ds.get(leftMost).x){
                leftMost = i;
            }
        }


        int p,q;
        p = leftMost;


       // for (int i = 0; i < numOfPoints; i++){
        //    System.out.println("Point "+i + "("+ point2Ds.get(i).x +", "+ point2Ds.get(i).y +")");
       // }

        do{
            q = (p+1) % numOfPoints;
            for (int i = 0; i < numOfPoints; i++){
                if(Utils.CCW(point2Ds.get(p), point2Ds.get(i), point2Ds.get(q)) == 2)
                    q = i;
            }
            next[p] = q;
            p = q;

        } while (p != leftMost);

        int count = 0;
        for (int i = 0; i < numOfPoints; i++){
            if(next[i]!= -1){
                System.out.println("Point " + i + ":("+ point2Ds.get(i).x +", "+ point2Ds.get(i).y +")");
                count++;
            }
        }
        int a = leftMost;
        for (int i = 0 ; i < count; i ++){
            pointCloud.addEdge(new Edge(point2Ds.get(a),point2Ds.get(next[a])));
            a = next[a];
        }
        System.out.println("Finished");

    }



}
