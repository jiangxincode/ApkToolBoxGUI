package edu.jiangxin.apktoolbox.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StreamHandler extends Thread {

	private static final Logger logger = LogManager.getLogger(StreamHandler.class);
	private InputStream inputStream;
	private int type;

	public StreamHandler(InputStream is, int type) {
		this.inputStream = is;
		this.type = type;
	}

	@Override
	public void run() {
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(inputStream, "UTF-8");
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				if (type == 0) {
					logger.info(line);
				} else {
					logger.error(line);
				}
			}
		} catch (IOException e) {
			logger.error("read bis error", e);
		} finally {
			try {
				br.close();
				isr.close();
			} catch (IOException ex) {
				logger.error("close error", ex);
			}
		}
	}
}