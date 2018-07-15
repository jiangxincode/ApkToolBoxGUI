package edu.jiangxin.i18n.tools;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class I18NUtils {
	private static final String sourceBaseStr = "D:\\temp\\Java\\I18NTools\\src\\test\\resources\\res1";

	private static final String[] targetBaseStrs = new String[] {
			"D:\\temp\\Java\\I18NTools\\src\\test\\resources\\res3" };

	private static final String[] itemNames = new String[] { "storage_is_full" };

	private static final String charset = "UTF-8";

	private static final Logger logger = LogManager.getLogger(I18NUtils.class);

	private static final boolean isReplace = true;

	private static final boolean isRemoveLastLF = true;

	private static Map<String, String> replace = new HashedMap<String, String>();

	static {
		replace.put("&quot;", "jiangxin001");
		replace.put("&#160;", "jiangxin002");
	}

	public static void main(String[] args) throws JDOMException, IOException {

		for (String targetBaseStr : targetBaseStrs) {
			for (String itemName : itemNames) {
				innerProcessor(targetBaseStr, itemName);
			}
		}
	}

	private static void innerProcessor(String targetBaseStr, String itemName)
			throws JDOMException, IOException, FileNotFoundException {
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
				SAXBuilder builder = new SAXBuilder();
				Document sourceDoc = builder.build(sourceFile);
				Element sourceRoot = sourceDoc.getRootElement();
				for (Element sourceChild : sourceRoot.getChildren()) {
					String sourceValue = sourceChild.getAttributeValue("name");
					if (sourceValue != null && sourceValue.equals(itemName)) {

						File targetParentFile = new File(targetBaseFile, sourceParentFile.getName());
						File targetFile = new File(targetParentFile, "strings.xml");
						logger.info("count: " + (++count) + ", in path: " + sourceFile.getCanonicalPath()
								+ ", out path: " + targetFile.getCanonicalPath());
						if (targetFile.exists()) {
							prePocess(targetFile);
							Document targetDoc = builder.build(targetFile);
							Element targetRoot = targetDoc.getRootElement();
							boolean isFinished = false;

							for (Element targetChild : targetRoot.getChildren()) {
								String targetValue = targetChild.getAttributeValue("name");
								if (targetValue != null && targetValue.equals(itemName)) {
									if (isReplace) {
										targetChild.setText(sourceChild.getText());
									}
									isFinished = true;
									break;
								}
							}

							if (!isFinished) {
								targetRoot.addContent("    ");
								targetRoot.addContent((Element) sourceChild.clone());
								targetRoot.addContent("\n");
							}

							XMLOutputter out = new XMLOutputter();
							Format format = Format.getRawFormat();
							format.setEncoding("UTF-8");
							format.setLineSeparator("\n");
							out.setFormat(format);
							out.output(targetDoc, new FileOutputStream(targetFile));
							postProcess(targetFile);
						} else {
							logger.error("output file doesn/t exist.");
						}
						break;
					}
				}
			}
		}
	}

	private static void prePocess(File file) throws IOException {
		String content = FileUtils.readFileToString(file, charset);
		for (Map.Entry<String, String> entry : replace.entrySet()) {
			content = content.replaceAll(entry.getKey(), entry.getValue());
		}
		FileUtils.writeStringToFile(file, content, charset);
	}

	private static void postProcess(File file) throws IOException {
		String content = FileUtils.readFileToString(file, charset);
		for (Map.Entry<String, String> entry : replace.entrySet()) {
			content = content.replaceAll(entry.getValue(), entry.getKey());
		}
		if (isRemoveLastLF) {
			content = StringUtils.removeEnd(content, "\n");
		}
		FileUtils.writeStringToFile(file, content, charset);
	}
}
