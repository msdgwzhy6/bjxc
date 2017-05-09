package com.zxwl.frame.activity;

import android.content.Context;
import android.content.Intent;

import com.jaeger.library.StatusBarUtil;
import com.zxwl.frame.R;

public class SetImageActivity extends BaseActivity {

    public static void startActivity(Context context){
        context.startActivity(new Intent(context,SetImageActivity.class));
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void initData() {
        StatusBarUtil.setTranslucentForImageView(SetImageActivity.this, 0, null);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_image;
    }

}
