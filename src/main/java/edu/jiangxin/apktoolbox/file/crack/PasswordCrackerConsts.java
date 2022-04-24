package edu.jiangxin.apktoolbox.file.crack;

class PasswordCrackerConsts {
    public static final String PASSWORD_CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";
    
    final int numThreads;
    final int passwordLength;
    final long passwordRangeSize;
    final long passwordSubRangeSize;
    final ICracker cracker;

    public PasswordCrackerConsts(int numThreads, int passwordLength, ICracker cracker) {
        this.numThreads = numThreads;
        this.passwordLength = passwordLength;
        this.cracker = cracker;
        passwordRangeSize = (long) Math.pow(PASSWORD_CHARS.length(), passwordLength);
        passwordSubRangeSize = (passwordRangeSize + numThreads - 1) / numThreads;
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

    public ICracker getCracker() {
        return cracker;
    }
}

