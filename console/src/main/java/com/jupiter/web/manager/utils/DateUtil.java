package com.jupiter.web.manager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	// private static long daybreak = 0L;
	// private static int TZ_OFFSET = 8;

	public static final int DAY = 86400000;
	public static final int HOUR = 3600000;
	public static final int MINUTE = 60000;
	public static final int SECOND = 1000;

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_MILL_FORMAT = "yyyy-MM-dd HH:mm:ss SSS";

	// public static void initOffset(int offset) {
	// TZ_OFFSET = offset;
	// }

	// private static int getOffset() {
	// return TZ_OFFSET;
	// }

	public static Date parseDate(String str) {
		return parseDate(str, DATE_FORMAT);
	}

	public static Date parseDate(String str, String format) {
		if (str == null) {
			return null;
		}
		try {
			return new SimpleDateFormat(format).parse(str);
		} catch (Exception e) {
			throw new RuntimeException("time format error" + str, e);
		}
	}

	public static long getDelayTime(String timeStr) {
		long now = System.currentTimeMillis();
		String dateStr = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		String str = dateStr + " " + timeStr;
		long time = DateUtil.parseDate(str).getTime();
		long delay = 0;
		if (now > time) {
			delay = time + DateUtil.DAY - now;
		} else {
			delay = time - now;
		}
		return delay;
	}

	// public static void initDaybreak() {
	// long now = System.currentTimeMillis();
	// if (daybreak == 0L || now > daybreak + DAY) {
	// long daybreakdelta = (now % DAY);
	// if (daybreakdelta < 0) {
	// daybreakdelta += DAY;
	// }
	// daybreak = now - daybreakdelta;
	// }
	// }

	public static int getCurrDayOfWeek() {
		int currDay = getCurrWeekDay();
		if (currDay == 1) {
			currDay = 7;
		} else {
			currDay--;
		}
		return currDay;
	}

	public static String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(date);
	}

	public static String formatDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String formatDate(Long date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static Long formatTimeMillis(String date, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// public static String formatOffsetDate(Date date, String format) {
	// SimpleDateFormat sdf = new SimpleDateFormat(format);
	// String timeZone = "GMT";
	// if (getOffset() >= 0) {
	// timeZone += "+" + getOffset();
	// } else {
	// timeZone += getOffset();
	// }
	// sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
	// return sdf.format(date);
	// }

	// public static Date doOffsetTime(String time) {
	// SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	// String timeZone = "GMT";
	// if (getOffset() >= 0) {
	// timeZone += "+" + getOffset();
	// } else {
	// timeZone += getOffset();
	// }
	// sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
	// Date endTime = null;
	// try {
	// endTime = sdf.parse(time);
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// return endTime;
	// }

	public static boolean isCurrSaturday() {
		return getCurrWeekDay() == 7;
	}

	public static boolean isCurrSunday() {
		return getCurrWeekDay() == 1;
	}

	public static int getCurrWeekDay() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int currDay = cal.get(Calendar.DAY_OF_WEEK);
		return currDay;
	}

	// public static int getCurrWeekDay() {
	// Calendar cal = Calendar.getInstance();
	// TimeZone timeZone;
	// String[] ids = TimeZone.getAvailableIDs(getOffset() * 60 * 60 * 1000);
	// if (ids.length == 0) {
	// timeZone = TimeZone.getDefault();
	// } else {
	// timeZone = new SimpleTimeZone(getOffset() * 60 * 60 * 1000, ids[0]);
	// }
	// cal.setTime(new Date());
	// cal.setTimeZone(timeZone);
	// int currDay = cal.get(Calendar.DAY_OF_WEEK);
	// return currDay;
	// }

	// public static long getDayBreak() {
	// initDaybreak();
	// return daybreak;
	// }
	//
	// public static boolean checkDate(Date time) {
	// initDaybreak();
	// if (time == null || time.getTime() < daybreak) {
	// return true;
	// }
	// return false;
	// }
	//
	// public static int breakDays(Date day) {
	// long daybreakdelta = (day.getTime() % DAY) + HOUR;
	// if (daybreakdelta < 0) {
	// daybreakdelta += DAY;
	// }
	// return (int) ((day.getTime() - daybreakdelta) / DAY);
	// }

	public static long getCurrDayZeroTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis();
	}

	public static long getCurrDayHour(int hour) {
		return getCurrDayTime(hour, 0, 0, 0);
	}

	public static long getCurrDayTime(int hour, int min, int sec, int mill) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, sec);
		calendar.set(Calendar.MILLISECOND, mill);
		return calendar.getTimeInMillis();
	}

}
