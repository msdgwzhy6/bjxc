package com.zxwl.frame.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dinuscxj.itemdecoration.GridOffsetsItemDecoration;
import com.zxwl.frame.R;
import com.zxwl.frame.adapter.NewConfExpandableListViewAdapter;
import com.zxwl.frame.bean.ConfBean;
import com.zxwl.frame.bean.DataList;
import com.zxwl.frame.bean.DepartBean;
import com.zxwl.frame.bean.Employee;
import com.zxwl.frame.net.api.ConfApi;
import com.zxwl.frame.net.callback.RxSubscriber;
import com.zxwl.frame.net.exception.ResponeThrowable;
import com.zxwl.frame.net.http.HttpUtils;
import com.zxwl.frame.net.transformer.ListDefaultTransformer;
import com.zxwl.frame.rx.RxBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jp.wasabeef.richeditor.RichEditor;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 会议审批对话框
 */
public class ConfApprovalDialogActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;//标题
    private TextView tvName;//会议名称
    private TextView tvContactsName;//联系人
    private TextView tvPhone;//联系电话
    private TextView tvConfNumber;//会议号码
    private TextView tvDeviceInfo;//设备信息
    private TextView tvCheck;//查看会议室详情
    private TextView tvConfTime;//会议时间
    private TextView tvDuration;//会议时长
    private TextView tvEmailCheck;//是否用邮件通知
    private TextView tvSmsCheck;//是否用短信通知
    private RichEditor etSmsContent;//短信内容
    private EditText etOpinion;//审批意见
    private TextView tvPass;//通过
    private TextView tvReject;//驳回

    public static final String CONF_BEAN = "conf_bean";

    private ConfBean confBean;
    private String contactList;//参会列表

    private List<Employee> hisEmployee = new ArrayList<>();
    private HashMap<String, List<Employee>> maps;
    private List<String> org1Names;
    private NewConfExpandableListViewAdapter expAdapter;
    private MaterialDialog dialog;
    private ExpandableListView explv;

    @Override
    protected void findViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title_lable);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvContactsName = (TextView) findViewById(R.id.tv_contacts_name);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvConfNumber = (TextView) findViewById(R.id.tv_conf_number);
        tvDeviceInfo = (TextView) findViewById(R.id.tv_device_info);
        tvCheck = (TextView) findViewById(R.id.tv_check);
        tvConfTime = (TextView) findViewById(R.id.tv_conf_time);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        tvEmailCheck = (TextView) findViewById(R.id.tv_email_check);
        tvSmsCheck = (TextView) findViewById(R.id.tv_sms_check);
        etSmsContent = (RichEditor) findViewById(R.id.et_sms_content);
        etOpinion = (EditText) findViewById(R.id.et_opinion);
        tvPass = (TextView) findViewById(R.id.tv_pass);
        tvReject = (TextView) findViewById(R.id.tv_reject);
    }

    @Override
    protected void initData() {
        confBean = (ConfBean) getIntent().getSerializableExtra(CONF_BEAN);

        getHistoryById(confBean.id);

        if (null != confBean) {
            tvName.setText(confBean.name);//会议名称
            tvContactsName.setText(confBean.contactPeople);//联系人名称
            tvPhone.setText(confBean.contactTelephone);//电话
            tvConfNumber.setText(confBean.smcConfId);//会议号
            tvConfTime.setText(confBean.beginTime);//开始时间
            tvDuration.setText("预计" + confBean.endTime + "结束会议,时长" + confBean.duration + "分钟");//结束时间
            etSmsContent.setHtml(textToHtml(confBean.smsContext));//短信内容
        }
    }

    //&lt;p&gt;您好&lt;--发送人--&gt;，&amp;nbsp;&amp;nbsp;&amp;nbsp;原定于&lt;--会议时间--&gt;召开的&lt;--会议名称--&gt;，因故取消！&amp;nbsp;&amp;nbsp;如有疑问，请联系视频管理员！&lt;--会场名称--&gt;&lt;/p&gt;
    //&amp;amp;lt;p&amp;amp;gt;各位领导、同事：兹定于&amp;lt;--会议时间--&amp;gt;;召开&amp;lt;--会场名称--&amp;gt;;，请您准时参加&amp;amp;lt;/p&amp;amp;gt;
    @Override
    protected void setListener() {
        tvTitle.setOnClickListener(this);
        tvCheck.setOnClickListener(this);
        tvPass.setOnClickListener(this);
        tvReject.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_conf_approval_dialog;
    }

    public static void startActivity(Context context, ConfBean confBean) {
        Intent intent = new Intent(context, ConfApprovalDialogActivity.class);
        intent.putExtra(CONF_BEAN, confBean);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭按钮
            case R.id.tv_title_lable:
                finish();
                break;

            case R.id.tv_check:
                //创建对话框
                dialog = new MaterialDialog.Builder(this)
                        .customView(initDialogContent(null, null, null), false)
                        .build();
                dialog.show();
                break;

            //通过
            case R.id.tv_pass:
                passRequest();
                break;

            //驳回
            case R.id.tv_reject:
                rejectRquest();
                break;
        }
    }

    @NonNull
    private View initDialogContent(RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager, GridOffsetsItemDecoration decoration) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_meeting_detail_dialog, null);
        ImageView iv_close = (ImageView) dialogView.findViewById(R.id.iv_close);
        explv = (ExpandableListView) dialogView.findViewById(R.id.expand_list);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if (maps.size() != 0 && org1Names.size() != 0) {
            expAdapter = new NewConfExpandableListViewAdapter(ConfApprovalDialogActivity.this, org1Names, maps);
            explv.setAdapter(expAdapter);
        }

        return dialogView;
    }

//    private void getHistoryById(String id) {
//        //通过findHistoryById获得的_TN_C1,_TN_C6727,_TN_C6516,-3829,3828,3785,3784,查询参会人员
//        HttpUtils.getInstance(this)
//                .getRetofitClinet()
//                .builder(ConfApi.class)
//                .findHistoryById(id)
//                .compose(this.<String>bindToLifecycle())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new RxSubscriber<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        contactList = s;
//                    }
//
//                    @Override
//                    protected void onError(ResponeThrowable responeThrowable) {
//
//                    }
//                });
//    }

    /**
     * 会议审批通过的接口
     */
    private void passRequest() {
        String vetos = etOpinion.getText().toString();
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .approveEntity(
                        contactList,
                        confBean.confParameters,
                        confBean.name,
                        confBean.schedulingTime,
                        confBean.endTime,
                        confBean.duration,
                        confBean.isEmail,
                        confBean.isSms,
                        confBean.smsId,
                        confBean.smsTitle,
                        confBean.smsContext,
                        confBean.contactPeople,
                        confBean.contactTelephone,
                        confBean.peopleIdOa,
                        confBean.id,
                        vetos)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        RxBus.getInstance().post(0);
                        Toast.makeText(ConfApprovalDialogActivity.this, "会议通过", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(ConfApprovalDialogActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
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
        // &conf.id=1240                                会议id
        // &conf.vetos=1240                             审批意见

//        Map<String, String> params = new HashMap<>();
//        params.put("contactList", contactList);
//        params.put("confParameters", confBean.confParameters);
//        params.put("conf.name", confBean.name);
//        params.put("conf.schedulingTime", confBean.schedulingTime);
//        params.put("conf.endTime", confBean.endTime);
//        params.put("conf.duration", confBean.duration);
//        params.put("conf.isEmail", confBean.isEmail);
//        params.put("conf.isSms", confBean.isSms);
//        params.put("conf.smsId", confBean.smsId);
//        params.put("conf.smsTitle", confBean.smsTitle);
//        params.put("conf.smsContext", confBean.smsContext);
//        params.put("conf.contactPeople", confBean.contactPeople);
//        params.put("conf.contactTelephone", confBean.contactTelephone);
//        params.put("operatorId", confBean.peopleIdOa);
//        params.put("conf.id", confBean.id);
//        params.put("conf.vetos", vetos);
//
//        String url = Urls.BASE_URL + Urls.APPROVE_ENTITY;
//
//        OkHttpUtils
//                .post()//
//                .url(url)//
//                .params(params)//
//                .build()//
//                .execute(new GenericsCallback<String>(new JsonGenericsSerializator() {
//                }) {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        Toast.makeText(ConfApprovalDialogActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        RxBus.getInstance().post(0);
//                        Toast.makeText(ConfApprovalDialogActivity.this, "会议通过", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                });
    }

    /**
     * 驳回的网络请求
     */
    private void rejectRquest() {
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .approveCancel(confBean.id)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        RxBus.getInstance().post(0);
                        Toast.makeText(ConfApprovalDialogActivity.this, "会议驳回", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(ConfApprovalDialogActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });


//        Map<String, String> params = new HashMap<>();
//        params.put("conf.id", confBean.id);
//
//        String url = Urls.BASE_URL + Urls.APPROVE_CANCEL;
//        OkHttpUtils
//                .post()//
//                .url(url)//
//                .params(params)//
//                .build()//
//                .execute(new GenericsCallback<String>(new JsonGenericsSerializator() {
//                }) {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        Toast.makeText(ConfApprovalDialogActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        Toast.makeText(ConfApprovalDialogActivity.this, "会议驳回", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                });
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
        return context.replaceAll("&amp;lt;--会场名称--&amp;gt", hcmc)
                .replaceAll("&amp;lt;--会议号码--&amp;gt", hyhm)
                .replaceAll("&amp;lt;--会议名称--&amp;gt", hymc)
                .replaceAll("&amp;lt;--会议时间--&amp;gt", hysj)
                .replaceAll("&amp;lt;--收件人--&amp;gt", sjr);
    }

    /**
     * 通过id查询历史会议
     *
     * @param id 会议Id
     */
    private void getHistoryById(String id) {
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .findHistoryById(id)
                .flatMap(new Func1<String, Observable<DataList<DepartBean>>>() {
                    @Override
                    public Observable<DataList<DepartBean>> call(String s) {
                        contactList = s;
                        return HttpUtils//
                                .getInstance(ConfApprovalDialogActivity.this)
                                .getRetofitClinet()
                                .builder(ConfApi.class)
                                .getPeopleList(s);
                    }
                })
                .compose(new ListDefaultTransformer<DepartBean>())
                .subscribe(new RxSubscriber<List<DepartBean>>() {
                    @Override
                    public void onSuccess(List<DepartBean> departments) {
                        //TODO 根据得到的参会人员列表departments设置右边的数据
                        Log.i("TAG", departments.toString());
                        maps = new HashMap<>();
                        org1Names = new ArrayList<>();
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
                        //Log.i("TAG","hisEmployee的值："+hisEmployee.toString());

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
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(ConfApprovalDialogActivity.this, responeThrowable.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
