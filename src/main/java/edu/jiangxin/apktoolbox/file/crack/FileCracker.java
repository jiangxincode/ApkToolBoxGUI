package edu.jiangxin.apktoolbox.file.crack;

import java.io.File;

public class FileCracker implements ICracker {
    protected File file;

    public FileCracker(File file) {
        this.file = file;
    }

    @Override
    public boolean prepareCracker() {
        return false;
    }

    @Override
    public boolean checkPwd(String pwd) {
        return false;
    }
}
