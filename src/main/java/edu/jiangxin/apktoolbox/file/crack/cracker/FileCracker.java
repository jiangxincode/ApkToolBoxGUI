package edu.jiangxin.apktoolbox.file.crack.cracker;

import java.io.File;

public abstract class FileCracker implements ICracker {
    protected File file;

    public FileCracker(File file) {
        this.file = file;
    }
}
