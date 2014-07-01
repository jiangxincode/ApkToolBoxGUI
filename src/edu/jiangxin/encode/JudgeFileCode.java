/**
 * 文件编码探测，使用了第三方探测包cpdetector
 * http://cpdetector.sourceforge.net/
 * @author jiangxin
 **/
package edu.jiangxin.encode;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.File;
import java.nio.charset.Charset;

public class JudgeFileCode {
	public static String judge(String fileName) {
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		Charset charset = null;
		File file = new File(fileName);
		if(!file.exists()) {
			System.out.println("Can't find the file!");
			return null;
		}
		try {
			charset = detector.detectCodepage(file.toURI().toURL());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null) {
			return charset.name();
		} else {
			return null;
		}
		
	}

	public static void main(String[] args) {
		
		judge("temp/test.txt");
	}
}
