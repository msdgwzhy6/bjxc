package com.zxwl.frame.bean;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/5/8 11:06
 * 会场信息的bean
 */
public class SitesBean {
    public String name;//会场名称
    public String uri;//会场标识
    public String type;//会场类型
    public String from;//会场来源。
    public String dialingMode;//呼叫方式   0：MCU 呼叫会场 1：会场主动呼入
    public String rate;//速率。格式为“速率值k/M”，如“1920k”。
    public String videoFormat;//视频格式
    public String videoProtocol;//视频协议
    public String isLockVideoSource;//是否锁定会场视频源，默认为不锁定。0：不锁定    1：锁定
    public String dtmf;//二次拨号信息。
    public String participantType;//与会类型，默认为终端会场
    public String mcuId;
    public String status;
}
