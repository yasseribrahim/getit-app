package com.fully.code.base.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatesUtils {
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static String formatDateOnly(Date date) {
        return DATE_FORMAT.format(date);
    }
}