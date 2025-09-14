package edu.jiangxin.apktoolbox.utils;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.PropertiesBuilderParameters;
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

    private static String userConfigPath;

    private static String userPluginDirPath;

    private static FileBasedConfigurationBuilder<FileBasedConfiguration> builder;

    public static String getPluginDirPath() {
        return userPluginDirPath;
    }

    public static boolean checkAndInitEnvironment() {
        boolean ret = initUserDir();
        if (!ret) {
            return false;
        }
        SystemInfoUtils.logSystemInfo();
        return true;
    }

    private static boolean initUserDir() {
        String userHomePath = System.getProperty("user.home");
        String userDirPath;
        if (StringUtils.isEmpty(userHomePath)) {
            logger.error("user.home is empty");
            userDirPath = ".apktoolboxgui";
        } else {
            userDirPath = userHomePath + File.separator + ".apktoolboxgui";
        }
        File userDirFile = new File(userDirPath);
        if (!userDirFile.exists()) {
            logger.info("userDirFile does not exist");
            boolean ret = userDirFile.mkdir();
            if (!ret) {
                logger.error("mkdir failed: {}", userDirPath);
                return false;
            }
        }
        userConfigPath = userDirPath + File.separator + "apktoolboxgui.properties";
        File userConfigFile = new File(userConfigPath);
        if (!userConfigFile.exists()) {
            try {
                logger.info("userConfigFile does not exist");
                boolean ret = userConfigFile.createNewFile();
                if (!ret) {
                    logger.error("createNewFile fail: {}", userConfigPath);
                    return false;
                }
            } catch (IOException e) {
                logger.error("createNewFile fail", e);
            }
        }
        userPluginDirPath = userDirPath + File.separator + "plugins";
        File userPluginDirFile = new File(userPluginDirPath);
        if (!userPluginDirFile.exists()) {
            logger.info("userPluginDirFile does not exist");
            boolean ret = userPluginDirFile.mkdir();
            if (!ret) {
                logger.error("mkdir failed: {}", userPluginDirPath);
                return false;
            }
        }
        return true;
    }

    public static Configuration getConfiguration() {
        if (builder == null) {
            logger.info("builder is null, create it");
            File configFile = new File(userConfigPath);
            Parameters params = new Parameters();
            PropertiesBuilderParameters parameters = params.properties().setFile(configFile);
            builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                    .configure(parameters);
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

    public static void executor(String cmd, boolean isBlocked) {
        logger.info(cmd);
        try (ProcessLogOutputStream outStream = new ProcessLogOutputStream(logger, Level.INFO);
             ProcessLogOutputStream errStream = new ProcessLogOutputStream(logger, Level.ERROR)
        ) {
            CommandLine commandLine = CommandLine.parse(cmd);
            DefaultExecutor exec = DefaultExecutor.builder().get();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outStream, errStream);
            exec.setStreamHandler(streamHandler);
            if (isBlocked) {
                int exitValue = exec.execute(commandLine);
                logger.info("exitValue: [" + exitValue + "]");
            } else {
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
            }

        } catch (IOException ioe) {
            logger.error("exec fail: {}", ioe.getMessage());
            JOptionPane.showMessageDialog(null, ioe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
