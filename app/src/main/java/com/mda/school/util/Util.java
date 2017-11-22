package com.mda.school.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by michael on 22/11/17.
 */

public class Util {

    public static String dateFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        return format.format(date);
    }

}
