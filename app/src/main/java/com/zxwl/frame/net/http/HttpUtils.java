package com.zxwl.frame.net.http;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.zxwl.frame.net.RetrofitClient;
import com.zxwl.frame.net.config.NetWorkConfiguration;
import com.zxwl.frame.net.interceptor.LogInterceptor;
import com.zxwl.frame.utils.NetworkUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/20 14:21
 * ClassName: ${Class_Name}
 */

public class HttpUtils {
    //获得HttpUtils实例
    private static HttpUtils mInstance;

    private OkHttpClient okHttpClient;

    private static NetWorkConfiguration netWorkConfiguration;

    private Context context;

    /**
     * 是否加载本地缓存数据，默认为true
     */
    private boolean isLoadDiskCache = true;

    /**
     * -->针对无网络情况
     * 是否加载本地缓存数据
     *
     * @param isCache true为加载，false为不加载
     * @return
     */
    public HttpUtils setLoadDiskCache(boolean isCache) {
        this.isLoadDiskCache = isCache;
        return this;
    }

    /**
     * ---> 针对有网络情况
     * 是否加载内存缓存数据
     * 默认为false
     */
    private boolean isLoadMemoryCache = false;

    /**
     * ---> 针对有网络情况
     * 是否加载内存缓存数据
     *
     * @param isCache true为加载 false不进行加载
     * @return
     */
    public HttpUtils setLoadMemoryCache(boolean isCache) {
        this.isLoadMemoryCache = isCache;
        return this;
    }

    public HttpUtils(Context context) {
        this.context = context.getApplicationContext();
        /**
         * 如果配置为空，进行默认配置
         */
        if (null == netWorkConfiguration) {
            netWorkConfiguration = new NetWorkConfiguration(context.getApplicationContext());
        }
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //获得是否缓存
        if (netWorkConfiguration.getIsCache()) {
            okHttpClient = new OkHttpClient.Builder()
                    //打印log
//                    .addInterceptor(logInterceptor)
                    .addInterceptor(new LogInterceptor())
                    .addInterceptor(cacheInterceptor)
                    .addNetworkInterceptor(cacheInterceptor)
                    //缓存路径
                    .cache(netWorkConfiguration.getDiskCache())
                    .connectTimeout(netWorkConfiguration.getconnectTimeOut(), TimeUnit.SECONDS)
                    .connectionPool(netWorkConfiguration.getConnectionPool())
                    .retryOnConnectionFailure(true)
                    .build();
        } else {
            okHttpClient = new OkHttpClient.Builder()
//                    .addInterceptor(logInterceptor)
                    .addInterceptor(new LogInterceptor())
                    .connectTimeout(netWorkConfiguration.getconnectTimeOut(), TimeUnit.SECONDS)
                    .connectionPool(netWorkConfiguration.getConnectionPool())
                    .retryOnConnectionFailure(true)
                    .build();
        }
    }

    /**
     * 获取请求网络实例
     *
     * @return
     */
    public static HttpUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (HttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtils(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }


    /**
     * 设置网络配置参数
     *
     * @param configuration
     */
    public static void setConFiguration(NetWorkConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("ImageLoader configuration can not be initialized with null");
        } else {
            if (null == HttpUtils.netWorkConfiguration) {
                Logger.d("Initialize NetWorkConfiguration with configuration");

                HttpUtils.netWorkConfiguration = configuration;
            } else {
                Logger.e("Try to initialize NetWorkConfiguration which had already been initialized before. To re-init NetWorkConfiguration with new configuration ");
            }
        }
        if (configuration != null) {
            Logger.i("ConFiguration" + configuration.toString());
        }
    }

    public RetrofitClient getRetofitClinet() {
//        Logger.i("configuration:" + netWorkConfiguration.toString());
        return new RetrofitClient(netWorkConfiguration.getBaseUrl(), okHttpClient);
    }

    /**
     * 网络拦截器
     * 进行网络操作的时候进行拦截
     */
    final Interceptor cacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //断网后是否加载本地缓存数据
            if (!NetworkUtil.isNetworkAvailable(netWorkConfiguration.context) && isLoadDiskCache) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }//加载内存缓存数据
            else if (isLoadMemoryCache) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }//加载网络数据
            else {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            }
            Response response = chain.proceed(request);
            //有网进行内存缓存数据
            if (NetworkUtil.isNetworkAvailable(netWorkConfiguration.context) && netWorkConfiguration.getIsMemoryCache()) {
                response.newBuilder()
                        //清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + netWorkConfiguration.getmemoryCacheTime())
                        .build();
            } else {
                //进行本地缓存数据
                if (netWorkConfiguration.getIsDiskCache()) {
                    response.newBuilder()
                            //清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + netWorkConfiguration.getDiskCacheTime())
                            .build();
                }
            }
            return response;
        }
    };

}
