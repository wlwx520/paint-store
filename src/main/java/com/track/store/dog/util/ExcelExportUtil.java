package com.track.store.dog.util;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

	public byte[] toBytes() {
		// TODO
		return null;
	}

	private void addRecord(XSSFSheet sheet, Record record) {
		// TODO

	}

	private void createHeadInSheet(XSSFSheet sheet) {
		// TODO
	}

}
