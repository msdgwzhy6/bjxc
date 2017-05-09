package com.zxwl.frame.net.interceptor;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/20 14:29
 * ClassName: ${Class_Name}
 */

public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(chain.request());
        MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        long t1 = System.nanoTime();
        Logger.i(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
        long t2 = System.nanoTime();
//        Logger.i(String.format("Received response for %s include_common_operation %s.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
//        Logger.i(String.format("Received response for %s include_common_operation %.1fms%n%s", response.request().url().toString(), (t2 - t1) / 1e6d, response.headers()));
        Logger.i("NetWork", "response body:" + content);
        if (response.body() != null) {
            ResponseBody body = ResponseBody.create(mediaType, content);
            return response.newBuilder().body(body).build();
        }
        return response;
    }
}
