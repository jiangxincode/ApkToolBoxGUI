package edu.jiangxin.apktoolbox.file.crack.cracker;

import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.NoLogOutputStream;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.IOException;

public final class RarUsingRarCracker extends FileCracker {
    private static final boolean DEBUG = false;
    private String toolPath;

    public RarUsingRarCracker() {
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
        return "RAR Cracker(Using Rar.exe)";
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
    public boolean checkPwd(String pwd) {
        String target = file.getAbsolutePath();
        String cmd = String.format("%s t -p%s %s", toolPath, pwd, target);
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
            result = (exitValue == 0);
        } catch (IOException e) {
        }
        return result;
    }
}
