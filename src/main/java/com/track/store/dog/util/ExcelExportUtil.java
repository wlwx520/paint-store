package com.track.store.dog.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.track.paint.core.Definiens;
import com.track.store.dog.bean.Record;

public class ExcelExportUtil {
	private ExcelExportUtil() {
	}

	private XSSFWorkbook workbook = new XSSFWorkbook();

	public static ExcelExportUtil createXlsx() {
		return new ExcelExportUtil();
	}

	public void writeRecord(Record record) {
		String partner = record.getPartner();
		XSSFSheet sheet = workbook.getSheet(partner);
		if (sheet == null) {
			sheet = workbook.createSheet(partner);
			createHeadInSheet(sheet);
		}

		addRecord(sheet, record);
	}

	public String toFile() {
		try {
			File file = new File(Definiens.UPLOAD_PATH + File.separator + "temp.xlsx");
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			return file.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void addRecord(XSSFSheet sheet, Record record) {
		int last = sheet.getLastRowNum();
		XSSFRow row = sheet.getRow(last);
		XSSFCell createCell = row.createCell(0);
		createCell.setCellValue(record.toString());
	}

	private void createHeadInSheet(XSSFSheet sheet) {
		XSSFRow headRow = sheet.createRow(0);
		XSSFCell cell0 = headRow.createCell(0);
		cell0.setCellValue("表头");
	}

}
