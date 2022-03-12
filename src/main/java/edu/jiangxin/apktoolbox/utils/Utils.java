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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * @author jiangxin
 * @author 2018-08-19
 *
 */
public class Utils {
    private static final Logger logger = LogManager.getLogger(Utils.class);

    private static FileBasedConfigurationBuilder<FileBasedConfiguration> builder;

    public static String loadStream(InputStream in) {
        StringBuffer buffer = new StringBuffer();
        if (in == null) {
            logger.error("in is null");
            return buffer.toString();
        }

        BufferedInputStream bis = new BufferedInputStream(in);
        try {
            int ptr = 0;
            while ((ptr = bis.read()) != -1) {
                buffer.append((char) ptr);
            }
        } catch (IOException e) {
            logger.error("read bis error", e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    logger.error("bis close error", e);
                }
            }
            try {
                in.close();
            } catch (IOException e) {
                logger.error("in close error", e);
            }
        }
        return buffer.toString();
    }

    public static String getCurrentDateString() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            logger.error("getCurrentDateString error", e);
        }
        return null;
    }

    public static Configuration getConfiguration() {
        if (builder == null) {
            File confiFile = new File("apktoolboxgui.properties");
            if (!confiFile.exists()) {
                try {
                    logger.info("confiFile does not exist");
                    boolean ret = confiFile.createNewFile();
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
                    .configure(params.properties().setFileName("apktoolboxgui.properties"));
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
            logger.error("exec fail", ioe.getMessage());
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
            logger.error("exec fail", ioe.getMessage());
        }
    }

    public static <T> boolean isEleTypeOf(Collection<T> coll, Class<?> obj) {
        if(coll == null || coll.size() == 0) {
            return true;
        }

        for(T ele : coll) {
            if(!ele.getClass().equals(obj)) {
                return false;
            }
        }
        return true;
    }
}
