package com.example.imageloader.executor;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorService extends ThreadPoolExecutor {
    private static final int THREAD_COUNT = 3;

    ExecutorService(ThreadFactory threadFactory) {
        super(THREAD_COUNT, THREAD_COUNT, 0, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<Runnable>(), threadFactory);
    }

    private void setThreadCount(int threadCount) {
        setCorePoolSize(threadCount);
        setMaximumPoolSize(threadCount);
    }
}
