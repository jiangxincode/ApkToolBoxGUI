package edu.jiangxin.apktoolbox.prop2xls;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;

public class Properties2ExcelWithPOI {

	public static void main(String[] args) {
		Properties properties = new OrderedProperties();
		System.setProperty("file.encoding", "UTF-8");

		try {

			InputStream is = Properties2ExcelWithPOI.class.getResourceAsStream("default_en_US.properties");
			BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF8"));
			properties.load(in);
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		HSSFWorkbook workBook = null;

		FileOutputStream fosExcel = null;

		try {
			workBook = new HSSFWorkbook();

			HSSFSheet worksheet = workBook.createSheet("Properties");

			HSSFCellStyle cellStyle = workBook.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			HSSFRow row = worksheet.createRow((short) 0);

			HSSFCell cell1 = row.createCell(0);
			cell1.setCellValue(new HSSFRichTextString("Keys"));
			cell1.setCellStyle(cellStyle);

			HSSFCell cell2 = row.createCell(1);
			cell2.setCellValue(new HSSFRichTextString("Values"));
			cell2.setCellStyle(cellStyle);

			Enumeration<Object> keysEnum = properties.keys();

			while (keysEnum.hasMoreElements()) {
				String propKey = (String) keysEnum.nextElement();
				String propValue = properties.getProperty(propKey);

				HSSFRow rowOne = worksheet.createRow(worksheet.getLastRowNum() + 1);

				HSSFCell cellZero = rowOne.createCell(0);
				HSSFCell cellOne = rowOne.createCell(1);

				cellZero.setCellValue(new HSSFRichTextString(propKey));
				cellOne.setCellValue(new HSSFRichTextString(propValue));
			}
			fosExcel = new FileOutputStream(
					Properties2ExcelWithPOI.class.getResource("").getPath() + "default_en_US_new.xls");
			workBook.write(fosExcel);
			fosExcel.flush();
			fosExcel.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (fosExcel != null) {
				try {
					fosExcel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (workBook != null) {
				try {
					workBook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
