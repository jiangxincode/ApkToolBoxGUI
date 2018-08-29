package edu.jiangxin.apktoolbox.utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utils {
	private static final Logger logger = LogManager.getLogger(Utils.class);

	public static void setJFrameCenterInScreen(JFrame jFrame) {
		int windowWidth = jFrame.getWidth();
		int windowHeight = jFrame.getHeight();
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		jFrame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);//设置窗口居中显示 
	}

	public static String loadStream(InputStream in) throws IOException {
		int ptr = 0;
		in = new BufferedInputStream(in);
		StringBuffer buffer = new StringBuffer();
		while ((ptr = in.read()) != -1) {
			buffer.append((char) ptr);
		}
		return buffer.toString();
	}

	public static String getCurrentDateString() {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			return dateFormat.format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getString(String key, String defaultValue) {
		String ret = null;
		try {
			String configPath;
			String userProfilePath = System.getenv("USERPROFILE") + File.separator + "apktoolboxgui.properties";
			File userProfile = new File(userProfilePath);
			if (userProfile.exists()) {
				configPath = userProfile.getCanonicalPath();
				logger.info("user config exists: " + configPath);
				FileReader reader = new FileReader(configPath);
				PropertiesConfiguration conf = new PropertiesConfiguration();
				conf.read(reader);
				ret = conf.getString(key, defaultValue);
			} else {
				return defaultValue;
			}
		} catch (ConfigurationException | IOException e1) {
			logger.error("getString faild: " + e1.getMessage());
			ret = defaultValue;
		}
		return ret;
	}

	public static void setString(String key, String value) {
		FileWriter writer = null;
		try {
			String configPath;
			String userProfilePath = System.getenv("USERPROFILE") + File.separator + "apktoolboxgui.properties";
			File userProfile = new File(userProfilePath);
			configPath = userProfile.getCanonicalPath();
			logger.info("user config exists: " + configPath);
			writer = new FileWriter(configPath);
			PropertiesConfiguration conf = new PropertiesConfiguration();
			conf.setProperty(key, value);
			conf.write(writer);
		} catch (ConfigurationException | IOException e1) {
			logger.error("getString faild: " + e1.getMessage());
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error("writer close failed.");
				}
			}
		}
	}
}
