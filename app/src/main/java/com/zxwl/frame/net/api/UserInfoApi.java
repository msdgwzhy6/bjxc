package com.zxwl.frame.net.api;

import com.zxwl.frame.bean.UserInfo;
import com.zxwl.frame.net.Urls;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/24 17:33
 * ClassName: ${Class_Name}
 */
public interface UserInfoApi {

    @POST(Urls.LOGIN)
    Observable<UserInfo> login(@Query("userName") String name, @Query("passWord") String pwd);

    @POST(Urls.CONFACTION_SAVECONF)
    Observable<UserInfo> saveConf( );

}
