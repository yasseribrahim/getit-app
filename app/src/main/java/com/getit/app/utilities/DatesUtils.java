package com.getit.app.utilities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;

import com.getit.app.Constants;
import com.getit.app.CustomApplication;
import com.getit.app.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DatesUtils {
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static String formatDateOnly(Date date) {
        return DATE_FORMAT.format(date);
    }
}