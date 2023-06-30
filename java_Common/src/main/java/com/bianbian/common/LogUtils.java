package com.bianbian.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LogUtils {
    private static final String TAG = "CodeCommit:";
    public static boolean isDebug = false;

    public static void debug(String tag, String logContent) {
        /* 这里 println 为换行输入; print 为整行输入 */
        if (isDebug) {
            String str = TAG + tag + "=>" + logContent;
            println(str);
        }
    }

    public static void info(String tag, String logContent) {
        /* 这里 println 为换行输入; print 为整行输入 */
        String str = TAG + tag + "=>" + logContent;
        println(str);
    }

    public static void err(String tag, String msg, Exception err) {
        String errMsg = "";
        String errClass = "";
        if (err != null) {
            errClass = err.getClass().getName();
            errMsg = err.getMessage();
        }
        String str = TAG + tag + "," + msg + ",err: " + errClass + "," + errMsg;
        println(str);
    }

    public static String logFormat(String str, int length) {
        String tmpStr = str;
        if (str.length() < length) {
            for (int i = 0; i < length - str.length(); i++) {
                tmpStr += " ";
            }
        }
        return tmpStr;
    }

    public static void printList(String tag1, String tag2, List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            log(tag1, tag2, "i:" + i + " str:" + list.get(i));
        }
    }

    private static void log(String tag, String tag2, String strTmp) {
        String str = TAG + tag + "=>" + tag2 + " " + strTmp;
        println(str);
    }

    private static String time() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
        return dateFormat.format(date);
    }

    private static void println(String str) {
        System.out.println("[" + time() + " " + Thread.currentThread().getId() + " " + Thread.currentThread().getName() + "]--" + str + "");
    }
}
