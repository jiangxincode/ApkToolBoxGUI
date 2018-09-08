package edu.jiangxin.apktoolbox.i18n.core;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class GetLongestLanguage {
	private static final String sourceBaseStr = "D:\\temp\\i18nTest\\res1";
	private static final String itemName = "storage_is_full";

	public static void main(String[] args) throws JDOMException, IOException {

		File sourceBaseFile = new File(sourceBaseStr);
		List<I18NInfo> infos = new ArrayList<I18NInfo>();

		for (File sourceParentFile : sourceBaseFile.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				return pathname.getName().startsWith("values");
			}
		})) {
			File sourceFile = new File(sourceParentFile, "strings.xml");
			if (sourceFile.exists()) {

				SAXBuilder builder = new SAXBuilder();
				Document sourceDoc = builder.build(sourceFile);
				Element sourceRoot = sourceDoc.getRootElement();
				for (Element child : sourceRoot.getChildren()) {
					String value = child.getAttributeValue("name");
					if (value != null && value.equals(itemName)) {
						String text = child.getText();
						if (text != null) {
							I18NInfo info = new I18NInfo(sourceFile.getCanonicalPath(), text, text.length());
							infos.add(info);
							break;
						}
					}
				}

			}

		}
		Collections.sort(infos, new Comparator<I18NInfo>() {
			public int compare(I18NInfo o1, I18NInfo o2) {
				return o1.length - o2.length;
			}
		});

		for (I18NInfo info : infos) {
			System.out.println("length: " + info.length + ", text: " + info.text + ", path: " + info.path);
		}

	}

}

class I18NInfo {
	String path;
	String text;
	int length;

	public I18NInfo(String path, String text, int length) {
		this.path = path;
		this.text = text;
		this.length = length;
	}

}
