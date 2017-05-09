package com.zxwl.frame.net.callback;

import com.orhanobut.logger.Logger;
import com.zxwl.frame.net.exception.ResponeThrowable;

import rx.Subscriber;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/20 12:45
 * ClassName: ${Class_Name}
 */

public abstract class ErrorSubscriber<T> extends Subscriber<T> {

    @Override
    public void onError(Throwable e) {
        Logger.e("错误信息：" + e.getMessage());
        if (e instanceof ResponeThrowable) {
            onError((ResponeThrowable) e);
        } else {
            onError(new ResponeThrowable(e, 1000));
        }
    }

    /**
     * 错误的回调
     */
    protected abstract void onError(ResponeThrowable responeThrowable);
}
