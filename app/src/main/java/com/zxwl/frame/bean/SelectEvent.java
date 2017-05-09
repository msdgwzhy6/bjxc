package com.zxwl.frame.bean;

/**
 * Created by asus-pc on 2017/5/1.
 */

public class SelectEvent {

    public int size;
    public boolean select;
    public int position;
    public SelectEvent(int size,boolean select,int position) {
        this.size = size;
        this.select = select;
        this.position = position;
    }
    public int getSize() {
        return size;
    }
}
