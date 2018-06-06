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
    
    private static final ExecutorService executorJuc = java.util.concurrent.Executors.newFixedThreadPool(1);
    
    @Benchmark
    public boolean moreExecutor(Blackhole bh) throws InterruptedException {
        return execute(executor);
    }

    @Benchmark
    public boolean jucExecutor(Blackhole bh) throws InterruptedException {
        return execute(executorJuc);
    }
    
    private boolean execute(ExecutorService executor) throws InterruptedException {
        AtomicLong count = new AtomicLong();
        Runnable r = () -> count.incrementAndGet();
        for (int i = 0;i <1000000;i++) {
            executor.execute(r);
        }
        CountDownLatch latch = new CountDownLatch(1);
        executor.execute(() -> latch.countDown());
        return latch.await(30, TimeUnit.SECONDS);
    }
    
    
}