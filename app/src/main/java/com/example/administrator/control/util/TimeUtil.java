package com.example.administrator.control.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author: ZhongMing
 * DATE: 2019/1/11 0011
 * Description:时间转换
 **/
public class TimeUtil {
    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    public static String nowTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }


    /**
     * 获取注册时间差
     *
     * @return
     */
    public static String getDef(String begin, String end) {
        Date d1 = null;
        Date d2 = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            d1 = sdf.parse(begin);
            d2 = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long betweenDay = (d2.getTime() - d1.getTime() + 1000000) / (60 * 60 * 24 * 1000);
        System.out.println(betweenDay);
        return String.valueOf(betweenDay);
    }
}
