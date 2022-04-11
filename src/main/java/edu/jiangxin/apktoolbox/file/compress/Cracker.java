package edu.jiangxin.apktoolbox.file.compress;

import java.io.File;

public interface Cracker {
    boolean isCrackerReady();

    String crack(File file, CodeIterator codeIterator);
}
