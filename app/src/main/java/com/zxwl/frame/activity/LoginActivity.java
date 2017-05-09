package com.zxwl.frame.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.zxwl.frame.R;
import com.zxwl.frame.bean.UserInfo;
import com.zxwl.frame.constant.Account;
import com.zxwl.frame.net.api.UserInfoApi;
import com.zxwl.frame.net.callback.RxSubscriber;
import com.zxwl.frame.net.exception.ResponeThrowable;
import com.zxwl.frame.net.http.HttpUtils;
import com.zxwl.frame.utils.Toastor;
import com.zxwl.frame.utils.UserHelper;
import com.zxwl.frame.utils.sharedpreferences.PreferencesHelper;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText etName;
    private EditText etPwd;
    private TextView tvLogin;
    private TextView tvSaveInfo;
    private TextView tvForgetPwd;

    //是否保存用户信息
    private boolean saveInfo = false;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    protected void findViews() {
        etName = (EditText) findViewById(R.id.et_name);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        tvSaveInfo = (TextView) findViewById(R.id.tv_save_info);
        tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
        tvLogin.setOnClickListener(this);
        tvSaveInfo.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //登录
            case R.id.tv_login:
                String name = etName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    new Toastor(LoginActivity.this).getSingletonToast("用户名不能为空").show();
                    return;
                }

                String pwd = etPwd.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    new Toastor(LoginActivity.this).getSingletonToast("用户名不能为空").show();
                    return;
                }
                loginRequest(name, pwd);
                break;

            //忘记密码
            case R.id.tv_forget_pwd:
                Toast.makeText(this, "忘记密码", Toast.LENGTH_SHORT).show();
                break;

            //保存用户信息
            case R.id.tv_save_info:
                saveInfo = !saveInfo;
                tvSaveInfo.setCompoundDrawablesWithIntrinsicBounds(saveInfo ? R.mipmap.tv_login_on : R.mipmap.tv_login_off, 0, 0, 0);
                break;
        }
    }

    /**
     * 登录的网络请求
     *
     * @param name 用户名
     * @param pwd  密码
     */
    private void loginRequest(String name, String pwd) {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content("正在登陆")
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .show();

        HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(UserInfoApi.class)
                .login(name, pwd)
                .compose(this.<UserInfo>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo userInfo) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        //存储登录状态
                        PreferencesHelper.saveData(Account.IS_LOGIN, "true");
                        //保存用户信息
                        UserHelper.saveUser(userInfo);
                        //跳转到登录界面
                        HomeActivity.startActivity(LoginActivity.this);
                        finish();
                    }

                    @Override
                    protected void onError(ResponeThrowable responeThrowable) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });


    }


}
