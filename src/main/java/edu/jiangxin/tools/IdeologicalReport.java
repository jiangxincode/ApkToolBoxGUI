package edu.jiangxin.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

class Coordinate {
	private int count_article;
	private int count_paragraph;
	private int count_sentence;

	public Coordinate() {
		count_article = 0;
		count_paragraph = 0;
		count_sentence = 0;
	}

	public Coordinate(int count_article, int count_paragraph, int count_sentence) {
		this.count_article = count_article;
		this.count_paragraph = count_paragraph;
		this.count_sentence = count_sentence;
	}

	public int getCount_article() {
		return count_article;
	}

	public void setCount_article(int count_article) {
		this.count_article = count_article;
	}

	public int getCount_paragraph() {
		return count_paragraph;
	}

	public void setCount_paragraph(int count_paragraph) {
		this.count_paragraph = count_paragraph;
	}

	public int getCount_sentence() {
		return count_sentence;
	}

	public void setCount_sentence(int count_sentence) {
		this.count_sentence = count_sentence;
	}

	@Override
	public String toString() {
		return "" + count_article + " " + count_paragraph + " "
				+ count_sentence;
	}

}

public class IdeologicalReport {

	public static void main(String[] args) throws IOException {
		
		String input = "target/test-classes/IdeologicalReportTest/";
		String output = "target/test-classes/IdeologicalReportTest/result.txt";

		HashMap<Coordinate, String> hashMap = new HashMap<Coordinate, String>();

		File dataDir = new File(input);

		int i = 0, j = 0, k = 0;

		File[] dataFile = dataDir.listFiles();
		for (i = 0; i < dataFile.length; i++) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(dataFile[i]),"GBK"));
			String inputPargraph;
			j = 0;
			while ((inputPargraph = reader.readLine()) != null) {
				if (inputPargraph.equals("")) {
					continue;
				}
				j++;
				String[] inputSentence = inputPargraph.split("。");
				for (k = 0; k < inputSentence.length; k++) {
					Coordinate coordinate = new Coordinate(i, j, k);
					hashMap.put(coordinate, inputSentence[k]);
				}
			}

			reader.close();

		}

		for (Iterator<Entry<Coordinate, String>> it = hashMap.entrySet()
				.iterator(); it.hasNext();) {
			Entry<Coordinate, String> entry = it.next();
			System.out.println(entry.getKey().toString() + " "
					+ entry.getValue());
		}

		String separator = System.getProperty("line.separator")
				+ System.getProperty("line.separator");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(output))));
		writer.append("思想汇报").append(separator);
		writer.append("尊敬的党组织").append(separator);
		int count = 0;
		for (Iterator<Entry<Coordinate, String>> it = hashMap.entrySet()
				.iterator(); it.hasNext();) {
			Entry<Coordinate, String> entry = it.next();
			writer.append(entry.getValue()).append('。');
			count++;
			if (count == 10) {
				writer.append(separator);
				count = 0;
			}
		}
		writer.append(separator).append("申请人：XXX");
		Calendar calendar = Calendar.getInstance();
		String date = "" + calendar.get(Calendar.YEAR) + "年"
				+ calendar.get(Calendar.MONTH) + "月"
				+ calendar.get(Calendar.DATE) + "日";
		writer.append(System.getProperty("line.separator")).append(date);

		writer.close();

	}

}
