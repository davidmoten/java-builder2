package org.davidmoten.executors.more;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Benchmark)
public class Benchmarks {

    @State(Scope.Thread)
    public static class Execs {

        public final ExecutorService executor = Executors.newFixedThreadPool(1);

        public final ExecutorService executorJuc = java.util.concurrent.Executors.newFixedThreadPool(1);

        public final ExecutorService executor2 = Executors.newFixedThreadPool(2);

        public final ExecutorService executor2Juc = java.util.concurrent.Executors.newFixedThreadPool(2);
        
        @TearDown(Level.Trial)
        public void shutdown() {
            executor.shutdown();
            executorJuc.shutdown();
            executor2.shutdown();
            executor2Juc.shutdown();
        }
    }

    @Benchmark
    public int executorDoNothingManyTimesSingleThreadMore(Execs execs) throws InterruptedException {
        return execute(execs.executor);
    }

    @Benchmark
    public int executorDoNothingManyTimesSingleThreadJuc(Execs execs) throws InterruptedException {
        return execute(execs.executorJuc);
    }

    // @Benchmark
    public int executorDoNothingManyTimesTwoThreadsMore(Execs execs) throws InterruptedException {
        return execute(execs.executor2);
    }

    // @Benchmark
    public int executorDoNothingManyTimesTwoThreadsJuc(Execs execs) throws InterruptedException {
        return execute(execs.executor2Juc);
    }

    private int execute(ExecutorService executor) throws InterruptedException {
        AtomicInteger count = new AtomicInteger();
        Runnable r = () -> count.incrementAndGet();
        for (int i = 0; i < 10000; i++) {
            executor.execute(r);
        }
        CountDownLatch latch = new CountDownLatch(1);
        executor.execute(() -> latch.countDown());
        latch.await(30, TimeUnit.SECONDS);
        return count.get();
    }

}