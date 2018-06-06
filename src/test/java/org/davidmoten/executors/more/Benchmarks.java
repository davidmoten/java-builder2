package org.davidmoten.executors.more;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class Benchmarks {

    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

    private static final ExecutorService executorJuc = java.util.concurrent.Executors
            .newFixedThreadPool(1);

    private static final ExecutorService executor2 = Executors.newFixedThreadPool(2);

    private static final ExecutorService executor2Juc = java.util.concurrent.Executors
            .newFixedThreadPool(2);

    @Benchmark
    public int executorDoNothingManyTimesSingleThreadMore()
            throws InterruptedException {
        return execute(executor);
    }

    @Benchmark
    public int executorDoNothingManyTimesSingleThreadJuc()
            throws InterruptedException {
        return execute(executorJuc);
    }

    // @Benchmark
    public int executorDoNothingManyTimesTwoThreadsMore()
            throws InterruptedException {
        return execute(executor2);
    }

    // @Benchmark
    public int executorDoNothingManyTimesTwoThreadsJuc()
            throws InterruptedException {
        return execute(executor2Juc);
    }

    private int execute(ExecutorService executor) throws InterruptedException {
        AtomicInteger count = new AtomicInteger();
        Runnable r = ()-> count.incrementAndGet();
        for (int i = 0; i < 10000; i++) {
            executor.execute(r);
        }
        CountDownLatch latch = new CountDownLatch(1);
        executor.execute(() -> latch.countDown());
        latch.await(30, TimeUnit.SECONDS);
        return count.get();
    }

}