package com.zxwl.frame.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zxwl.frame.R;
import com.zxwl.frame.adapter.ConfControlAdapter;
import com.zxwl.frame.bean.ConfBean;
import com.zxwl.frame.bean.DataList;
import com.zxwl.frame.net.api.ConfApi;
import com.zxwl.frame.net.callback.RxSubscriber;
import com.zxwl.frame.net.exception.ResponeThrowable;
import com.zxwl.frame.net.http.HttpUtils;
import com.zxwl.frame.utils.UserHelper;
import com.zxwl.frame.views.spinner.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 会议控制列表
 */
public class ConfControlListActivity extends BaseActivity {
    /*头部公用控件-start*/
    private TextView tvLogOut;
    private TextView tvIssue;
    private TextView tvHome;
    private TextView tvName;
    /*头部公用控件-end*/

    private EditText etTopTitleSearch;//搜索框
    private NiceSpinner timeSpinner;//时间选择

    /*列表刷新-start*/
    private TwinklingRefreshLayout refreshLayout;
    private RecyclerView rvList;
    private int PAGE_SIZE = 5;
    private int PAGE_NUM = 0;
    private List<ConfBean> list = new ArrayList<>();
    /*列表刷新-end*/

    private ConfControlAdapter adapter;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ConfControlListActivity.class));
    }

    @Override
    protected void findViews() {
        tvLogOut = (TextView) findViewById(R.id.tv_log_out);
        tvIssue = (TextView) findViewById(R.id.tv_issue);
        tvHome = (TextView) findViewById(R.id.tv_home);
        tvName = (TextView) findViewById(R.id.tv_name);

        etTopTitleSearch = (EditText) findViewById(R.id.et_top_title_search);
        timeSpinner = (NiceSpinner) findViewById(R.id.time_spinner);
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refreshLayout);
        rvList = (RecyclerView) findViewById(R.id.rv_list);
    }

    @Override
    protected void initData() {
        //顶部退出按钮
        tvLogOut.setVisibility(View.VISIBLE);
        tvIssue.setVisibility(View.VISIBLE);
        tvHome.setVisibility(View.VISIBLE);
        tvName.setVisibility(View.VISIBLE);

        tvName.setText(UserHelper.getSavedUser().name);

        adapter = new ConfControlAdapter(list);
        adapter.setOnItemClickListener(new ConfControlAdapter.onItemClickListener() {
            @Override
            public void onClick(int position) {
                setAdapterShow(position);
            }

            @Override
            public void onControl(int position) {
                setAdapterShow(position);
                //控制会议
                ConfControlActivity.startActivity(ConfControlListActivity.this, list.get(position).smcConfId);
            }

            @Override
            public void onFinish(int position) {
                setAdapterShow(position);

                ConfBean confBean = list.get(position);
                //结束会议的网络请求
                finishConfRequest(confBean.id, confBean.smcConfId,position);
            }
        });
        rvList.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
        rvList.setAdapter(adapter);
        initRefresh();
        refreshLayout.startRefresh();
    }

    /**
     * 结束会议
     *  @param confId
     * @param smcConfId
     * @param position
     */
    private void finishConfRequest(String confId, String smcConfId, final int position) {
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .finishConf(confId, smcConfId)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(ConfControlListActivity.this, "会议结束", Toast.LENGTH_SHORT).show();
                        adapter.reomve(position);
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(ConfControlListActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 设置adapter的显示
     *
     * @param position 下标
     */
    private void setAdapterShow(int position) {
        for (int i = 0, count = list.size(); i < count; i++) {
            if (i != position) {
                list.get(i).showControl = false;
            }
        }
        ConfBean bean = list.get(position);
        bean.showControl = !bean.showControl;
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void setListener() {
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                //刷新数据
                getData(1);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                //判断当前条数是否大于数据总条数
                //结束加载更多，需手动调用
                getData(PAGE_NUM + 1);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_conf_control_list;
    }

    /**
     * 初始化刷新控件
     */
    private void initRefresh() {
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
        //设置头部高度
        refreshLayout.setHeaderHeight(140);
        //设置头部的最大高度
        refreshLayout.setMaxHeadHeight(200);
        //设置刷新的view
        refreshLayout.setTargetView(rvList);
    }

    /**
     * 获得会议列表数据
     *
     * @param pageNum 页码
     */
    private void getData(final int pageNum) {
        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(ConfApi.class)
                .getConfBeinglList(PAGE_SIZE, pageNum)
                .compose(this.<DataList<ConfBean>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<DataList<ConfBean>>() {
                    @Override
                    public void onSuccess(DataList<ConfBean> result) {
                        //成功则代表请求到数据
                        if (null != result.dataList) {
//                                && result.dataList.size() > 0
                            //给所有的bean添加状态
                            for (int i = 0, count = result.dataList.size(); i < count; i++) {
                                result.dataList.get(i).showControl = false;
                            }

                            //1为刷新，否则为加载更多
                            if (1 == pageNum) {
                                PAGE_NUM = 1;
                                list.clear();
                                list.addAll(result.dataList);
                                refreshLayout.finishRefreshing();
                                //刷新的时候设置加载更多可以使用
                                refreshLayout.setEnableLoadmore(true);
                                Toast.makeText(ConfControlListActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                            } else {
                                PAGE_NUM++;
                                list.addAll(result.dataList);
                                refreshLayout.finishLoadmore();
                                Toast.makeText(ConfControlListActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                            }

                            //刷新适配器
                            adapter.notifyDataSetChanged();

                            //如果当前条数大于或等于总条数则禁用加载更多
                            if (list.size() >= Integer.valueOf(result.rowSum)) {
                                refreshLayout.setEnableLoadmore(false);
                            }
                        } else {
                            if (1 == pageNum) {
                                //清空所有数据
                                list.clear();
                                //根据样式刷新布局
                                adapter.notifyDataSetChanged();
                                Toast.makeText(ConfControlListActivity.this, "当前没有会议", Toast.LENGTH_SHORT).show();
                                refreshLayout.finishRefreshing();
                            } else {
                                Toast.makeText(ConfControlListActivity.this, "当前没有更多会议", Toast.LENGTH_SHORT).show();
                                refreshLayout.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(ConfControlListActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                        if (1 == pageNum) {
                            refreshLayout.finishRefreshing();
                        } else {
                            refreshLayout.finishLoadmore();
                        }
                    }
                });
    }

}
