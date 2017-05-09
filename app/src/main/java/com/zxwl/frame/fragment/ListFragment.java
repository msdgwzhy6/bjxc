package com.zxwl.frame.fragment;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zxwl.frame.R;

/**
 * Test的Fragment
 */
public class ListFragment extends BaseLazyFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TwinklingRefreshLayout refreshLayout;
    private RecyclerView rvList;

    private int PAGE_SIZE = 5;//每页显示的数据
    private int PAGE_NUM = 0;//页码

    public ListFragment() {
    }

    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View inflateContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.refresh_recycler_view, container, false);
    }

    @Override
    protected void findViews(View view) {
        refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.refreshLayout);
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
    }

    @Override
    protected void addListener() {
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

    private void getData(int pageNum) {

    }

    @Override
    protected void initData() {
        //初始化刷新
        initRefresh();
    }

    /**
     * 初始化刷新控件
     */
    private void initRefresh() {
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
        refreshLayout.setHeaderHeight(140);
        //设置头部的最大高度
        refreshLayout.setMaxHeadHeight(240);
        //设置刷新的view
        refreshLayout.setTargetView(rvList);
    }

}
