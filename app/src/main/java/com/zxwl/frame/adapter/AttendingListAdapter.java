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
 * author：hw
 * data:2017/4/26 11:09
 * 参加会议列表的适配器
 */
public class AttendingListAdapter extends RecyclerView.Adapter<AttendingListAdapter.Holder> {
    private List<ConfBean> list;

    private boolean allSelect = false;

    public AttendingListAdapter(List<ConfBean> list) {
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_join, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        ConfBean bean = list.get(position);

        holder.tvNumber.setText("");
        if (bean.select) {
            holder.tvNumber.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_login_check_on, 0, 0, 0);
        } else {
            holder.tvNumber.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_login_check_off, 0, 0, 0);
        }

        holder.tvUnit.setText(bean.unitIdName);
        holder.tvDeviceNumber.setText(bean.deviceNumber);
        holder.tvDeviceName.setText(bean.deviceName);
        holder.tvDelete.setText("");
        holder.tvDelete.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.list_del, 0, 0, 0);

        holder.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    itemClickListener.onClick(position);
                }
            }
        });

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    itemClickListener.onDelete(position);
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

    public List<ConfBean> getList(){
        return list;
    }

    public void add(ConfBean confBean){
        list.add(confBean);
        notifyDataSetChanged();
    }

    public void addAll(List<ConfBean> list){
        list.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int position){
        list.remove(position);
        notifyDataSetChanged();
    }


    public static class Holder extends RecyclerView.ViewHolder {
        PercentLinearLayout llContent;
        TextView tvNumber;
        TextView tvUnit;
        TextView tvDeviceNumber;
        TextView tvDeviceName;
        TextView tvDelete;


        public Holder(View itemView) {
            super(itemView);
            llContent = (PercentLinearLayout) itemView.findViewById(R.id.ll_content);
            tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
            tvUnit = (TextView) itemView.findViewById(R.id.tv_unit);
            tvDeviceNumber = (TextView) itemView.findViewById(R.id.tv_device_number);
            tvDeviceName = (TextView) itemView.findViewById(R.id.tv_device_name);
            tvDelete = (TextView) itemView.findViewById(R.id.tv_delete);
        }
    }

    private onItemClickListener itemClickListener;

    public void setOnItemClickListener(AttendingListAdapter.onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onClick(int position);

        void onDelete(int position);
    }
}
