package edu.jiangxin.apktoolbox.file.crack;

import java.io.File;
import java.util.List;

public interface ICracker {
    boolean prepareCracker();

    String getPwd(File file, List<CodeIterator> codeIterator);

    String getPwd(File file, CodeIterator codeIterator);

    boolean checkPwd(File file, String pwd);

    String[] getFileExtension();
}
