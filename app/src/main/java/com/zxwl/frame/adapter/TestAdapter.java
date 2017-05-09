package com.zxwl.frame.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxwl.frame.R;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/25 11:59
 * ClassName: ${Class_Name}
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestHolder> {

    @Override
    public TestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false));
    }

    @Override
    public void onBindViewHolder(TestHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 20;
    }

    public static class TestHolder extends RecyclerView.ViewHolder{

        public TestHolder(View itemView) {
            super(itemView);
        }
    }

}
