package com.singh.sudhanshu.epaytest.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Sudhanshu on 07/06/17.
 */

public class ToastUtil {

    /**
     * Displays a Toast with duration SHORT. From a String Res ID
     *
     * @param context
     * @param resID
     */
    public static void showSmallToast(Context context, int resID) {

        Toast.makeText(context, context.getResources().getString(resID), Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays a Toast with duration SHORT. From a String literal
     *
     * @param context
     * @param message
     */
    public static void showSmallToast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays a Toast with duration LENGTH_LONG. From a String Res ID
     *
     * @param context
     * @param resID
     */
    public static void showLongToast(Context context, int resID) {

        Toast.makeText(context, context.getResources().getString(resID), Toast.LENGTH_LONG).show();
    }

    /**
     * Displays a Toast with duration LENGTH_LONG. From a String literal
     *
     * @param context
     * @param message
     */
    public static void showLongToast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
