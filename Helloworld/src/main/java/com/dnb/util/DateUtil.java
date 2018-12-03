package com.dnb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String date;
	public static String fileDate;

	public static String getDate() {
		return date;
	}

	public static void setDate(String currentDate) {

		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat format2 = new SimpleDateFormat("MMM YYYY");
		SimpleDateFormat format3 = new SimpleDateFormat("MMM_YYYY");
		try {
			Date interimDate = format1.parse(currentDate);
			DateUtil.date = format2.format(interimDate);
			DateUtil.fileDate = format3.format(interimDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static String getFileDate() {
		return fileDate;
	}

}
