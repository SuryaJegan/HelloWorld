package com.dnb.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dnb.constants.Constant;
import com.dnb.model.BizVO;

public class ExcelUtil {
	public void writeToExcel(List<Object> statsVO) throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet biz_ip_mid = workbook.createSheet("biz_ip_mid");
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		CellStyle style1 = workbook.createCellStyle();
		style1.setBorderBottom(CellStyle.BORDER_THIN);
		style1.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style1.setBorderLeft(CellStyle.BORDER_THIN);
		style1.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style1.setBorderRight(CellStyle.BORDER_THIN);
		style1.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style1.setBorderTop(CellStyle.BORDER_THIN);
		style1.setTopBorderColor(IndexedColors.BLACK.getIndex());
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setColor(IndexedColors.WHITE.getIndex());
		style1.setFont(font);
		style1.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		style1.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		int rownum = 1;

		XSSFRow headrow = biz_ip_mid.createRow(0);

		for (int i = 0; i < 5; i++) {
			XSSFCell cell = headrow.createCell(i);
			cell.setCellStyle(style1);

			if (i == 0) {
				cell.setCellValue("country");
			} else if (i == 1) {
				cell.setCellValue("biz_ip_future_duns");
			} else if (i == 2) {
				cell.setCellValue("biz_ip_duns");
			} else if (i == 3) {
				cell.setCellValue("overlapping_duns");
			} else if (i == 4) {
				cell.setCellValue("percent_of_prior");
			}
			biz_ip_mid.autoSizeColumn(i);
		}

		for (Object sourceVO : statsVO) {
			XSSFRow row = biz_ip_mid.createRow(rownum++);
			for (int i = 0; i < 5; i++) {

				XSSFCell cell = row.createCell(i);
				cell.setCellStyle(style);
				if (i == 0) {
					cell.setCellValue(((BizVO) sourceVO).getCntryName());
				} else if (i == 1) {
					cell.setCellValue(((BizVO) sourceVO).getBizIpFutureDunsCount());
				} else if (i == 2) {
					cell.setCellValue(((BizVO) sourceVO).getBizIpDunsCount());
				} else if (i == 3) {
					cell.setCellValue(((BizVO) sourceVO).getOverlappingDunsSum());
				} else if (i == 4) {
					cell.setCellValue(((BizVO) sourceVO).getPercentOfPrior());
				}

			}

		}

		try {

			File directory = new File(Constant.PATH);
			if (!directory.exists()) {
				System.out.println("Creating the " + directory.getAbsolutePath() + " as it is not available");

				directory.mkdirs();
			}
			String excelFileName = Constant.FILE_NAME.replace("<MMM_YYYY>", DateUtil.getFileDate());
			String excelFileLocation = Constant.PATH + excelFileName;
			FileOutputStream out = new FileOutputStream(excelFileLocation);
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
		}
	}

}
