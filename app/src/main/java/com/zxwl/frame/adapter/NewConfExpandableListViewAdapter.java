package com.zxwl.frame.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxwl.frame.R;
import com.zxwl.frame.bean.Employee;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by asus-pc on 2017/5/7.
 */

public class NewConfExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> orgNames;
    private Map<String, List<Employee>> mMaps;
    // 用于存放Indicator的集合
    private SparseArray<ImageView> mIndicators = new SparseArray<>();

    public NewConfExpandableListViewAdapter(Context context, List<String> orgNames, Map<String, List<Employee>> mMaps) {
        this.context = context;
        this.orgNames = orgNames;
        this.mMaps = mMaps;
    }

    public Iterator<List<Employee>> getMapV() {
        return mMaps.values().iterator();
    }

    //        获取分组的个数
    @Override
    public int getGroupCount() {
        return orgNames.size();
    }

    public void setIndicatorState(int groupPosition, boolean isExpanded) {
        if (isExpanded) {
            mIndicators.get(groupPosition).setImageResource(R.mipmap.ic_collapse);
        } else {
            mIndicators.get(groupPosition).setImageResource(R.mipmap.ic_expand);
        }
    }

    //        获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        String key = orgNames.get(groupPosition);
        int size = mMaps.get(key).size();
        return size;
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return orgNames.get(groupPosition);
    }

    //        获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String key = orgNames.get(groupPosition);
        return mMaps.get(key).get(childPosition);
    }

    //        获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //        获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //        分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们。
    @Override
    public boolean hasStableIds() {
        return true;
    }

    //        获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup
            parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.newconf_contact_group_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_group);
            groupViewHolder.ivIndicator = (ImageView) convertView.findViewById(R.id.iv_indicator);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        if (mMaps.get(orgNames.get(groupPosition)).size() == 0) {
            groupViewHolder.ivIndicator.setVisibility(View.GONE);
        } else {
            groupViewHolder.ivIndicator.setVisibility(View.VISIBLE);
        }
        //      把位置和图标添加到Map
        mIndicators.put(groupPosition, groupViewHolder.ivIndicator);
        //      根据分组状态设置Indicator
        setIndicatorState(groupPosition, isExpanded);
        groupViewHolder.tvTitle.setText(orgNames.get(groupPosition));
        return convertView;
    }

    //获取显示指定分组中的指定子选项的视图
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        final String key = orgNames.get(groupPosition);
        final Employee info = mMaps.get(key).get(childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.newconf_contact_child_item, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.rl = (RelativeLayout) convertView.findViewById(R.id.rl);
            childViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_child);
            childViewHolder.iv = (ImageView) convertView.findViewById(R.id.iv_delete);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.tvTitle.setText(info.getName());
        if (info.isClicked()) {
            childViewHolder.rl.setBackgroundColor(Color.GRAY);
        } else {
            childViewHolder.rl.setBackgroundColor(Color.WHITE);
        }
        childViewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!info.isClicked()) {
                    info.setClicked(true);
                    childViewHolder.rl.setBackgroundColor(Color.GRAY);
                    childViewHolder.iv.setVisibility(View.VISIBLE);
                } else {
                    info.setClicked(false);
                    childViewHolder.iv.setVisibility(View.GONE);
                    childViewHolder.rl.setBackgroundColor(Color.WHITE);
                }

            }
        });
        childViewHolder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMaps.get(orgNames.get(groupPosition)).remove(childPosition);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    //        指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class GroupViewHolder {
        TextView tvTitle;
        ImageView ivIndicator;
    }

    class ChildViewHolder {
        TextView tvTitle;
        RelativeLayout rl;
        ImageView iv;
    }

}
