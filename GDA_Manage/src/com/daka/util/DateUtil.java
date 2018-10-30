package com.daka.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil
{
    private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

    private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

    private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");

    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final static SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");

    /**
     * 获取YYYY格式
     * 
     * @return
     */
    public static String getYear()
    {
        return sdfYear.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD格式
     * 
     * @return
     */
    public static String getDay()
    {
        return sdfDay.format(new Date());
    }

    /**
     * 获取YYYYMMDD格式
     * 
     * @return
     */
    public static String getHour()
    {
        return sdfHour.format(new Date());
    }

    /**
     * 获取YYYYMMDD格式
     * 
     * @return
     */
    public static String getDays()
    {
        return sdfDays.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     * 
     * @return
     */
    public static String getTime()
    {
        SimpleDateFormat fmtTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmtTime.format(new Date());
    }

    /**
     * @Title: compareDate
     * @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
     * @param s
     * @param e
     * @return boolean
     * @throws
     * @author luguosui
     */
    public static boolean compareDate(String s, String e)
    {
        if (fomatDate(s) == null || fomatDate(e) == null)
        {
            return false;
        }
        return fomatDate(s).getTime() >= fomatDate(e).getTime();
    }

    /**
     * 计算时间之间的秒数
     *
     * @author Administrator
     * @param s
     * @param e
     * @return -1代表参数格式有误
     * @since JDK 1.7
     */
    public static int getDiffSecond(String s, String e)
    {
        if (fomatTime(s) == null || fomatTime(e) == null)
        {
            return -1;
        }
        return (int)((fomatTime(s).getTime() - fomatTime(e).getTime()) / 1000);
    }

    /**
     * 格式化日期
     * 
     * @return
     */
    public static Date fomatDate(String date)
    {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            return fmt.parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期
     * 
     * @return
     */
    public static Date fomatDate(String date, String formatCode)
    {
        DateFormat fmt = new SimpleDateFormat(formatCode);
        try
        {
            return fmt.parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Date fomatTime(String time)
    {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            return fmt.parse(time);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String fomatDataString(Object date)
    {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(date);
    }

    public static String fomatDataToStrByType(Date date, String fomatStr)
    {
        DateFormat fmt = new SimpleDateFormat(fomatStr);
        return fmt.format(date);
    }

    /**
     * 校验日期是否合法
     * 
     * @return
     */
    public static boolean isValidDate(String s)
    {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            fmt.parse(s);
            return true;
        }
        catch (Exception e)
        {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    public static int getDiffYear(String startTime, String endTime)
    {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            long aa = 0;
            int years =
                (int)(((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
            return years;
        }
        catch (Exception e)
        {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return 0;
        }
    }

    /**
     * <li>功能描述：时间相减得到天数
     * 
     * @param beginDateStr
     * @param endDateStr
     * @return long
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr, String endDateStr)
    {
        long day = 0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate = null;
        java.util.Date endDate = null;

        try
        {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        // System.out.println("相隔的天数="+day);

        return day;
    }

    /**
     * 得到n天之后的日期
     * 
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days)
    {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    /**
     * 得到n天之后的日期
     * 
     * @param days
     * @return
     */
    public static String getAfterDay(String days)
    {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    /**
     * 得到当前日期下一年的日期
     * 
     * @param days
     * @return
     */
    public static String getAfterDayYear()
    {

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.YEAR, +1); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    /**
     * 得到当前日期下一年的日期
     * 
     * @param days
     * @return
     */
    public static String getAfterDayYear(String date)
    {

        Calendar canlendar = Calendar.getInstance(); // java.util包

        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        try
        {
            canlendar.setTime(format.parse(date));
        }
        catch (ParseException e)
        {

            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        canlendar.add(Calendar.YEAR, +1); // 日期减 如果不够减会将月变动
        canlendar.add(Calendar.DATE, -1);
        Date date1 = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdfd.format(date1);

        return dateStr;
    }

    /**
     * 得到n天之后是周几
     * 
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days)
    {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);

        return dateStr;
    }

    /**
     * 将传入的字符串型时间转换成目标类型，要求传入的时间格式必须为yyyy-MM-dd HH:mm:ss
     * 
     * @author Administrator
     * @return
     * @since JDK 1.6
     */
    public static String convertStrTimeToTargetFormat(String oldTimeStr, String formatStr)
    {
        DateFormat targetTimeFmt = new SimpleDateFormat(formatStr);
        String result = "";
        try
        {
            Date oldDate = sdfTime.parse(oldTimeStr);
            result = targetTimeFmt.format(oldDate);

        }
        catch (ParseException e)
        {
            return "";
        }
        return result;
    }

    /**
     * 将DateTime型的日期转成日期和时间
     * 
     * @author Administrator
     * @param longDateStr yyyy-MM-dd HH:mm:ss.ms
     * @return
     * @since JDK 1.6
     */
    public static Map<String, String> getDateAndTimeFromDateTime(String dateTimeStr)
    {
        Map<String, String> result = new HashMap<String, String>();
        int index = dateTimeStr.indexOf(" ");
        if (-1 == index)
        {
            return result;
        }
        String tempTime = dateTimeStr.substring(index + 1, dateTimeStr.length());
        result.put("startDate", dateTimeStr.substring(0, index));
        result.put("startTime", tempTime.substring(0, tempTime.indexOf(":", tempTime.indexOf(":") + 1)));
        return result;
    }

    /**
     * 将long型的日期转成日期
     * 
     * @author Administrator
     * @param longDateStr
     * @return
     * @since JDK 1.6
     */
    public synchronized static String convertLongDateToDate(String longDateStr)
    {
        DateFormat timeFmt = new SimpleDateFormat("yyyy-MM-dd");
        long longdate = Long.valueOf(longDateStr) * 1000;
        Date tempDate = new Date(longdate);
        return timeFmt.format(tempDate);
    }

    /**
     * 排序用的long时间
     * 
     * @author Administrator
     * @param dateTime
     * @return
     * @since JDK 1.6
     */
    public synchronized static String getLongTimeToSort(String dateTime)
    {
        DateFormat timeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            return String.valueOf(timeFmt.parse(dateTime).getTime());
        }
        catch (ParseException e)
        {
            return "0";
        }
    }

    /**
     * 排序用的long时间
     * 
     * @author Administrator
     * @return
     * @since JDK 1.6
     */
    public static String getLongTimeToSort(String dateStr, String timeStr)
    {
        String tempDateTime = dateStr + " " + timeStr + ":00";
        DateFormat timeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            Date tempDate = timeFmt.parse(tempDateTime);
            long tempTime = tempDate.getTime();
            return String.valueOf(tempTime);
        }
        catch (Exception e)
        {
            System.out.println(tempDateTime);
            return "0";
        }
    }

    /**
     * 得到两个时间的和，时间的格式必须为"mm:ss"
     * 
     * @author Administrator
     * @param oldTime
     * @param costTime
     * @return
     * @since JDK 1.6
     */
    public static String getSumOfTime(String oldTime, String costTime)
    {
        DateFormat minSecondFmt = new SimpleDateFormat("mm:ss");
        String result = "";
        try
        {
            Date oldTimeLong = minSecondFmt.parse(oldTime);
            Date newTimeLong = minSecondFmt.parse(costTime);
            long sumTime = oldTimeLong.getTime() + newTimeLong.getTime();
            result = minSecondFmt.format(new Date(sumTime));
        }
        catch (ParseException e)
        {
            result = "";
        }

        return result;
    }
}
