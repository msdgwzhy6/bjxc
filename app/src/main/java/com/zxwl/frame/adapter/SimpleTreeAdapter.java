package com.zxwl.frame.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zxwl.frame.R;
import com.zxwl.frame.bean.Department;
import com.zxwl.frame.bean.SelectEvent;
import com.zxwl.frame.views.treeListView.bean.Node;
import com.zxwl.frame.views.treeListView.bean.TreeListViewAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T> {

    private Context context;
    private List<T> datas;
    private List<T> selected;
    public HashMap<Integer, Boolean> map;
    private EventBus eventBus;

    public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas,
                             int defaultExpandLevel, EventBus eventBus) throws IllegalArgumentException,
            IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
        this.datas = datas;
        this.eventBus = eventBus;
        map = new HashMap<>();
        selected = new ArrayList<>();
        init();
    }

    private void init() {

        if (null == datas || datas.size() <= 0) {
            return;
        }
        for (int i = 0, p = datas.size(); i < p; i++) {
            map.put(i, false);
        }
    }

    @Override
    public View getConvertView(Node node, final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listmeeting_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView
                    .findViewById(R.id.id_treenode_icon);
            viewHolder.label = (TextView) convertView
                    .findViewById(R.id.id_treenode_label);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_check);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Department dep = (Department) datas.get(position);
        viewHolder.checkBox.setTag(new Integer(position));//防止划回来时选中消失
        if (node.getIcon() == -1) {
            //viewHolder.icon.setVisibility(View.INVISIBLE);
            //viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(R.mipmap.dot);
        } else {
            if (node.getId().equals("1")) {
                viewHolder.icon.setImageResource(R.mipmap.location);
            } else {
                viewHolder.icon.setImageResource(R.mipmap.rect);
            }
            //viewHolder.checkBox.setVisibility(View.GONE);

        }

        viewHolder.label.setText(node.getName());
        if (map != null) {
            viewHolder.checkBox.setChecked((map.get(position)));
        } else {
            viewHolder.checkBox.setChecked(false);
        }

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer mFlags = (Integer) view.getTag();
                if (map != null) {
                    if (map.get(position)) {
                        map.put(position, false);
                        eventBus.post(new SelectEvent(selected(map), false, position));
                    } else {
                        map.put(mFlags, Boolean.TRUE);
                        eventBus.post(new SelectEvent(selected(map), true, position));
                    }
                }
            }
        });

        return convertView;
    }

    private int selected(HashMap<Integer, Boolean> map) {
        int size = 0;
        for (Integer key : map.keySet()) {
            if (map.get(key)) {
                size++;
            }
        }
        return size;
    }

    public HashMap<Integer, Boolean> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, Boolean> map) {
        this.map = map;
        notifyDataSetChanged();
    }

    private final class ViewHolder {
        ImageView icon;
        TextView label;
        CheckBox checkBox;
    }


}
