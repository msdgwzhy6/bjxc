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
 * 模板历史列表的适配器
 */
public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.Holder> {
    private List<ConfBean> list;
    private Integer currenIndex;

    public void setCurrenIndex(int currenIndex) {
        this.currenIndex = currenIndex;
        notifyDataSetChanged();
    }

    public HistoryListAdapter(List<ConfBean> list) {
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        ConfBean confBean = list.get(position);

        holder.tvNumber.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.single_off, 0, 0, 0);

        if (null != currenIndex) {
            if (position == currenIndex) {
                holder.tvNumber.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.single_on, 0, 0, 0);
            } else {
                holder.tvNumber.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.single_off, 0, 0, 0);
            }
        }

        holder.tvNumber.setText(String.valueOf(position));//序号
        holder.tvName.setText(confBean.name);//会议名称
        holder.tvProposer.setText(confBean.applyPeople);//申请人
        holder.tvDepartment.setText(confBean.applyDept);//主办部门
        holder.tvUnit.setText(confBean.unitIdName);//所在单位
        holder.tvConfParametersId.setText(confBean.confParameters);//高级参数ID

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

    public static class Holder extends RecyclerView.ViewHolder {
        PercentLinearLayout llContent;
        TextView tvNumber;
        TextView tvName;
        TextView tvProposer;
        TextView tvDepartment;
        TextView tvUnit;
        TextView tvConfParametersId;

        public Holder(View itemView) {
            super(itemView);
            llContent = (PercentLinearLayout) itemView.findViewById(R.id.ll_content);
            tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvProposer = (TextView) itemView.findViewById(R.id.tv_proposer);
            tvDepartment = (TextView) itemView.findViewById(R.id.tv_department);
            tvUnit = (TextView) itemView.findViewById(R.id.tv_unit);
            tvConfParametersId = (TextView) itemView.findViewById(R.id.tv_conf_parameters_id);
        }
    }

    public void addAll(List<ConfBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    private onItemClickListener itemClickListener;

    public interface  onItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        itemClickListener = listener;
    }
}
