package com.zxwl.frame.bean;

import java.io.Serializable;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/26 09:12
 * 会议的实体bean
 */
public class ConfBean implements Serializable {
    // Fields
    public String id;//id
    public String smcConfId;//会议ID
    public String beginTime;//会议开始时间
    //（由于建成后不是存储过程改动 要么就是显示和后台中改动，不方便所以做此改动）
    public String factBeginTime;//实际会议开始时间
    public String endTime;//预约会议结束时间
    public String factEndTime;//实际会议结束时间
    public String schedulingTime;//会议实际调度时间
    public String duration;//会议时长（单位分钟）
    public String createTime;//创建时间
    public String name;//会议名称
    public String peopleIdOa;//申请人id
    public String applyPeople;//会议申请人
    public String deptId;//主办部门id
    public String applyDept;//主办部门
    public String unitId;//主办单位id
    public String unit;//所属单位
    public String unitIdName;//
    public String oneArea;//一级区域
    public String twoArea;//二级区域
    public String threeArea;//三级区域
    public String fourArea;//四级区域
    public String confType;//会议类型  0=周期性视频会议、1=周期性非视频会议、2=视频会议、3=非视频会议、4=即时视频会议、5=即时非视频会议 6、周期性子视频会议 7、周期性子非视频会议
    public String confState;//会议状态 0=暂存 1=取消会议  10=等待审批 15=审批超时 20=等待召开 30=正在召开 40=会议异常  50=会议结束 5=审批驳回 （原来为：0：等待召开 1：正在召开 2：召开完毕  3 ：暂存  ）
    public String confMode;//会议模式 0：宣导模式		1：讨论模式
    public String confRange;//参会范围
    public String remarks;//会议备注
    //public String auditState;//审批状态 0：等待审批 1：审批通过 2：审批失败
    public String vetos;//审批意见
    public String checkPeopleIdOa;//审批人id
    public String checkPeople;//会议审批人
    public String checkTime;//审批时间
    public String staffCount;//参会人员数
    public String siteCount;//参会会场数
    public String state;//标记删除状态
    public String isEmail;//是否需要邮件 0：发送 1或者空为不需要发送
    public String isSms;//是否需要短信 0：发送 1或者空为不需要发送
    public String emailId;//邮件模版id
    public String emailTitle;//邮件模板标题
    public String emailContext;//邮件模版内容
    public String smsId;//短信模版id
    public String smsTitle;//短信模版标题
    public String smsContext;//短信模版内容
    public String isFirst;//时间是否从第一个值开始
    public String confExplain;//说明
    /*--sy add 2014- 8-9- ConfParameters表*/
    public String confParameters;//ConfParameters表id
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
    public String confException;//会议异常错误
    public String isKeepSecret;//是否保密(0:不保密 ，1：保密);
    public String departPath;//部门路径
    public String errorCode;//会议调度失败错误码
    public String cancelConfExplain;//错误原因
    public String parentId;//父节点id
    public String contactPeople;//联系人
    public String contactTelephone;//联系电话
    public String organizer;//承办部门

    public String deviceNumber;//设备数量
    public String deviceName;//设备名称
    public boolean select;//复选框是否选中
    public boolean showControl;//是否显示控制界面
//    public String select;//复选框是否选中

//    // Fields
//    private Integer id;//id
//    private String smcConfId;//会议ID
//    private String beginTime;//会议开始时间
//    //（由于建成后不是存储过程改动 要么就是显示和后台中改动，不方便所以做此改动）
//    private String factBeginTime;//实际会议开始时间
//    private String endTime;//预约会议结束时间
//    private String factEndTime;//实际会议结束时间
//    private String schedulingTime;//会议实际调度时间
//    private Integer duration;//会议时长（单位分钟）
//    private String createTime;//创建时间
//    private String name;//会议名称
//    private Integer peopleIdOa;//申请人id
//    private String applyPeople;//会议申请人
//    private Integer deptId;//主办部门id
//    private String applyDept;//主办部门
//    private Integer unitId;//主办单位id
//    private String unit;//所属单位
//    private Integer oneArea;//一级区域
//    private Integer twoArea;//二级区域
//    private Integer threeArea;//三级区域
//    private Integer fourArea;//四级区域
//    private Integer confType;//会议类型  0=周期性视频会议、1=周期性非视频会议、2=视频会议、3=非视频会议、4=即时视频会议、5=即时非视频会议 6、周期性子视频会议 7、周期性子非视频会议
//    private Integer confState;//会议状态 0=暂存 1=取消会议  10=等待审批 15=审批超时 20=等待召开 30=正在召开 40=会议异常  50=会议结束 5=审批驳回 （原来为：0：等待召开 1：正在召开 2：召开完毕  3 ：暂存  ）
//    private Integer confMode;//会议模式 0：宣导模式		1：讨论模式
//    private String confRange;//参会范围
//    private String remarks;//会议备注
//    //private Integer auditState;//审批状态 0：等待审批 1：审批通过 2：审批失败
//    private String vetos;//审批意见
//    private Integer checkPeopleIdOa;//审批人id
//    private String checkPeople;//会议审批人
//    private String checkTime;//审批时间
//    private Integer staffCount;//参会人员数
//    private Integer siteCount;//参会会场数
//    private Integer state;//标记删除状态
//    private Integer isEmail;//是否需要邮件 0：发送 1或者空为不需要发送
//    private Integer isSms;//是否需要短信 0：发送 1或者空为不需要发送
//    private Integer emailId;//邮件模版id
//    private String emailTitle;//邮件模板标题
//    private String emailContext;//邮件模版内容
//    private Integer smsId;//短信模版id
//    private String smsTitle;//短信模版标题
//    private String smsContext;//短信模版内容
//    private Integer isFirst;//时间是否从第一个值开始
//    private String confExplain;//说明
//    /*--sy add 2014- 8-9- ConfParameters表*/
//    private Integer confParameters;//ConfParameters表id
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
//    private String confException;//会议异常错误
//    private Integer isKeepSecret;//是否保密(0:不保密 ，1：保密);
//    private String departPath;//部门路径
//    private Integer errorCode;//会议调度失败错误码
//
//    private Integer cancelConfExplain;//错误原因
//
//    private Integer parentId;//父节点id
//    private String contactPeople;//联系人
//    private String contactTelephone;//联系电话
//    private String organizer;//承办部门
}
