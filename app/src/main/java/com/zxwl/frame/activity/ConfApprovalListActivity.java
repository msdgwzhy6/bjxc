package com.zxwl.frame.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zxwl.frame.R;
import com.zxwl.frame.adapter.ConfApprovalGridAdapter;
import com.zxwl.frame.adapter.ConfApprovalListAdapter;
import com.zxwl.frame.bean.ConfBean;
import com.zxwl.frame.bean.DataList;
import com.zxwl.frame.net.api.ConfApi;
import com.zxwl.frame.net.callback.RxSubscriber;
import com.zxwl.frame.net.exception.ResponeThrowable;
import com.zxwl.frame.net.http.HttpUtils;
import com.zxwl.frame.rx.RxBus;
import com.zxwl.frame.utils.UserHelper;
import com.zxwl.frame.views.spinner.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 会议审批列表
 */
public class ConfApprovalListActivity extends BaseActivity implements View.OnClickListener {
    /*头部公用控件-start*/
    private TextView tvLogOut;
    private TextView tvIssue;
    private TextView tvHome;
    private TextView tvName;
    /*头部公用控件-end*/

    private EditText etTopTitleSearch;//搜索框
    private NiceSpinner timeSpinner;//时间选择
    private TextView tvLayout;
    private PercentLinearLayout rlOperate;

    /*列表刷新-start*/
    private TwinklingRefreshLayout refreshLayout;
    private RecyclerView rvList;
    private int PAGE_SIZE = 5;
    private int PAGE_NUM = 0;
    private List<ConfBean> list = new ArrayList<>();
    /*列表刷新-end*/

    private ConfApprovalListAdapter listAdapter;
    private ConfApprovalGridAdapter gridAdapter;

    private boolean listFalg = true;//是否是list列表样式

    private int approvalIndex;//当前审批会议下标

    private Subscription subscribe;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ConfApprovalListActivity.class));
    }

    @Override
    protected void findViews() {
        tvLogOut = (TextView) findViewById(R.id.tv_log_out);
        tvIssue = (TextView) findViewById(R.id.tv_issue);
        tvHome = (TextView) findViewById(R.id.tv_home);
        tvName = (TextView) findViewById(R.id.tv_name);

        rlOperate = (PercentLinearLayout) findViewById(R.id.ll_list_title);
        tvLayout = (TextView) findViewById(R.id.tv_layout);
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

        initRecycler();
        initRefresh();

        initRxBus();
        //开始刷新
        refreshLayout.startRefresh();

    }
    private void initRxBus() {
        subscribe = RxBus.getInstance()
                .toObserverable(Integer.class)
                .compose(this.<Integer>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        list.remove(approvalIndex);
                        refreshAdapter();
                    }
                });
    }


    /**
     * 初始化recyclerview
     */
    private void initRecycler() {
        listAdapter = new ConfApprovalListAdapter(list);
        gridAdapter = new ConfApprovalGridAdapter(list);
        gridAdapter.setOnItemClickListener(new ConfApprovalGridAdapter.onItemClickListener() {
            @Override
            public void onClick(int position) {
                approvalIndex = position;
                ConfApprovalDialogActivity.startActivity(ConfApprovalListActivity.this, list.get(position));
            }
        });

        listAdapter.setOnItemClickListener(new ConfApprovalListAdapter.onItemClickListener() {
            @Override
            public void onClick(int position) {
                approvalIndex = position;
                ConfApprovalDialogActivity.startActivity(ConfApprovalListActivity.this, list.get(position));
            }
        });

        setRecyclerLayout();
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
        refreshLayout.setMaxHeadHeight(150);
        //设置刷新的view
        refreshLayout.setTargetView(rvList);
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

        tvLayout.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_conf_aoorival_list;
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
                .getConfApprovalList(PAGE_SIZE, pageNum)
                .compose(this.<DataList<ConfBean>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<DataList<ConfBean>>() {
                    @Override
                    public void onSuccess(DataList<ConfBean> result) {
                        //成功则代表请求到数据
                        if (null != result.dataList
//                                && result.dataList.size() > 0
                                ) {
                            //1为刷新，否则为加载更多
                            if (1 == pageNum) {
                                PAGE_NUM = 1;
                                list.clear();
                                list.addAll(result.dataList);
                                refreshLayout.finishRefreshing();
                                //刷新的时候设置加载更多可以使用
                                refreshLayout.setEnableLoadmore(true);
                                Toast.makeText(ConfApprovalListActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                            } else {
                                PAGE_NUM++;
                                list.addAll(result.dataList);
                                refreshLayout.finishLoadmore();
                                Toast.makeText(ConfApprovalListActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                            }

                            //刷新适配器
                            refreshAdapter();

                            //如果当前条数大于或等于总条数则禁用加载更多
                            if (list.size() >= Integer.valueOf(result.rowSum)) {
                                refreshLayout.setEnableLoadmore(false);
                            }
                        } else {
                            if (1 == pageNum) {
                                //清空所有数据
                                list.clear();
                                //根据样式刷新布局
                                refreshAdapter();
                                Toast.makeText(ConfApprovalListActivity.this, "当前没有待审批会议", Toast.LENGTH_SHORT).show();
                                refreshLayout.finishRefreshing();
                            } else {
                                Toast.makeText(ConfApprovalListActivity.this, "当前没有更多待审批会议", Toast.LENGTH_SHORT).show();
                                refreshLayout.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        Toast.makeText(ConfApprovalListActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                        if (1 == pageNum) {
                            refreshLayout.finishRefreshing();
                        } else {
                            refreshLayout.finishLoadmore();
                        }
                    }
                });
    }

    /**
     * 刷新适配器
     */
    private void refreshAdapter() {
        //根据样式刷新布局
        if (listFalg) {
            listAdapter.notifyDataSetChanged();
        } else {
            gridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //切换布局样式
            case R.id.tv_layout:
                listFalg = !listFalg;
                //设置他的样式
                setRecyclerLayout();
                break;
        }
    }

    /**
     * 设置recyclerview的适配器和样式管理器
     */
    private void setRecyclerLayout() {
        //设置按钮的图标
        tvLayout.setCompoundDrawablesWithIntrinsicBounds(listFalg ? R.mipmap.icon_linear_layout : R.mipmap.icon_grid_layout, 0, 0, 0);

        //如果为list样式
        if (listFalg) {
            rvList.setLayoutManager(new LinearLayoutManager(ConfApprovalListActivity.this));
            rvList.setAdapter(listAdapter);
            rlOperate.setVisibility(View.VISIBLE);
        } else {
            rvList.setLayoutManager(new GridLayoutManager(ConfApprovalListActivity.this, 5, GridLayoutManager.VERTICAL, false));
            rvList.setAdapter(gridAdapter);
            rlOperate.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != subscribe) {
            subscribe.unsubscribe();
        }
    }
}