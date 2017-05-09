package com.zxwl.frame.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dinuscxj.itemdecoration.GridOffsetsItemDecoration;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;
import com.zxwl.frame.R;
import com.zxwl.frame.adapter.ConfTemplateGridAdapter;
import com.zxwl.frame.adapter.HistoryGridAdapter;
import com.zxwl.frame.adapter.NewConfExpandableListViewAdapter;
import com.zxwl.frame.bean.ConfBean;
import com.zxwl.frame.bean.ConfParametersBean;
import com.zxwl.frame.bean.ConfirmEvent;
import com.zxwl.frame.bean.DataList;
import com.zxwl.frame.bean.DepartBean;
import com.zxwl.frame.bean.Employee;
import com.zxwl.frame.bean.ManagementGroupBean;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import jp.wasabeef.richeditor.RichEditor;
import okhttp3.Call;
import rx.Observable;
import rx.functions.Func1;

/**
 * 新建会议
 */
public class NewConfActivity extends BaseActivity implements View.OnClickListener {
    /*头部公用控件-start*/
    private TextView tvLogOut;
    private TextView tvIssue;
    private TextView tvHome;
    private TextView tvName;
    /*头部公用控件-end*/

    private TextView tvSubscribe;//会议预约
    private TextView tvImmediate;//即时会议

    private TextView tvOutTemplate;//导出模板
    private TextView tvInTemplate;//导入模板
    private EditText etTopTitleSearch;//搜索
    private TextView tvSave;//保存
    private TextView tvReset;//重置
    private EditText etName;//会议名称
    private EditText etContactsName;//联系人
    private EditText etPhone;//联系电话
    private TextView tvTemplate;//从模板生成
    private TextView tvHistory;//从历史生成
    private TextView tvContactList;//通讯录
    private TextView tvCommonGroup;//常用群组
    private TextView tvDelete;//删除设备
    private TextView tvDevice;//会议设备
    private TextView tvNewConfNumber;//生成会议号码
    private TextView tvConfNumber;//会议号码
    private TextView tvDeviceInfo;//设备信息----参会单位1个，设备28个
    private TextView tvConfTime;//会议时间
    private TextView tvDuration;//会议时长
    private TextView tvEmailCheck;//邮件复选框
    private TextView tvSmsCheck;//短信复选框
    private TextView tvEmailEdit;//邮件编辑按钮
    private EditText etEmail;//邮件内容
    private TextView tvEmailTemplateLable;
    private TextView tvSmsTemplateLable;//
    private TextView tvSmsEdit;//短信编辑按钮
    private FrameLayout flSMSContent;
    private RichEditor reSMSContent;

//    private EditText etSms;//短信内容

    private boolean emailNoticeFlag = false;//是否用邮件通知
    private boolean smsNoticeFlag = true;//是否用短信通知

    public static final String DIALOG_TYPE_HISTORY = "历史会议";
    public static final String DIALOG_TYPE_CONF_TEMPLATE = "会议模板";

    /*历史会议列表-start*/
    private HistoryGridAdapter historyAdapter;//历史会议列表适配器
    private int currentHistoryIndex;//历史会议列表当前选中条目下标
    private ConfBean currentConfBean;
    /*历史会议列表-end*/

    /*会议模板列表-start*/
    private ConfTemplateGridAdapter templateAdapter;//会议模板列表适配器
    private int currentTemplateIndex;//会议模板列表当前选中条目下标
    private ConfBean currentTemplateConfBean;
    /*会议模板列表-end*/


    /*时间选择框-start*/
    private TimePickerDialog startTimeDialog;
    private TimePickerDialog endTimeDialog;
    private long startTimeLong;
    private long endTimeLong;
    private String startTime;
    private String endTime;
    private long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
    /*时间选择框-end*/

    /*短信列表模板适配器--start*/
    private List<SMSBean> smsBeanList = new ArrayList<>();
    private List<String> smsNameList = new ArrayList<>();
    private String smsId;//短信模板id
    private String showSMSContent;//显示的短信内容
    private String smsContent;//短信内容
    private String smsTitle;//短信主题
    private RichEditor etSMScontent;
    /*短信列表模板适配器--end*/

    private String contactList;//参会的单位号和人员号_TN_C1,_TN_C6727,_TN_C6516,-3829,3828,3785,3784,

    /*高级参数列表-start*/
    private List<ConfParametersBean> confParametersBeanList = new ArrayList<>();
    private String confParametersId;//高级参数id
    /*高级参数列表-end*/

    /*右侧联系人列表-start*/
    private ExpandableListView exLv;
    private HashMap<String, List<Employee>> mMaps = new HashMap<>();
    private List<String> orgNames = new ArrayList<>();
    //private List<EmployeeBean> values;
    private NewConfExpandableListViewAdapter expAdapter;
    private List<Employee> hisEmployee = new ArrayList<>();
    /*右侧联系人列表-end*/

    private MaterialDialog dialog;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, NewConfActivity.class));
    }

    @Override
    protected void findViews() {
        tvLogOut = (TextView) findViewById(R.id.tv_log_out);
        tvIssue = (TextView) findViewById(R.id.tv_issue);
        tvHome = (TextView) findViewById(R.id.tv_home);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvSubscribe = (TextView) findViewById(R.id.tv_subscribe);
        tvImmediate = (TextView) findViewById(R.id.tv_immediate);
        tvOutTemplate = (TextView) findViewById(R.id.tv_out_template);
        tvInTemplate = (TextView) findViewById(R.id.tv_in_template);
        etTopTitleSearch = (EditText) findViewById(R.id.et_top_title_search);
        tvSave = (TextView) findViewById(R.id.tv_save);
        tvReset = (TextView) findViewById(R.id.tv_reset);
        etName = (EditText) findViewById(R.id.et_name);
        etContactsName = (EditText) findViewById(R.id.tv_contacts_name);
        etPhone = (EditText) findViewById(R.id.tv_phone);
        tvTemplate = (TextView) findViewById(R.id.tv_template);
        tvHistory = (TextView) findViewById(R.id.tv_history);
        tvContactList = (TextView) findViewById(R.id.tv_contact_list);
        tvCommonGroup = (TextView) findViewById(R.id.tv_common_group);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        tvDevice = (TextView) findViewById(R.id.tv_device);
        tvNewConfNumber = (TextView) findViewById(R.id.tv_new_conf_number);
        tvConfNumber = (TextView) findViewById(R.id.tv_conf_number);
        tvDeviceInfo = (TextView) findViewById(R.id.tv_device_info);
        tvConfTime = (TextView) findViewById(R.id.tv_conf_time);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        tvEmailCheck = (TextView) findViewById(R.id.tv_email_check);
        tvSmsCheck = (TextView) findViewById(R.id.tv_sms_check);
        tvEmailEdit = (TextView) findViewById(R.id.tv_email_edit);
        etEmail = (EditText) findViewById(R.id.et_email);
        tvSmsEdit = (TextView) findViewById(R.id.tv_sms_edit);
        flSMSContent = (FrameLayout) findViewById(R.id.fl_sms_content);
        reSMSContent = (RichEditor) findViewById(R.id.re_sms_content);

//        etSms = (EditText) findViewById(R.id.et_sms);
        tvEmailTemplateLable = (TextView) findViewById(R.id.tv_email_template_lable);
        tvSmsTemplateLable = (TextView) findViewById(R.id.tv_sms_template_lable);

        exLv = (ExpandableListView) findViewById(R.id.expand_list);
        tvEmailTemplateLable = (TextView) findViewById(R.id.tv_email_template_lable);
        tvSmsTemplateLable = (TextView) findViewById(R.id.tv_sms_template_lable);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);


        //顶部退出按钮
        tvLogOut.setVisibility(View.VISIBLE);
        tvIssue.setVisibility(View.VISIBLE);
        tvHome.setVisibility(View.VISIBLE);
        tvName.setVisibility(View.VISIBLE);
        tvName.setText(UserHelper.getSavedUser().name);

        //获得高级参数Id
        getConfParametersList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void setListener() {
        tvSubscribe.setOnClickListener(this);//会议预约
        tvImmediate.setOnClickListener(this);//即时会议
        tvOutTemplate.setOnClickListener(this);//导出模板
        tvInTemplate.setOnClickListener(this);//导入模板
        tvTemplate.setOnClickListener(this);//从模板生成
        tvHistory.setOnClickListener(this);//从历史生成
        tvContactList.setOnClickListener(this);//通讯录
        tvCommonGroup.setOnClickListener(this);//常用群组
        tvDelete.setOnClickListener(this);//删除设备
        tvNewConfNumber.setOnClickListener(this);//生成会议号码
        tvConfTime.setOnClickListener(this);//会议时间
        tvEmailCheck.setOnClickListener(this);//邮件复选框
        tvSmsCheck.setOnClickListener(this);//短信复选框
        tvEmailEdit.setOnClickListener(this);//邮件编辑按钮
        tvSmsEdit.setOnClickListener(this);//短信编辑按钮
        tvSave.setOnClickListener(this);//保存
        tvReset.setOnClickListener(this);//重置

        exLv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                boolean groupExpanded = parent.isGroupExpanded(groupPosition);
                if (groupExpanded) {
                    parent.collapseGroup(groupPosition);
                } else {
                    parent.expandGroup(groupPosition, true);
                }
                expAdapter.setIndicatorState(groupPosition, groupExpanded);
                return true;
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_conf;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //从模板生成
            case R.id.tv_template:
                //获得模板列表数据
                getTemplateList();
                break;

            //从历史生成
            case R.id.tv_history:
                //获得历史会议列表的数据
                getHistoryList();
                break;

            //通讯录
            case R.id.tv_contact_list:
                ContactBookDialogActivity.startActivity(NewConfActivity.this);
                break;

            //常用群组
            case R.id.tv_common_group:
//                initDialog("常用群组");
                Toast.makeText(this, "功能正在开发", Toast.LENGTH_SHORT).show();
                break;

            //删除选中的条目
            case R.id.tv_delete:
                Toast.makeText(this, "功能正在开发", Toast.LENGTH_SHORT).show();
                break;

            //生成会议号码
            case R.id.tv_new_conf_number:
                int i = new Random().nextInt(100000000);
                tvConfNumber.setText(String.valueOf(i));
                break;

            //会议时间
            case R.id.tv_conf_time:
                showStartTimeDialog();
                break;

            //是否用邮件发送通知
            case R.id.tv_email_check:
                showEmailWidget();
                break;

            //是否用短信发送通知
            case R.id.tv_sms_check:
//                showSMSWidget();
                break;

            //显示短信编辑弹出框
            case R.id.tv_sms_edit:
                getSmsList();
                break;
//            private String jointContactList() {
//                StringBuilder sbUnitName = new StringBuilder();
//                StringBuilder sbDeviceName = new StringBuilder();
//                for (int i = 0, count = attendingList.size(); i < count; i++) {
//                    String unitIdName = "_TN_C";
//                    sbUnitName.append(unitIdName + attendingList.get(i).unitIdName + ",");
//                    sbDeviceName.append(attendingList.get(i).deviceName);
//                }
//                Logger.i(sbUnitName.toString());
//                Logger.i(sbDeviceName.toString());
//                return sbUnitName.toString() + sbDeviceName.toString();
//            }
            //保存
            case R.id.tv_save:
                if (TextUtils.isEmpty(contactList)) {
                    String unitIdName = "_TN_C";
                    StringBuilder sbUnitName = new StringBuilder();
                    StringBuilder sbDeviceName = new StringBuilder();
                    if (null != expAdapter) {
                        Iterator<List<Employee>> mapV = expAdapter.getMapV();
                        while (mapV.hasNext()) {
                            List<Employee> next = mapV.next();
                            sbUnitName.append(unitIdName + next.get(0).getOrgNo() + ",");
                            for (int j = 0, count = next.size(); j < count; j++) {
                                sbDeviceName.append(next.get(j).getId() + ",");
                            }
                        }
                        contactList = sbUnitName.toString() + "-" + sbDeviceName.toString();
                    }
                }

                //设备名称
                if (TextUtils.isEmpty(contactList)) {
                    Toast.makeText(this, "请选择参会列表", Toast.LENGTH_SHORT).show();
                    return;
                }

                //高级参数ID
                if (TextUtils.isEmpty(confParametersId)) {
                    Toast.makeText(this, "高级参数ID不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //会议名称
                String confName = etName.getText().toString();
                if (TextUtils.isEmpty(confName)) {
                    Toast.makeText(this, "会议名称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //开始时间
                if (TextUtils.isEmpty(startTime)) {
                    Toast.makeText(this, "开始时间不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //结束时间
                if (TextUtils.isEmpty(endTime)) {
                    Toast.makeText(this, "结束时间不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //短信id
                if (TextUtils.isEmpty(smsId)) {
                    Toast.makeText(this, "短信ID不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //短信主题
                if (TextUtils.isEmpty(smsTitle)) {
                    Toast.makeText(this, "短信主题不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //短信内容
                if (TextUtils.isEmpty(smsContent)) {
                    Toast.makeText(this, "短信内容不能为空", Toast.LENGTH_SHORT).show();
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

                //获得操作人ID
                //用户信息
                UserInfo userInfo = UserHelper.getSavedUser();
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

                //持续时间
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

            //重置
            case R.id.tv_reset:
                setConfContent(null);
                break;

            //会议名称
            case R.id.tv_conf_name:
                etSMScontent.insertImage("file:///android_asset/icon_hymc.png", "");
                break;

            //收件人
            case R.id.tv_consignee:
                etSMScontent.insertImage("file:///android_asset/icon_sjr.png", "");
                break;

            //会议号码
            case R.id.tv_conf_number:
                etSMScontent.insertImage("file:///android_asset/icon_hyhm.png", "");
                break;

            //会议时间
            case R.id.tv_dialog_time:
                etSMScontent.insertImage("file:///android_asset/icon_hysj.png", "");
                break;

            //会场名称
            case R.id.tv_site_name:
                etSMScontent.insertImage("file:///android_asset/icon_hcmc.png", "");
                break;
        }
    }

    /**
     * 获得历史会议的列表
     */
    private void getHistoryList() {
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .getHistoryList()
                .compose(this.<DataList<ConfBean>>bindToLifecycle())
                .compose(new ListDefaultTransformer<ConfBean>())
                .subscribe(new RxSubscriber<List<ConfBean>>() {
                    @Override
                    public void onSuccess(List<ConfBean> confBeenList) {
                        historyAdapter = new HistoryGridAdapter(confBeenList);
                        historyAdapter.setOnItemClickListener(new HistoryGridAdapter.onItemClickListener() {
                            @Override
                            public void onClick(int position) {
                                historyAdapter.setCurrenIndex(position);
                                //设置当前选中的历史bean
                                currentHistoryIndex = position;

                                //TODO 点击适配器隐藏dialog
                                if (null != dialog && dialog.isShowing()) {
                                    dialog.dismiss();
                                    currentConfBean = historyAdapter.getItem(position);
                                    getHistoryById(currentConfBean.id);
                                }
                            }
                        });

                        initDialog(DIALOG_TYPE_HISTORY,//
                                historyAdapter,//
                                new GridLayoutManager(NewConfActivity.this, 4, GridLayoutManager.VERTICAL, false),//
                                null
                        );
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(NewConfActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
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
                .compose(this.<DataList<ConfBean>>bindToLifecycle())
                .compose(new ListDefaultTransformer<ConfBean>())
                .subscribe(new RxSubscriber<List<ConfBean>>() {
                    @Override
                    public void onSuccess(List<ConfBean> list) {
                        templateAdapter = new ConfTemplateGridAdapter(list);
                        templateAdapter.setOnItemClickListener(new ConfTemplateGridAdapter.onItemClickListener() {
                            @Override
                            public void onClick(int position) {
                                templateAdapter.setCurrenIndex(position);
                                //设置当前选中的历史bean
                                currentTemplateIndex = position;

                                //TODO 点击适配器隐藏dialog
                                if (null != dialog && dialog.isShowing()) {
                                    dialog.dismiss();

                                    currentTemplateConfBean = templateAdapter.getItem(position);
                                    getTemplateById(currentTemplateConfBean.id);
                                }
                            }
                        });

                        initDialog(DIALOG_TYPE_CONF_TEMPLATE,//
                                templateAdapter,//
                                new GridLayoutManager(NewConfActivity.this, 4, GridLayoutManager.VERTICAL, false),//
                                null
                        );
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(NewConfActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 获得常用群组列表
     */
    private void getManagementGroupList() {
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .getManagementGroupList()
                .compose(this.<DataList<ManagementGroupBean>>bindToLifecycle())
                .compose(new ListDefaultTransformer<ManagementGroupBean>())
                .subscribe(new RxSubscriber<List<ManagementGroupBean>>() {
                    @Override
                    public void onSuccess(List<ManagementGroupBean> list) {
//                        initDialog(DIALOG_TYPE_CONF_TEMPLATE,//
//                                new ConfTemplateGridAdapter(),//
//                                new GridLayoutManager(NewConfActivity.this, 4, GridLayoutManager.VERTICAL, false),//
//                                null
//                        );
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(NewConfActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 获得短信模板列表显示短信编辑dialog
     */
    private void getSmsList() {
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .getSmsModelList()
                .compose(this.<DataList<SMSBean>>bindToLifecycle())
                .compose(new ListDefaultTransformer<SMSBean>())
                .subscribe(new RxSubscriber<List<SMSBean>>() {
                    @Override
                    public void onSuccess(List<SMSBean> list) {
                        smsBeanList.clear();
                        smsNameList.clear();
                        //判断返回的数据是否为空
                        if (null != list && list.size() > 0) {
                            smsBeanList.addAll(list);
                            smsNameList.add("--请选择--");
                            for (int i = 0, count = list.size(); i < count; i++) {
                                smsNameList.add(list.get(i).name);
                            }
                            //设置短信ID
                            smsId = smsBeanList.get(0).id;
                            smsTitle = smsNameList.get(0);
                        }

                        View dialogView = LayoutInflater.from(NewConfActivity.this).inflate(R.layout.dialog_sms, null);
                        final NiceSpinner smsSpinner = (NiceSpinner) dialogView.findViewById(R.id.sms_spinner);
                        final EditText etTheme = (EditText) dialogView.findViewById(R.id.et_theme);

                        TextView tvConfName = (TextView) dialogView.findViewById(R.id.tv_conf_name);//会议名称
                        TextView tvConsignee = (TextView) dialogView.findViewById(R.id.tv_consignee);//收件人
                        TextView tvConfNumber = (TextView) dialogView.findViewById(R.id.tv_conf_number);//会议号码
                        TextView tvConfTime = (TextView) dialogView.findViewById(R.id.tv_dialog_time);//会议时间
                        TextView tvSiteName = (TextView) dialogView.findViewById(R.id.tv_site_name);//会场名称
                        etSMScontent = (RichEditor) dialogView.findViewById(R.id.et_content);

                        tvConfName.setOnClickListener(NewConfActivity.this);
                        tvConsignee.setOnClickListener(NewConfActivity.this);
                        tvConfNumber.setOnClickListener(NewConfActivity.this);
                        tvConfTime.setOnClickListener(NewConfActivity.this);
                        tvSiteName.setOnClickListener(NewConfActivity.this);

                        smsSpinner.attachDataSource(smsNameList);
                        smsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position == 0) {
                                    etTheme.setText("");
                                    etSMScontent.setHtml("");
                                    return;
                                }

                                SMSBean smsBean = smsBeanList.get(position - 1);
                                smsId = smsBean.id;
                                smsTitle = smsNameList.get(position);

                                etTheme.setText(smsBean.name);
                                showSMSContent = textToHtml(smsBean.context);
                                etSMScontent.setHtml(showSMSContent);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                        //创建对话框
                        MaterialDialog dialog = new MaterialDialog.Builder(NewConfActivity.this)
                                .title("短信")
                                .customView(dialogView, true)
                                .positiveText("确认")
                                .negativeText(android.R.string.cancel)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        //获得短信内容
                                        smsContent = htmlToText(etSMScontent.getHtml());
                                        //获得短信主题
                                        smsTitle = etTheme.getText().toString();
                                        //设置主界面短信内容的显示
                                        reSMSContent.setHtml(showSMSContent);
                                    }
                                })
                                .build();
                        //设置对话框的宽度
                        dialog.getWindow().setLayout(DisplayUtil.getScreenWidth(NewConfActivity.this) * 1 / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
                        //点击对话框以外的地方，对话框不消失
                        //dialog.setCanceledOnTouchOutside(false);
                        //点击对话框意外的地方和返回键，对话框都不消失
                        dialog.setCancelable(false);
                        dialog.show();
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(NewConfActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 将里面的文本转换成图片
     *
     * @param context 传递过来的文本数据
     * @return 返回包含图片的数据
     */
    private String textToHtml(String context) {
        //会场名称
//        String imgUrl = <img src="file:///android_asset/ic_launcher.png" alt="">;
        String hcmc = "<img src=\"file:///android_asset/icon_hcmc.png\" alt=\"\">";
        String hyhm = "<img src=\"file:///android_asset/icon_hyhm.png\" alt=\"\">";
        String hymc = "<img src=\"file:///android_asset/icon_hymc.png\" alt=\"\">";
        String hysj = "<img src=\"file:///android_asset/icon_hysj.png\" alt=\"\">";
        String sjr = "<img src=\"file:///android_asset/icon_sjr.png\" alt=\"\">";
        return context.replaceAll("&lt;--会场名称--&gt", hcmc)
                .replaceAll("&lt;--会议号码--&gt", hyhm)
                .replaceAll("&lt;--会议名称--&gt", hymc)
                .replaceAll("&lt;--会议时间--&gt", hysj)
                .replaceAll("&lt;--收件人--&gt", sjr);
    }

    /**
     * 将html转换成html
     *
     * @param context
     * @return
     */
    private String htmlToText(String context) {
        //会场名称
//        String imgUrl = <img src="file:///android_asset/ic_launcher.png" alt="">;
        String hcmc = "<img src=\"file:///android_asset/icon_hcmc.png\" alt=\"\">";
        String hyhm = "<img src=\"file:///android_asset/icon_hyhm.png\" alt=\"\">";
        String hymc = "<img src=\"file:///android_asset/icon_hymc.png\" alt=\"\">";
        String hysj = "<img src=\"file:///android_asset/icon_hysj.png\" alt=\"\">";
        String sjr = "<img src=\"file:///android_asset/icon_sjr.png\" alt=\"\">";

        return context
                .replaceAll(hcmc, "<--会场名称-->")
                .replaceAll(hyhm, "<--会议号码-->")
                .replaceAll(hymc, "<--会议名称-->")
                .replaceAll(hysj, "<--会议时间-->")
                .replaceAll(sjr, "<--收件人-->");
    }

    /**
     * 通过id查询历史会议
     *
     * @param id 会议Id
     */
    private void getHistoryById(String id) {
        //通过findHistoryById获得的_TN_C1,_TN_C6727,_TN_C6516,-3829,3828,3785,3784,查询参会人员
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .findHistoryById(id)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<DataList<DepartBean>>>() {
                    @Override
                    public Observable<DataList<DepartBean>> call(String s) {
                        //设置参会的单位和人员号
                        contactList = s;
                        return HttpUtils//
                                .getInstance(NewConfActivity.this)
                                .getRetofitClinet()
                                .builder(ConfApi.class)
                                .getPeopleList(s);
                    }
                })
                .compose(this.<DataList<DepartBean>>bindToLifecycle())
                .compose(new ListDefaultTransformer<DepartBean>())
                .subscribe(new RxSubscriber<List<DepartBean>>() {
                    @Override
                    public void onSuccess(List<DepartBean> departments) {
                        //TODO 根据得到的参会人员列表departments设置右边的数据
                        HashMap<String, List<Employee>> maps = new HashMap<>();
                        List<String> org1Names = new ArrayList<>();
                        //Toast.makeText(NewConfActivity.this, departments.size() + "", Toast.LENGTH_SHORT).show();
                        //Log.i("TAG",departments.toString());
                        hisEmployee.clear();
                        for (int i = 0; i < departments.size(); i++) {
                            String[] strName = departments.get(i).getEmployeeName().split(",");
                            String[] strId = departments.get(i).getEmployeeId().split(",");
                            String orgName = departments.get(i).getDeptName();
                            for (int j = 0; j < strName.length; j++) {
                                Employee employee = new Employee();
                                employee.setName(strName[j]);
                                employee.setId(strId[j]);
                                employee.setOrgName(orgName);
                                hisEmployee.add(employee);
                            }
                        }

                        maps.clear();
                        org1Names.clear();
                        for (int i = 0; i < hisEmployee.size(); i++) {
                            //EmployeeBean bean = new EmployeeBean();
                            String orgName = hisEmployee.get(i).getOrgName();
                            String name = hisEmployee.get(i).getName();
                            Employee employee = hisEmployee.get(i);

                            List<Employee> list = maps.get(orgName);
                            if (list == null) {
                                list = new ArrayList<>();
                            }

                            if (!list.contains(employee)) {
                                list.add(employee);
                            }
                            if (!maps.containsKey(orgName)) {
                                maps.put(orgName, list);
                            }

                        }

                        Set<String> set = maps.keySet();
                        Iterator<String> iterator = set.iterator();
                        for (int i = 0; i < maps.size(); i++) {
                            String key = iterator.next();
                            if (!org1Names.contains(key)) {
                                org1Names.add(key);
                            }
                            List<Employee> values = maps.get(key);
                        }
                        expAdapter = new NewConfExpandableListViewAdapter(NewConfActivity.this, org1Names, maps);
                        exLv.setAdapter(expAdapter);
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(NewConfActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 通过id查询模板会议
     *
     * @param id 会议Id
     */
    private void getTemplateById(String id) {
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .findTemplateById(id)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<DataList<DepartBean>>>() {
                    @Override
                    public Observable<DataList<DepartBean>> call(String s) {
                        contactList = s;
                        return HttpUtils//
                                .getInstance(NewConfActivity.this)
                                .getRetofitClinet()
                                .builder(ConfApi.class)
                                .getPeopleList(s);
                    }
                })
                .compose(this.<DataList<DepartBean>>bindToLifecycle())
                .compose(new ListDefaultTransformer<DepartBean>())
                .subscribe(new RxSubscriber<List<DepartBean>>() {
                    @Override
                    public void onSuccess(List<DepartBean> departments) {
                        //TODO 根据得到的参会人员列表departments设置右边的数据
                        HashMap<String, List<Employee>> maps = new HashMap<>();
                        List<String> org1Names = new ArrayList<>();
                        //Toast.makeText(NewConfActivity.this, departments.size() + "", Toast.LENGTH_SHORT).show();
                        //Log.i("TAG",departments.toString());
                        hisEmployee.clear();
                        for (int i = 0; i < departments.size(); i++) {
                            String[] strName = departments.get(i).getEmployeeName().split(",");
                            String[] strId = departments.get(i).getEmployeeId().split(",");
                            String orgName = departments.get(i).getDeptName();
                            for (int j = 0; j < strName.length; j++) {
                                Employee employee = new Employee();
                                employee.setName(strName[j]);
                                employee.setId(strId[j]);
                                employee.setOrgName(orgName);
                                hisEmployee.add(employee);
                            }
                        }

                        maps.clear();
                        org1Names.clear();
                        for (int i = 0; i < hisEmployee.size(); i++) {
                            //EmployeeBean bean = new EmployeeBean();
                            String orgName = hisEmployee.get(i).getOrgName();
                            String name = hisEmployee.get(i).getName();
                            Employee employee = hisEmployee.get(i);

                            List<Employee> list = maps.get(orgName);
                            if (list == null) {
                                list = new ArrayList<>();
                            }

                            if (!list.contains(employee)) {
                                list.add(employee);
                            }
                            if (!maps.containsKey(orgName)) {
                                maps.put(orgName, list);
                            }
                        }

                        Set<String> set = maps.keySet();
                        Iterator<String> iterator = set.iterator();
                        for (int i = 0; i < maps.size(); i++) {
                            String key = iterator.next();
                            if (!org1Names.contains(key)) {
                                org1Names.add(key);
                            }
                            List<Employee> values = maps.get(key);
                        }

                        expAdapter = new NewConfExpandableListViewAdapter(NewConfActivity.this, org1Names, maps);
                        exLv.setAdapter(expAdapter);
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(NewConfActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
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
                .compose(this.<DataList<ConfParametersBean>>bindToLifecycle())
                .compose(new ListDefaultTransformer<ConfParametersBean>())
                .subscribe(new RxSubscriber<List<ConfParametersBean>>() {
                    @Override
                    public void onSuccess(List<ConfParametersBean> beanList) {
                        if (null != beanList && beanList.size() > 0) {
                            //设置高级参数ID
                            confParametersId = beanList.get(0).id;
                        }
                    }

                    @Override
                    protected void onError(ResponeThrowable throwable) {
                        Toast.makeText(NewConfActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
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
//        HttpUtils.getInstance(this)
//                .getRetofitClinet()
//                .builder(ConfApi.class)
//                .saveConf(
//                        contactList,
//                        confParameters,
//                        name,
//                        schedulingTime,
//                        endTime,
//                        duration,
//                        isEmail,
//                        isSms,
//                        smsId,
//                        smsTitle,
//                        smsContext,
//                        contactPeople,
//                        contactTelephone,
//                        operatorId
//                )
//                .compose(this.<String>bindToLifecycle())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new RxSubscriber<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        Toast.makeText(NewConfActivity.this, "预约成功", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//
//                    @Override
//                    protected void onError(ResponeThrowable responeThrowable) {
//                        Toast.makeText(NewConfActivity.this, "预约失败", Toast.LENGTH_SHORT).show();
//                    }
//                });

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
                        Toast.makeText(NewConfActivity.this, "预约失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(NewConfActivity.this, "预约成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    /**
     * 初始化dialog
     *
     * @param title         dialog标题
     * @param adapter       dialog里recyclerview的适配器
     * @param layoutManager recyclerview的layoutmanager
     * @param decoration
     */
    private void initDialog(final String title, RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager, GridOffsetsItemDecoration decoration) {
        //获得dialog填充的内容
        final View dialogView = initDialogContent(adapter, layoutManager, decoration);
        //TODO 创建对话框
        dialog = new MaterialDialog.Builder(this)
                .title(title)
                .customView(dialogView, false)
//                .positiveText("确认")
//                .negativeText(android.R.string.cancel)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        switch (title) {
//                            //历史会议
//                            case DIALOG_TYPE_HISTORY:
//                                //通过id获得会议参会人员列表
//                                currentConfBean = historyAdapter.getItem(currentHistoryIndex);
//                                getHistoryById(currentConfBean.id);
//                                break;
//
//                            //会议模板
//                            case DIALOG_TYPE_CONF_TEMPLATE:
//                                //通过id获得会议参会人员列表
//                                currentTemplateConfBean = templateAdapter.getItem(currentTemplateIndex);
//                                getTemplateById(currentTemplateConfBean.id);
//                                break;
//                        }
//                    }
//                })
                .build();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //设置对话框的宽度
        dialog.getWindow().setLayout(DisplayUtil.getScreenWidth(this) * 2 / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        //点击对话框以外的地方，对话框不消失
        dialog.setCanceledOnTouchOutside(false);
        //点击对话框意外的地方和返回键，对话框都不消失
//        dialog.setCancelable(false);
        dialog.show();
    }

    @NonNull
    private View initDialogContent(RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager, GridOffsetsItemDecoration decoration) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.refresh_recycler_view, null);
        TwinklingRefreshLayout refreshLayout = (TwinklingRefreshLayout) dialogView.findViewById(R.id.refreshLayout);
        RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.rv_list);
        //设置布局管理器和适配器
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        if (null != decoration) {
            recyclerView.addItemDecoration(decoration);
        }

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
     * 显示短信内容的界面
     */
    private void showSMSWidget() {
        smsNoticeFlag = !smsNoticeFlag;
        tvSmsCheck.setCompoundDrawablesWithIntrinsicBounds(smsNoticeFlag ? R.mipmap.icon_check_on : R.mipmap.icon_check_off, 0, 0, 0);
        tvSmsTemplateLable.setVisibility(smsNoticeFlag ? View.VISIBLE : View.GONE);//
        tvSmsEdit.setVisibility(smsNoticeFlag ? View.VISIBLE : View.GONE);//短信编辑按钮
        flSMSContent.setVisibility(smsNoticeFlag ? View.VISIBLE : View.GONE);//短信内容
//        etSms.setVisibility(smsNoticeFlag ? View.VISIBLE : View.GONE);//短信内容
    }

    /**
     * 显示email内容的界面
     */
    private void showEmailWidget() {
        emailNoticeFlag = !emailNoticeFlag;
        tvEmailCheck.setCompoundDrawablesWithIntrinsicBounds(emailNoticeFlag ? R.mipmap.icon_check_on : R.mipmap.icon_check_off, 0, 0, 0);
        tvEmailTemplateLable.setVisibility(emailNoticeFlag ? View.VISIBLE : View.GONE);//
        tvEmailEdit.setVisibility(emailNoticeFlag ? View.VISIBLE : View.GONE);//email编辑按钮
        etEmail.setVisibility(emailNoticeFlag ? View.VISIBLE : View.GONE);//email内容
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
                        long minute = (endTimeLong - startTimeLong) / 1000 / 60;
                        tvConfTime.setText(startTime);
                        tvDuration.setText("预计" + endTime + "结束会议,时长" + minute + "分钟");
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
     * 通过历史会议的bean或会议模板的bean设置内容,如果confBean为空代表重置
     *
     * @param confBean
     */
    private void setConfContent(ConfBean confBean) {
        if (null == confBean) {
            etName.setText("");//会议名称
            etContactsName.setText("");//联系人
            etPhone.setText("");//联系电话
            tvDevice.setText("");//会议设备
            tvConfNumber.setText("");//会议号码
            tvDeviceInfo.setText("");//设备信息---参会单位1个，设备28个
            tvConfTime.setText("");//会议时间
            tvDuration.setText("");//会议时长
            etEmail.setText("");//邮件内容
            reSMSContent.setHtml("");//短信内容
            return;
        }

        //TODO 根据confBean填写会议的信息
        etName.setText(confBean.name);//会议名称
        etContactsName.setText(confBean.contactPeople);//联系人
        etPhone.setText(confBean.contactTelephone);//联系电话
        tvDevice.setText("");//会议设备
        tvDeviceInfo.setText("");//设备信息----参会单位1个，设备28个
    }

    //获取到选中的employee集合
    @Subscribe
    public void onConfirmEvent(ConfirmEvent event) {
        //当选中联系人的时候把通过历史会议选中的人员列表至为空
        contactList = null;

        List<Employee> employeesData = event.data;
        int allSize = event.size;
        if (employeesData.size() != allSize) {
            for (int i = 0; i < employeesData.size(); i++) {
                //EmployeeBean bean = new EmployeeBean();
                String orgName = employeesData.get(i).getOrgName();
                String name = employeesData.get(i).getName();
                Employee employee = employeesData.get(i);

                List<Employee> list = mMaps.get(orgName);
                if (list == null) {
                    list = new ArrayList<>();
                }
                if (list.size() == 0) {
                    list.add(employee);
                } else if (list.size() != 0) {
                    for (int k = 0; k < list.size(); k++) {
                        if (!list.get(k).getId().equals(employee.getId())) {
                            list.add(employee);
                        }
                    }
                }
                if (!mMaps.containsKey(orgName)) {
                    mMaps.put(orgName, list);
                }
            }
            Set<String> set = mMaps.keySet();
            Iterator<String> iterator = set.iterator();
            for (int i = 0; i < mMaps.size(); i++) {
                String key = iterator.next();
                if (!orgNames.contains(key)) {
                    orgNames.add(key);
                }
                List<Employee> values = mMaps.get(key);
            }
            expAdapter = new NewConfExpandableListViewAdapter(this, orgNames, mMaps);
            exLv.setAdapter(expAdapter);
        } else {
            //全部选中后，再点击通讯录，清空之前全部选中的数据
            for (int i = 0; i < employeesData.size(); i++) {
                //EmployeeBean bean = new EmployeeBean();
                String orgName = employeesData.get(i).getOrgName();
                Employee employee = employeesData.get(i);
                String name = employeesData.get(i).getName();
                List<Employee> list = mMaps.get(orgName);
                if (list == null) {
                    list = new ArrayList<>();
                }
                if (list.size() == 0) {
                    list.add(employee);
                } else if (list.size() != 0) {
                    for (int k = 0; k < list.size(); k++) {
                        if (!list.get(k).getId().equals(employee.getId())) {
                            list.add(employee);
                        }
                    }
                }
                if (!mMaps.containsKey(orgName)) {
                    mMaps.put(orgName, list);
                }
            }

            HashMap<String, List<Employee>> map = new HashMap<>();
            List<String> temp = new ArrayList<>();
            map.clear();
            map.putAll(mMaps);
            Set<String> set = map.keySet();
            Iterator<String> iterator = set.iterator();
            for (int i = 0; i < mMaps.size(); i++) {
                String key = iterator.next();
                if (!temp.contains(key)) {
                    temp.add(key);
                }
                List<Employee> values = map.get(key);
            }
            expAdapter = new NewConfExpandableListViewAdapter(this, temp, map);
            exLv.setAdapter(expAdapter);
            orgNames.clear();
            mMaps.clear();
        }
    }

}
