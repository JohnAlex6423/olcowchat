package com.olcow.chat.until;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * 时间通用类
 *
 * create on 2018/11/6
 */

public class GetCurrentTime {


    /**
     *
     * @return get current time
     *
     */

    public static String getCurrentTime(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss ");
        return simpleDateFormat.format(date);
    }
}