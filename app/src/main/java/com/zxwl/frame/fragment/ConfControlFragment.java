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
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
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

import static com.zxwl.frame.R.id.refresh;

/**
 * 会议控制fragment
 */
public class ConfControlFragment extends BaseFragment {
    private TextView tvAssemblyName;//会场名称
    private TextView tvStartTime;//召开时间
    private TextView tvEndTime;//结束时间
    private TextView tvVoiceSwitch;//声控切换
    private TextView tvLectureHall;//演讲会场
    private TextView tvChairmanHall;//主席会场
    private TextView tvBroadcastHall;//广播会场
    private TextView tvSecondaryHall;//辅流会场

//    private SwitchView svSetPoll;//设置轮询
//    private SwitchView svFractionalPoll;//分频轮询
//    private SwitchView svVenueVolume;//会场音量
//    private SwitchView svVenueSounds;//会场声音

    private SwitchView svSetChairman;//设置主席
    private SwitchView svSetVoice;//声控切换
    private SwitchView svBroadcastVenue;//广播会场
    private TextView tvTitle;//会议名称

    private TwinklingRefreshLayout refreshLayout;
    private RecyclerView rvList;
    private ConfControlGridAdapter adapter;
    private List<Site> beanList = new ArrayList<>();

    private int PAGE_SIZE = 5;
    private int PAGE_NUM = 0;

    private String smcConfId;//会议id

    private Gson gson = new Gson();
    private ConferenceInfo conferenceInfo;//会议信息
    private ConferenceStatus conferenceStatus;//会议状态
    private List<Site> siteList = new ArrayList<>();

    private int audioSwitch;//声控切换  1开启  0关闭

    private int currentIndex;//当前选中的下标

    private int chairIndex = -1;//主席会场的下标
    private int broadcastIndex = -1;//广播会场的下标

    public ConfControlFragment() {
    }

    public static ConfControlFragment newInstance(String smcConfId) {
        ConfControlFragment fragment = new ConfControlFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConfControlActivity.SMC_CONF_ID, smcConfId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View inflateContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_conf_control, container, false);
    }

    @Override
    protected void findViews(View view) {
        tvAssemblyName = (TextView) view.findViewById(R.id.tv_assembly_name);
        tvStartTime = (TextView) view.findViewById(R.id.tv_start_time);
        tvEndTime = (TextView) view.findViewById(R.id.tv_end_time);
        tvVoiceSwitch = (TextView) view.findViewById(R.id.tv_voice_switch);
        tvLectureHall = (TextView) view.findViewById(R.id.tv_lecture_hall);
        tvChairmanHall = (TextView) view.findViewById(R.id.tv_chairman_hall);
        tvBroadcastHall = (TextView) view.findViewById(R.id.tv_broadcast_hall);
        tvSecondaryHall = (TextView) view.findViewById(R.id.tv_secondary_hall);
        svSetChairman = (SwitchView) view.findViewById(R.id.sv_set_chairman);
        svSetVoice = (SwitchView) view.findViewById(R.id.sv_set_voice);
        svBroadcastVenue = (SwitchView) view.findViewById(R.id.sv_broadcast_venue);
//        svSetPoll = (SwitchView) view.findViewById(R.id.sv_set_poll);
//        svFractionalPoll = (SwitchView) view.findViewById(R.id.sv_fractional_poll);
//        svVenueVolume = (SwitchView) view.findViewById(R.id.sv_venue_volume);
//        svVenueSounds = (SwitchView) view.findViewById(R.id.sv_venue_sounds);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        refreshLayout = (TwinklingRefreshLayout) view.findViewById(refresh);
        rvList = (RecyclerView) view.findViewById(R.id.rv_content);
    }

    @Override
    protected void init() {
        Bundle arguments = getArguments();
        smcConfId = (String) arguments.get(ConfControlActivity.SMC_CONF_ID);

        initRefresh();

        adapter = new ConfControlGridAdapter(beanList);
        adapter.setOnItemClickListener(new ConfControlGridAdapter.onItemClickListener() {
            @Override
            public void onClick(int position) {
                currentIndex = position;
                setAdapterShow(position);

                //判断是否是主席会场
                if (chairIndex == currentIndex) {
                    svSetChairman.setOpened(true);
                } else {
                    svSetChairman.setOpened(false);
                }

                //判断是否是广播会场
                if (broadcastIndex == currentIndex) {
                    svBroadcastVenue.setOpened(true);
                } else {
                    svBroadcastVenue.setOpened(false);
                }
            }

            @Override
            public void onDelete(int position) {
                Toast.makeText(mContext, "此功能正在开发中", Toast.LENGTH_SHORT).show();
//                adapter.remove(position);
//                Site site = siteList.get(position);
//                //TODO 移除会场
//                delSIte(smcConfId, conferenceInfo.confId, site.siteInfo.uri, position);
            }
        });
        rvList.setAdapter(adapter);
        rvList.setLayoutManager(new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false));

        //获得会议信息
        getConfInfo();
    }

    @Override
    protected void addListeners() {
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                //刷新数据
                //getData(1);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                //判断当前条数是否大于数据总条数
                //结束加载更多，需手动调用
                //getData(PAGE_NUM + 1);
            }
        });

        //设置主席
        svSetChairman.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                // 设置主席
                Site site = siteList.get(currentIndex);
                setChair(smcConfId, site.siteInfo.uri, site.siteInfo.name, String.valueOf(1));
            }

            @Override
            public void toggleToOff(SwitchView view) {
                //取消主席
                Site site = siteList.get(currentIndex);
                releaseChair(smcConfId, site.siteInfo.uri, String.valueOf(0));
            }
        });

        //声控切换
        svSetVoice.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                audioSwitch(smcConfId, String.valueOf(1));
            }

            @Override
            public void toggleToOff(SwitchView view) {
                audioSwitch(smcConfId, String.valueOf(0));
            }
        });

        //广播会场
        svBroadcastVenue.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                //获得当前选中的控制会场
                Site site = siteList.get(currentIndex);
                broadcastSite(smcConfId, site.siteInfo.uri, site.siteInfo.name, String.valueOf(0));
            }

            @Override
            public void toggleToOff(SwitchView view) {
                //获得当前选中的控制会场
                Site site = siteList.get(currentIndex);
                broadcastSite(smcConfId, site.siteInfo.uri, site.siteInfo.name, String.valueOf(1));
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

    /**
     * 初始化刷新控件
     */
    private void initRefresh() {
        Logger.i(refreshLayout.toString());
        //设置刷新的头部
        ProgressLayout progressLayout = new ProgressLayout(getContext());
        refreshLayout.setHeaderView(progressLayout);
        //设置加载的底部
        LoadingView loadingView = new LoadingView(getContext());
        refreshLayout.setBottomView(loadingView);

        //设置像SwipeRefreshLayout一样的悬浮刷新模式了
        refreshLayout.setFloatRefresh(true);
        //设置是否回弹
        refreshLayout.setEnableOverScroll(false);
        //设置头部高度
        refreshLayout.setHeaderHeight(100);
        //设置头部的最大高度
        refreshLayout.setMaxHeadHeight(120);
        //设置刷新的view
        refreshLayout.setTargetView(rvList);
        //设置刷新不可用
        refreshLayout.setEnableRefresh(false);
        //设置上拉加载不可用
        refreshLayout.setEnableLoadmore(false);
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
                            Logger.i("response", s);
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

                            //设置bean的操作状态
                            for (int i = 0, count = siteList.size(); i < count; i++) {
                                siteList.get(i).showControl = false;
                            }

                            //刷新适配器
                            adapter.addAll(siteList);

                            // 会议状态信息填写
                            tvTitle.setText(conferenceStatus.name);
                            tvAssemblyName.setText("会议名称：" + conferenceStatus.name);//会议名称
                            tvStartTime.setText("召开时间：" + conferenceStatus.beginTime);//召开时间
                            tvEndTime.setText("结束时间：" + conferenceStatus.endTime);//结束时间
                            tvLectureHall.setText("演讲会场：" + conferenceStatus.speaking);//演讲会场
                            tvChairmanHall.setText("主席会场：" + conferenceStatus.chair);//主席会场
                            tvBroadcastHall.setText("广播会场：" + conferenceStatus.broadcast);//广播会场
                            tvSecondaryHall.setText("辅流会场：" + conferenceStatus.presentation);//辅流会场
                            //设置声控切换的状态
                            audioSwitch = conferenceStatus.isAudioSwitch;
                            //设置声音切换的状态
                            svSetVoice.setOpened(0 == audioSwitch);
                            tvVoiceSwitch.setText("声控切换：" + (0 == audioSwitch ? "打开" : "关闭"));//声控切换
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
     * 移除会场
     *
     * @param smcConfId 会议id
     * @param confId    会议id
     * @param siteUris  会场uri
     * @param position  adapter位置
     */
    private void delSIte(String smcConfId,
                         String confId,
                         String siteUris,
                         final int position) {
        HttpUtils.getInstance(getContext())
                .getRetofitClinet()
                .builder(ConfApi.class)
                .delSiteFromConf(smcConfId, confId, siteUris)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(getContext(), "会场移除成功", Toast.LENGTH_SHORT).show();
                        adapter.remove(position);
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 设置主席
     *
     * @param smcConfId
     * @param siteUris
     * @param siteName
     * @param flag
     */
    private void setChair(String smcConfId,
                          String siteUris,
                          final String siteName,
                          final String flag) {
        HttpUtils.getInstance(getContext())
                .getRetofitClinet()
                .builder(ConfApi.class)
                .requestChair(smcConfId, siteUris)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(getContext(), "设置主席成功", Toast.LENGTH_SHORT).show();
                        svSetChairman.setOpened(true);
                        tvChairmanHall.setText("主席会场：" + siteName);//主席会场
                        //设置主席会场的下标
                        chairIndex = currentIndex;
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 取消主席
     *
     * @param smcConfId
     * @param siteUris
     * @param flag
     */
    private void releaseChair(String smcConfId,
                              String siteUris,
                              final String flag) {
        HttpUtils.getInstance(getContext())
                .getRetofitClinet()
                .builder(ConfApi.class)
                .releaseChair(smcConfId, siteUris)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(getContext(), "取消主席成功", Toast.LENGTH_SHORT).show();
                        svSetChairman.setOpened(false);
                        tvChairmanHall.setText("主席会场：");//主席会场
                        //设置主席会场的下标
                        chairIndex = -1;
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 广播会场
     *
     * @param smcConfId
     * @param siteUris
     * @param isBroadcast 0开始  1关闭
     * @param siteName
     */
    private void broadcastSite(String smcConfId,
                               String siteUris,
                               final String siteName,
                               final String isBroadcast) {
        HttpUtils.getInstance(getContext())
                .getRetofitClinet()
                .builder(ConfApi.class)
                .broadcastSite(smcConfId, siteUris, isBroadcast)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(getContext(), "广播会场切换成功", Toast.LENGTH_SHORT).show();
                        if ("0".equals(isBroadcast)) {
                            svBroadcastVenue.setOpened(true);
                            tvBroadcastHall.setText("广播会场：" + siteName);
                            //设置广播会场的下标
                            broadcastIndex = currentIndex;
                        } else {
                            svBroadcastVenue.setOpened(false);
                            tvBroadcastHall.setText("广播会场：");
                            broadcastIndex = -1;
                        }
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 声控切换
     *
     * @param smcConfId
     * @param isSwitch  1 开启，0 关闭
     */
    private void audioSwitch(String smcConfId,
                             final String isSwitch) {
        HttpUtils.getInstance(getContext())
                .getRetofitClinet()
                .builder(ConfApi.class)
                .audioSwitch(smcConfId, isSwitch)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(getContext(), "声控切换成功", Toast.LENGTH_SHORT).show();
                        if ("1".equals(isSwitch)) {
                            svSetVoice.setOpened(true);
                            tvVoiceSwitch.setText("声控切换：打开");//声控切换
                        } else {
                            svSetVoice.setOpened(false);
                            tvVoiceSwitch.setText("声控切换：关闭");//声控切换
                        }
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
