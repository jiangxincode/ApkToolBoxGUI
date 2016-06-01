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
	static final String[] divClassNames = { "Header", "aspNetHidden", "Search", "clearAll", "Header" };
	static final String[] divIds = { "Header", "leftmenu" };

	public static void main(String[] args) throws IOException {
		ArrayList<File> files = new FileFilterWrapper().list("C:/aspose_pdf/a", "htm");
		for (File file : files) {
			Document doc = Jsoup.parse(file, charsetName);
			for (int i = 0; i < divClassNames.length; i++) {
				Elements eles = doc.getElementsByClass(divClassNames[i]); // eles不可能为null

				eles.remove();
			}
			for (int i = 0; i < divIds.length; i++) {
				Element ele = doc.getElementById(divIds[i]);
				if (ele != null) {
					ele.remove();
				}

			}

			Elements eles = doc.getElementsByTag("script");
			for (int i = 0; i < eles.size(); i++) {
				Element ele = eles.get(i);
				if (ele.attr("language").equals("javascript") && ele.attr("type").equals("text/javascript")) {
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
