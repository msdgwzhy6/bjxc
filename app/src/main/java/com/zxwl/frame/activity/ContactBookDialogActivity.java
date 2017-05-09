package com.zxwl.frame.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zxwl.frame.R;
import com.zxwl.frame.adapter.ConfirmAdapter;
import com.zxwl.frame.adapter.EmployeeAdapter;
import com.zxwl.frame.adapter.SimpleTreeAdapter;
import com.zxwl.frame.bean.ClickEvent;
import com.zxwl.frame.bean.ConfirmEvent;
import com.zxwl.frame.bean.Department;
import com.zxwl.frame.bean.Employee;
import com.zxwl.frame.bean.SelectEvent;
import com.zxwl.frame.net.Urls;
import com.zxwl.frame.views.treeListView.bean.Node;
import com.zxwl.frame.views.treeListView.bean.TreeHelper;
import com.zxwl.frame.views.treeListView.bean.TreeListViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class ContactBookDialogActivity extends Activity implements View.OnClickListener {
    private List<Department> mDatas3 = new ArrayList<Department>();
    //private List<Department> mDatas4 = new ArrayList<Department>();
    private List<Employee> allEmployee = new ArrayList<>();
    // private List<Employee> itemDepartment = new ArrayList<>();
    private List<Employee> selectedDepartment = new ArrayList<>();
    private ListView mTree;
    private SimpleTreeAdapter<Department> mAdapter;
    private EmployeeAdapter employeeAdapter;
    private ListView lv_employee;
    private TextView tv_noData;
    private CheckBox cb_checkAll;
    private TextView tv_checkedNum;
    private int checkedNum = 0;
    private TextView tv_allNum;
    private int allNum;
    private Department department;
    private EventBus eventBus;
    private boolean isChange = false;
    private int size;
    private TextView tvNum;
    private Button btn_clear;
    private List<Employee> confirmEmployee = new ArrayList<>();
    private Button btn_confirm;
    private Button btn_cancel;
    private ImageView iv_close;
    private ConfirmAdapter confirmAdapter;
    private List<Employee> dialogListEmployee;
    private List<Node> nodes;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ContactBookDialogActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactbook_dialog);
        eventBus = EventBus.getDefault();
        eventBus.register(this);

        mTree = (ListView) findViewById(R.id.id_tree);
        lv_employee = (ListView) findViewById(R.id.lv_employee);
        tv_noData = (TextView) findViewById(R.id.tv_nodata);
        cb_checkAll = (CheckBox) findViewById(R.id.cb_checkAll);
        tv_checkedNum = (TextView) findViewById(R.id.tv_checkedNum);
        tv_allNum = (TextView) findViewById(R.id.tv_allNum);
        tvNum = (TextView) findViewById(R.id.tv_num);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        iv_close = (ImageView) findViewById(R.id.iv_close);

        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        iv_close.setOnClickListener(this);

        //initDatas1();
        initDatas();
        tvNum.setText("(" + 0 + ")");

        //全选按钮
        cb_checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //全选操作
                    isChange = false;
                    selectedDepartment.clear();
                    selectedDepartment.addAll(allEmployee);
                    setAdapter(selectedDepartment);
                    tvNum.setText("(" + allEmployee.size() + ")");
                    tv_noData.setVisibility(View.GONE);


                } else {
                    //取消全选操作
                    if (!isChange) {
                        selectedDepartment.clear();
                        employeeAdapter.notifyDataSetChanged();
                        tvNum.setText("(" + 0 + ")");
                        tv_noData.setVisibility(View.VISIBLE);
                    }
                }
                try {
                    HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();
                    int count = 0;
//                    if (isChecked) {
//                        isChange = false;
//                    }
                    for (int i = 0, p = mDatas3.size(); i < p; i++) {
                        if (isChecked) {
                            map.put(i, true);
                            count++;
                        } else {
                            if (!isChange) {
                                map.put(i, false);
                                count = 0;
                            } else {
                                map = mAdapter.getMap();
                                count = map.size();
                            }
                        }
                    }
                    setText(count, allNum);
                    mAdapter.setMap(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        //清空
//        btn_clear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectedDepartment.clear();
//                for (int i = 0, p = mDatas3.size(); i < p; i++) {
//                    mAdapter.getMap().put(i,false);
//                }
//                mAdapter.notifyDataSetChanged();
//                cb_checkAll.setChecked(false);
//                setText(0,allNum);
//                tvNum.setText("("+0+")");
//                tv_noData.setVisibility(View.VISIBLE);
//            }
//        });


    }

    private void initDatas1() {
        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(getAssets().open(
                    "2.json"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStreamReader.close();
            bufferedReader.close();
            JSONArray array = new JSONArray(stringBuilder.toString());

            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                Department department = new Department(item.getString("id"), item.getString("parentId"), item.getString("departmentName"));
                department.setId(item.getString("id"));
                department.setpId(item.getString("parentId"));
                department.setDepartmentName(item.getString("departmentName"));
                department.setIsParent(item.getString("isParent"));
                mDatas3.add(department);

            }
            nodes = TreeHelper.getSortedNodes(mDatas3, -1);
//            for (int i=0;i<nodes.size();i++){
//                if (nodes.get(i).getChildren().size()==0){
//                    //mDatas4.add(nodes.get(i));
//                    mDatas4.add(mDatas3.get(i));
//                }
//            }

            mAdapter = new SimpleTreeAdapter(mTree, ContactBookDialogActivity.this, mDatas3, 10, eventBus);
            mTree.setAdapter(mAdapter);
            allNum = mDatas3.size();
            setText(0, allNum);
            mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(Node node, int position) {

                    //itemDepartment.clear();
//                    String orgNo = mDatas3.get(position).getId();
//                    Log.i("TAG", "name===" + mDatas3.get(position).getDepartmentName() + "," + "orgNo====" + orgNo + "," + position);
//
//                    for (int i = 0; i < itemDepartment.size(); i++) {
//                        if (allEmployee.get(i).getOrgNo().equals(orgNo)) {
//                            itemDepartment.add(allEmployee.get(i));
//                        }
//                    }
//                    if (itemDepartment.size() == 0) {
//                        tv_noData.setVisibility(View.VISIBLE);
//                    } else {
//                        tv_noData.setVisibility(View.GONE);
//                    }
//                    employeeAdapter = new EmployeeAdapter(ContactBookDialogActivity.this, itemDepartment);
//                    lv_employee.setAdapter(employeeAdapter);
                    Toast.makeText(getApplicationContext(), node.getName(),
                            Toast.LENGTH_SHORT).show();

                }

            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDatas() {
        OkHttpUtils.get()
                .url(Urls.BASE_URL + Urls.QUERY_ALL_DEPARTMENT)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONArray array = new JSONArray(response.toString());

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject item = array.getJSONObject(i);
                                Department department = new Department(item.getString("id"), item.getString("parentId"), item.getString("departmentName"));
                                department.setId(item.getString("id"));
                                department.setpId(item.getString("parentId"));
                                department.setDepartmentName(item.getString("departmentName"));
                                department.setIsParent(item.getString("isParent"));
                                mDatas3.add(department);

                            }
                            mAdapter = new SimpleTreeAdapter(mTree, ContactBookDialogActivity.this, mDatas3, 10, eventBus);
                            mTree.setAdapter(mAdapter);
                            nodes = TreeHelper.getSortedNodes(mDatas3, -1);
                            allNum = mDatas3.size();
                            setText(0, allNum);
                            mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                                @Override
                                public void onClick(Node node, int position) {
//										mDatas5.clear();
//										String orgNo = mDatas3.get(position).getId();
//										Log.i("TAG","name==="+mDatas3.get(position).getDepartmentName()+","+"orgNo===="+orgNo+","+position);
//										for (int i=0;i<mDatas4.size();i++){
//											if (mDatas4.get(i).getOrgNo().equals(orgNo)){
//												mDatas5.add(mDatas4.get(i));
//											}
//										}
//										if (mDatas5.size()==0){
//											tv_noData.setVisibility(View.VISIBLE);
//										}else{
//											tv_noData.setVisibility(View.GONE);
//										}
//										employeeAdapter = new EmployeeAdapter(ContactBookDialogActivity.this,mDatas5);
//										lv_employee.setAdapter(employeeAdapter);
//                                    Toast.makeText(getApplicationContext(), node.getName(),
//                                            Toast.LENGTH_SHORT).show();
                                }

                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


        OkHttpUtils.get()
                .url(Urls.BASE_URL + "employeeAction_queryList1.action")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("dataList");
                            for (int i = 0; i < array.length(); i++) {
                                Employee employee = new Employee();
                                JSONObject object1 = array.getJSONObject(i);
                                employee.setId(object1.getString("id"));
                                employee.setUserName(object1.getString("userName"));
                                employee.setName(object1.getString("name"));
                                employee.setOrgNo(object1.getString("orgNo"));
                                employee.setTerminalId(object1.getString("terminalId"));
                                employee.setOrgName(object1.getString("orgName"));
                                employee.setTypeName(object1.getString("typeName"));
                                allEmployee.add(employee);
                            }
//                            employeeAdapter = new EmployeeAdapter(ContactBookDialogActivity.this, allEmployee);
//                            lv_employee.setAdapter(employeeAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

//        InputStreamReader inputStreamReader;
//
//
//        try {
//            inputStreamReader = new InputStreamReader(getAssets().open(
//                    "1.json"), "UTF-8");
//            BufferedReader bufferedReader = new BufferedReader(
//                    inputStreamReader);
//            String line;
//            StringBuilder stringBuilder = new StringBuilder();
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//            inputStreamReader.close();
//            bufferedReader.close();
//            JSONObject object = new JSONObject(stringBuilder.toString());
//            JSONArray array = object.getJSONArray("dataList");
//
//            for (int i = 0; i < array.length(); i++) {
//
//                JSONObject choosedmeeting_item = array.getJSONObject(i);
//                Employee employee = new Employee();
//                employee.setId(choosedmeeting_item.getString("id"));
//                employee.setUserName(choosedmeeting_item.getString("userName"));
//                employee.setName(choosedmeeting_item.getString("name"));
//                employee.setOrgNo(choosedmeeting_item.getString("orgNo"));
//                employee.setOrgName(choosedmeeting_item.getString("orgName"));
//                employee.setTerminalId(choosedmeeting_item.getString("terminalId"));
//                employee.setTypeName(choosedmeeting_item.getString("typeName"));
//                allEmployee.add(employee);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

    //接收条目中checkbox事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(SelectEvent event) {
        size = event.getSize();
        boolean select = event.select;
        int position = event.position;
//        Log.i("TAG", "size====：" + size);
        if (size < mDatas3.size()) {
            isChange = true;
            cb_checkAll.setChecked(false);
        } else {
            cb_checkAll.setChecked(true);
            isChange = false;
        }
        //点击
        if (select) {

            String orgNo = nodes.get(position).getId();
            List<Employee> tempCheck = new ArrayList<>();//临时存储
            tempCheck.clear();//每次点击先重置，用来确定该单位下有没有数据
            for (int i = 0; i < allEmployee.size(); i++) {
                if (allEmployee.get(i).getOrgNo().equals(orgNo)) {
                    selectedDepartment.add(allEmployee.get(i));
                    tempCheck.add(allEmployee.get(i));
                }
            }

            if (tempCheck.size() == 0) {
                Toast.makeText(this, "此单位下没有数据！", Toast.LENGTH_SHORT).show();
            }

            if (selectedDepartment.size() == 0) {
                tv_noData.setVisibility(View.VISIBLE);
            } else {
                tv_noData.setVisibility(View.GONE);
            }

            setAdapter(selectedDepartment);
//            employeeAdapter = new EmployeeAdapter(ContactBookDialogActivity.this, selectedDepartment, eventBus);
//            lv_employee.setAdapter(employeeAdapter);

        } else {//取消

            String orgNo = nodes.get(position).getId();
            List<Employee> tempUnCheck = new ArrayList<>();
            for (int i = 0; i < allEmployee.size(); i++) {
                if (allEmployee.get(i).getOrgNo().equals(orgNo)) {
                    tempUnCheck.add(allEmployee.get(i));
                }
            }
            if (tempUnCheck.size() == 0) {
                Toast.makeText(this, "此单位下没有数据！", Toast.LENGTH_SHORT).show();
            }
            selectedDepartment.removeAll(tempUnCheck);//删除该单位下所有数据
            tempUnCheck.clear();


            if (selectedDepartment.size() == 0) {
                tv_noData.setVisibility(View.VISIBLE);
            } else {
                tv_noData.setVisibility(View.GONE);
            }

        }
        setAdapter(selectedDepartment);
        setText(size, allNum);
        tvNum.setText("(" + selectedDepartment.size() + ")");

    }

    private void setAdapter(List<Employee> list) {
        employeeAdapter = new EmployeeAdapter(ContactBookDialogActivity.this, list, eventBus);
        lv_employee.setAdapter(employeeAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }

    //设置选中数量
    private void setText(int checkedNum, int allNum) {
        tv_checkedNum.setText(checkedNum + "");
        tv_allNum.setText(allNum + "");
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    //接收employeeAdapter中点击事件
    @Subscribe
    public void onClickEvent(ClickEvent event) {
        boolean isClicked = event.isClicked;
        int p = event.p;
        Employee employee = selectedDepartment.get(p);
        if (isClicked) {
            //选中
            if (!confirmEmployee.contains(employee)) {
                confirmEmployee.add(employee);
            }
        } else {
            //取消选中
            if (confirmEmployee.contains(employee)) {
                confirmEmployee.remove(employee);
            }

        }
        Log.i("TAG", confirmEmployee.toString());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel://取消
                finish();
                break;
            case R.id.btn_confirm: //确认
                MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title("参会列表")
                        .customView(initDialogContent(), false)
                        .positiveText("确认")
                        .negativeText(android.R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //Toast.makeText(ContactBookDialogActivity.this, "11", Toast.LENGTH_SHORT).show();
                                eventBus.post(new ConfirmEvent(confirmEmployee, allEmployee.size()));
                                finish();
                            }
                        })
                        .build();
                dialog.show();

                break;
            case R.id.btn_clear://清空
                selectedDepartment.clear();
                for (int i = 0, p = mDatas3.size(); i < p; i++) {
                    mAdapter.getMap().put(i, false);
                }
                mAdapter.notifyDataSetChanged();
                cb_checkAll.setChecked(false);
                setText(0, allNum);
                tvNum.setText("(" + 0 + ")");
                tv_noData.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_close:
                finish();
                break;
        }
    }

    private View initDialogContent() {
        dialogListEmployee = new ArrayList<>();
        dialogListEmployee.clear();
        dialogListEmployee.addAll(confirmEmployee);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.contactbook_dialog, null);
        SwipeMenuListView lv_confirm = (SwipeMenuListView) dialogView.findViewById(R.id.lv_confirm);
        confirmAdapter = new ConfirmAdapter(dialogListEmployee, this);
        lv_confirm.setAdapter(confirmAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.mipmap.delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        lv_confirm.setMenuCreator(creator);
        lv_confirm.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        if (dialogListEmployee.contains(dialogListEmployee.get(position))) {
                            dialogListEmployee.remove(position);
                            confirmAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });


        return dialogView;
    }
}
