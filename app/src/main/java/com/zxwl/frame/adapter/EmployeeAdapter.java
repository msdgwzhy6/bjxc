package com.zxwl.frame.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhy.android.percent.support.PercentRelativeLayout;
import com.zxwl.frame.R;
import com.zxwl.frame.bean.ClickEvent;
import com.zxwl.frame.bean.Employee;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by asus-pc on 2017/4/27.
 */

public class EmployeeAdapter extends BaseAdapter {
    private List<Employee> data;
    private Context context;
    private LayoutInflater inflater;
    private EventBus eventBus;
    public EmployeeAdapter(Context context, List<Employee> data, EventBus eventBus){
        this.context = context;
        this.data = data;
        this.eventBus = eventBus;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
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
        final int id = Integer.valueOf(data.get(position).getId());
        viewHolder.tvNo.setTag(id);
        //Log.i("TAG","id=========="+data.get(position).getName()+","+"choose==="+data.get(position).ischecked());
        final ViewHolder finalViewHolder = viewHolder;

        viewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (finalViewHolder.tvNo.getTag()!=null&& finalViewHolder.tvNo.getTag().equals(id)){
                        if (!data.get(position).ischecked()){
                            view.setBackgroundResource(R.drawable.item_bg);
                            data.get(position).setIschecked(true);
                            eventBus.post(new ClickEvent(true,position));
                        }else{
                            view.setBackgroundResource(R.drawable.item_bg1);
                            data.get(position).setIschecked(false);
                            eventBus.post(new ClickEvent(false,position));
                        }

                    }

                }


        });
        if (data.get(position).ischecked()){
            viewHolder.rl.setBackgroundResource(R.drawable.item_bg);
        }else{
            viewHolder.rl.setBackgroundResource(R.drawable.item_bg1);
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

