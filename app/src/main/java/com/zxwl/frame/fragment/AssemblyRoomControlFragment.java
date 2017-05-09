package com.zxwl.frame.fragment;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.orhanobut.logger.Logger;
import com.zxwl.frame.R;
import com.zxwl.frame.activity.ConfControlActivity;
import com.zxwl.frame.adapter.ConfControlGridAdapter;
import com.zxwl.frame.bean.ConferenceInfo;
import com.zxwl.frame.bean.ConferenceStatus;
import com.zxwl.frame.bean.Site;
import com.zxwl.frame.net.api.ConfApi;
import com.zxwl.frame.net.callback.RxSubscriber;
import com.zxwl.frame.net.exception.ResponeThrowable;
import com.zxwl.frame.net.http.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.ielse.view.SwitchView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 会场控制
 */
public class AssemblyRoomControlFragment extends BaseFragment {
    private TextView tvTerminalLogo;
    private TextView tvUnit;
    private TextView tvAssemblyName;
    private TextView tvWatchAssembly;
    private TextView tvIp;
    private TextView tvConnectionStatus;

    private SwitchView svAssemblySounds;//喇叭静音
    private SwitchView svMicrophone;//麦克风静音
    private SwitchView svCall;//呼叫会场

    private TextView tvTitle;
    private TwinklingRefreshLayout refresh;
    private RecyclerView rvContent;
    private ConfControlGridAdapter adapter;
    private List<Site> beanList = new ArrayList<>();

    private String smcConfId;//会议id

    private Gson gson = new Gson();
    private ConferenceInfo conferenceInfo;//会议信息
    private ConferenceStatus conferenceStatus;//会议状态
    private List<Site> siteList = new ArrayList<>();

    private int currentIndex;//当前选中的下标

    public AssemblyRoomControlFragment() {
    }

    public static AssemblyRoomControlFragment newInstance(String smcConfId) {
        AssemblyRoomControlFragment fragment = new AssemblyRoomControlFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConfControlActivity.SMC_CONF_ID, smcConfId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View inflateContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_assembly_room_control, container, false);
    }

    @Override
    protected void findViews(View view) {
        tvTerminalLogo = (TextView) view.findViewById(R.id.tv_terminal_logo);
        tvUnit = (TextView) view.findViewById(R.id.tv_unit);
        tvAssemblyName = (TextView) view.findViewById(R.id.tv_assembly_name);
        tvWatchAssembly = (TextView) view.findViewById(R.id.tv_watch_assembly);
        tvIp = (TextView) view.findViewById(R.id.tv_ip);
        tvConnectionStatus = (TextView) view.findViewById(R.id.tv_connection_status);
        svAssemblySounds = (SwitchView) view.findViewById(R.id.sv_assembly_sounds);
        svMicrophone = (SwitchView) view.findViewById(R.id.sv_microphone);
        svCall = (SwitchView) view.findViewById(R.id.sv_call);

        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        refresh = (TwinklingRefreshLayout) view.findViewById(R.id.refresh);
        rvContent = (RecyclerView) view.findViewById(R.id.rv_content);
    }

    @Override
    protected void init() {
        Bundle arguments = getArguments();
        smcConfId = (String) arguments.get(ConfControlActivity.SMC_CONF_ID);

        //初始化刷新头部
        initRefresh();

        adapter = new ConfControlGridAdapter(beanList);
        adapter.setOnItemClickListener(new ConfControlGridAdapter.onItemClickListener() {
            @Override
            public void onClick(int position) {
                currentIndex = position;
                setAdapterShow(position);

                //会场的信息
                Site site = adapter.getItem(position);
                setSiteInfo(site);
            }

            @Override
            public void onDelete(int position) {
                Toast.makeText(mContext, "此功能正在开发中", Toast.LENGTH_SHORT).show();
            }
        });
        rvContent.setAdapter(adapter);
        rvContent.setLayoutManager(new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false));

        //获得会议信息
        getConfInfo();
    }

    @Override
    protected void addListeners() {
        //会场静音
        svAssemblySounds.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                Site site = adapter.getItem(currentIndex);
                changeSiteIsQuiet(smcConfId, site.siteInfo.uri, "0");
            }

            @Override
            public void toggleToOff(SwitchView view) {
                Site site = adapter.getItem(currentIndex);
                changeSiteIsQuiet(smcConfId, site.siteInfo.uri, "1");
            }
        });

        //麦克风静音
        svMicrophone.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                Site site = adapter.getItem(currentIndex);
                changeSiteIsMute(smcConfId, site.siteInfo.uri, "0");
            }

            @Override
            public void toggleToOff(SwitchView view) {
                Site site = adapter.getItem(currentIndex);
                changeSiteIsMute(smcConfId, site.siteInfo.uri, "1");
            }
        });

        //呼叫会场
        svCall.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                Site site = adapter.getItem(currentIndex);
                callSite(smcConfId, site.siteInfo.uri);
            }

            @Override
            public void toggleToOff(SwitchView view) {
                Site site = adapter.getItem(currentIndex);
                disconnectSite(smcConfId, site.siteInfo.uri);
            }
        });
    }

    /**
     * 初始化刷新控件
     */
    private void initRefresh() {
        //设置刷新的头部
        ProgressLayout progressLayout = new ProgressLayout(getContext());
        refresh.setHeaderView(progressLayout);
        //设置加载的底部
        LoadingView loadingView = new LoadingView(getContext());
        refresh.setBottomView(loadingView);

        //设置像SwipeRefreshLayout一样的悬浮刷新模式了
        refresh.setFloatRefresh(true);
        //设置是否回弹
        refresh.setEnableOverScroll(false);
        //设置头部高度
        refresh.setHeaderHeight(100);
        //设置头部的最大高度
        refresh.setMaxHeadHeight(120);
        //设置刷新的view
        refresh.setTargetView(rvContent);
        //设置刷新不可用
        refresh.setEnableRefresh(false);
        //设置上拉加载不可用
        refresh.setEnableLoadmore(false);
    }

    /**
     * 设置会场信息
     *
     * @param site
     */
    private void setSiteInfo(Site site) {
        tvTerminalLogo.setText("终端标识符：" + site.siteInfo.uri);
        tvUnit.setText("单位名称：" + site.unitName);
        tvAssemblyName.setText("会场名称：" + site.siteStatus.name);
        tvWatchAssembly.setText("观看会场：" + site.siteStatus.videoSource);
        String status = "";
        if ("0".equals(site.siteStatus.status)) {
            status = "未知状态";
        } else if ("1".equals(site.siteStatus.status)) {
            status = "会场不存在";
        } else if ("2".equals(site.siteStatus.status)) {
            status = "在会议中";
        } else if ("3".equals(site.siteStatus.status)) {
            status = "未入会";
        } else if ("4".equals(site.siteStatus.status)) {
            status = "正在呼叫";
        } else if ("5".equals(site.siteStatus.status)) {
            status = "正在振铃";
        }
        tvConnectionStatus.setText("连接状态：" + status);
        //麦克风状态
        if ("0".equals(site.siteStatus.isMute)) {
            svMicrophone.setOpened(true);
        } else {
            svMicrophone.setOpened(false);
        }

        //喇叭状态
        if ("0".equals(site.siteStatus.isQuiet)) {
            svAssemblySounds.setOpened(true);
        } else {
            svAssemblySounds.setOpened(false);
        }

        //呼叫会场状态
        if ("2".equals(site.siteStatus.status)) {
            svCall.setOpened(true);
        } else {
            svCall.setOpened(false);
        }
    }

    /**
     * 获得会议信息
     */
    private void getConfInfo() {
        HttpUtils.getInstance(getContext())
                .getRetofitClinet()
                .builder(ConfApi.class)
                .joinTOConf(smcConfId)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            JSONArray array = object.getJSONArray("conference");
                            //会议信息
                            String s1 = array.getString(0);
                            conferenceInfo = gson.fromJson(s1, ConferenceInfo.class);

                            //会议状态
                            String s2 = array.getString(1);
                            Logger.i(s2);
                            conferenceStatus = gson.fromJson(s2, ConferenceStatus.class);

                            //参会的会场列表
                            String s3 = array.getString(2);
                            Logger.i(s3);
                            siteList = gson.fromJson(s3, new TypeToken<List<Site>>() {
                            }.getType());

                            for (int i = 0, count = siteList.size(); i < count; i++) {
                                siteList.get(i).showControl = false;
                            }

                            //刷新适配器
                            adapter.addAll(siteList);

                            tvTitle.setText(conferenceStatus.name);
                            if (siteList.size() > 0) {
                                Site site = siteList.get(0);
                                setSiteInfo(site);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(mContext, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * 会场控制：喇叭静音(会场静音)
     *
     * @param smcConfId
     * @param siteUris
     * @param isQuiet   1 取消静音，0 静音喇叭
     */
    private void changeSiteIsQuiet(String smcConfId,
                                   String siteUris,
                                   final String isQuiet) {
        HttpUtils.getInstance(getContext())
                .getRetofitClinet()
                .builder(ConfApi.class)
                .changeSiteIsQuiet(smcConfId, siteUris, isQuiet)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Site site = adapter.getItem(currentIndex);
                        //静音
                        if ("0".equals(isQuiet)) {
                            svAssemblySounds.setOpened(true);
                            site.siteStatus.isQuiet = "0";
                            Toast.makeText(getContext(), "设置喇叭静音", Toast.LENGTH_SHORT).show();
                        } else {
                            svAssemblySounds.setOpened(false);
                            site.siteStatus.isQuiet = "1";
                            Toast.makeText(getContext(), "取消喇叭静音", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 会场控制：麦克风静音
     *
     * @param smcConfId
     * @param siteUris
     * @param isMute:   1 取消静音，0 静音麦克风
     */
    private void changeSiteIsMute(String smcConfId,
                                  String siteUris,
                                  final String isMute) {
        HttpUtils.getInstance(getContext())
                .getRetofitClinet()
                .builder(ConfApi.class)
                .changeSiteIsMute(smcConfId, siteUris, isMute)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Site site = adapter.getItem(currentIndex);
                        if ("0".equals(isMute)) {
                            svMicrophone.setOpened(true);
                            site.siteStatus.isMute = "0";
                            Toast.makeText(getContext(), "设置麦克风静音", Toast.LENGTH_SHORT).show();
                        } else {
                            svMicrophone.setOpened(false);
                            site.siteStatus.isMute = "1";
                            Toast.makeText(getContext(), "取消麦克风静音", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 会场控制：呼叫会场
     *
     * @param smcConfId
     * @param siteUris
     */
    private void callSite(String smcConfId,
                          String siteUris) {
        HttpUtils.getInstance(getContext())
                .getRetofitClinet()
                .builder(ConfApi.class)
                .callSite(smcConfId, siteUris)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Site site = adapter.getItem(currentIndex);
                        svCall.setOpened(true);
                        site.siteStatus.status = "2";
                        tvConnectionStatus.setText("连接状态：在会议中");
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 会场控制：断开会场
     *
     * @param smcConfId
     * @param siteUris
     */
    private void disconnectSite(String smcConfId,
                                String siteUris) {
        HttpUtils.getInstance(getContext())
                .getRetofitClinet()
                .builder(ConfApi.class)
                .disconnectSite(smcConfId, siteUris)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Site site = adapter.getItem(currentIndex);
                        svCall.setOpened(false);
                        site.siteStatus.status = "3";
                        tvConnectionStatus.setText("连接状态：未入会");
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 设置adapter的显示
     *
     * @param position 下标
     */
    private void setAdapterShow(int position) {
        for (int i = 0, count = beanList.size(); i < count; i++) {
            if (i != position) {
                beanList.get(i).showControl = false;
            }
        }
        Site site = beanList.get(position);
        site.showControl = !site.showControl;
        adapter.notifyDataSetChanged();
    }
}
