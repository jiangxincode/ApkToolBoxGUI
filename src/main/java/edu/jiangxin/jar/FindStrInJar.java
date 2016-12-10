package edu.jiangxin.jar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 寻找指定路径下jar包中含特定字符串的文件
 *
 */
public class FindStrInJar {

	private static final Logger LOGGER = Logger.getLogger(FindStrInJar.class);

	public String condition; // 查询的条件

	public ArrayList<String> jarFiles = new ArrayList<String>();

	public FindStrInJar() {
	}

	public FindStrInJar(String condition) {
		this.condition = condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public List<String> find(String dir, boolean recurse) {
		searchDir(dir, recurse);
		return this.jarFiles;
	}

	public List<String> getFilenames() {
		return this.jarFiles;
	}

	protected void searchDir(String dir, boolean recurse) {
		try {
			File d = new File(dir);
			if (!d.isDirectory()) {
				return;
			}
			File[] files = d.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (recurse && files[i].isDirectory()) {
					searchDir(files[i].getAbsolutePath(), true);
				} else {
					String filename = files[i].getAbsolutePath();
					if (filename.endsWith(".jar") || filename.endsWith(".zip")) {
						ZipFile zip = new ZipFile(filename);
						Enumeration<?> entries = zip.entries();
						while (entries.hasMoreElements()) {
							ZipEntry entry = (ZipEntry) entries.nextElement();

							String thisClassName = new StringBuffer(entry.getName().replace("/", ".")).toString();

							if (StringUtils.endsWithAny(thisClassName, ".class", ".xml", ".properties", "txt")) {

								BufferedReader r = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)));
								while (r.read() != -1) {
									String tempStr = r.readLine();
									if (null != tempStr && tempStr.indexOf(condition) > -1) {
										this.jarFiles.add(filename + "  --->  " + thisClassName);
										break;
									}
								}
							} else {

							}

						}

						zip.close();
					} else if (StringUtils.endsWithAny(filename, ".sh", ".xml", ".properties")) {

						findStrInPlainText(files[i], condition);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int findStrInPlainText(File filename, String findStr) throws IOException {

		int count = 0;
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));

		while (br.read() != -1) {
			String tempStr = br.readLine();
			if (null != tempStr && tempStr.indexOf(findStr) > -1) {
				LOGGER.info(filename.getAbsolutePath());
				count++;
			}
		}
		br.close();

		return count;

	}

}
