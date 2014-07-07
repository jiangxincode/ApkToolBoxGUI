package edu.jiangxin.encode;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.File;
import java.nio.charset.Charset;

/**
 * 编码探测检测器.
 * 使用了第三方的包:cpdetector<br>
 * http://cpdetector.sourceforge.net/<br>
 * 使用说明及包下载请查看官方介绍<br>
 * @author jiangxin
 */
public class EncoderDetector {
	
	/**
	 * 判断文件编码类型
	 * @param fileName
	 * @return 编码类型字符串
	 */
	public static String judgeFile(String fileName) {

		File file = new File(fileName);
		if (!file.exists()) {
			System.out.println("Can't find the file!");
			return null;
		}

		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();

		// first one returning non-null wins the decision
		detector.add(new ParsingDetector(false));
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());

		Charset charset = null;
		try {
			charset = detector.detectCodepage(file.toURI().toURL()); // f.toURL()已经废弃，建议通过toURI()间接转换
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

		String charset = judgeFile("temp/test.txt");
		System.out.println(charset);
	}
}
