package edu.jiangxin.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.jiangxin.common.FileFilterWrapper;

public class RemoveHtmlElement {

	static final String charsetName = "UTF-8";
	// static final String[] divClassNames = { "Header", "aspNetHidden",
	// "Search", "clearAll", "Header" };
	// static final String[] ids = { "Header", "leftmenu" };
	private static final String[] classNames = {"siteTop","clearAll","logo","ajs-menu-bar"};
	private static final String[] ids = { "login-link","quick-search","footer","splitter-sidebar" };
	private static final String directoryName = "C:/aspose/www.aspose.com/docs/display/pdfjava";

	public static void main(String[] args) throws IOException {
		removeContent();

	}

	private static void removeContent() throws IOException {
		ArrayList<File> files = new FileFilterWrapper().list(directoryName, "htm");

		for (File file : files) {
			Document doc = Jsoup.parse(file, charsetName);
			for (int i = 0; i < classNames.length; i++) {
				Elements eles = doc.getElementsByClass(classNames[i]); // eles不可能为null
				for(int j=0;j<eles.size();j++) {
					Element ele = eles.get(j);
					if(ele.attr("hrep").startsWith("javascript:if(confirm")) {
						ele.remove();
					}
				}
				eles.remove();
			}
			for (int i = 0; i < ids.length; i++) {
				Element ele = doc.getElementById(ids[i]);
				if (ele != null) {
					ele.remove();
				}

			}

			Elements eles = doc.getElementsByTag("script");
			for (int i = 0; i < eles.size(); i++) {
				Element ele = eles.get(i);
				if (ele.attr("type").equals("text/javascript")) {
					ele.remove();
				}
			}

			FileOutputStream fos = new FileOutputStream(file, false);
			OutputStreamWriter osw = new OutputStreamWriter(fos, charsetName);
			osw.write(doc.html());
			osw.close();
			System.out.println(file.getAbsolutePath());
		}

	}



}
