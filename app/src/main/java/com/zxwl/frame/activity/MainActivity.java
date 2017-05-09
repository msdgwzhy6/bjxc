package com.zxwl.frame.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jaeger.library.StatusBarUtil;
import com.zxwl.frame.AppManager;
import com.zxwl.frame.R;
import com.zxwl.frame.bean.TabEntity;
import com.zxwl.frame.fragment.ImageFragment;
import com.zxwl.frame.fragment.ListFragment;

import java.util.ArrayList;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {
    private CommonTabLayout tabLayout;

    private String[] mTitles = {
            "one",
            "two",
            "three",
            "four"
    };

    private int[] iconUnselectIds = {
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher

    };

    private int[] iconSelectIds = {
            R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round,
            R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round};

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private ArrayList<Fragment> fragments = new ArrayList<>();

    /**
     * 判断是否已经点击过一次回退键
     */
    private boolean isBackPressed = false;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void findViews() {
        tabLayout = (CommonTabLayout) findViewById(R.id.tab_layout);
    }

    @Override
    protected void initData() {
        //初始化tab
        initTab();

        //当fragment顶部有图片时设置
        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, null);
    }

    @Override
    protected void setListener() {}

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    // 初始化tab
    private void initTab() {
        for (int i = 0, count = mTitles.length; i < count; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], iconSelectIds[i], iconUnselectIds[i]));
        }
        fragments.add(ListFragment.newInstance("第一个","Fragment"));
        fragments.add(ListFragment.newInstance("第二个","Fragment"));
        fragments.add(ImageFragment.newInstance("第三个","Fragment"));
        fragments.add(ListFragment.newInstance("第四个","Fragment"));

        tabLayout.setTabData(mTabEntities, this, R.id.fl_content, fragments);
        tabLayout.setCurrentTab(0);

        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void doublePressBackToast() {
        if (!isBackPressed) {
            Log.i("doublePressBackToast", "再次点击返回退出程序");
            isBackPressed = true;
            Toast.makeText(this, "再次点击返回退出程序", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("doublePressBackToast", "exit");
            finish();
            AppManager.getInstance().appExit();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressed = false;
            }
        }, 2000);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doublePressBackToast();
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }
}
