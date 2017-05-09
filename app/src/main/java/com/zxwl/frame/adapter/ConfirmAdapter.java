package com.zxwl.frame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhy.android.percent.support.PercentRelativeLayout;
import com.zxwl.frame.R;
import com.zxwl.frame.bean.Employee;

import java.util.List;

/**
 * Created by asus-pc on 2017/5/5.
 */

public class ConfirmAdapter extends BaseAdapter {

    private List<Employee> data;
    private Context context;
    private LayoutInflater inflater;

    public ConfirmAdapter(List<Employee> data, Context context) {
        this.data = data;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.choosedmeeting_item,parent,false);
            viewHolder.rl = (PercentRelativeLayout) convertView.findViewById(R.id.employee_rl);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvEmployeeName);
            viewHolder.tvNo = (TextView) convertView.findViewById(R.id.tvEmployeeNo);
            viewHolder.tvType = (TextView) convertView.findViewById(R.id.tvEmployeeType);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(data.get(position).getName());
        viewHolder.tvNo.setText(data.get(position).getTerminalId());
        viewHolder.tvType.setText(data.get(position).getTypeName());
        return convertView;
    }

    static class ViewHolder{
        TextView tvName;
        TextView tvNo;
        TextView tvType;
        PercentRelativeLayout rl;
    }

}
