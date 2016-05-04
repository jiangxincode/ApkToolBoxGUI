package edu.jiangxin.jar;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 寻找指定路径下jar包中含特定字符串的文件
 * @author 千年独步
 */
public class FindStrInJar {

	public String condition; //查询的条件

	public ArrayList<String> jarFiles = new ArrayList<String>();

	public FindStrInJar() {
	}

	public FindStrInJar(String condition) {
		this.condition = condition;
	}

	public FindStrInJar(String condition, String exclude) {
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

	protected String getClassName(ZipEntry entry) {
		StringBuffer className = new StringBuffer(entry.getName().replace("/", "."));
		return className.toString();
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
						Enumeration entries = zip.entries();
						while (entries.hasMoreElements()) {
							ZipEntry entry = (ZipEntry) entries.nextElement();
							String thisClassName = getClassName(entry);

							//不搜索扩展名为.class的文件
							if (thisClassName.lastIndexOf(".class") == -1) {
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
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		FindStrInJar findInJar = new FindStrInJar("append"); //要寻找的字符串
		List<String> jarFiles = findInJar.find("D:/other_workspace/idtag4/idtag-API/WebContent/WEB-INF/lib", true);
		if (jarFiles.size() == 0) {
			System.out.println("Not Found");
		} else {
			for (int i = 0; i < jarFiles.size(); i++) {
				System.out.println(jarFiles.get(i));
			}
		}
	}

}
