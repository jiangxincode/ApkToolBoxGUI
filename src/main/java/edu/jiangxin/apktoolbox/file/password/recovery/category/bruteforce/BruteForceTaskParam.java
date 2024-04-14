package edu.jiangxin.apktoolbox.file.password.recovery.category.bruteforce;

import edu.jiangxin.apktoolbox.file.password.recovery.checker.IChecker;

public class BruteForceTaskParam {
    final int numThreads;
    final int passwordLength;
    final long passwordRangeSize;
    final long passwordSubRangeSize;
    final IChecker checker;
    final String charsSet;

    public BruteForceTaskParam(int numThreads, int passwordLength, IChecker checker, String charsSet) {
        this.numThreads = numThreads;
        this.passwordLength = passwordLength;
        this.checker = checker;
        this.passwordRangeSize = (long) Math.pow(charsSet.length(), passwordLength);
        this.passwordSubRangeSize = (passwordRangeSize + numThreads - 1) / numThreads;
        this.charsSet = charsSet;
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public long getPasswordTotalRangeSize() {
        return passwordRangeSize;
    }

    public long getPasswordSubRangeSize() {
        return passwordSubRangeSize;
    }

    public IChecker getChecker() {
        return checker;
    }

    public String getCharsSet() {
        return charsSet;
    }
}

