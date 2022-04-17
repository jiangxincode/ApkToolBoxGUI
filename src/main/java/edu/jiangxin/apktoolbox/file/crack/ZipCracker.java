package edu.jiangxin.apktoolbox.file.crack;

import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.ProcessLogOutputStream;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public final class ZipCracker extends BaseCracker {
    private Logger logger;
    private Configuration conf;
    private String path;

    public ZipCracker() {
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

    //检查返回值来提升速度？
    //使用库来提升速度？
    //使用多线程来提升速度？
    //比较不同的方式来决定最终的方案
    @Override
    public boolean checkPwd(File file, String pwd) {
        String target = file.getAbsolutePath();
        String cmd = String.format("%s t %s -p%s", path, target, pwd);
        logger.info("checkPwd cmd: " + cmd);
        boolean result = false;
        try (ProcessLogOutputStream outStream = new ProcessLogOutputStream(logger, Level.INFO);
             ProcessLogOutputStream errStream = new ProcessLogOutputStream(logger, Level.ERROR)
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
            logger.error("exec fail");
        }
        return result;
    }

    @Override
    public String[] getFileExtension() {
        return new String[]{"zip"};
    }
}
