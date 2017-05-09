package com.zxwl.frame.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxwl.frame.R;
import com.zxwl.frame.activity.ConfControlActivity;

/**
 * 分屏
 */
public class SplitScreenFragment extends BaseFragment {

    public SplitScreenFragment() {
        // Required empty public constructor
    }

    public static SplitScreenFragment newInstance(String smcConfId){
        SplitScreenFragment fragment = new SplitScreenFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConfControlActivity.SMC_CONF_ID, smcConfId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View inflateContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_split_screen, container, false);
    }

    @Override
    protected void findViews(View view) {

    }

    @Override
    protected void init() {

    }

    @Override
    protected void addListeners() {

    }

}
