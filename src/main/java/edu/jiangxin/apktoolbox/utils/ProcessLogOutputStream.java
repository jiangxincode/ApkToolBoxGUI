package edu.jiangxin.apktoolbox.utils;

import org.apache.commons.exec.LogOutputStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

/**
 * Record output or errput of child process to Apache logging system
 */
public class ProcessLogOutputStream extends LogOutputStream {
    Logger logger;
    Level level;

    public ProcessLogOutputStream(Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
    }

    @Override
    protected void processLine(String line, int logLevel) {
        logger.log(level, "[" + line + "]");
    }
}
