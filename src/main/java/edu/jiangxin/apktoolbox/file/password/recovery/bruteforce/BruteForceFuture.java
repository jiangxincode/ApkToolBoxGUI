package edu.jiangxin.apktoolbox.file.password.recovery.bruteforce;

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

/**
 * A {@code Future} represents the result of an asynchronous
 * computation.  Methods are provided to check if the computation is
 * complete, to wait for its completion, and to retrieve the result of
 * the computation.  The result can only be retrieved using method
 * {@code get} when the computation has completed, blocking if
 * necessary until it is ready.  Cancellation is performed by the
 * {@code cancel} method.  Additional methods are provided to
 * determine if the task completed normally or was cancelled. Once a
 * computation has completed, the computation cannot be cancelled.
 * If you would like to use a {@code Future} for the sake
 * of cancellability but not provide a usable result, you can
 * declare types of the form {@code Future<?>} and
 * return {@code null} as a result of the underlying task.
 **/
// If you want to know the Future class, refer to site; https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Future.html

// Complete this class using a lock and the condition variable
public class BruteForceFuture implements Future<String> {
    private static final Logger logger = LogManager.getLogger(BruteForceFuture.class.getSimpleName());
    private String result = null;
    private AtomicInteger finishedTaskCount = new AtomicInteger(0);
    private int taskCount;
    private final Lock lock = new ReentrantLock();
    private final Condition resultSet = lock.newCondition(); // refer to Condition and Lock class in javadoc

    public BruteForceFuture(int taskCount) {
        this.taskCount = taskCount;
    }

    /*  ### set ###
     *  set the result and send signal to thread waiting for the result
     */
    public void set(String result) {
        lock.lock();
        try {
            if (result != null || finishedTaskCount.incrementAndGet() >= taskCount) {
                this.result = result;
                resultSet.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    /*  ### get ###
     *  if result is ready, return it.
     *  if not, wait on the conditional variable.
     */
    @Override
    public String get() {
        lock.lock();
        try {
            resultSet.await();
        } catch (InterruptedException e) {
            logger.error("get InterruptedException");
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
        return result;
    }

    /*  ### isDone ###
     *  returns true if result is set
     */
    @Override
    public boolean isDone() {
        return result != null;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        // no need to implement this. We don't use this...
        return null;
    }
}
