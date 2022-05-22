package edu.jiangxin.apktoolbox.file.password.recovery.checker;

import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public abstract class FileChecker implements IChecker {
    protected File file;
    protected Logger logger;
    protected Configuration conf;

    public FileChecker() {
        logger = LogManager.getLogger(this.getClass().getSimpleName());
        conf = Utils.getConfiguration();
    }

    public void attachFile(File file) {
        this.file = file;
    }

    public int getMaxThreadNum() {
        return 1000;
    }

    public boolean prepareChecker() {
        return true;
    }

    @Override
    public String toString() {
        return getDescription();
    }

    /**
     * Return all supported file extensions in LOWERCASE, for example: ["zip", "7z", "rar"]
     *
     * @return All supported file extensions in LOWERCASE
     */
    public abstract String[] getFileExtensions();

    public abstract String getFileDescription();

    public abstract String getDescription();
}
