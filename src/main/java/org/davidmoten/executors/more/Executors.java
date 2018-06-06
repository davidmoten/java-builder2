package org.davidmoten.executors.more;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public final class Executors {

    private Executors() {
        // prevent instantiation
    }

    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, false,
                new LinkedBlockingQueue<Runnable>());
    }

    public static ThreadFactory defaultThreadFactory() {
        return java.util.concurrent.Executors.defaultThreadFactory();
    }

}
