package com.zxwl.frame.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.zxwl.frame.AppManager;


/**
 * author：hw
 * data:2017/4/11 08:10
 * 基类的activity
 */
public abstract class BaseActivity extends RxAppCompatActivity {
    /**
     * 初始化view
     */
    protected abstract void findViews();

    /**
     * 初始化view的数据
     */
    protected abstract void initData();

    /**
     * 设置view的监听事件
     */
    protected abstract void setListener();

    /**
     * 获得布局layout id
     * @return
     */
    protected abstract int getLayoutId();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        //添加Activity到管理栈中
        AppManager.getInstance().addActivity(this);
        findViews();
        initData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        AppManager.getInstance().finishActivity(this);
    }
}
