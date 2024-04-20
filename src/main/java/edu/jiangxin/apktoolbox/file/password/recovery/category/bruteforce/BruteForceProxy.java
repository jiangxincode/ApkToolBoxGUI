package edu.jiangxin.apktoolbox.file.password.recovery.category.bruteforce;

import edu.jiangxin.apktoolbox.file.password.recovery.RecoveryPanel;
import edu.jiangxin.apktoolbox.file.password.recovery.State;
import edu.jiangxin.apktoolbox.file.password.recovery.category.ICategory;
import edu.jiangxin.apktoolbox.file.password.recovery.checker.FileChecker;
import edu.jiangxin.apktoolbox.file.password.recovery.checker.IChecker;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BruteForceProxy implements ICategory {
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

    private String startAndGet(int numThreads, int passwordLength, IChecker checker, String charsSet, RecoveryPanel panel) {
        executorService = Executors.newFixedThreadPool(numThreads);
        bruteForceFuture = new BruteForceFuture(numThreads);
        BruteForceTaskParam param = new BruteForceTaskParam(numThreads, passwordLength, checker, charsSet);
        for (int taskId = 0; taskId < numThreads; taskId++) {
            BruteForceRunnable task = new BruteForceRunnable(taskId, param, bruteForceFuture, panel);
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

    @Override
    public void start(RecoveryPanel panel) {
        String charset = panel.getCharset();
        logger.info("Charset: {}", charset);
        if (StringUtils.isEmpty(charset)) {
            JOptionPane.showMessageDialog(panel, "Character set is empty!");
            return;
        }

        int minLength = panel.getMinLength();
        int maxLength = panel.getMaxLength();
        if (minLength > maxLength) {
            JOptionPane.showMessageDialog(panel, "Minimum length is bigger than maximum length!");
            return;
        }

        panel.setCurrentState(State.WORKING);
        String password = null;
        for (int length = minLength; length <= maxLength; length++) {
            if (panel.getCurrentState() != State.WORKING) {
                logger.info("Break because of state: {}", panel.getCurrentState());
                break;
            }
            panel.setProgressMaxValue((int) Math.pow(charset.length(), length));
            panel.setProgressBarValue(0);
            long startTime = System.currentTimeMillis();
            FileChecker fileChecker = panel.getCurrentFileChecker();
            int numThreads = getThreadCount(charset.length(), length, fileChecker.getMaxThreadNum());
            logger.info("[{}]Current attempt length: {}, thread number: {}", fileChecker, length, numThreads);
            password = startAndGet(numThreads, length, fileChecker, charset, panel);
            long endTime = System.currentTimeMillis();
            logger.info("Current attempt length: {}, Cost time: {}ms", length, (endTime - startTime));
            if (password != null) {
                break;
            }
        }
        panel.showResultWithDialog(password);
    }

    @Override
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

    private int getThreadCount(int charSetSize, int length, int maxThreadCount) {
        int result = 1;
        for (int i = 1; i <= length; i++) {
            result *= charSetSize;
            if (result >= maxThreadCount) {
                return maxThreadCount;
            }
        }
        return result;
    }
}
