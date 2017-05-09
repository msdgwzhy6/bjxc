package com.zxwl.frame.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 主席轮询信息类
 *
 * @author QYW
 * @date 2016-11-22 上午11:54:46
 */
public class ChairPollInfo {
    public List<String> uriList = new ArrayList<String>();
    public int index = 0;
    public String smcConfId;//SMC
    public int isLock;//是否锁定
    public String siteUri;//主席会场URI
    public Boolean pollFlag;//轮询标记 （true:轮询 false:不轮询）

//    private List<String> uriList = new ArrayList<String>();
//    private int index = 0;
//    private String smcConfId;//SMC
//    private int isLock;//是否锁定
//    private String siteUri;//主席会场URI
//    private Boolean pollFlag;//轮询标记 （true:轮询 false:不轮询）

//    private TurnChairScreen turnChairScreen = new TurnChairScreen(this);
//
//    public String getNextUri() {
//        if (uriList.size() <= 0) {
//            return null;
//        }
//        String curUri = uriList.get(this.index);
//
//        if (this.index < uriList.size() - 1) {
//            this.index += 1;
//        } else {
//            this.index = 0;
//        }
//        return curUri;
//    }
//
//    public void pollScreen(String smcConfId, String siteUri, String[] videoSourceUris, int isLock, int pollTime) {
//        this.smcConfId = smcConfId;
//        this.siteUri = siteUri;
//        this.isLock = isLock;
//        this.pollFlag = true;
//        pollTime = pollTime * 1000;
//        for (int i = 0; i < videoSourceUris.length; i++) {
//            this.uriList.add(videoSourceUris[i]);
//        }
//        this.turnChairScreen.setTimeInterval(pollTime);
//        this.turnChairScreen.start();
//    }
//
//
//    public void pauseTurn() {
//        if (turnChairScreen != null) {
//            turnChairScreen.stop();
//        }
//    }
//
//    public void startTurn() {
//        if (turnChairScreen != null) {
//            turnChairScreen.start();
//        }
//    }
//
//    public void stopTurn() {
//        if (turnChairScreen != null) {
//            turnChairScreen.unregisterTask();
//        }
//    }
//
//    public boolean isTurn() {
//        return (uriList.size() > 0);
//    }
//
//    public void addUri(String uri) {
//        this.uriList.add(uri);
//    }
//
//    public List<String> getUriList() {
//        return uriList;
//    }
//
//    public void setUriList(List<String> uriList) {
//        this.uriList = uriList;
//    }
//
//    public int getIndex() {
//        return index;
//    }
//
//    public void setIndex(int index) {
//        this.index = index;
//    }
//
//    public String getSmcConfId() {
//        return smcConfId;
//    }
//
//    public void setSmcConfId(String smcConfId) {
//        this.smcConfId = smcConfId;
//    }
//
//    public int getIsLock() {
//        return isLock;
//    }
//
//    public void setIsLock(int isLock) {
//        this.isLock = isLock;
//    }
//
//    public String getSiteUri() {
//        return siteUri;
//    }
//
//    public void setSiteUri(String siteUri) {
//        this.siteUri = siteUri;
//    }
//
//    public TurnChairScreen getTurnChairScreen() {
//        return turnChairScreen;
//    }
//
//    public void setTurnChairScreen(TurnChairScreen turnChairScreen) {
//        this.turnChairScreen = turnChairScreen;
//    }
//
//    public Boolean getPollFlag() {
//        return pollFlag;
//    }
//
//    public void setPollFlag(Boolean pollFlag) {
//        this.pollFlag = pollFlag;
//    }

}
