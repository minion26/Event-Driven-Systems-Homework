package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadedTask {
    public ThreadedTask(int threads, int count, Runnable task) {
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        long startTime = System.nanoTime();

        for (int i = 0; i < count; i++) {
            executor.submit(task);
        }

        executor.shutdown();

        try {
            if(!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        long endTime = System.nanoTime();
        System.out.println("Time taken for " + threads + " threads: " + (endTime - startTime) / 1000000 + "ms");
    }
}
