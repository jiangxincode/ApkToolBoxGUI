package edu.jiangxin.apktoolbox.file.crack;

import edu.jiangxin.apktoolbox.file.crack.cracker.StringCracker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PasswordCrackerMain {
    private static final String PASSWORD_CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static void main(String args[]) {
        if (args.length < 4) {
            System.out.println("Usage: PasswordCrackerMain numThreads passwordLength isEarlyTermination encryptedPassword");
            return;
        }
        
        int numThreads = Integer.parseInt(args[0]);
        int passwordLength = Integer.parseInt(args[1]);
        boolean isEarlyTermination = Boolean.parseBoolean(args[2]);
        String encryptedPassword = args[3];
        
        // If you want to know the ExecutorService,
        // refer to site; https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html
        // init pool to the fixed number of threads available
        ExecutorService  workerPool = Executors.newFixedThreadPool(numThreads);
        PasswordFuture passwordFuture = new PasswordFuture();
        PasswordCrackerConsts consts = new PasswordCrackerConsts(numThreads, passwordLength, new StringCracker(encryptedPassword), PASSWORD_CHARS);

        // Create PasswordCrackerTask and use executor
        // service to run in a separate thread
        for (int i = 0; i < numThreads; i++) {
            workerPool.execute(new PasswordCrackerTask(i, isEarlyTermination, consts, passwordFuture));
        }
        System.out.println("20175324");
        System.out.println(numThreads);
        System.out.println(passwordLength);
        System.out.println(isEarlyTermination);
        System.out.println(encryptedPassword);
        try {
            // wait till cracked and get found password
            System.out.println(passwordFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerPool.shutdown();
        }
    }
}


