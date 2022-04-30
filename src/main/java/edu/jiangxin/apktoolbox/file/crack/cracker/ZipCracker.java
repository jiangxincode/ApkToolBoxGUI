package edu.jiangxin.apktoolbox.file.crack.cracker;

import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.NoLogOutputStream;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public final class ZipCracker extends FileCracker {
    private static final boolean DEBUG = false;
    private Logger logger;
    private Configuration conf;
    private String path;

    public ZipCracker(File file) {
        super(file);
        logger = LogManager.getLogger(this.getClass().getSimpleName());
        conf = Utils.getConfiguration();
        path = conf.getString(Constants.SEVEN_ZIP_PATH_KEY);
    }

    @Override
    public boolean prepareCracker() {
        try {
            Runtime.getRuntime().exec(path);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkPwd(String pwd) {
        String target = file.getAbsolutePath();
        String cmd = String.format("%s t %s -p%s", path, target, pwd);
        if (DEBUG) {
            logger.info("checkPwd cmd: " + cmd);
        }
        boolean result = false;
        try (NoLogOutputStream outStream = new NoLogOutputStream();
             NoLogOutputStream errStream = new NoLogOutputStream()
        ) {
            CommandLine commandLine = CommandLine.parse(cmd);
            DefaultExecutor exec = new DefaultExecutor();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outStream, errStream);
            exec.setStreamHandler(streamHandler);
            int exitValue = exec.execute(commandLine);
            logger.info("exitValue: [" + exitValue + "]");
            if (exitValue == 0) {
                result = true;
            }
        } catch (IOException ioe) {
        }
        return result;
    }
}
