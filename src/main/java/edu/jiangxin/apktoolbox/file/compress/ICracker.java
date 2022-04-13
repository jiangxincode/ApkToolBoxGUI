package edu.jiangxin.apktoolbox.file.compress;

import java.io.File;

public interface ICracker {
    boolean prepareCracker();

    String crack(File file, CodeIterator codeIterator);
}
