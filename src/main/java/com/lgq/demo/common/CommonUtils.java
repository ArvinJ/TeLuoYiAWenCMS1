package com.lgq.demo.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by liguoqing on 2015/12/21.
 */
public class CommonUtils {


    public static String getUUID(){
        String s = UUID.randomUUID().toString();
        return s.replace("-","");
    }


    /**
     * 日期转换成字符串
     * @param date
     * @param formatStr
     * @return
     */
    public static String DateToString(Date date, String formatStr){
        DateFormat df = new SimpleDateFormat(formatStr);
        return df.format(date);
    }

    /**
     * 字符串转换成日期
     * @param date
     * @param formatStr
     * @return
     */
    public static Date StringToDate(String date, String formatStr){
        DateFormat df = new SimpleDateFormat(formatStr);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            //日期格式转换错误
            e.printStackTrace();
        }
        return null;
    }
}
