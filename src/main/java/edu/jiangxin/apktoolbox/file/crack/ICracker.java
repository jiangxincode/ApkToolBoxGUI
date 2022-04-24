package edu.jiangxin.apktoolbox.file.crack;

import java.util.List;

public interface ICracker {
    boolean prepareCracker();

    boolean checkPwd(String pwd);
}
