package com.zxwl.frame.bean;

import java.util.List;

/**
 * Created by asus-pc on 2017/5/5.
 */

public class ConfirmEvent {

    public List<Employee> data;
    public int size;
    public ConfirmEvent(List<Employee> data,int size) {
        this.data = data;
        this.size = size;
    }
}
