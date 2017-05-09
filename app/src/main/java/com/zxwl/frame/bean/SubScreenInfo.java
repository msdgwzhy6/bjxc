package com.zxwl.frame.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * create  by  sh
 * 多画面子屏类
 */
public class SubScreenInfo {
    private List<String> uriList = new ArrayList<String>();
    private int index = 0;

    public String getNextUri() {
        if (uriList.size() <= 0) {
            return null;
        }
        String curUri = uriList.get(this.index);

        if (this.index < uriList.size() - 1) {
            this.index += 1;
        } else {
            this.index = 0;
        }
        return curUri;
    }

    public boolean isTurn() {
        return (uriList.size() > 0);
    }

    public void addUri(String uri) {
        this.uriList.add(uri);
    }
}
