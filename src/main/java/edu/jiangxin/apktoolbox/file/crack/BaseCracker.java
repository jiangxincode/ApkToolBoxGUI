package edu.jiangxin.apktoolbox.file.crack;

import java.io.File;
import java.util.List;

public class BaseCracker implements ICracker {
    @Override
    public boolean prepareCracker() {
        return false;
    }

    @Override
    public String getPwd(File file, List<CodeIterator> codeIterators) {
        for (CodeIterator codeIterator : codeIterators) {
            String pass = getPwd(file, codeIterator);
            if (pass != null) {
                return pass;
            }
        }
        return null;
    }

    @Override
    public String getPwd(File file, CodeIterator codeIterator) {
        String pass = codeIterator.nextCode();
        while (pass != null) {
            boolean isHit = checkPwd(file, pass);
            if (isHit) {
                return pass;
            } else {
                pass = codeIterator.nextCode();
            }
        }
        return null;
    }

    @Override
    public boolean checkPwd(File file, String pwd) {
        return false;
    }

    @Override
    public String[] getFileExtension() {
        return null;
    }
}
