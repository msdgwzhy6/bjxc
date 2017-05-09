package com.zxwl.frame.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxwl.frame.R;
import com.zxwl.frame.bean.ConfBean;

import java.util.List;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/5/3 16:12
 * 会议控制列表的适配器
 */
public class ConfControlAdapter extends RecyclerView.Adapter<ConfControlAdapter.ConfControlHolder> {

    private List<ConfBean> list;

    public ConfControlAdapter(List<ConfBean> list) {
        this.list = list;
    }


    @Override
    public ConfControlHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConfControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conf_control, parent, false));
    }

    @Override
    public void onBindViewHolder(final ConfControlHolder holder, final int position) {
        ConfBean confBean = list.get(position);
        //0=View.VISIBLE    4=View.INVISIBLE
        if (confBean.showControl) {
            holder.rlControl.setVisibility(View.VISIBLE);
            holder.rlInfo.setVisibility(View.INVISIBLE);
        } else {
            holder.rlControl.setVisibility(View.INVISIBLE);
            holder.rlInfo.setVisibility(View.VISIBLE);
        }

        holder.tvName.setText(confBean.name);//会议名称
        holder.tvTime.setText(confBean.beginTime);
        holder.tvPhone.setText(confBean.contactTelephone);
        holder.tvPattern.setText(confBean.confMode);
        holder.tvType.setText(confBean.confType);

        holder.rlContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    itemClickListener.onClick(position);
                }
            }
        });

        holder.tvControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    itemClickListener.onControl(position);
                }
            }
        });
        holder.tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    itemClickListener.onFinish(position);
                }
            }
        });
    }

    public ConfBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return null != list ? list.size() : 0;
    }


    public void reomve(int position){
        list.remove(position);
        notifyDataSetChanged();
    }

    public static class ConfControlHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlContent;
        TextView tvName;
        RelativeLayout rlInfo;
        TextView tvTime;
        TextView tvPhone;
        TextView tvPattern;
        TextView tvType;
        RelativeLayout rlControl;
        TextView tvControl;
        TextView tvFinish;

        public ConfControlHolder(View itemView) {
            super(itemView);

            rlContent = (RelativeLayout) itemView.findViewById(R.id.rl_content);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            rlInfo = (RelativeLayout) itemView.findViewById(R.id.rl_info);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            tvPattern = (TextView) itemView.findViewById(R.id.tv_pattern);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            rlControl = (RelativeLayout) itemView.findViewById(R.id.rl_control);
            tvControl = (TextView) itemView.findViewById(R.id.tv_control);
            tvFinish = (TextView) itemView.findViewById(R.id.tv_finish);
        }
    }

    private onItemClickListener itemClickListener;

    public void setOnItemClickListener(ConfControlAdapter.onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onClick(int position);

        void onControl(int position);

        void onFinish(int position);
    }

}
