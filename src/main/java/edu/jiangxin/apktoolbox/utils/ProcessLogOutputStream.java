package edu.jiangxin.apktoolbox.utils;

import org.apache.commons.exec.LogOutputStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

/**
 * Record output or errput of child process to Apache logging system
 */
public class ProcessLogOutputStream extends LogOutputStream {
    Logger logger;

    public ProcessLogOutputStream(Logger logger, Level logLevel) {
        super(logLevel.intLevel());
        this.logger = logger;
    }

    @Override
    protected void processLine(String line, int logLevel) {
        String message;
        if (logLevel == Level.INFO.intLevel()) {
            message = "Process output: [" + line + "]";
        } else {
            message = "Process errput: [" + line + "]";
        }
        logger.log(Level.valueOf(String.valueOf(logLevel)), message);
    }
}
