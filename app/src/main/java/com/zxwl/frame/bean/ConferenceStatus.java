package com.zxwl.frame.bean;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/5/8 11:10
 * 会议状态
 */
public class ConferenceStatus {
    public String id;//会议id
    /**
     * beginTime : 2017-05-08 10-58-29
     * broadcast :
     * chair :
     * endTime : 2017-05-08 12-14-29
     * id : 2717
     * isAudioSwitch : 0
     * isLock : 0
     * isLockPresentation :
     * lockPresentation :
     * name : 0508_3
     * presentation :
     * speaking :
     * status : 3
     * switchGate : 100
     */
    public String name;//会议名称
    public String broadcast;//广播会场
    public String chair;//主席会场
    public String beginTime;//会议开始时间
    public String endTime;//结束时间
    public int isAudioSwitch;//会议是否开启声控切换功能  0：打开  1：关闭
    public String isLock;//会议是否锁定   0：否1：是
    public String isLockPresentation;//会议是否处于锁定辅流令牌状态。  0：否 1：是
    public String lockPresentation;//锁定辅流令牌的会场
    public String presentation;//正在发送辅流的会场。
    public String speaking;//正在发言的会场（音量最大的会场）。

    /**会议状态
     * 0：未知状态（保留）1：会议不存在2：会议已调度，未召开3：会议已经召开4：没有权限查询会议状态5：会议已经结束
     */
    public String status;//会议状态。

    //isAudioSwitch设置为0时，表示启用默认值（门限默认值为15），或当前会议的历史设置值（此会议曾经设置过该值）。
    //isAudioSwitch设置为其他值，表示对应的门限值。
    public String switchGate;//声控切换门限值 取值范围为0～100

}
