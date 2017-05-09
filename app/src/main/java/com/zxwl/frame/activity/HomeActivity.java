package com.zxwl.frame.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zxwl.frame.AppManager;
import com.zxwl.frame.R;
import com.zxwl.frame.utils.UserHelper;

/**
 * 主页
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvLogOut;
    private TextView tvIssue;
    private TextView tvHome;
    private TextView tvName;

    private TextView tvConfSubscribe;
    private TextView tvConfApprove;
    private TextView tvConfControl;
    private TextView tvTemplateManage;

    /**
     * 判断是否已经点击过一次回退键
     */
    private boolean isBackPressed = false;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    @Override
    protected void findViews() {
        tvLogOut = (TextView) findViewById(R.id.tv_log_out);
        tvIssue = (TextView) findViewById(R.id.tv_issue);
        tvHome = (TextView) findViewById(R.id.tv_home);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvConfSubscribe = (TextView) findViewById(R.id.tv_conf_subscribe);
        tvConfApprove = (TextView) findViewById(R.id.tv_conf_approve);
        tvConfControl = (TextView) findViewById(R.id.tv_conf_control);
        tvTemplateManage = (TextView) findViewById(R.id.tv_template_manage);
    }

    @Override
    protected void initData() {
        tvLogOut.setVisibility(View.VISIBLE);
        tvIssue.setVisibility(View.VISIBLE);
        tvHome.setVisibility(View.VISIBLE);
        tvName.setVisibility(View.VISIBLE);

        if (null != UserHelper.getSavedUser()) {
            tvName.setText(UserHelper.getSavedUser().name);
        }
    }

    @Override
    protected void setListener() {
        tvConfSubscribe.setOnClickListener(this);
        tvConfApprove.setOnClickListener(this);
        tvConfControl.setOnClickListener(this);
        tvTemplateManage.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //会议预约
            case R.id.tv_conf_subscribe:
                NewConfActivity.startActivity(this);
                break;

            //会议审批
            case R.id.tv_conf_approve:
                ConfApprovalListActivity.startActivity(this);
                break;

            //会议控制
            case R.id.tv_conf_control:
                ConfControlListActivity.startActivity(this);
                break;

            case R.id.tv_template_manage:
                Toast.makeText(this, "此功能正在开发中", Toast.LENGTH_SHORT).show();
                break;
        }
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
