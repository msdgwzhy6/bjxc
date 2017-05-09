package com.zxwl.frame.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.android.percent.support.PercentLinearLayout;
import com.zxwl.frame.R;
import com.zxwl.frame.bean.ConfBean;

import java.util.List;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/5/3 09:43
 * 会议审批列表的适配器--grid样式
 */
public class ConfApprovalListAdapter extends RecyclerView.Adapter<ConfApprovalListAdapter.ConfApprovalHolder> {
    private List<ConfBean> list;

    public ConfApprovalListAdapter(List<ConfBean> list) {
        this.list = list;
    }

    @Override
    public ConfApprovalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConfApprovalHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conf_approval_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ConfApprovalHolder holder, final int position) {
        ConfBean bean = getItem(position);

        holder.tvNumber.setText(String.valueOf((position + 1)));
        holder.tvName.setText(bean.name);
        holder.tvProposer.setText(bean.applyPeople);
        holder.tvUnit.setText(bean.applyDept);
        holder.tvStartTime.setText(bean.beginTime);
        holder.tvEndTime.setText(bean.endTime);

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
        return null != list ? list.size() : 0;
    }

    public ConfBean getItem(int position) {
        return list.get(position);
    }

    public static class ConfApprovalHolder extends RecyclerView.ViewHolder {
        PercentLinearLayout llContent;
        TextView tvNumber;
        TextView tvName;
        TextView tvProposer;
        TextView tvUnit;
        TextView tvStartTime;
        TextView tvEndTime;
        TextView tvStatus;

        public ConfApprovalHolder(View itemView) {
            super(itemView);
            llContent = (PercentLinearLayout) itemView.findViewById(R.id.ll_content);
            tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvProposer = (TextView) itemView.findViewById(R.id.tv_proposer);
            tvUnit = (TextView) itemView.findViewById(R.id.tv_unit);
            tvStartTime = (TextView) itemView.findViewById(R.id.tv_start_time);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
        }
    }

    private onItemClickListener itemClickListener;

    public void setOnItemClickListener(ConfApprovalListAdapter.onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onClick(int position);
    }

}
