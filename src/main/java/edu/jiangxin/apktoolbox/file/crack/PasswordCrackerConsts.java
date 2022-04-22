package edu.jiangxin.apktoolbox.file.crack;

class PasswordCrackerConsts {
    public static final String PASSWORD_CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";
    
    final int numThreads;
    final int passwordLength;
    final long passwordRangeSize;
    final long passwordSubRangeSize;
    final String encryptedPassword;
    
    public PasswordCrackerConsts(int numThreads, int passwordLength, String encryptedPassword) {
        this.numThreads = numThreads;
        this.passwordLength = passwordLength;
        this.encryptedPassword = encryptedPassword;
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
    
    public String getEncryptedPassword() {
        return encryptedPassword;
    }
}

