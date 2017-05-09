package com.zxwl.frame.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxwl.frame.R;

/**
 * author：hw
 * data:2017/4/26 11:09
 * 常用群组列表的适配器
 */
public class TemplateListAdapter extends RecyclerView.Adapter<TemplateListAdapter.Holder> {

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_template, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
