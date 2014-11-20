package com.computation.common.concurrent;

import com.computation.common.Console;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by mwkurian on 11/19/2014.
 */

public abstract class Search<T, O extends Collection<T>> {

    private final static Console console = Console.getInstance(Search.class);
    protected final ExecutorService executorService;

    protected O data;
    protected int availableThreads;
    protected Lock lock;

    public Search(ExecutorService executorService, int availableThreads, O data) {
        this.executorService = executorService;
        this.data = data;
        this.availableThreads = availableThreads;
        this.lock = new ReentrantLock();
    }

    public Lock getLock() {
        return lock;
    }

    public void setAvailableThreads(int availableThreads) {
        this.availableThreads = availableThreads;
    }

    public final Reference<T> find() {

        int size = data.size();
        int offset = size / availableThreads;

        Reference<T> ref = getReferenceInstance();
        Reference<Integer> threadCount = new Reference<Integer>(availableThreads);

        if (availableThreads == 0) {
            // Execute directly on main thread
            new Subset(0, size,
                    ref, threadCount).run();
        } else {
            for (int i = 0; i < size; i += offset) {
                executorService.execute(new Subset(i, Math.min(i + offset, size),
                        ref, threadCount));
            }
        }

        synchronized (threadCount) {
            if (threadCount.get() > 0) {
                try {
                    threadCount.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        console.log("Finished");

        return ref;
    }

    protected abstract Reference<T> getReferenceInstance();

    protected abstract void onSearch(int start, int end, Reference<T> ref);

    private class Subset implements Runnable {
        private final int start;
        private final int end;
        private final Reference<T> ref;
        private final Reference<Integer> threadCount;

        public Subset(int start, int end, Reference<T> ref,
                      Reference<Integer> threadCount) {
            this.start = start;
            this.end = end;
            this.ref = ref;
            this.threadCount = threadCount;
        }

        @Override
        public void run() {

            onSearch(start, end, ref);

            synchronized (threadCount) {
                int currThreadCount = threadCount.get() - 1;
                threadCount.update(currThreadCount);

                if (currThreadCount <= 0) {
                    threadCount.notify();
                }
            }
        }
    }
}
