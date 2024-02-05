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
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author jiangxin
 * @author 2018-08-19
 *
 */
public class Utils {
    private static final String DOWNLOAD_VERSION = "v1.0.4";
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

    public static boolean checkAndDownloadPlugin(String pluginFilename, boolean isPluginNeedUnzip) {
        File pluginFile = new File(Utils.userPluginDirPath, pluginFilename);
        if (!pluginFile.exists()) {
            int userChoose = JOptionPane.showConfirmDialog(null, "未找到对应插件，是否下载", "提示", JOptionPane.YES_NO_OPTION);
            if (userChoose != JOptionPane.YES_OPTION) {
                logger.warn("userChoose: {}", userChoose);
                return false;
            }
            String downloadUrlStr = "https://gitee.com/jiangxinnju/apk-tool-box-gui-plugins/releases/download/" + DOWNLOAD_VERSION + "/" + pluginFilename;
            URL url;
            try {
                url = new URL(downloadUrlStr);
            } catch (MalformedURLException e) {
                logger.error("MalformedURLException: {}", e.getMessage());
                return false;
            }
            File pluginDir = new File(Utils.userPluginDirPath);
            boolean ret = FileUtils.downloadFileToDir(url, pluginDir);
            if (!ret) {
                JOptionPane.showMessageDialog(null, "下载失败，请检查网络", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (isPluginNeedUnzip) {
                ret = FileUtils.unzipFile(pluginFile);
                if (!ret) {
                    JOptionPane.showMessageDialog(null, "解压失败", "错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        return true;
    }
}
