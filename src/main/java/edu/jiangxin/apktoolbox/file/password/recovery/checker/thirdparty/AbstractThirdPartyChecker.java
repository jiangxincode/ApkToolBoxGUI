package edu.jiangxin.apktoolbox.file.password.recovery.checker.thirdparty;

import edu.jiangxin.apktoolbox.file.password.recovery.checker.FileChecker;
import edu.jiangxin.apktoolbox.utils.NoLogOutputStream;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.IOException;

public abstract class AbstractThirdPartyChecker extends FileChecker {
    protected static final boolean DEBUG = false;

    protected String toolPath;

    @Override
    public boolean prepareChecker() {
        toolPath = getToolPath();
        if (toolPath == null) {
            logger.error("7z.exe not found");
            return false;
        }
        try {
            Runtime.getRuntime().exec(toolPath);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkPassword(String password) {
        if (isFiltered(password)) {
            logger.warn("password is filtered.");
            return false;
        }

        if (password != null && password.contains("\"")) {
            // It is useless to escape the password
            logger.warn("checkPassword password contain double quote characters[Not Supported]");
            return false;
        }
        String cmd = getCmd(password);
        logger.debug("checkPassword cmd: {}", cmd);
        boolean result = false;
        try (NoLogOutputStream outStream = new NoLogOutputStream();
             NoLogOutputStream errStream = new NoLogOutputStream()
        ) {
            CommandLine commandLine = CommandLine.parse(cmd);
            DefaultExecutor exec = DefaultExecutor.builder().get();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outStream, errStream);
            exec.setStreamHandler(streamHandler);
            int exitValue = exec.execute(commandLine);
            result = (exitValue == 0);
        } catch (IOException e) {
            if (DEBUG) {
                logger.error("checkPassword: IOException");
            }
        }
        return result;
    }

    public abstract String getToolPath();

    public abstract boolean isFiltered(String password);

    public abstract String getCmd(String password);
}
