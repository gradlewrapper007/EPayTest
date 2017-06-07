package com.singh.sudhanshu.epaytest.application;

import android.app.Application;
import com.singh.sudhanshu.epaytest.utils.PreferenceUtil;

/**
 * Created by Sudhanshu on 14-May-16.
 */
public class BaseApp extends Application {

    private static BaseApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        //First thing to do
        init();
        //Initialize Utils
        PreferenceUtil.init(BaseApp.getContext(), "shared_preferences_epay");

    }

    private void init() {
        if(app==null){
            app = BaseApp.this;
        }

    }

    public static BaseApp getContext() {
        return app;
    }
}
