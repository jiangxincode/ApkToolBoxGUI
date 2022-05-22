package edu.jiangxin.apktoolbox.file.password.recovery.checker;

import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.NoLogOutputStream;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.IOException;

public class ArchiveUsingWinRarChecker extends FileChecker {
    private static final boolean DEBUG = false;
    private String toolPath;

    public ArchiveUsingWinRarChecker() {
        super();
        toolPath = conf.getString(Constants.WIN_RAR_PATH_KEY);
    }

    @Override
    public String[] getFileExtensions() {
        return new String[]{"rar", "zip", "7z", "arj", "bz2", "cab", "gz", "iso", "jar", "lz", "lzh", "tar", "uue", "xz", "z", "zst"};
    }

    @Override
    public String getFileDescription() {
        return "*.rar; *.zip; *.7z; ...";
    }

    @Override
    public String getDescription() {
        return "Archive Checker(Using WinRar.exe)";
    }

    @Override
    public int getMaxThreadNum() {
        return 5;
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
        String cmd = String.format("%s t -inul -ibck -p%s \"%s\"", toolPath, password, target);
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
