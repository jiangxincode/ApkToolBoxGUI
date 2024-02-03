package edu.jiangxin.apktoolbox.utils;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.exec.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * @author jiangxin
 * @author 2018-08-19
 *
 */
public class Utils {
    private static final Logger logger = LogManager.getLogger(Utils.class.getSimpleName());

    private static String userConfigDirPath;

    private static FileBasedConfigurationBuilder<FileBasedConfiguration> builder;

    public static boolean checkAndInitEnvironment() {
        boolean ret = initUserConfigDir();
        if (!ret) {
            return false;
        }
        SystemInfoUtils.logSystemInfo();
        return true;
    }

    private static boolean initUserConfigDir() {
        String userHomePath = System.getProperty("user.home");
        if (StringUtils.isEmpty(userHomePath)) {
            logger.error("user.home is empty");
            userConfigDirPath = ".apktoolboxgui";
        } else {
            userConfigDirPath = userHomePath + File.separator + ".apktoolboxgui";
        }
        File userConfigDirFile = new File(userConfigDirPath);
        if (!userConfigDirFile.exists()) {
            boolean ret = userConfigDirFile.mkdir();
            if (!ret) {
                logger.error("mkdir fail");
                return false;
            }
        }
        return true;
    }

    public static Configuration getConfiguration() {
        if (builder == null) {
            File configFile = new File(userConfigDirPath, "apktoolboxgui.properties");
            if (!configFile.exists()) {
                try {
                    logger.info("confiFile does not exist");
                    boolean ret = configFile.createNewFile();
                    if (!ret) {
                        logger.error("createNewFile fail");
                    }
                } catch (IOException e) {
                    logger.error("createNewFile fail", e);
                }
            }
            logger.info("builder is null, create it");
            Parameters params = new Parameters();
            builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                    .configure(params.properties().setFile(configFile));
        }
        Configuration conf = null;
        try {
            conf = builder.getConfiguration();
        } catch (ConfigurationException e) {
            logger.error("getConfiguration error", e);
        }
        return conf;
    }

    public static void saveConfiguration() {
        try {
            if (builder == null) {
                logger.info("builder is null");
                return;
            }
            builder.save();
            logger.info("saveConfiguration success");
        } catch (ConfigurationException e) {
            logger.error("saveConfiguration error", e);
        }
    }

    public static String getToolsPath() {
        String tmp = Utils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (StringUtils.isEmpty(tmp)) {
            logger.error("tmp is empty");
            return null;
        }
        File file = new File(tmp);
        if (!file.exists()) {
            logger.error("file does not exist");
            return null;
        }
        while (file.getParentFile().exists()) {
            File parent = file.getParentFile();
            File tools = new File(parent, "tools");
            if (tools.exists()) {
                try {
                    return tools.getCanonicalPath();
                } catch (IOException e) {
                    logger.error("getCanonicalPath fail");
                    return null;
                }
            }
            file = file.getParentFile();
        }

        return null;

    }
    
    public static String getFrameTitle(JComponent component) {
        Container container = component.getParent();
        while(container != null) {
            if (container instanceof JFrame) {
                return ((JFrame) container).getTitle();
            }
            container = container.getParent();
        }
        return "";
    }
    
    public static void setFrameTitle(JComponent component, String title) {
        Container container = component.getParent();
        while(container != null) {
            if (container instanceof JFrame) {
                ((JFrame) container).setTitle(title);
                return;
            }
            container = container.getParent();
        }
    }

    public static void blockedExecutor(String cmd) {
        logger.info(cmd);
        try (ProcessLogOutputStream outStream = new ProcessLogOutputStream(logger, Level.INFO);
             ProcessLogOutputStream errStream = new ProcessLogOutputStream(logger, Level.ERROR)
        ) {
            CommandLine commandLine = CommandLine.parse(cmd);
            DefaultExecutor exec = new DefaultExecutor();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outStream, errStream);
            exec.setStreamHandler(streamHandler);
            int exitValue = exec.execute(commandLine);
            logger.info("exitValue: [" + exitValue + "]");
        } catch (IOException ioe) {
            logger.error("exec fail. ", ioe);
        }
    }

    public static void unBlockedExecutor(String cmd) {
        logger.info(cmd);
        try (ProcessLogOutputStream outStream = new ProcessLogOutputStream(logger, Level.INFO);
             ProcessLogOutputStream errStream = new ProcessLogOutputStream(logger, Level.ERROR)
        ) {
            CommandLine commandLine = CommandLine.parse(cmd);
            DefaultExecutor exec = new DefaultExecutor();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outStream, errStream);
            exec.setStreamHandler(streamHandler);
            DefaultExecuteResultHandler erh = new DefaultExecuteResultHandler() {
                @Override
                public void onProcessComplete(int exitValue) {
                    logger.info("complete: [" + cmd + "], exitValue: [" + exitValue + "]");
                }

                @Override
                public void onProcessFailed(ExecuteException ee) {
                    logger.info("failed: [" + cmd + "], execute exception: [" + ee.getMessage() + "]");
                }
            };
            exec.execute(commandLine, erh);
        } catch (IOException ioe) {
            logger.error("exec fail. ", ioe);
        }
    }

    public static int getFileLineCount(File file) {
        try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file))) {
            while (lineNumberReader.skip(Long.MAX_VALUE) > 0) {
                logger.info("getFileLineCount skip " + Long.MAX_VALUE + " characters");
            }
            return lineNumberReader.getLineNumber();
        } catch (IOException e) {
            logger.error("getFileLineCount IOException");
            return 0;
        }
    }
}
