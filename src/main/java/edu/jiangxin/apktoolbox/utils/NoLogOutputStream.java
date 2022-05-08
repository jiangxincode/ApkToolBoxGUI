package edu.jiangxin.apktoolbox.utils;

import org.apache.commons.exec.LogOutputStream;

public class NoLogOutputStream extends LogOutputStream {
    @Override
    protected void processLine(String line, int logLevel) {

    }
}
