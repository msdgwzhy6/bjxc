package com.zxwl.frame.net.transformer;

import android.text.TextUtils;

import com.zxwl.frame.bean.DataList;
import com.zxwl.frame.net.exception.ExceptionHandle;
import com.zxwl.frame.net.exception.ServerException;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/10 08:59
 * ClassName: ${Class_Name}
 */

public class ListErrorTransformer<T> implements Observable.Transformer<DataList<T>, List<T>> {
    @Override
    public Observable<List<T>> call(Observable<DataList<T>> httpResponseObservable) {
        return httpResponseObservable.map(new Func1<DataList<T>, List<T>>() {
            @Override
            public List<T> call(DataList<T> response) {
                if(!TextUtils.isEmpty(response.result)){
                    throw new ServerException(response.message, 0x111);
                }
//                //如果不等于成功码
//                if (HttpResponse.SUCCEED_CODE != response.error) {
//                    //如果服务端有错误信息，那么抛出异常，让下面的方法去捕获异常做统一处理
//                    throw new ServerException(response.message, response.error);
//                }
                //服务器请求数据成功,返回里面的实体
                return  response.dataList;
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<? extends List<T>>>() {
            @Override
            public Observable<? extends List<T>> call(Throwable throwable) {
                throwable.printStackTrace();
                return Observable.error(ExceptionHandle.handleException(throwable));
            }
        });
    }

    public static <T> ListErrorTransformer<T> create(){
        return new ListErrorTransformer<>();
    }

    private static volatile ListErrorTransformer instance = null;

    private ListErrorTransformer(){}

    /**
     * 双重锁校验单例
     */
    public static <T> ListErrorTransformer<T> getInstance(){
        if (null==instance) {
            synchronized (ListErrorTransformer.class){
                if(null==instance){
                    instance = new ListErrorTransformer();
                }
            }
        }
        return instance;
    }

}
