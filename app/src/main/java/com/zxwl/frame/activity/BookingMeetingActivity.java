package com.zxwl.frame.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;
import com.zxwl.frame.R;
import com.zxwl.frame.adapter.AttendingListAdapter;
import com.zxwl.frame.adapter.HistoryListAdapter;
import com.zxwl.frame.bean.ConfBean;
import com.zxwl.frame.bean.ConfParametersBean;
import com.zxwl.frame.bean.SMSBean;
import com.zxwl.frame.bean.UserInfo;
import com.zxwl.frame.net.Urls;
import com.zxwl.frame.net.api.ConfApi;
import com.zxwl.frame.net.callback.JsonGenericsSerializator;
import com.zxwl.frame.net.callback.RxSubscriber;
import com.zxwl.frame.net.exception.ResponeThrowable;
import com.zxwl.frame.net.http.HttpUtils;
import com.zxwl.frame.net.transformer.ListDefaultTransformer;
import com.zxwl.frame.utils.DateUtil;
import com.zxwl.frame.utils.DisplayUtil;
import com.zxwl.frame.utils.UserHelper;
import com.zxwl.frame.views.spinner.NiceSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


/**
 * 预约会议，作废
 */
public class BookingMeetingActivity extends BaseActivity implements View.OnClickListener {
    private EditText etName;//会议名称
    private EditText etContactsName;//联系人名称
    private EditText etPhone;//联系人电话
    private NiceSpinner spinner;//选择高级参数
    private TextView tvHistory;//从历史生成
    private TextView tvTemplate;//从模板生成
    private TextView tvContactList;//通讯录
    private TextView tvCommonGroup;//常用群组
    private TextView tvDelete;//删除
    private TextView tvConfNumber;//会议号码
    private EditText etConfNumber;//会议号码
    private TextView tvParticipantInfo;//参会单位和设备数
    private EditText etRemark;//备注信息
    private TextView tvSave;//保存
    private TextView tvJoinNumber;//已参会列表的全选按钮
    private TextView tvTimeLable;//会议开始时间
    private TextView tvDurationLable;//会议结束和持续时间
    private TwinklingRefreshLayout refreshLayout;//刷新
    private RecyclerView rvAttendingList;//参会列表

    private TextView tvSmsLable;

    private boolean allSelect = false;//已参加会议列表的全选框

    /*高级参数的适配器--start*/
    private boolean confParamrRequestFalg = false;//高级参数查询是否成功的标记
    private List<ConfParametersBean> confParametersBeanList = new ArrayList<>();
    private List<String> spinnerList = new ArrayList<>();
    private String confParametersId; //高级参数id
    /*高级参数的适配器--end*/

    /*短信列表模板适配器--start*/
    private List<SMSBean> smsBeanList = new ArrayList<>();
    private List<String> smsNameList = new ArrayList<>();
    private String smsId;//短信模板id
    private String smsContent;//短信内容
    private String smsTitle;//短信标题
    /*短信列表模板适配器--end*/

    /*从历史会议中选择--start*/
    private HistoryListAdapter historyAdapter;
    private int currentHistoryIndex; //选中的历史bean对象
    private ConfBean currentHistoryBean;
    /*从历史会议中选择--end*/


    /*已选择的参会列表适配器--start*/
    private AttendingListAdapter attendingListAdapter;
    private List<ConfBean> attendingList = new ArrayList<>();
    /*已选择的参会列表适配器--end*/

    public static final String DIALOG_TYPE_HISTORY = "从历史生成";
    public static final String DIALOG_TYPE_TEMPLATE = "从模板生成";

    /*时间选择框*/
    private TimePickerDialog startTimeDialog;
    private TimePickerDialog endTimeDialog;
    private long startTimeLong;
    private long endTimeLong;
    private String startTime;
    private String endTime;
    private long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
    /*时间选择框*/

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, BookingMeetingActivity.class));
    }

    @Override
    protected void findViews() {
        etName = (EditText) findViewById(R.id.tv_name);
        etContactsName = (EditText) findViewById(R.id.tv_contacts_name);
        etPhone = (EditText) findViewById(R.id.tv_phone);
        spinner = (NiceSpinner) findViewById(R.id.sms_spinner);
        tvHistory = (TextView) findViewById(R.id.tv_history);
        tvTemplate = (TextView) findViewById(R.id.tv_template);
        tvContactList = (TextView) findViewById(R.id.tv_contact_list);
        tvCommonGroup = (TextView) findViewById(R.id.tv_common_group);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        tvConfNumber = (TextView) findViewById(R.id.tv_conf_number);
        etConfNumber = (EditText) findViewById(R.id.et_conf_number);
        tvParticipantInfo = (TextView) findViewById(R.id.tv_participant_info);
        etRemark = (EditText) findViewById(R.id.et_remark);
        tvSave = (TextView) findViewById(R.id.tv_save);
        tvJoinNumber = (TextView) findViewById(R.id.tv_number);
        tvTimeLable = (TextView) findViewById(R.id.tv_time_lable);
        tvDurationLable = (TextView) findViewById(R.id.tv_duration_lable);
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refreshLayout);
        rvAttendingList = (RecyclerView) findViewById(R.id.rv_list);

        tvSmsLable = (TextView) findViewById(R.id.tv_sms_lable);
    }

    @Override
    protected void initData() {
        //设置已加入的复选框文本为空
        tvJoinNumber.setText("");

        //初始化下拉框
        initSpinner();

        //初始化参会列表
        initAttendingRecycler();

        //获得高级配置列表数据
        getConfParametersList();
    }

    /**
     * 显示会议开始时间对话框
     */
    private void showStartTimeDialog() {
        startTimeDialog = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        startTime = DateUtil.longToString(millseconds, DateUtil.FORMAT_DATE_TIME_SECOND_HORIZONTAL);
                        startTimeLong = millseconds;
                        showEndTimeDialog();
                    }
                })
                .setCancelStringId("Cancel")
                .setSureStringId("Sure")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setTitleStringId("请选择会议开始时间")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build();
        startTimeDialog.show(getSupportFragmentManager(), "all");
    }

    /**
     * 显示会议结束时间对话框
     */
    private void showEndTimeDialog() {
        endTimeDialog = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        endTime = DateUtil.longToString(millseconds, DateUtil.FORMAT_DATE_TIME_SECOND_HORIZONTAL);
                        endTimeLong = millseconds;
                        String duration = DateUtil.longToString(endTimeLong - startTimeLong);
                        tvTimeLable.setText("会议时间:" + startTime);
                        tvDurationLable.setText("会议时长:" + endTime + " 持续时间" + duration);
                    }
                })
                .setCancelStringId("Cancel")
                .setSureStringId("Sure")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setTitleStringId("请选择会议结束时间")
                .setCyclic(false)
                .setMinMillseconds(startTimeLong)
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build();
        endTimeDialog.show(getSupportFragmentManager(), "all");
    }

    /**
     * 初始化参会列表
     */
    private void initAttendingRecycler() {
        //初始化刷新头部
        initAttendingRefresh();
        //设置layoutmanager
        rvAttendingList.setLayoutManager(new LinearLayoutManager(this));
        //创建适配器
        attendingListAdapter = new AttendingListAdapter(attendingList);
        attendingListAdapter.setOnItemClickListener(new AttendingListAdapter.onItemClickListener() {
            @Override
            public void onClick(int position) {
                attendingListAdapter.getItem(position).select = !attendingListAdapter.getItem(position).select;
                attendingListAdapter.notifyDataSetChanged();
                setAllSelect();
            }

            @Override
            public void onDelete(final int position) {
                new MaterialDialog.Builder(BookingMeetingActivity.this)
                        .title("系统提示")
                        .content("是否删除该单位下的设备?")
                        .positiveText("确定")
                        .negativeText(android.R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                attendingListAdapter.remove(position);
                                setAllSelect();
                            }
                        })
                        .build().show();
            }
        });
        //设置适配器
        rvAttendingList.setAdapter(attendingListAdapter);
    }

    /**
     * 每次点击之后判断全选的复选框是否要选中
     */
    private void setAllSelect() {
        boolean falg = true;
        for (int i = 0, count = attendingList.size(); i < count; i++) {
            if (!attendingList.get(i).select) {
                falg = false;
                break;
            }
        }
        if (falg && attendingList.size() != 0) {
            allSelect = true;
        } else {
            allSelect = false;
        }

        tvJoinNumber.setCompoundDrawablesWithIntrinsicBounds(allSelect ? R.mipmap.icon_login_check_on : R.mipmap.icon_login_check_off, 0, 0, 0);
    }

    /**
     * 初始化已加入列表的刷新控件
     */
    private void initAttendingRefresh() {
        //设置刷新的头部
        ProgressLayout progressLayout = new ProgressLayout(this);
        refreshLayout.setHeaderView(progressLayout);
        //设置加载的底部
        LoadingView loadingView = new LoadingView(this);
        refreshLayout.setBottomView(loadingView);
        //设置像SwipeRefreshLayout一样的悬浮刷新模式了
        refreshLayout.setFloatRefresh(true);
        //设置是否回弹
        refreshLayout.setEnableOverScroll(false);
//        //设置头部高度
//        refreshLayout.setHeaderHeight(140);
//        //设置头部的最大高度
//        refreshLayout.setMaxHeadHeight(240);
        //设置刷新的view
        refreshLayout.setTargetView(rvAttendingList);
    }


    /**
     * 初始化dialog
     *
     * @param title   dialog标题
     * @param adapter dialog里recyclerview的适配器
     */
    private void initDialog(final String title, RecyclerView.Adapter adapter) {
        //获得dialog填充的内容
        View dialogView = initDialogContent(adapter);
        //创建对话框
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(title)
                .customView(dialogView, false)
                .positiveText("确认")
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        switch (title) {
                            //从历史获取
                            case DIALOG_TYPE_HISTORY:
                                //得到选中的bean
                                currentHistoryBean = historyAdapter.getItem(currentHistoryIndex);
                                //通过id得到设备名称
                                getHistoryById(currentHistoryBean.id);
                                break;
                        }
                    }
                })
                .build();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        //设置对话框的宽度
        dialog.getWindow().setLayout(DisplayUtil.getScreenWidth(this) * 2 / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        //点击对话框以外的地方，对话框不消失
        //dialog.setCanceledOnTouchOutside(false);
        //点击对话框意外的地方和返回键，对话框都不消失
        dialog.setCancelable(false);
        dialog.show();
    }

    @NonNull
    private View initDialogContent(RecyclerView.Adapter adapter) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.refresh_recycler_view, null);
        TwinklingRefreshLayout refreshLayout = (TwinklingRefreshLayout) dialogView.findViewById(R.id.refreshLayout);
        RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //设置刷新的头部
        ProgressLayout progressLayout = new ProgressLayout(this);
        refreshLayout.setHeaderView(progressLayout);
        //设置加载的底部
        LoadingView loadingView = new LoadingView(this);
        refreshLayout.setBottomView(loadingView);

        //设置像SwipeRefreshLayout一样的悬浮刷新模式了
        refreshLayout.setFloatRefresh(true);
        //设置是否回弹
        refreshLayout.setEnableOverScroll(false);
        //设置刷新的view
        refreshLayout.setTargetView(recyclerView);
        return dialogView;
    }

    /**
     * 初始化下拉框
     */
    private void initSpinner() {
        spinnerList.add("");
        spinner.attachDataSource(spinnerList);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                confParametersId = confParametersBeanList.get(position).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void setListener() {
        tvHistory.setOnClickListener(this);
        tvTemplate.setOnClickListener(this);
        tvContactList.setOnClickListener(this);
        tvCommonGroup.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        tvConfNumber.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        tvJoinNumber.setOnClickListener(this);
        tvTimeLable.setOnClickListener(this);

        tvSmsLable.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_booking_meeting;
    }


    /**
     * 将String[]转换成字符串
     *
     * @param arrayData 源数据
     * @return 转换后得到的字符串
     */
    public String getToString(String[] arrayData) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0, count = arrayData.length; i < count; i++) {
            stringBuilder.append(arrayData[i]);
            if (i < count - 1) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 将List<String>转换成字符串
     *
     * @param arrayData 源数据
     * @return 转换后得到的字符串
     */
    public String getToString(List<String> arrayData) {
        String[] newData = (String[]) arrayData.toArray();
        return getToString(newData);
    }

    /**
     * 等到会议的持续时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 会议持续时间
     */
    private String getDuration(String startTime, String endTime) {
        long startLong = DateUtil.stringToLong(startTime, DateUtil.FORMAT_DATE_TIME_SECOND_HORIZONTAL);
        long endLong = DateUtil.stringToLong(endTime, DateUtil.FORMAT_DATE_TIME_SECOND_HORIZONTAL);
        long c = startLong - endLong;
        //将时间差转换成时间格式
        String duration = DateUtil.longToString(c);

        return duration;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //从历史生成
            case R.id.tv_history:
                //获得历史会议列表的数据
                getHistoryList();
                break;

            //从模板生成
            case R.id.tv_template:
                getTemplateList();
                break;

            //通讯录
            case R.id.tv_contact_list:
//                initDialog("通讯录");
                break;

            //常用群组
            case R.id.tv_common_group:
//                initDialog("常用群组");
                break;

            //删除选中的条目
            case R.id.tv_delete:
                Toast.makeText(this, "删除", Toast.LENGTH_SHORT).show();
                break;

            //会议号码
            case R.id.tv_conf_number:
                break;

            //请求预约会议的接口
            case R.id.tv_save:
                //设备名称
                if (attendingList.size() <= 0) {
                    Toast.makeText(this, "请选择参会列表", Toast.LENGTH_SHORT).show();
                    return;
                }
                //已参会列表
                String contactList = attendingList.get(0).deviceName;
                //会议名称
                String confName = etName.getText().toString();
                if (TextUtils.isEmpty(confName)) {
                    Toast.makeText(this, "会议名称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //联系人名称
                String contactsName = etContactsName.getText().toString();
                if (TextUtils.isEmpty(contactsName)) {
                    Toast.makeText(this, "联系人名称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //联系人电话
                String phone = etPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "联系人电话不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //用户信息
                UserInfo userInfo = UserHelper.getSavedUser();
                //操作人ID
                String operatorId = "";
                if (null != userInfo && !TextUtils.isEmpty(userInfo.id)) {
                    operatorId = userInfo.id;
                }
                if (TextUtils.isEmpty(operatorId)) {
                    Toast.makeText(this, "申请人Id不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
//                contactList      参会人员列表
//                confParameters   高级参数设计
//                name             会议名称
//                schedulingTime   开始时间
//                endTime          结束时间
//                duration         持续时间
//                isEmail          是否通过Email发送会议通知 1为是，0为否
//                isSms            是否通过Sms发送会议通知 1为是，0为否
//                smsId            短信Id
//                smsTitle         短信标题
//                smsContext       短信内容
//                contactPeople    联系人
//                contactTelephone 联系电话
//                operatorId        操作人id
                long durationTime = (endTimeLong - startTimeLong) / 1000 / 60;
                //请求预约会议的接口
                saveConf(contactList,
                        confParametersId,
                        confName,
                        startTime,
                        endTime,
                        String.valueOf(durationTime),
                        "0",
                        "1",
                        smsId,
                        smsTitle,
                        smsContent,
                        contactsName,
                        phone,
                        operatorId
                );
                break;

            //已选中的参会列表中的全选按钮
            case R.id.tv_number:
                allSelect = !allSelect;
                tvJoinNumber.setCompoundDrawablesWithIntrinsicBounds(allSelect ? R.mipmap.icon_login_check_on : R.mipmap.icon_login_check_off, 0, 0, 0);

                if (allSelect) {
                    for (int i = 0, count = attendingList.size(); i < count; i++) {
                        attendingList.get(i).select = true;
                    }
                } else {
                    for (int i = 0, count = attendingList.size(); i < count; i++) {
                        attendingList.get(i).select = false;
                    }
                }
                attendingListAdapter.notifyDataSetChanged();
                break;

            //选择时间
            case R.id.tv_time_lable:
                showStartTimeDialog();
                break;


            case R.id.tv_sms_lable:
                getSmsList();
                break;
        }
    }

    /**
     * 将选中的参会列表拼接成ContactList
     * _TN_C湖北省委,_TN_C湖北省委,_TN_C1,_TN_C6727,_TN_C6516,-3829,3828,3785,_TN_C1,_TN_C6727,_TN_C6516,-3829,3828,3785,3784,
     */
    private String jointContactList() {
        StringBuilder sbUnitName = new StringBuilder();
        StringBuilder sbDeviceName = new StringBuilder();
        for (int i = 0, count = attendingList.size(); i < count; i++) {
            String unitIdName = "_TN_C";
            sbUnitName.append(unitIdName + attendingList.get(i).unitIdName + ",");
            sbDeviceName.append(attendingList.get(i).deviceName);
        }
        Logger.i(sbUnitName.toString());
        Logger.i(sbDeviceName.toString());
        return sbUnitName.toString() + sbDeviceName.toString();
    }

    /**
     * 获得短信模板列表
     */
    private void getSmsList() {
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .getSmsModelList()
                .compose(new ListDefaultTransformer<SMSBean>())
                .subscribe(new RxSubscriber<List<SMSBean>>() {
                    @Override
                    public void onSuccess(List<SMSBean> list) {
                        smsBeanList.clear();
                        smsNameList.clear();
                        //判断返回的数据是否为空
                        if (null != list && list.size() > 0) {
                            smsBeanList.addAll(list);
                            for (int i = 0, count = list.size(); i < count; i++) {
                                smsNameList.add(list.get(i).name);
                            }
                            //更新Spinner的数据源
                            //spinner.attachDataSource(spinnerList);
                            //设置高级参数ID
                            smsId = smsBeanList.get(0).id;
                            smsTitle = smsNameList.get(0);
                        }

                        View dialogView = LayoutInflater.from(BookingMeetingActivity.this).inflate(R.layout.dialog_sms, null);
                        final NiceSpinner smsSpinner = (NiceSpinner) dialogView.findViewById(R.id.sms_spinner);
                        EditText etTheme = (EditText) dialogView.findViewById(R.id.et_theme);
                        TextView tvConfName = (TextView) dialogView.findViewById(R.id.tv_conf_name);
                        TextView tvConsignee = (TextView) dialogView.findViewById(R.id.tv_consignee);
                        TextView tvConfNumber = (TextView) dialogView.findViewById(R.id.tv_conf_number);
                        TextView tvConfTime = (TextView) dialogView.findViewById(R.id.tv_conf_time);
                        TextView tvSiteName = (TextView) dialogView.findViewById(R.id.tv_site_name);
                        final EditText etSMSContent = (EditText) dialogView.findViewById(R.id.et_content);

                        smsSpinner.attachDataSource(smsNameList);
                        smsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                smsId = smsBeanList.get(position).id;
                                smsTitle = smsNameList.get(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        //创建对话框
                        MaterialDialog dialog = new MaterialDialog.Builder(BookingMeetingActivity.this)
                                .title("短信")
                                .customView(dialogView, false)
                                .positiveText("确认")
                                .negativeText(android.R.string.cancel)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        //获得短信内容
                                        smsContent = etSMSContent.getText().toString();
                                    }
                                })
                                .build();
                        dialog.show();
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(BookingMeetingActivity.this, "数据请求失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 获得会议模板列表
     */
    private void getTemplateList() {
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .getTemplateList()
                .compose(new ListDefaultTransformer<ConfBean>())
                .subscribe(new RxSubscriber<List<ConfBean>>() {
                    @Override
                    public void onSuccess(List<ConfBean> list) {
                        Toast.makeText(BookingMeetingActivity.this, "" + list.size(), Toast.LENGTH_SHORT).show();
                        initDialog(DIALOG_TYPE_TEMPLATE, null);
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(BookingMeetingActivity.this, "数据请求失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 请求预约会议的接口
     *
     * @param contactList      参会人员列表
     * @param confParameters   高级参数设计
     * @param name             会议名称
     * @param schedulingTime   开始时间
     * @param endTime          结束时间
     * @param duration         持续时间
     * @param isEmail          是否通过Email发送会议通知 1为是，0为否
     * @param isSms            是否通过Sms发送会议通知 1为是，0为否
     * @param smsId            短信Id
     * @param smsTitle         短信标题
     * @param smsContext       短信内容
     * @param contactPeople    联系人
     * @param contactTelephone 联系电话
     * @param operatorId       操作人id
     */
    private void saveConf(String contactList,
                          String confParameters,
                          String name,
                          String schedulingTime,
                          String endTime,
                          String duration,
                          String isEmail,
                          String isSms,
                          String smsId,
                          String smsTitle,
                          String smsContext,
                          String contactPeople,
                          String contactTelephone,
                          String operatorId) {

//        _TN_C1,_TN_C2,-3882,3883,3884,3885,
        // contactList=_TN_C1,-3882,3883,3884,3885,     参会列表
        // &confParameters=26                           高级参数设计
        // &conf.name=qwer                              会议名称
        // &conf.schedulingTime=2017-04-25 14:45:00     开始时间
        // &conf.endTime=2017-04-25 15:15:00            结束时间
        // &conf.duration=30                            持续时间
        // &conf.isEmail=0                              是否用email通知
        // &conf.isSms=1                                是否用短信通知
        // &conf.smsId=12                               短信Id
        // &conf.smsTitle=预约通过模板（勿删除）           短信标题
        // &conf.smsContext=                            短信内容      <p>各位领导、同事：</p
        // &conf.contactPeople=qweqwr                   联系人
        // &conf.contactTelephone=123123                联系电话
        // &operatorId=1240                             操作人id
        Map<String, String> params = new HashMap<>();
        params.put("contactList", contactList);
        params.put("confParameters", confParameters);
        params.put("conf.name", name);
        params.put("conf.schedulingTime", schedulingTime);
        params.put("conf.endTime", endTime);
        params.put("conf.duration", duration);
        params.put("conf.isEmail", isEmail);
        params.put("conf.isSms", isSms);
        params.put("conf.smsId", smsId);
        params.put("conf.smsTitle", smsTitle);
        params.put("conf.smsContext", smsContext);
        params.put("conf.contactPeople", contactPeople);
        params.put("conf.contactTelephone", contactTelephone);
        params.put("operatorId", operatorId);

        String url = Urls.BASE_URL + Urls.CONFACTION_SAVECONF;
        OkHttpUtils
                .post()//
                .url(url)//
                .params(params)//
                .build()//
                .execute(new GenericsCallback<String>(new JsonGenericsSerializator() {
                }) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(BookingMeetingActivity.this, "预约失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(BookingMeetingActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 获得高级配置的列表
     */
    private void getConfParametersList() {
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .getConfParametersBeanList()
                .compose(new ListDefaultTransformer<ConfParametersBean>())
                .subscribe(new RxSubscriber<List<ConfParametersBean>>() {
                    @Override
                    public void onSuccess(List<ConfParametersBean> beanList) {
                        spinnerList.clear();
                        confParametersBeanList.clear();
                        //判断返回的数据是否为空
                        if (null != spinnerList && beanList.size() > 0) {
                            confParametersBeanList.addAll(beanList);
                            for (int i = 0, count = beanList.size(); i < count; i++) {
                                spinnerList.add(beanList.get(i).name);
                            }
                            //更新Spinner的数据源
                            spinner.attachDataSource(spinnerList);
                            //设置高级参数ID
                            confParametersId = confParametersBeanList.get(0).id;
                        }
                        confParamrRequestFalg = true;
                    }

                    @Override
                    protected void onError(ResponeThrowable throwable) {
                        Toast.makeText(BookingMeetingActivity.this, "获取数据失败,请稍后再试", Toast.LENGTH_SHORT).show();
                        confParamrRequestFalg = false;
                    }
                });
    }

    /**
     * 通过id查询历史会议的设备名称
     *
     * @param id 会议Id
     */
    private void getHistoryById(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);

        String url = Urls.BASE_URL + Urls.QUERY_HISTORY_ID;

        OkHttpUtils
                .post()//
                .url(url)//
                .params(params)//
                .build()//
                .execute(new GenericsCallback<String>(new JsonGenericsSerializator() {
                }) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(BookingMeetingActivity.this, "获取数据失败,情稍后再试", Toast.LENGTH_SHORT).show();
                    }

                    //_TN_C1,_TN_C6727,_TN_C6516,-3829,3828,3785,
                    @Override
                    public void onResponse(String response, int id) {
                        String[] split = response.split(",");
                        currentHistoryBean.deviceNumber = String.valueOf(split.length);
                        currentHistoryBean.deviceName = response;
                        // 选中的条目添加到列表中去
                        attendingListAdapter.add(currentHistoryBean);
                    }
                });
    }

    /**
     * 获得历史会议的列表
     */
    private void getHistoryList() {
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .getHistoryList()
                .compose(new ListDefaultTransformer<ConfBean>())
                .subscribe(new RxSubscriber<List<ConfBean>>() {
                    @Override
                    public void onSuccess(List<ConfBean> confBeen) {
                        historyAdapter = new HistoryListAdapter(confBeen);
                        historyAdapter.setOnItemClickListener(new HistoryListAdapter.onItemClickListener() {
                            @Override
                            public void onClick(int position) {
                                historyAdapter.setCurrenIndex(position);
                                //设置当前选中的历史bean
                                currentHistoryIndex = position;
                            }
                        });
                        initDialog(DIALOG_TYPE_HISTORY, historyAdapter);
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(BookingMeetingActivity.this, "获取数据失败,请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
