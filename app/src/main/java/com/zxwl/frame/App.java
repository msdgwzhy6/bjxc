package com.zxwl.frame;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.zxwl.frame.net.Urls;
import com.zxwl.frame.net.config.NetWorkConfiguration;
import com.zxwl.frame.net.http.HttpUtils;
import com.zxwl.frame.utils.sharedpreferences.PreferencesHelper;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/14 17:17
 * ClassName: ${Class_Name}
 */

public class App extends Application {
    private static App mInstance;

    public static App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        PreferencesHelper.init(mInstance);

        NetWorkConfiguration configuration = new NetWorkConfiguration(this)
                .baseUrl(Urls.BASE_URL);

        HttpUtils.setConFiguration(configuration);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app include_common_operation this process.
            return;
        }

        LeakCanary.install(this);

        //腾讯bug线上搜集工具 TODO KEY_ID换成真实的数据
        CrashReport.initCrashReport(getApplicationContext(), "568d7ac2da", false);
    }


}
