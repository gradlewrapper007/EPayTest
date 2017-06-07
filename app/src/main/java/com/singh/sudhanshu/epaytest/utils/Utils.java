package com.singh.sudhanshu.epaytest.utils;

import android.app.Activity;

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
}
