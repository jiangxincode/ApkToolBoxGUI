package edu.jiangxin.apktoolbox.prop2xls;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Excel2PropertiesWithPOI {

	public static void main(String[] args) {

		HashMap<String, String> properties = new HashMap<String, String>();
		HSSFCell cell1 = null;
		HSSFCell cell2 = null;
		HSSFWorkbook workBook = null;

		try {
			workBook = new HSSFWorkbook(Excel2PropertiesWithPOI.class.getResourceAsStream("default_en_US.xls"));
			HSSFSheet sheet = workBook.getSheet("Properties");
			Iterator<Row> rowIterator = sheet.rowIterator();
			while (rowIterator.hasNext()) {
				HSSFRow row = (HSSFRow) rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					cell1 = (HSSFCell) cellIterator.next();
					String key = cell1.getRichStringCellValue().toString();
					if (!cellIterator.hasNext()) {
						System.out.println("No Such Element");
						String value = "";
						properties.put(key, value);
					} else {
						cell2 = (HSSFCell) cellIterator.next();
						String value = cell2.getRichStringCellValue().toString();
						properties.put(key, value);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				workBook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Properties props = new Properties();
		try {
			FileOutputStream xlsFos = new FileOutputStream(
					Excel2PropertiesWithPOI.class.getResource("").getPath() + "default_en_US_new.properties");
			Iterator<String> mapIterator = properties.keySet().iterator();
			while (mapIterator.hasNext()) {
				String key = mapIterator.next().toString();
				String value = properties.get(key);
				props.setProperty(key, value);
			}
			props.store(xlsFos, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}