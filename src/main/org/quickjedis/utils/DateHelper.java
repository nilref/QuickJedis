package org.quickjedis.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateHelper {

	public static String GetDateNow(String format) {
		return new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
	}

	public static Date GetDateNow() {
		return Calendar.getInstance().getTime();
	}

	public static Date AddMinutes(Date dt, int minutes) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(dt);
		calendar.add(Calendar.MINUTE, minutes);
		return calendar.getTime();
	}
}
