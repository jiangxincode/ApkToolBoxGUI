package edu.jiangxin.apktoolbox.file.crack.cracker;

public interface ICracker {
    boolean prepareCracker();

    boolean checkPwd(String pwd);
}
