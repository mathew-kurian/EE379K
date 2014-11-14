package com.computation.algo;

import com.computation.common.ConvexHull;
import com.computation.common.Point2D;

import java.util.List;

/**
 * Created by kgowru on 11/12/14.
 */
public class GiftWrapping extends ConvexHull{

    public GiftWrapping(int points, int width, int height, int threads) {
        super(points, width, height, threads);
    }

    protected void findHull(){
        //S is our set of points
        int numOfPoints = point2Ds.size(); //total number of points in S

        // Set some fields
        pointCloud.setField("ThreadPool", true);

        // Get points
        List<Point2D> point2Ds = pointCloud.getPoints();

        //find the leftmost point in S
        int leftMost = 0;
        for (int i = 0; i < numOfPoints; i++){
            if(point2Ds.get(i).x < point2Ds.get(leftMost).x){
                l = i;
            }
        }

        int p,q;
        p = q = leftMost;

        do{
            for (int i = 0; i < numOfPoints; i++){
                if()
            }


        } while (p != leftMost){


        }

        //loop till endpoint == first hull point (P[0])

            //P[i] = pointOnHull

            //endpoint = S[0]

            //from 1 to number of points in S
                //if (endpoint == pointOnHull) or (S[j] is on the left of line from P[i] to endpoint)
                   // endpoint = S[j]
            //i++
            //pointOnHull = endpoint


    }



}
