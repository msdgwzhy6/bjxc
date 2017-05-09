package com.zxwl.frame.bean;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/5/8 17:44
 * ClassName: ${Class_Name}
 */

public class SiteStatus {
    public String uri;//会场标识
    public String name;//会场名称
    /**
     * 0：未知状态（保留）
     * 1：会场不存在
     * 2：在会议中
     * 3：未入会
     * 4：正在呼叫
     * 5：正在振铃
     */
    public String status;//会场状态
    public String volume;//会场音量值
    public String videoSource;//会场的视频源
    public String isMute;//是否闭音  0：闭音  1：不闭音
    public String isQuiet;//是否静音  0：静音 1：不静音
    public String type;//会场类型
    public String isLocalVideoOpen;//会场本地视频是否打开 0：否    1：是
    /**
     * 0：终端会场
     * 1：上级级联通道
     * 2：下级级联通道
     * 3：融合网关通道
     * 4：可视化调度台
     * 5：多媒体级联会场
     * 6：WEB虚拟会场
     */
    public String participantType;//与会者类型，默认为终端会场。
    public String screens;//会场有多屏时，表示会场的屏数。会场屏数是1或者为语音会场时，此参数置空。
}
