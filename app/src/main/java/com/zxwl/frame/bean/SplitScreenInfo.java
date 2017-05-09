package com.zxwl.frame.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * create by sh
 * 多画面分屏信息类
 */
public class SplitScreenInfo {
    public int presenceMode;// 画面布局
    public List<SubScreenInfo> screenList = new ArrayList<SubScreenInfo>();
    public int splitScreenTime;//分屏时间间隔
    public String smcConfId;//smc会议ID
//    public TurnScreen turnScreen = new TurnScreen(this);
    public Boolean splitFlag;//分屏轮询标记 （true:轮询 false: 不轮询）


//    private int presenceMode;// 画面布局
//    private List<SubScreenInfo> screenList = new ArrayList<SubScreenInfo>();
//    private int splitScreenTime;//分屏时间间隔
//    private String smcConfId;//smc会议ID
//    private TurnScreen turnScreen = new TurnScreen(this);
//    private Boolean splitFlag;//分屏轮询标记 （true:轮询 false: 不轮询）
//
//    public SubScreenInfo getScreen(int index) {
//        return screenList.get(index);
//    }
//
//    public int getSubScreenSize() {
//        return screenList.size();
//    }
//
//    public boolean IsTurn() {
//        for (SubScreenInfo subInfo : screenList) {
//            if (subInfo.isTurn()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public int getPresenceMode() {
//        return presenceMode;
//    }
//
//    public void setPresenceMode(int presenceMode) {
//        this.presenceMode = presenceMode;
//    }
//
//    public List<SubScreenInfo> getScreenList() {
//        return screenList;
//    }
//
//    public void setScreenList(List<SubScreenInfo> screenList) {
//        this.screenList = screenList;
//    }
//
//    public int getSplitScreenTime() {
//        return splitScreenTime;
//    }
//
//    public void setSplitScreenTime(int splitScreenTime) {
//        this.splitScreenTime = splitScreenTime;
//    }
//
//    public String getSmcConfId() {
//        return this.smcConfId;
//    }
//
//    public void setSmcConfId(String smcConfId) {
//        this.smcConfId = smcConfId;
//    }
//
//    public void splitScreen(String smcConfId, String target, Integer presenceMode, String[] subPics, Integer splitScreenTime) {
//        this.smcConfId = smcConfId;
//        this.presenceMode = presenceMode;
//        this.splitScreenTime = splitScreenTime * 1000;
//        this.splitFlag = true;
//        for (int i = 0; i < subPics.length; i++) {
//            SubScreenInfo subScreenInfo = new SubScreenInfo();
//            if (!subPics[i].contains(",")) {
//                subScreenInfo.addUri(subPics[i]);
//            } else {
//                String subPicsStr[] = subPics[i].split(",");
//                for (int j = 0; j < subPicsStr.length; j++) {
//                    subScreenInfo.addUri(subPicsStr[j]);
//                }
//            }
//            this.screenList.add(subScreenInfo);
//        }
//        turnScreen.setTarget(target);
//        turnScreen.setTimeInterval(this.splitScreenTime);
//        turnScreen.start();
//    }
//
//    public void pauseTurn() {
//        if (turnScreen != null) {
//            turnScreen.stop();
//        }
//    }
//
//    public void startTurn() {
//        if (turnScreen != null) {
//            turnScreen.start();
//        }
//    }
//
//    public void stopTurn() {
//        if (turnScreen != null) {
//            turnScreen.unregisterTask();
//        }
//    }
//
//    public TurnScreen getTurnScreen() {
//        return turnScreen;
//    }
//
//    public void setTurnScreen(TurnScreen turnScreen) {
//        this.turnScreen = turnScreen;
//    }
//
//    public Boolean getSplitFlag() {
//        return splitFlag;
//    }
//
//    public void setSplitFlag(Boolean splitFlag) {
//        this.splitFlag = splitFlag;
//    }


}
