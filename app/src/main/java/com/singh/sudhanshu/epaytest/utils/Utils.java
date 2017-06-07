package com.singh.sudhanshu.epaytest.utils;

import android.app.Activity;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Sudhanshu on 6/7/2017.
 */

public class Utils {

    /**
     * Validates email.
     *
     * @param target
     * @param activity
     * @return
     */
    public static boolean isValidEmail(CharSequence target, Activity activity) {

        boolean ret = false;
        if (target == null) {
            ret = false;
        } else {
            ret = android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
        return ret;
    }

    public static String fromISO8601UTC(String dateStr) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        inputFormat.setTimeZone(tz);
        try {
            Date parsed = inputFormat.parse(dateStr);
            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a", Locale.US);
            outputFormat.setTimeZone(tz);
            Log.i("eeee", "fromISO8601UTC: " + outputFormat.format(parsed));
            return outputFormat.format(parsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Return an ISO 8601 combined date and time string for current date/time
     *
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    public static String getISO8601StringForCurrentDate() {
        Date now = new Date();
        return getISO8601StringForDate(now);
    }

    /**
     * Return an ISO 8601 combined date and time string for specified date/time
     *
     * @param date Date
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    private static String getISO8601StringForDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

}
