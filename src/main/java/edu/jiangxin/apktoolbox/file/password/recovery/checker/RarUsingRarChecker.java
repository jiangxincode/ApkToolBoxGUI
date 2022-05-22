package edu.jiangxin.apktoolbox.file.password.recovery.checker;

import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.NoLogOutputStream;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.IOException;

/**
 * The RAR command line supports a larger number of functions when compared to WinRAR, but does not support ZIP and other formats.
 * https://www.win-rar.com/cmd-shell-mode.html
 */
public final class RarUsingRarChecker extends FileChecker {
    private static final boolean DEBUG = false;
    private String toolPath;

    public RarUsingRarChecker() {
        super();
        toolPath = conf.getString(Constants.RAR_PATH_KEY);
    }

    @Override
    public String[] getFileExtensions() {
        return new String[]{"rar"};
    }

    @Override
    public String getFileDescription() {
        return "*.rar";
    }

    @Override
    public String getDescription() {
        return "RAR Checker(Using Rar.exe)";
    }

    @Override
    public boolean prepareChecker() {
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
        String cmd = String.format("%s t -p\"%s\" \"%s\"", toolPath, password, target);
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
