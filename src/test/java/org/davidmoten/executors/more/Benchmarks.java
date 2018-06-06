package org.davidmoten.executors.more;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.davidmoten.executors.more.Executors;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class Benchmarks {

    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

    private static final ExecutorService executorJuc = java.util.concurrent.Executors
            .newFixedThreadPool(1);

    private static final ExecutorService executor2 = Executors.newFixedThreadPool(2);

    private static final ExecutorService executor2Juc = java.util.concurrent.Executors
            .newFixedThreadPool(2);

    @Benchmark
    public boolean executorDoNothingManyTimesSingleThreadMore(Blackhole bh)
            throws InterruptedException {
        return execute(executor, bh);
    }

    @Benchmark
    public boolean executorDoNothingManyTimesSingleThreadJuc(Blackhole bh)
            throws InterruptedException {
        return execute(executorJuc, bh);
    }

    // @Benchmark
    public boolean executorDoNothingManyTimesTwoThreadsMore(Blackhole bh)
            throws InterruptedException {
        return execute(executor2, bh);
    }

    // @Benchmark
    public boolean executorDoNothingManyTimesTwoThreadsJuc(Blackhole bh)
            throws InterruptedException {
        return execute(executor2Juc, bh);
    }

    private boolean execute(ExecutorService executor, Blackhole bh) throws InterruptedException {
        Runnable r = new Runnable() {
            int count;
            @Override
            public void run() {
                count++;
                bh.consume(count);
            }
        };
        for (int i = 0; i < 10000; i++) {
            executor.execute(r);
        }
        CountDownLatch latch = new CountDownLatch(1);
        executor.execute(() -> latch.countDown());
        return latch.await(30, TimeUnit.SECONDS);
    }

}