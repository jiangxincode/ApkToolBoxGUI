package edu.jiangxin.i18n.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jdom2.JDOMException;

public class I18NRemoveItem {
	private static final String sourceBaseStr = "D:\\temp\\i18nTest\\res1";

	private static final String targetBaseStr = "D:\\temp\\i18nTest\\res2";

	private static final String itemName = "storage_is_full";

	private static final String charset = "UTF-8";

	public static void main(String[] args) throws JDOMException, IOException {

		File sourceBaseFile = new File(sourceBaseStr);
		File targetBaseFile = new File(targetBaseStr);

		for (File sourceParentFile : sourceBaseFile.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				return pathname.getName().startsWith("values");
			}
		})) {
			File sourceFile = new File(sourceParentFile, "strings.xml");
			if (sourceFile.exists()) {
				System.out.println("read from: " + sourceFile.getCanonicalPath());
				String content = FileUtils.readFileToString(sourceFile, charset);
				String resultString = content.replaceAll("(?i)<string name=\"" + itemName + "\".*>.*</string>", "");
				File targetParentFile = new File(targetBaseFile, sourceParentFile.getName());
				File targetFile = new File(targetParentFile, "strings.xml");
				if (targetFile.exists()) {
					System.out.println("write to: " + targetFile.getCanonicalPath());
					FileUtils.writeStringToFile(targetFile, resultString, "UTF-8");
				} else {
					System.err.println("targetFile doesn't exist: " + targetFile.getCanonicalPath());
				}

			}
		}
	}

}
