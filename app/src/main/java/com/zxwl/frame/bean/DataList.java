package com.zxwl.frame.bean;

import java.util.List;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/26 14:48
 * list数据
 */
public class DataList<T> {
    public String message;

    public String result;

    public String hql;
    public String pageNum;
    public String pageSize;
    public String pageSum;
    public String rowSum;
    public List<T> dataList;


}
