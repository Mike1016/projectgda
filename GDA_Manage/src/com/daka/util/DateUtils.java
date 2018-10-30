package com.daka.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils
{
    private static DateUtils dateUtils;

    private DateUtils()
    {

    }

    public synchronized static DateUtils getInstance()
    {
        if (null == dateUtils)
        {
            dateUtils = new DateUtils();
        }
        return dateUtils;
    }

    private static final SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
    


    /*
     * 短日期转换 Date->String
     */
    public String DateToString(Date date)
    {

        return simple.format(date);
    }

    /**
     * 返回当前短时间格式日期
     */
    public static String nowDate()
    {
        return simple.format(new Date());
    }

    /**
     * String 转 Date
     * 
     * @throws ParseException
     */
    public Date StringToDate(String date)
        throws ParseException
    {
        return simple.parse(date);
    }

    /**
     * 增加日期
     */
    public String addDate(Date date, String time)
    {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        if (time.equals("1"))
        {
            rightNow.add(Calendar.DAY_OF_YEAR, 3);
        }
        if (time.equals("2"))
        {
            rightNow.add(Calendar.DAY_OF_YEAR, 7);
        }
        if (time.equals("3"))
        {
            rightNow.add(Calendar.DAY_OF_YEAR, 30);
        }
        if (time.equals("4"))
        {
            rightNow.add(Calendar.MONTH, 3);
        }
        if (time.equals("5"))
        {
            rightNow.add(Calendar.YEAR, 1);
        }
        Date toDate = rightNow.getTime();
        return DateToString(toDate);
    }

    /**
     * 得到当前 年-月-日 时-分-秒 格式的时间 getCurrentTime:(这里用一句话描述这个方法的作用). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     *
     * @author Administrator
     * @return
     * @since JDK 1.6
     */
    public static String getCurrentTimeYMDHMS()
    {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(currentTime);
        return sFormat.format(date);
    }

    /**
     * 获取按照批次号格式的时间（年_月_日_时分秒） getTimeFormatAsLotNo:(这里用一句话描述这个方法的作用). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     *
     * @author 丁
     * @return
     * @since JDK 1.6
     */
    public static String getTimeFormatAsLotNo()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
        return format.format(Calendar.getInstance().getTime());
    }

    public static String dayForWeek(String pTime)
        throws Exception
    {
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1)
        {
            dayForWeek = 7;
        }
        else
        {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        switch (dayForWeek)
        {
            case 1:
                result = "星期一";
                break;
            case 2:
                result = "星期二";
                break;
            case 3:
                result = "星期三";
                break;
            case 4:
                result = "星期四";
                break;
            case 5:
                result = "星期五";
                break;
            case 6:
                result = "星期六";
                break;
            case 7:
                result = "星期天";
                break;

            default:
                break;
        }
        return result;
    }

    public static boolean isSameDate(Date date1, Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
        return isSameDate;
    }
    
    /**把yyyy-MM-dd HH:mm:ss格式的时间转化为时间戳
     * @param time  yyyy-MM-dd HH:mm:ss格式的时间
     * @return 时间戳
     * @throws ParseException
     */
    public static long getTimestampByTime(String time) throws ParseException{
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date confirmTime=simpleDateFormat.parse(time);
    	return confirmTime.getTime();
    }
    
    /**
     * @param time  yyyy-MM-dd 格式的时间
     * @return 获取第二天时间
     * @throws ParseException
     */
    public static String getTomorrowTime() {
        Calendar cal = Calendar.getInstance(); 
        cal.setTime(new Date()); 
        cal.add(Calendar.DAY_OF_YEAR, 1);        
        return simple.format(cal.getTime());
    }    
}
