package com.zxwl.frame.bean;

import com.huawei.esdk.tp.professional.local.bean.ConferenceInfoEx;
import com.huawei.esdk.tp.professional.local.bean.ConferenceStatusEx;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/5/5 11:35
 * 会议对象实体
 */
public class Conference {
    private Map<String, Site> siteList = new HashMap<String, Site>();
    private List<Map<String, Site>> mapList = new ArrayList<Map<String, Site>>();
//    private StateScanThread thread;
    private Integer confId;//会议记录ID
    private String chairUri;//主席会场URI
    private Integer confMode;//会议模式（0：宣导	1：讨论）
    private ConferenceInfoEx conferenceInfoEx = new ConferenceInfoEx();
    private ConferenceStatusEx conferenceStatusEx = new ConferenceStatusEx();
    private Boolean refresh = false;// 是否开始刷新会议信息
    private List<String> myConfIds;//自己的会议ID
    private int conferenceOldState; // 会议前一次状态
    private Date conferenceOldEndTime;// 会议原来的结束时间
    private boolean cascadeFlag;//级联会议标识（true:级联会议		false:非级联会议）
    private boolean exceptionFlag;//异常标记 （true:异常	 false:正常）
    private SplitScreenInfo splitScreenInfo; //分屏多画面信息
    private ChairPollInfo chairPollInfo;//主席轮询信息
    private int errorCode;//会议调度失败返回错误码
    private Map<String, Site> specialSiteList = new HashMap<String, Site>();//特殊会场MAP（会议调度成功后再加入到会议中去）
    // add by wsw
    private String smcUserName; // smc用户名
    private String smcPassword; // smc密码

    @Override
    public String toString() {
        return "Conference{" +
                "siteList=" + siteList +
                ", mapList=" + mapList +
                ", confId=" + confId +
                ", chairUri='" + chairUri + '\'' +
                ", confMode=" + confMode +
                ", conferenceInfoEx=" + conferenceInfoEx +
                ", conferenceStatusEx=" + conferenceStatusEx +
                ", refresh=" + refresh +
                ", myConfIds=" + myConfIds +
                ", conferenceOldState=" + conferenceOldState +
                ", conferenceOldEndTime=" + conferenceOldEndTime +
                ", cascadeFlag=" + cascadeFlag +
                ", exceptionFlag=" + exceptionFlag +
                ", splitScreenInfo=" + splitScreenInfo +
                ", chairPollInfo=" + chairPollInfo +
                ", errorCode=" + errorCode +
                ", specialSiteList=" + specialSiteList +
                ", smcUserName='" + smcUserName + '\'' +
                ", smcPassword='" + smcPassword + '\'' +
                '}';
    }

    //    private Map<String, Site> siteList = new HashMap<String, Site>();
//    private List<Map<String, Site>> mapList = new ArrayList<Map<String, Site>>();
//    private StateScanThread thread;
//    private Integer confId;//会议记录ID
//    private String chairUri;//主席会场URI
//    private Integer confMode;//会议模式（0：宣导	1：讨论）
//    private ConferenceInfoEx conferenceInfoEx = new ConferenceInfoEx();
//    private ConferenceStatusEx conferenceStatusEx = new ConferenceStatusEx();
//    private Boolean refresh = false;// 是否开始刷新会议信息
//    private List<String> myConfIds;//自己的会议ID
//    private int conferenceOldState; // 会议前一次状态
//    private Date conferenceOldEndTime;// 会议原来的结束时间
//    private boolean cascadeFlag;//级联会议标识（true:级联会议		false:非级联会议）
//    private boolean exceptionFlag;//异常标记 （true:异常	 false:正常）
//    private SplitScreenInfo splitScreenInfo; //分屏多画面信息
//    private ChairPollInfo chairPollInfo;//主席轮询信息
//    private int errorCode;//会议调度失败返回错误码
//    private Map<String, Site> specialSiteList = new HashMap<String, Site>();//特殊会场MAP（会议调度成功后再加入到会议中去）
//    // add by wsw
//    private String smcUserName; // smc用户名
//    private String smcPassword; // smc密码
}
