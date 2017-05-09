package com.zxwl.frame.bean;

import java.util.List;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/5/8 11:08
 * 会议的bean的第一段数据
 */
public class ConferenceInfo {
    /*
     * accessCode : 02710093
     * auxVideoFormat :
     * auxVideoProtocol :
     * beginTime :
     * billCode :
     * chairmanPassword :
     * chairmanUuid :
     * commonUuid :
     * confId : 2717
     * confMediaType :
     * conferenceNotice :
     * continuousPresenceMode :
     * cpResouce :
     * duration :
     * isDataConference :
     * isLiveBroadcast :
     * isRecording :
     * mainMcuId :
     * mainSiteUri :
     * maxSitesCount :
     * mediaEncryptType :
     * name :
     * password :
     * presentation :
     * rate :
     * recordParam :
     * recorderAddr :
     * reservedCsdPorts :
     * sites : [{"dialingMode":"","dtmf":"","from":"","isLockVideoSource":"","mcuId":"","name":"杨柳","participantType":"","rate":"","status":"","type":"","uri":"0724131","videoFormat":"","videoProtocol":""},{"dialingMode":"","dtmf":"","from":"","isLockVideoSource":"","mcuId":"","name":"刘明","participantType":"","rate":"","status":"","type":"","uri":"0724001","videoFormat":"","videoProtocol":""}]
     * status :
     */
    public String confId;//会议id
    public String name;//会议名称
    public String beginTime;//会议开始时间
    public String duration;//会议时长（单位分钟）
    public String accessCode;//会议接入码，必须为数字格式的字符串，如“075560166”
    public String password;//会议接入密码
    public String auxVideoFormat;//辅流视频格式
    public String auxVideoProtocol;//辅流视频协议
    public String billCode;//计费码
    public String chairmanPassword;//主席密码或会议启动密码
    public String chairmanUuid;//主席唯一标识符
    public String commonUuid;
    public String confMediaType;//会议媒体类型
    public String conferenceNotice;
    public String continuousPresenceMode;
    public String cpResouce;//多画面资源数
    public String isDataConference;
    public String isLiveBroadcast;//录播会议是否支持直播功能，默认不支持
    public String isRecording;//会议是否支持录制
    public String mainMcuId;
    public String mainSiteUri;
    public String maxSitesCount;
    public String mediaEncryptType;//媒体流加密方式
    public String presentation;//演示方式
    public String rate;//速率
    public String recordParam;
    public String recorderAddr;//录播地址
    public String reservedCsdPorts;
    public String status;//状态

    public List<SitesBean> sites;
}
