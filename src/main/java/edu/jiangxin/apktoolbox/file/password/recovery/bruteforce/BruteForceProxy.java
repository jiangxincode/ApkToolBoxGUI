package edu.jiangxin.apktoolbox.file.password.recovery.bruteforce;

import edu.jiangxin.apktoolbox.file.password.recovery.Synchronizer;
import edu.jiangxin.apktoolbox.file.password.recovery.checker.IChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BruteForceProxy {
    private static final Logger logger = LogManager.getLogger(BruteForceProxy.class.getSimpleName());

    private ExecutorService executorService;

    private BruteForceFuture bruteForceFuture;

    private static class BruteForceProxyHolder {
        private static final BruteForceProxy instance = new BruteForceProxy();
    }

    private BruteForceProxy() {
    }

    public static BruteForceProxy getInstance() {
        return BruteForceProxyHolder.instance;
    }

    public String startAndGet(int numThreads, int passwordLength, IChecker checker, String charsSet, Synchronizer synchronizer) {
        executorService = Executors.newFixedThreadPool(numThreads);
        bruteForceFuture = new BruteForceFuture(numThreads);
        BruteForceTaskParam param = new BruteForceTaskParam(numThreads, passwordLength, checker, charsSet);
        for (int taskId = 0; taskId < numThreads; taskId++) {
            BruteForceRunnable task = new BruteForceRunnable(taskId, param, bruteForceFuture, synchronizer);
            executorService.execute(task);
        }
        String password = null;
        try {
            password = bruteForceFuture.get();
        } catch (Exception e) {
            logger.info("Exception test: ", e);
        } finally {
            if (executorService.isShutdown()) {
                executorService.shutdown();
            }
        }
        return password;
    }

    public void cancel() {
        bruteForceFuture.cancel(false);
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        while (executorService != null && !executorService.isTerminated()) {
            try {
                final boolean isTimeout = !executorService.awaitTermination(100, TimeUnit.SECONDS);
                logger.info("awaitTermination isTimeout: " + isTimeout);
            } catch (InterruptedException e) {
                logger.error("awaitTermination InterruptedException");
                Thread.currentThread().interrupt();
            }
        }
    }
}
