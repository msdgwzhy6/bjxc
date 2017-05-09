package com.zxwl.frame.net.callback;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/20 12:58
 * ClassName: ${Class_Name}
 */

public abstract class RxSubscriber<T> extends ErrorSubscriber<T> {
    /**
     * 开始请求网络
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    //请求完成
    @Override
    public void onCompleted() {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    public abstract void onSuccess(T t);
}
