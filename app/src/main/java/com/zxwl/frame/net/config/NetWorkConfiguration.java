package com.zxwl.frame.net.config;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.utils.L;
import com.zxwl.frame.App;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/20 13:46
 * 对网络进行配置
 */
public class NetWorkConfiguration {
    //是否进行缓存
    private boolean isCache;

    //是否进行磁盘缓存
    private boolean isDiskCache;

    //是否进行内存缓存
    private boolean isMemoryCache;

    //内存缓存时间单位 S：秒  (默认60s)
    private int memoryCacheTime;

    //本地缓存时间单位 S：秒  (默认4周)
    private int diskCacheTime;

    //缓存本地大小 单位字节 (默认为30M)
    private int maxDiskCacheSize;

    //缓存路径
    private Cache diskCache;

    //设置超时时间
    private int connectTimeOut;

    //设置网络最大连接数
    private ConnectionPool connectionPool;

    public Context context;

    private String baseUrl;

    public NetWorkConfiguration(Context content) {
        this.context = content.getApplicationContext();
        this.isCache = false;
        this.isDiskCache = false;
        this.isMemoryCache = false;
        this.memoryCacheTime = 60;
        this.diskCacheTime = 60 * 60 * 24 * 28;
        this.maxDiskCacheSize = 30 * 1024 * 1024;
        this.diskCache = new Cache(new File(App.getInstance().getCacheDir(), "httpCache"), maxDiskCacheSize);
        this.connectTimeOut = 10000;
        this.connectionPool = new ConnectionPool(50, 60, TimeUnit.SECONDS);
        baseUrl = null;
    }

    /**
     * 设置是否进行缓存
     *
     * @param iscache
     * @return
     */
    public NetWorkConfiguration isCache(boolean iscache) {
        this.isCache = iscache;
        return this;
    }

    public boolean getIsCache() {
        return this.isCache;
    }

    /**
     * 是否进行磁盘缓存
     *
     * @param diskcache
     * @return
     */
    public NetWorkConfiguration isDiskCache(boolean diskcache) {
        this.isDiskCache = diskcache;
        return this;
    }

    public boolean getIsDiskCache() {
        return this.isDiskCache;
    }

    /**
     * 是否进行内存缓存
     *
     * @param memorycache
     * @return
     */
    public NetWorkConfiguration isMemoryCache(boolean memorycache) {
        this.isMemoryCache = memorycache;
        return this;
    }

    public boolean getIsMemoryCache() {
        return this.isMemoryCache;
    }

    /**
     * 设置内存缓存时间
     *
     * @param memorycachetime
     * @return
     */
    public NetWorkConfiguration memoryCacheTime(int memorycachetime) {
        if (memorycachetime <= 0) {
            Logger.e("configure memoryCacheTime  exception!");
            return this;
        }
        this.memoryCacheTime = memorycachetime;
        return this;
    }

    public int getmemoryCacheTime() {
        return this.memoryCacheTime;
    }

    /**
     * 设置本地缓存时间
     *
     * @param diskcahetime
     * @return
     */
    public NetWorkConfiguration diskCacheTime(int diskcahetime) {
        if (diskcahetime <= 0) {
            Logger.e("configure diskCacheTime  exception!");
            return this;
        }
        this.diskCacheTime = diskcahetime;
        return this;
    }

    public int getDiskCacheTime() {
        return this.diskCacheTime;
    }

    /**
     * 设置本地缓存路径以及 缓存大小
     *
     * @param saveFile         本地路径
     * @param maxDiskCacheSize 大小
     * @return
     */
    public NetWorkConfiguration diskCaChe(File saveFile, int maxDiskCacheSize) {
        if (!saveFile.exists() && maxDiskCacheSize <= 0) {
            Logger.e("configure connectTimeOut  exception!");
            return this;
        }
        diskCache = new Cache(saveFile, maxDiskCacheSize);
        return this;
    }

    public Cache getDiskCache() {
        return this.diskCache;
    }

    /**
     * 设置网络超时时间
     *
     * @param timeout
     * @return
     */
    public NetWorkConfiguration connectTimeOut(int timeout) {
        if (connectTimeOut <= 0) {
            Logger.e("configure connectTimeOut  exception!");
            return this;
        }
        this.connectTimeOut = timeout;
        return this;
    }

    public int getconnectTimeOut() {
        return this.connectTimeOut;
    }

    /**
     * 设置网络线程池
     *
     * @param connectionCount 线程个数
     * @param connectionTime  连接时间
     * @param unit            时间单位
     * @return
     */
    public NetWorkConfiguration connectionPool(int connectionCount, int connectionTime, TimeUnit unit) {
        /**
         *    线程池 线程个数和连接时间设置过小
         */
        if (connectionCount <= 0 && connectionTime <= 0) {
            return this;
        }
        this.connectionPool = new ConnectionPool(connectionCount, connectionTime, unit);
        return this;
    }

    public ConnectionPool getConnectionPool() {
        return this.connectionPool;
    }

    /**
     * 设置网络BaseUrl地址
     *
     * @param url
     * @return
     */
    public NetWorkConfiguration baseUrl(String url) {
        if (url != null) {
            this.baseUrl = url;
        } else {
            L.e("NetWorkConfiguration no  baseUrl");
        }
        return this;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    @Override
    public String toString() {
        return "NetWorkConfiguration{" +
                "isCache=" + isCache +
                ", isDiskCache=" + isDiskCache +
                ", isMemoryCache=" + isMemoryCache +
                ", memoryCacheTime=" + memoryCacheTime +
                ", diskCacheTime=" + diskCacheTime +
                ", maxDiskCacheSize=" + maxDiskCacheSize +
                ", diskCache=" + diskCache +
                ", connectTimeOut=" + connectTimeOut +
                ", connectionPool=" + connectionPool +
                ", context=" + context +
                ", baseUrl='" + baseUrl + '\'' +
                '}';
    }

}
