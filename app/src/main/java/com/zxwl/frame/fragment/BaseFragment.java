package com.zxwl.frame.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * author：hw
 * data:2017/4/14 16:58
 * Fragment的基类
 */
public abstract class BaseFragment extends RxFragment {
    protected Context mContext;

    private View mView;

    protected abstract View inflateContentView(LayoutInflater inflater,
                                               ViewGroup container);

    protected abstract void findViews(View view);

    protected abstract void init();

    protected abstract void addListeners();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        return inflateContentView(inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        findViews(view);
        init();
        addListeners();
    }
}
