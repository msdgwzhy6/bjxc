package com.zxwl.frame.net;

import com.zxwl.frame.App;
import com.zxwl.frame.utils.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/20 11:33
 * ClassName: ${Class_Name}
 */
public class RetrofitHelper {
    private static OkHttpClient okHttpClient;

    //设置缓存的大小
    private static long CACHE_MAX_SIZE = 1024 * 1024 * 10;

    //有网络时，设置缓存超时时间为1个小时
    private static int MAX_AGE = 60 * 60;
    //无网络时，超时时间为1天
    private static int MAX_STALE = 60 * 60 * 24;

    static {
        initOkHttpClient();
    }

    private static void initOkHttpClient() {
        //创建日志记录拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //创建日志拦截器
        if (null == okHttpClient) {
            synchronized (RetrofitHelper.class) {
                if (null == okHttpClient) {
                    //设置http缓存路径
                    Cache cache = new Cache(
                            new File(App.getInstance().getCacheDir(), "httpCache"), CACHE_MAX_SIZE);
                    okHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            //添加日志拦截器
                            .addInterceptor(loggingInterceptor)
                            //设置缓存
                            .addNetworkInterceptor(new CacheInterceptor())
                            //在连接失败时重试
                            .retryOnConnectionFailure(true)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    private static class CacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //获得请求
            Request request = chain.request();
            //判断网络是否连接
            if (NetworkUtil.isNetworkAvailable(App.getInstance())) {
                //有网络时只从网络获取
                request = request//
                        .newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            } else {
                //无网络时只从缓存中读取
                request = request//
                        .newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            //得到响应
            Response response = chain.proceed(request);
            //判断网络是否连接
            if (NetworkUtil.isNetworkAvailable(App.getInstance())) {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + MAX_AGE)
                        .build();
            } else {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + MAX_STALE)
                        .build();
            }
            return response;
        }
    }
}
