package com.track.store.dog.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.track.paint.core.Definiens;
import com.track.store.dog.bean.Record;

public class ExcelExportUtil {
	private static SimpleDateFormat DATAFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

	public String toFile(String name) {
		try {
			int sheetNum = workbook.getNumberOfSheets();
			for (int i = 0; i < sheetNum; i++) {
				XSSFSheet sheet = workbook.getSheetAt(i);
				int lastRow = sheet.getLastRowNum();
				double x = 0;
				double y = 0;
				double z = 0;
				for (int j = 3; j <= lastRow; j++) {
					x += sheet.getRow(j).getCell(6).getNumericCellValue();
					y += sheet.getRow(j).getCell(7).getNumericCellValue();
					z += sheet.getRow(j).getCell(8).getNumericCellValue();
				}

				XSSFRow row0 = sheet.getRow(0);
				row0.createCell(0).setCellValue("货款总计");
				row0.createCell(1).setCellValue(x);
				row0.createCell(3).setCellValue("运费总计");
				row0.createCell(4).setCellValue(y);
				row0.createCell(6).setCellValue("总款总计");
				row0.createCell(7).setCellValue(z);
			}
			File file = new File(Definiens.UPLOAD_PATH + File.separator + name + ".xlsx");
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
		XSSFRow row = sheet.createRow(last + 1);
		double x = record.getInOrOut().equals("出货") ? record.getUnivalent() : -record.getUnivalent();
		double y = -record.getFreight();
		row.createCell(0).setCellValue(DATAFORMAT.format(record.getTime()));
		row.createCell(1).setCellValue(record.getGoods());
		row.createCell(2).setCellValue(record.getPartner());
		row.createCell(3).setCellValue(record.getInOrOut());
		row.createCell(4).setCellValue(x);
		row.createCell(5).setCellValue(record.getCount());
		row.createCell(6).setCellValue(x * record.getCount());
		row.createCell(7).setCellValue(y);
		row.createCell(8).setCellValue(x * record.getCount() + y);
	}

	private void createHeadInSheet(XSSFSheet sheet) {
		sheet.createRow(0);
		sheet.createRow(1);
		XSSFRow headRow = sheet.createRow(2);
		headRow.createCell(0).setCellValue("时间");
		headRow.createCell(1).setCellValue("货物");
		headRow.createCell(2).setCellValue("伙伴");
		headRow.createCell(3).setCellValue("状态");
		headRow.createCell(4).setCellValue("单价");
		headRow.createCell(5).setCellValue("数量");
		headRow.createCell(6).setCellValue("货款");
		headRow.createCell(7).setCellValue("运费");
		headRow.createCell(8).setCellValue("总款");
	}

}
