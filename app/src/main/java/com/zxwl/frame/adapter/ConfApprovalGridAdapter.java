package com.zxwl.frame.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxwl.frame.R;
import com.zxwl.frame.bean.ConfBean;

import java.util.List;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/5/3 09:43
 * 会议审批列表的适配器--grid样式
 */
public class ConfApprovalGridAdapter extends RecyclerView.Adapter<ConfApprovalGridAdapter.ConfApprovalHolder> {
    private List<ConfBean> list;

    public ConfApprovalGridAdapter(List<ConfBean> list) {
        this.list = list;
    }

    @Override
    public ConfApprovalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConfApprovalHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conf_approval_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(ConfApprovalHolder holder, final int position) {
        ConfBean bean = getItem(position);

        holder.tvName.setText(bean.name);

        holder.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    itemClickListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
//        return 10;
        return null != list ? list.size() : 0;
    }

    public ConfBean getItem(int position) {
        return list.get(position);
    }

    public static class ConfApprovalHolder extends RecyclerView.ViewHolder {
        LinearLayout llContent;
        TextView tvName;

        public ConfApprovalHolder(View itemView) {
            super(itemView);
            llContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    private onItemClickListener itemClickListener;

    public void setOnItemClickListener(ConfApprovalGridAdapter.onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onClick(int position);
    }
}
