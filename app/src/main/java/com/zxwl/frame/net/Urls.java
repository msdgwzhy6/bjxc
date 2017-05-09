package com.zxwl.frame.net;

/**
 * author：hw
 * data:2017/4/20 11:25
 * ClassName: ${Class_Name}
 */

public class Urls {
    //    http://192.168.222.137/hbsw/myLoginAction_AppTplogin.action?userName=admin&passWord=admin
//    public static final String BASE_URL = "http://192.168.222.137/hbsw/";
    public static final String BASE_URL = "http://192.168.0.134:8080/bjxc/";

    /**
     * 登录的url
     */
    public static final String LOGIN = "myLoginAction_AppToLogin.action";

    // http://192.168.200.142:8080/hbsw/confAction_saveConf.action?
    // contactList=_TN_C1,-3882,3883,3884,3885,     参会列表    ok
    // &confParameters=26                           参数ID  ok
    // &conf.name=qwer                              会议名称    ok
    // &conf.schedulingTime=2017-04-25 14:45:00     开始时间    ok
    // &conf.endTime=2017-04-25 15:15:00            结束时间    ok
    // &conf.duration=30                            持续时间
    // &conf.isEmail=0                              是否用email通知  ok
    // &conf.isSms=1                                是否用短信通知     ok
    // &conf.smsId=12                               短信Id            ok
    // &conf.smsTitle=预约通过模板（勿删除）          短信标题            ok
    // &conf.smsContext=                            短信内容      <p>各位领导、同事：</p><p><br />兹定于<--会议时间-->召开<--会议名称-->，请您准时参加</p>
    // &conf.contactPeople=qweqwr                   联系人         ok
    // &conf.contactTelephone=123123                联系电话    ok
    // &operatorId=1240                             操作人id   ok
    /**
     * 预约会议
     */
    public static final String CONFACTION_SAVECONF = "confAction_saveConf.action";

    /**
     * 查询通讯录
     */
    public static final String QUERY_ALL_DEPARTMENT = "departmentAction_queryAllDepartment.action";

    /**
     * 查询会议历史列表
     * 会议状态: confState=50
     * 0=暂存 1=取消会议  10=等待审批 15=审批超时 20=等待召开 30=正在召开 40=会议异常  50=会议结束 5=审批驳回 （原来为：0：等待召开 1：正在召开 2：召开完毕  3 ：暂存  ）
     */
    public static final String QUERY_HISTORY_LIST = "confAction_queryList1.action?confState=50";

    /**
     * 查询会议待审批列表
     * 会议状态: confState=10
     * 0=暂存 1=取消会议  10=等待审批 15=审批超时 20=等待召开 30=正在召开 40=会议异常  50=会议结束 5=审批驳回 （原来为：0：等待召开 1：正在召开 2：召开完毕  3 ：暂存  ）
     */
    public static final String QUERY_APPROVAL_LIST = "confAction_queryList1.action?confState=10";

    /**
     * 查询正在召开的会议列表
     * 会议状态: confState=30
     * 0=暂存 1=取消会议  10=等待审批 15=审批超时 20=等待召开 30=正在召开 40=会议异常  50=会议结束 5=审批驳回 （原来为：0：等待召开 1：正在召开 2：召开完毕  3 ：暂存  ）
     */
    public static final String QUERY_BEING_LIST = "confAction_queryList1.action?confState=30";

    /**
     * 查询会议模板列表
     */
    public static final String QUERY_TEMPLATE_LIST = "confModelAction_queryList.action";

    /**
     * 查询常用群组列表
     */
    public static final String QUERY_MANAGEMENTGROUP_LIST = "managementGroupAction_queryList.action";

    /**
     * 查询参数列表
     */
    public static final String QUERY_CONF_PARAMETERS_LIST = "confParametersAction_queryList.action";

    /**
     * 查询短信模板列表
     */
    public static final String QUERY_SMSMODEL_LIST = "smsModelAction_queryList.action";

    /**
     * 通过id查询会议历史的参会人员列表
     * 参数 ：id
     */
    public static final String QUERY_HISTORY_ID = "confAction_queryContart.action";

    /**
     * 通过id查询会议模板的参会人员列表
     * 参数 ：id
     */
    public static final String QUERY_TEMPLATE_ID = "confModelAction_queryContart.action";

    /**
     * 通过id查询常用群组
     * 参数 ：id
     */
    public static final String QUERY_MANAGEMENTGROUP_ID = "managementGroupAction_queryContart.action";

    /**
     * 通过id查询部门信息
     * 参数 ：id
     */
    public static final String QUERY_DEPARTMENT_ID = "departmentAction_query.action";

    /**
     * 通过id查询设备信息
     * 参数 ：id
     */
    public static final String QUERY_EMPLOYEE_ID = "employeeAction_query.action";

    /**
     * 通过contactList查询参加会议的人员列表
     * 参数 ：contactList
     */
    public static final String QUERY_PEOPLE_LIST = "peopleAction_queryOrgNo.action";

    /**
     * 会议审批--->通过
     */
    public static final String APPROVE_ENTITY = "confAction_approveEntity.action";

    /**
     * 会议审批--->驳回
     * 参数：conf.id  会议id
     */
    public static final String APPROVE_CANCEL = "confAction_cancel.action";

    /**
     * 进入会控界面-2
     * 参数：smcConfId  会议id
     * return ：会场map SiteMap
     */
    public static final String JOIN_TO_CONF = "confControlAction_toRefreshVideoConf1.action";

    /**
     * 结束会议
     * 参数：
     * smcConfId
     * confId
     */
    public static final String FINISH_CONF = "confControlAction_cancelConf.action";

    /**
     * 会议操作: 移除会场
     * 参数：
     * smcConfId
     * confId
     * siteUris
     */
    public static final String DEL_SITE_FROM_CONF = "confControlAction_delSiteFromConf.action";

    /**
     * 会议操作：设置主席
     * 参数：
     * smcConfId
     * siteUris
     */
    public static final String REQUEST_CHAIR = "confControlAction_requestChair.action";

    /**
     * 会议操作：取消主席
     * 参数：
     * smcConfId
     * siteUris
     */
    public static final String RELEASE_CHAIR = "confControlAction_releaseChair.action";


    /**
     * 会议控制：声控切换
     * 参数:
     * smcConfId
     * isSwitch: 1 开启，0 关闭
     */
    public static final String SET_AUDIO_SWITCH = "confControlAction_setAudioSwitch.action";

    /**
     * 会议控制：广播会场
     * 参数:
     * smcConfId
     * siteUris
     * isBroadcast: isBroadcast 0开始  1关闭
     */
    public static final String SET_BROADCAST_SITE = "confControlAction_setBroadcastSite.action";


    /**
     * 会场控制：喇叭静音(会场声音)
     * 参数:
     * smcConfId
     * siteUris
     * isQuiet: 1 取消静音，0 静音喇叭
     */
    public static final String CHANGE_SITE_IS_QUIET = "confControlAction_changeSiteIsQuiet.action";

    /**
     * 会场控制：麦克风静音
     * 参数:
     * smcConfId
     * siteUris
     * isMute: 1 取消静音，0 静音麦克风
     */
    public static final String CHANGE_SITE_IS_MUTE = "confControlAction_changeSiteIsMute.action";


    /**
     * 会场控制：呼叫会场
     * 参数:
     * smcConfId
     * siteUris
     */
    public static final String CALL_SITE = "confControlAction_callSite.action";

    /**
     * 会场控制：断开会场
     * 参数:
     * smcConfId
     * siteUris
     */
    public static final String DISCONNECT_SITE = "confControlAction_disconnectVideoSite.action";


    //    confControlAction_changeIsSplitScreen.action
    //    smcConfId:smcConfId,splitFlag:splitFlag
    //    splitFlag=0 开始   splitFlag=1 停止
}
