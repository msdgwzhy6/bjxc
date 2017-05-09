package com.zxwl.frame.bean;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/26 11:13
 * 视频会议设置-->高级参数
 */
public class ConfParametersBean {
    public String id;//流水号
    public String name;//名称
    public String accessCode;//会议接入码
    public String password;//会议接入密码
    public String mediaEncrypptType;//媒体流加密方式
    public String auxVideoFormat;//辅流视频格式
    public String auxVideoProtocol;//辅流视频协议
    public String cpResouce;//多画面资源数
    public String rate;//速率
    public String isRecording;//会议是否支持录制
    public String recorderAddr;//录播地址
    public String isLiveBroadcast;//录播会议是否支持直播功能，默认不支持
    public String presentation;//演示方式
    public String chairmanPassword;//主席密码或会议启动密码
    public String billCode;//计费码
    public String state;//标记删除 0未删除 1已删除
    public String createTime;//创建时间
    public String isGsm;//是否支持电话桥
    public String isKeepSecret;//是否保密(0:不保密 ，1：保密);

    @Override
    public String toString() {
        return "ConfParametersBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", accessCode='" + accessCode + '\'' +
                ", password='" + password + '\'' +
                ", mediaEncrypptType='" + mediaEncrypptType + '\'' +
                ", auxVideoFormat='" + auxVideoFormat + '\'' +
                ", auxVideoProtocol='" + auxVideoProtocol + '\'' +
                ", cpResouce='" + cpResouce + '\'' +
                ", rate='" + rate + '\'' +
                ", isRecording='" + isRecording + '\'' +
                ", recorderAddr='" + recorderAddr + '\'' +
                ", isLiveBroadcast='" + isLiveBroadcast + '\'' +
                ", presentation='" + presentation + '\'' +
                ", chairmanPassword='" + chairmanPassword + '\'' +
                ", billCode='" + billCode + '\'' +
                ", state='" + state + '\'' +
                ", createTime='" + createTime + '\'' +
                ", isGsm='" + isGsm + '\'' +
                ", isKeepSecret='" + isKeepSecret + '\'' +
                '}';
    }

    //    private Integer id;//流水号
//    private String name;//名称
//    private String accessCode;//会议接入码
//    private String password;//会议接入密码
//    private Integer mediaEncrypptType;//媒体流加密方式
//    private Integer auxVideoFormat;//辅流视频格式
//    private Integer auxVideoProtocol;//辅流视频协议
//    private Integer cpResouce;//多画面资源数
//    private String rate;//速率
//    private Integer isRecording;//会议是否支持录制
//    private String recorderAddr;//录播地址
//    private Integer isLiveBroadcast;//录播会议是否支持直播功能，默认不支持
//    private Integer presentation;//演示方式
//    private String chairmanPassword;//主席密码或会议启动密码
//    private String billCode;//计费码
//    private Integer state;//标记删除 0未删除 1已删除
//    private Date createTime;//创建时间
//    private Integer isGsm;//是否支持电话桥
//    private Integer isKeepSecret;//是否保密(0:不保密 ，1：保密);
}
