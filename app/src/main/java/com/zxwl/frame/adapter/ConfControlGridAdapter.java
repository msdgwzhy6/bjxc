package com.zxwl.frame.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxwl.frame.R;
import com.zxwl.frame.bean.Site;

import java.util.List;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/5/3 09:43
 * 会议控制列表的适配器--grid样式
 */
public class ConfControlGridAdapter extends RecyclerView.Adapter<ConfControlGridAdapter.ConfApprovalHolder> {
    private List<Site> list;
    private Context context;

    public ConfControlGridAdapter(List<Site> list) {
        this.list = list;
    }

    @Override
    public ConfApprovalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConfApprovalHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conf_control_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(ConfApprovalHolder holder, final int position) {
        Site site = list.get(position);

        //会场名称
        holder.tvName.setText(site.siteInfo.name);

        //0=View.VISIBLE    4=View.INVISIBLE
        if (site.showControl) {
            holder.tvRemove.setVisibility(View.VISIBLE);
            holder.ivImg.setVisibility(View.INVISIBLE);
            holder.tvName.setBackgroundColor(0XFF56D6C7);
            holder.llContent.setBackgroundResource(R.drawable.bg_item_conf_grid);
        } else {
            holder.tvRemove.setVisibility(View.INVISIBLE);
            holder.ivImg.setVisibility(View.VISIBLE);
            holder.tvName.setBackgroundColor(0XFFFAB951);
            holder.llContent.setBackgroundResource(0);
        }

        holder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    itemClickListener.onDelete(position);
                }
            }
        });

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

    public Site getItem(int position) {
        return list.get(position);
    }

    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    public void addAll(List<Site> newList){
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    public static class ConfApprovalHolder extends RecyclerView.ViewHolder {
        LinearLayout llContent;
        FrameLayout flContent;
        TextView tvName;
        ImageView ivImg;
        TextView tvRemove;

        public ConfApprovalHolder(View itemView) {
            super(itemView);
            llContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
            flContent = (FrameLayout) itemView.findViewById(R.id.fl_content);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ivImg = (ImageView) itemView.findViewById(R.id.iv_img);
            tvRemove = (TextView) itemView.findViewById(R.id.tv_remove);
        }
    }


    private onItemClickListener itemClickListener;

    public void setOnItemClickListener(ConfControlGridAdapter.onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onClick(int position);

        void onDelete(int position);
    }
}
