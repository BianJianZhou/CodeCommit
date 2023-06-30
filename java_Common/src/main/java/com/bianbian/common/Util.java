package com.bianbian.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static String time() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh_mm_ss_SSS");
        return dateFormat.format(date);
    }
}
