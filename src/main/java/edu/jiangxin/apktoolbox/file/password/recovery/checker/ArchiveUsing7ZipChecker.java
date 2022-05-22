package edu.jiangxin.apktoolbox.file.password.recovery.checker;

import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.NoLogOutputStream;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.IOException;

public final class ArchiveUsing7ZipChecker extends FileChecker {
    private static final boolean DEBUG = false;
    private String toolPath;

    public ArchiveUsing7ZipChecker() {
        super();
        toolPath = conf.getString(Constants.SEVEN_ZIP_PATH_KEY);
    }

    @Override
    public String[] getFileExtensions() {
        return new String[]{"7z", "zip", "rar", "gz", "tar", "xz", "z"};
    }

    @Override
    public String getFileDescription() {
        return "*.7z; *.zip; *.rar; ...";
    }

    @Override
    public String getDescription() {
        return "Archive Checker(Using 7z.exe)";
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
        String cmd = String.format("%s t \"%s\" -p\"%s\"", toolPath, target, password);
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
