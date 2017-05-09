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
 * author：hw
 * data:2017/4/26 11:09
 * 历史会议列表的适配器
 */
public class HistoryGridAdapter extends RecyclerView.Adapter<HistoryGridAdapter.Holder> {
    private List<ConfBean> list;
    private Integer currenIndex;

    private int[] resIds = {
            R.mipmap.icon_conf_yellow,
            R.mipmap.icon_conf_blue,
            R.mipmap.icon_conf_green,
            R.mipmap.icon_conf_grey,
            R.mipmap.icon_conf_purple
    };


    public HistoryGridAdapter(List<ConfBean> list) {
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conf_template, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        ConfBean confBean = list.get(position);
//        holder.tvNumber.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.single_off, 0, 0, 0);
//        if (null != currenIndex) {
//            if (position == currenIndex) {
//                holder.tvNumber.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.single_on, 0, 0, 0);
//            } else {
//                holder.tvNumber.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.single_off, 0, 0, 0);
//            }
//        }
        holder.tvName.setText(confBean.name);//会议名称

        int i = position % resIds.length;
        holder.tvName.setCompoundDrawablesWithIntrinsicBounds(0, resIds[i], 0, 0);


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

    public void addAll(List<ConfBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setCurrenIndex(int currenIndex) {
        this.currenIndex = currenIndex;
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        LinearLayout llContent;
        TextView tvName;

        public Holder(View itemView) {
            super(itemView);
            llContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    private onItemClickListener itemClickListener;

    public interface onItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        itemClickListener = listener;
    }
}
