package edu.jiangxin.apktoolbox.file.password.recovery.category.bruteforce;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BruteForceFuture implements Future<String> {
    private static final Logger logger = LogManager.getLogger(BruteForceFuture.class.getSimpleName());
    private volatile String result = null;
    private final AtomicInteger finishedTaskCount = new AtomicInteger(0);
    private final int taskCount;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    private boolean isCancelled;

    public BruteForceFuture(int taskCount) {
        this.taskCount = taskCount;
    }

    public void set(String result) {
        lock.lock();
        try {
            if (result != null || finishedTaskCount.incrementAndGet() >= taskCount) {
                this.result = result;
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String get() {
        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            logger.error("await InterruptedException");
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
        return result;
    }

    @Override
    public boolean isDone() {
        return result != null;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        isCancelled = true;
        return true;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        // no need to implement this. We don't use this...
        return null;
    }
}
