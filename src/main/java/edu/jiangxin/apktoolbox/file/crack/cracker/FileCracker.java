package edu.jiangxin.apktoolbox.file.crack.cracker;

import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public abstract class FileCracker implements ICracker {
    protected File file;
    protected Logger logger;
    protected Configuration conf;

    public FileCracker() {
        logger = LogManager.getLogger(this.getClass().getSimpleName());
        conf = Utils.getConfiguration();
    }

    public void attachFile(File file) {
        this.file = file;
    }

    public int getMaxThreadNum() {
        return 1000;
    }

    @Override
    public String toString() {
        return getDescription();
    }

    public abstract String[] getFileExtensions();

    public abstract String getFileDescription();

    public abstract String getDescription();
}
