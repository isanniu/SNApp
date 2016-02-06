package com.sannniu.ncore.utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间/日期--工具类
 */
public final class DateUtil implements Serializable {

    private static final long serialVersionUID = -3098985139095632110L;

    public static final String TODAY = "今天";
    public static final String YESTERDAY = "昨天";
    public static final String TOMORROW = "明天";
    public static final String BEFORE_YESTERDAY = "前天";
    public static final String AFTER_TOMORROW = "后天";
    public static final String SUNDAY = "星期日";
    public static final String MONDAY = "星期一";
    public static final String TUESDAY = "星期二";
    public static final String WEDNESDAY = "星期三";
    public static final String THURSDAY = "星期四";
    public static final String FRIDAY = "星期五";
    public static final String SATURDAY = "星期六";

    private DateUtil() {
    }

    /**
     * 格式化时间【PromtionActivitiesAdapter】
     * 
     * @param str
     * @return
     */
    public static String formatTimes(String str) {
        String result = "";
        if (str != null && str.length() > 10) {
            str = str.substring(0, 10);
            if (str.contains("-")) {
                String[] times = str.split("-");
                if (times == null || times.length <= 0) {
                    return result;
                }
                for (int i = 1; i < times.length; i++) {
                    if (i != (times.length - 1)) {
                        result += times[i] + ".";
                    } else {
                        result += times[i];
                    }
                }
            }
        }
        return result;
    }

    /**
     * 日期格式化
     * 
     * @param sdate
     * @param format
     * @return
     */
    public static String dateFormat(String sdate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        java.sql.Date date = java.sql.Date.valueOf(sdate);
        String dateString = formatter.format(date);

        return dateString;
    }

    /**
     * 获取格式化的日期对象
     * 
     * @param sDate
     * @param dateFormat
     * @return
     */
    public static Date getDate(String sDate, String dateFormat) {
        SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
        ParsePosition pos = new ParsePosition(0);

        return fmt.parse(sDate, pos);
    }

    /**
     * 获取当前时间【年】
     * 
     * @return
     */
    public static String getCurrentYear() {
        return getFormatCurrentTime("yyyy");
    }

    /**
     * 获取当前时间【月】
     * 
     * @return
     */
    public static String getCurrentMonth() {
        return getFormatCurrentTime("MM");
    }

    /**
     * 获取当前时间【日】
     * 
     * @return
     */
    public static String getCurrentDay() {
        return getFormatCurrentTime("dd");
    }

    /**
     * 获取当前日期
     * 
     * @return
     */
    public static String getCurrentDate() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd");
    }

    /**
     * 获取当前详细时间
     * 
     * @return
     */
    public static String getCurrentDateTime() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化日期
     * 
     * @param date
     * @return
     */
    public static String getFormatDate(Date date) {
        return getFormatDateTime(date, "yyyy-MM-dd");
    }

    /**
     * 格式化当前日期
     * 
     * @param format
     * @return
     */
    public static String getFormatDate(String format) {
        return getFormatDateTime(new Date(), format);
    }

    /**
     * 获取当前详细时间
     * 
     * @return
     */
    public static String getCurrentTime() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化Date的详细时间
     * 
     * @param date
     * @return
     */
    public static String getFormatTime(Date date) {
        return getFormatDateTime(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化Date的详细日期
     * 
     * @param date
     * @return
     */
    public static String getFormatShortTime(Date date) {
        return getFormatDateTime(date, "yyyy-MM-dd");
    }

    /**
     * 格式化当前日期
     * 
     * @param format
     * @return
     */
    public static String getFormatCurrentTime(String format) {
        return getFormatDateTime(new Date(), format);
    }

    /**
     * 格式化时间
     * 
     * @param date
     * @param format
     * @return
     */
    public static String getFormatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 传入日期加传入天数
     * 
     * @param date
     * @param dateType
     * @param days
     * @return
     */
    public static Date getDatePlus(Date date, int dateType, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(dateType, days);
        return cal.getTime();
    }

    /**
     * 字符串转Date
     * 
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static Date StringToDate(String dateStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat(formatStr, Locale.CHINA);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 根据时间字符串处理成想要的格式
     * 
     * @param sDate
     * @param sFormat
     *            目标格式
     * @param tFormat
     *            数据源格式
     * @return
     */
    public static String getFormatDateStr(String sDate, String tFormat, String sFormat) {
        String dateString = null;
        try {
            Date date = new SimpleDateFormat(sFormat).parse(sDate);
            dateString = new SimpleDateFormat(tFormat).format(date);
        } catch (ParseException e) {
            // 时间格式错误
        }
        return dateString;
    }

    /**
     * 是否在指定天数范围内
     * 
     * @param beginTime
     *            开始时间
     * @param days
     *            指定天数范围 默认3
     * @return
     */
    public static boolean isInTheRange(long beginTime, int days) {
        long endTime = new Date().getTime();
        long cha = (long) ((endTime - beginTime) / (1000 * 60 * 60 * 24));
        return cha / 3 < 1;
    }

    /**
     * 将日期信息转换成今天、明天、后天、星期
     * 
     * @param date
     * @return
     */
    public static String getDateDetail(String date) {
        Calendar today = Calendar.getInstance();
        Calendar target = Calendar.getInstance();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            today.setTime(df.parse(getCurrentDate()));
            today.set(Calendar.HOUR, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            target.setTime(df.parse(date));
            target.set(Calendar.HOUR, 0);
            target.set(Calendar.MINUTE, 0);
            target.set(Calendar.SECOND, 0);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        long intervalMilli = target.getTimeInMillis() - today.getTimeInMillis();
        int xcts = (int) (intervalMilli / (24 * 60 * 60 * 1000));
        return showDateDetail(xcts, target);

    }

    /**
     * 将日期差显示为日期或者星期
     * 
     * @param xcts
     * @param target
     * @return
     */
    private static String showDateDetail(int xcts, Calendar target) {
        switch (xcts) {
        case 0:
            return TODAY;
        case 1:
            return TOMORROW;
        case 2:
            return AFTER_TOMORROW;
        case -1:
            return YESTERDAY;
        case -2:
            return BEFORE_YESTERDAY;
        default:
            int dayForWeek = 0;
            dayForWeek = target.get(Calendar.DAY_OF_WEEK);
            switch (dayForWeek) {
            case 1:
                return SUNDAY;
            case 2:
                return MONDAY;
            case 3:
                return TUESDAY;
            case 4:
                return WEDNESDAY;
            case 5:
                return THURSDAY;
            case 6:
                return FRIDAY;
            case 7:
                return SATURDAY;
            default:
                return null;
            }

        }
    }

    /**
     * 判断date1是否在date1 在date2 之前 时间格式 2005-4-21 16:16:34
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isDateBefore(String date1, String date2) {
        try {
            DateFormat df = DateFormat.getDateTimeInstance();
            return df.parse(date1).before(df.parse(date2));
        } catch (ParseException e) {
            System.out.print("[SYS] " + e.getMessage());
            return false;
        }
    }

    /**
     * 判断当前时间是否在时间date2之前 时间格式 2005-4-21 16:16:34
     * 
     * @param date2
     * @return
     */
    public static boolean isDateBefore(String date2) {
        try {
            Date date1 = new Date();
            System.out.println(date1);
            DateFormat df = DateFormat.getDateTimeInstance();
            return date1.before(df.parse(date2));
        } catch (ParseException e) {
            return false;
        }
    }

}