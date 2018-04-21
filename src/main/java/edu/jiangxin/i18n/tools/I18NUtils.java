package edu.jiangxin.i18n.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class I18NUtils {
	private static final String sourceBaseStr = "D:\\temp\\i18nTest\\res1";

	private static final String targetBaseStr = "D:\\temp\\i18nTest\\res3";

	private static final String itemName = "storage_is_full";

	private static final String charset = "UTF-8";

	public static void main(String[] args) throws JDOMException, IOException {

		File sourceBaseFile = new File(sourceBaseStr);
		File targetBaseFile = new File(targetBaseStr);

		int count = 0;
		File[] sourceParentFiles = sourceBaseFile.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				return pathname.getName().startsWith("values");
			}
		});
		for (File sourceParentFile : sourceParentFiles) {
			File sourceFile = new File(sourceParentFile, "strings.xml");
			if (sourceFile.exists()) {

				Map<String, String> replace = new HashedMap<String, String>();

				replace.put("&quot;", "jiangxin001");
				replace.put("&#160;", "jiangxin002");

				SAXBuilder builder = new SAXBuilder();
				Document sourceDoc = builder.build(sourceFile);
				Element sourceRoot = sourceDoc.getRootElement();
				for (Element child : sourceRoot.getChildren()) {
					String value = child.getAttributeValue("name");
					if (value.equals(itemName)) {
						File targetParentFile = new File(targetBaseFile, sourceParentFile.getName());
						File targetFile = new File(targetParentFile, "strings.xml");
						System.out.println("count: " + (++count) + ", in path: " + sourceFile.getCanonicalPath()
								+ ", out path: " + targetFile.getCanonicalPath());
						if (targetFile.exists()) {
							prePocess(targetFile, replace);
							Document targetDoc = builder.build(targetFile);
							Element targetRoot = targetDoc.getRootElement();
							targetRoot.addContent("    ");
							targetRoot.addContent((Element) child.clone());
							targetRoot.addContent("\n");
							XMLOutputter out = new XMLOutputter();
							Format format = Format.getRawFormat();
							format.setEncoding("UTF-8");
							format.setLineSeparator("\n");
							out.setFormat(format);
							out.output(targetDoc, new FileOutputStream(targetFile));
							postProcess(targetFile, replace);
						} else {
							System.err.println("output file doesn/t exist.");
						}
						break;
					}
				}
			}
		}
	}

	private static void postProcess(File file, Map<String, String> map) throws IOException {
		String content = FileUtils.readFileToString(file, charset);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			content = content.replaceAll(entry.getValue(), entry.getKey());
		}
		FileUtils.writeStringToFile(file, content, charset);
	}

	private static void prePocess(File file, Map<String, String> map) throws IOException {
		String content = FileUtils.readFileToString(file, charset);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			content = content.replaceAll(entry.getKey(), entry.getValue());
		}
		FileUtils.writeStringToFile(file, content, charset);
	}
}
