package edu.jiangxin.apktoolbox.file.crack.cracker;

import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.NoLogOutputStream;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.IOException;

public final class ZipUsing7ZipCracker extends FileCracker {
    private static final boolean DEBUG = false;
    private String toolPath;

    public ZipUsing7ZipCracker() {
        super();
        toolPath = conf.getString(Constants.SEVEN_ZIP_PATH_KEY);
    }

    @Override
    public String[] getFileExtensions() {
        return new String[]{"zip"};
    }

    @Override
    public String getFileDescription() {
        return "*.zip";
    }

    @Override
    public String getDescription() {
        return "ZIP Cracker(Using 7z.exe)";
    }

    @Override
    public boolean prepareCracker() {
        try {
            Runtime.getRuntime().exec(toolPath);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkPassword(String password) {
        String target = file.getAbsolutePath();
        String cmd = String.format("%s t \"%s\" -p%s", toolPath, target, password);
        if (DEBUG) {
            logger.info("checkPassword cmd: " + cmd);
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
            result = (exitValue == 0);
        } catch (IOException e) {
        }
        return result;
    }
}
