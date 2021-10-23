package com.flobiz.app.util;

import java.util.Date;

public class Formatter {
	public static String format(String string) {
		Date date = new Date(Long.parseLong(string) * 1000);
		String day = date.getDate() + "";
		String month = (date.getMonth() + 1) + "";
		String year = (date.getYear() + 1900) + "";

		if (date.getDate() < 9) day = "0" + day;
		if (date.getMonth() < 9) month = "0" + month;

		return day + "-" + month + "-" + year;
	}
}
