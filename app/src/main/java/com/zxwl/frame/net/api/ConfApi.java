package com.zxwl.frame.net.api;

import com.zxwl.frame.bean.ConfBean;
import com.zxwl.frame.bean.ConfParametersBean;
import com.zxwl.frame.bean.DataList;
import com.zxwl.frame.bean.DepartBean;
import com.zxwl.frame.bean.ManagementGroupBean;
import com.zxwl.frame.bean.SMSBean;
import com.zxwl.frame.net.Urls;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/26 14:52
 * 关于会议的api
 */
public interface ConfApi {

    /**
     * 获得高级参数列表
     *
     * @return
     */
    @GET(Urls.QUERY_CONF_PARAMETERS_LIST)
    Observable<DataList<ConfParametersBean>> getConfParametersBeanList();

    /**
     * 获得历史会议列表
     *
     * @return
     */
    @GET(Urls.QUERY_HISTORY_LIST)
    Observable<DataList<ConfBean>> getHistoryList();

    /**
     * 获得会议模板列表
     *
     * @return
     */
    @GET(Urls.QUERY_TEMPLATE_LIST)
    Observable<DataList<ConfBean>> getTemplateList();

    /**
     * 获得短信模板列表
     *
     * @return
     */
    @GET(Urls.QUERY_SMSMODEL_LIST)
    Observable<DataList<SMSBean>> getSmsModelList();

    /**
     * 获得常用群组列表
     *
     * @return
     */
    @GET(Urls.QUERY_MANAGEMENTGROUP_LIST)
    Observable<DataList<ManagementGroupBean>> getManagementGroupList();


    /**
     * 通过ID获得历史会议信息
     *
     * @return
     */
    @GET(Urls.QUERY_HISTORY_ID)
    Observable<String> findHistoryById(@Query("id") String id);

    /**
     * 通过ID获得会议模板信息
     *
     * @return
     */
    @GET(Urls.QUERY_TEMPLATE_ID)
    Observable<String> findTemplateById(@Query("id") String id);

    /**
     * 通过contactList查询参加会议的人员列表
     *
     * @return
     */
    @GET(Urls.QUERY_PEOPLE_LIST)
    Observable<DataList<DepartBean>> getPeopleList(@Query("contactList") String contactList);


    /**
     * 获得待审批会议列表
     *
     * @return
     */
    @GET(Urls.QUERY_APPROVAL_LIST)
    Observable<DataList<ConfBean>> getConfApprovalList(@Query("pageSize") int pageSize, @Query("pageNum") int pageNum);

    /**
     * 获得正在召开的会议列表
     *
     * @return
     */
    @GET(Urls.QUERY_BEING_LIST)
    Observable<DataList<ConfBean>> getConfBeinglList(@Query("pageSize") int pageSize, @Query("pageNum") int pageNum);

    /**
     * 预约会议
     *
     * @param contactList
     * @param confParameters
     * @param name
     * @param schedulingTime
     * @param endTime
     * @param duration
     * @param isEmail
     * @param isSms
     * @param smsId
     * @param smsTitle
     * @param smsContext
     * @param contactPeople
     * @param contactTelephone
     * @param operatorId
     * @return
     */
    @POST(Urls.CONFACTION_SAVECONF)
    Observable<String> saveConf(@Query("contactList") String contactList,
                                @Query("confParameters") String confParameters,
                                @Query("conf.name") String name,
                                @Query("conf.schedulingTime") String schedulingTime,
                                @Query("conf.endTime") String endTime,
                                @Query("conf.duration") String duration,
                                @Query("conf.isEmail") String isEmail,
                                @Query("conf.isSms") String isSms,
                                @Query("conf.smsId") String smsId,
                                @Query("conf.smsTitle") String smsTitle,
                                @Query("conf.smsContext") String smsContext,
                                @Query("conf.contactPeople") String contactPeople,
                                @Query("conf.contactTelephone") String contactTelephone,
                                @Query("operatorId") String operatorId);


    /**
     * 会议审核-->通过
     *
     * @param contactList
     * @param confParameters
     * @param name
     * @param schedulingTime
     * @param endTime
     * @param duration
     * @param isEmail
     * @param isSms
     * @param smsId
     * @param smsTitle
     * @param smsContext
     * @param contactPeople
     * @param contactTelephone
     * @param operatorId
     * @param condId
     * @param vetos
     * @return
     */
    @GET(Urls.APPROVE_ENTITY)
    Observable<String> approveEntity(@Query("contactList") String contactList,
                                     @Query("confParameters") String confParameters,
                                     @Query("conf.name") String name,
                                     @Query("conf.schedulingTime") String schedulingTime,
                                     @Query("conf.endTime") String endTime,
                                     @Query("conf.duration") String duration,
                                     @Query("conf.isEmail") String isEmail,
                                     @Query("conf.isSms") String isSms,
                                     @Query("conf.smsId") String smsId,
                                     @Query("conf.smsTitle") String smsTitle,
                                     @Query("conf.smsContext") String smsContext,
                                     @Query("conf.contactPeople") String contactPeople,
                                     @Query("conf.contactTelephone") String contactTelephone,
                                     @Query("operatorId") String operatorId,
                                     @Query("conf.id") String condId,
                                     @Query("conf.vetos") String vetos
    );

    /**
     * 会议审核--》驳回
     *
     * @param id
     * @return
     */
    @GET(Urls.APPROVE_CANCEL)
    Observable<String> approveCancel(@Query("conf.id") String id);

    /**
     * 进入会议控制界面
     * 返回会议对象实体
     * Conference
     * smcConfId 2684
     *
     * @return
     */
    @GET(Urls.JOIN_TO_CONF)
    Observable<String> joinTOConf(@Query("smcConfId") String smcConfId);


    /**
     * 会议控制：结束会议
     *
     * @param smcConfId
     * @param confId
     * @return
     */
    @GET(Urls.FINISH_CONF)
    Observable<String> finishConf(@Query("smcConfId") String smcConfId,
                                  @Query("confId") String confId);


    /**
     * 会议控制：移除会场
     *
     * @param smcConfId
     * @param confId
     * @param siteUris
     * @return
     */
    @GET(Urls.DEL_SITE_FROM_CONF)
    Observable<String> delSiteFromConf(@Query("smcConfId") String smcConfId,
                                       @Query("confId") String confId,
                                       @Query("siteUris") String siteUris);

    /**
     * 会议操作：设置主席
     */
    @GET(Urls.REQUEST_CHAIR)
    Observable<String> requestChair(@Query("smcConfId") String smcConfId,
                                    @Query("siteUris") String siteUris);

    /**
     * 会议操作：取消主席
     */
    @GET(Urls.RELEASE_CHAIR)
    Observable<String> releaseChair(@Query("smcConfId") String smcConfId,
                                    @Query("siteUris") String siteUris);

    /**
     * 会议操作：声控切换
     *
     * @param smcConfId
     * @param isSwitch  1 开启，0 关闭
     */
    @GET(Urls.SET_AUDIO_SWITCH)
    Observable<String> audioSwitch(@Query("smcConfId") String smcConfId,
                                   @Query("isSwitch") String isSwitch);

    /**
     * 会议控制：广播会场
     *
     * @param smcConfId
     * @param siteUris
     * @param isBroadcast 0开始  1关闭
     * @return
     */
    @GET(Urls.SET_BROADCAST_SITE)
    Observable<String> broadcastSite(@Query("smcConfId") String smcConfId,
                                     @Query("siteUris") String siteUris,
                                     @Query("isBroadcast") String isBroadcast);

    /**
     * 会场控制：喇叭静音(会场声音)
     *
     * @param smcConfId
     * @param siteUris
     * @param isQuiet:  1 取消静音，0 静音喇叭
     */
    @GET(Urls.CHANGE_SITE_IS_QUIET)
    Observable<String> changeSiteIsQuiet(@Query("smcConfId") String smcConfId,
                                         @Query("siteUris") String siteUris,
                                         @Query("isQuiet") String isQuiet);

    /**
     * 会场控制：麦克风静音
     * 参数:
     *
     * @param smcConfId
     * @param siteUris
     * @param isMute:   1 取消静音，0 静音麦克风
     */
    @GET(Urls.CHANGE_SITE_IS_MUTE)
    Observable<String> changeSiteIsMute(@Query("smcConfId") String smcConfId,
                                        @Query("siteUris") String siteUris,
                                        @Query("isMute") String isMute);

    /**
     * 会场控制:呼叫会场
     * 参数:
     *
     * @param smcConfId
     * @param siteUris
     */
    @GET(Urls.CALL_SITE)
    Observable<String> callSite(@Query("smcConfId") String smcConfId,
                                @Query("siteUris") String siteUris);
    /**
     * 会场控制:断开会场
     * 参数:
     *
     * @param smcConfId
     * @param siteUris
     */
    @GET(Urls.DISCONNECT_SITE)
    Observable<String> disconnectSite(@Query("smcConfId") String smcConfId,
                                @Query("siteUris") String siteUris);

}

