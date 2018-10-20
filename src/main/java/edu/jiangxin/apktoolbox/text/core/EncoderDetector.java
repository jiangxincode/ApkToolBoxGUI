package edu.jiangxin.apktoolbox.text.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mozilla.universalchardet.UniversalDetector;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

public class EncoderDetector {
	private static Logger logger = LogManager.getLogger(EncoderDetector.class.getSimpleName());

	/**
	 * Detect the charset of some file
	 * @param fileName
	 * @return charset
	 */
	public static String judgeFile(String fileName) {

		File file = new File(fileName);
		if (!file.exists()) {
			logger.error("Can't find the file: " + fileName);
			return null;
		}

		final String[] detectorCharset = new String[2];

		CountDownLatch countDownLatch = new CountDownLatch(2);

		Thread cpDetectorThread = new Thread(new Runnable() {

			@Override
			public void run() {
				CodepageDetectorProxy cpDetectorProxy = CodepageDetectorProxy.getInstance();

				// first one returning non-null wins the decision
				cpDetectorProxy.add(new ParsingDetector(false));
				cpDetectorProxy.add(JChardetFacade.getInstance());
				cpDetectorProxy.add(ASCIIDetector.getInstance());
				cpDetectorProxy.add(UnicodeDetector.getInstance());
				Charset charset = null;
				try {
					charset = cpDetectorProxy.detectCodepage(file.toURI().toURL()); // f.toURL()已经废弃，建议通过toURI()间接转换
				} catch (IOException e) {
					logger.error("cpDetector failed", e);
					detectorCharset[0] = null;
				}
				if (charset != null) {
					detectorCharset[0] = charset.name();
				} else {
					detectorCharset[0] = null;
				}
				countDownLatch.countDown();
			}

		});

		Thread universalDetectorThread = new Thread(new Runnable() {

			@Override
			public void run() {
				UniversalDetector universalDetector = new UniversalDetector(null);
				byte[] buf = new byte[4096];
				FileInputStream fis;
				try {
					fis = new FileInputStream(file);
					int nread;
					while ((nread = fis.read(buf)) > 0 && !universalDetector.isDone()) {
						universalDetector.handleData(buf, 0, nread);
					}
					universalDetector.dataEnd();
					detectorCharset[1] = universalDetector.getDetectedCharset();
				} catch (IOException e) {
					logger.error("universalDetector failed", e);
					detectorCharset[1] = null;
				}
				countDownLatch.countDown();
			}

		});

		cpDetectorThread.start();
		universalDetectorThread.start();

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			logger.error("await failed", e);
			return null;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("cpDetector: ").append(detectorCharset[0]).append(", universalDetector: ").append(detectorCharset[1]);
		if (StringUtils.isEmpty(detectorCharset[0]) && StringUtils.isEmpty(detectorCharset[1])) {
			logger.warn(sb.toString());
			return null;
		} else if (StringUtils.isEmpty(detectorCharset[0]) && StringUtils.isNotEmpty(detectorCharset[1])) {
			logger.info(sb.toString());
			return detectorCharset[1];
		} else if (StringUtils.isNotEmpty(detectorCharset[0]) && StringUtils.isEmpty(detectorCharset[1])) {
			logger.info(sb.toString());
			return detectorCharset[0];
		} else if (detectorCharset[0].equals(detectorCharset[1])) {
			logger.info(sb.toString());
			return detectorCharset[1];
		} else {
			logger.warn(sb.toString());
			return detectorCharset[1];
		}
	}
}
