package com.computation.experimental;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by mwkurian on 11/18/2014.
 */
public class OptimalSearch {

    static final int MIN_SIZE = 20000;
    static final int MAX_SIZE = 15000000;
    static final int OFFSET_SIZE = 1000000;
    static final int MIN_THREADS = 10;
    static final int MAX_THREADS = 20;
    static final int OFFSET_THREADS = 1;

    private static List<Integer> generate(int length) {
        List<Integer> data = new ArrayList<Integer>();
        Random rand = new Random();

        for (int i = 0; i < length; i++) {
            data.add(rand.nextInt());
        }

        return data;
    }

    private int totalTests = 0;
    private int doneTests = 0;

    public void find() {
        totalTests = (MAX_THREADS - MIN_THREADS) / OFFSET_THREADS
                * (MAX_SIZE - MIN_SIZE) / OFFSET_SIZE * 2;
        findHelper(MIN_THREADS, MIN_SIZE, new ArrayList<Result>());
    }

    private void findHelper(final int threads, final int length, final ArrayList<Result> results) {

        if (threads >= MAX_THREADS) {
            // Done
            Collections.sort(results);
            for (Result r : results) {
                System.out.printf("(size: %d)(threads: %d)(time: %fs)\n",
                        r.size, r.threads, r.toString());
            }
            return;
        }

        if (length >= MAX_SIZE) {
            findHelper(threads + OFFSET_THREADS, MIN_SIZE, results);
            return;
        }

        final List<Integer> searchData = OptimalSearch.generate(length);

        final ConcurrentSearch cs =
                new ConcurrentSearch<Integer>(searchData, Integer.MIN_VALUE, threads) {
                    @Override
                    public void found(Integer integer) {
                        super.found(integer);
                        doneTests++;
                        System.out.println(Math.round(((double)doneTests / (double)totalTests) * 100.0) + "%");
                        results.add(new Result(length, threads, elapsed()));
                        findHelper(threads, length + OFFSET_SIZE, results);
                    }
                };

        if(threads == MIN_THREADS) {
            new SimpleSearch<Integer>(searchData, Integer.MIN_VALUE) {
                @Override
                public void found(Integer integer) {
                    super.found(integer);
                    doneTests++;
                    results.add(new Result(length, threads, elapsed()));
                    cs.start();
                }
            }.start();
        } else {
            cs.start();
        }
    }

    private class Result implements Comparable<Result> {
        public final int size;
        public final int threads;
        public final double time;

        public Result(int size, int threads, double time) {
            this.size = size;
            this.threads = threads;
            this.time = time;
        }

        @Override
        public int compareTo(Result o) {
            return Double.compare(time, o.time);
        }
    }

}
