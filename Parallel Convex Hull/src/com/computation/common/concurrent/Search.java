package com.computation.common.concurrent;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mwkurian on 11/19/2014.
 */

public abstract class Search<T> {

    protected final ExecutorService executorService;
    protected final List<T> data;
    protected int availableThreads;

    public Search(ExecutorService executorService, int availableThreads, List<T> data) {
        this.executorService = executorService;
        this.data = data;
        this.availableThreads = availableThreads;
    }

    public  void setAvailableThreads(int availableThreads){
        this.availableThreads = availableThreads;
    }

    public final Reference find(){

        int size = data.size();
        int offset = size / availableThreads;

        Reference<T> ref = getReferenceInstance();
        AtomicInteger atomicCount = new AtomicInteger(availableThreads);

        if(availableThreads == 0){
            // Execute directly on main thread
            new Subset(0, size,
                    ref, atomicCount).run();
            return ref;
        }

        for (int i = 0; i < size; i += offset) {
            executorService.execute(new Subset(i, Math.min(i + offset, size),
                    ref, atomicCount));
        }

        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

       return ref;
    }

    protected abstract Reference<T> getReferenceInstance();
    protected abstract void onSearch(int start, int end, Reference<T> ref);

    private class Subset implements Runnable {
        private final int start;
        private final int end;
        private final Reference<T> ref;
        private final AtomicInteger threadCount;

        public Subset(int start, int end, Reference<T> ref,
                      AtomicInteger threadCount) {
            this.start = start;
            this.end = end;
            this.ref = ref;
            this.threadCount = threadCount;
        }

        @Override
        public void run() {

            onSearch(start, end, ref);

            if (threadCount.decrementAndGet() == 0) {
                synchronized (Search.this) {
                    Search.this.notify();
                }
            }
        }
    }
}
