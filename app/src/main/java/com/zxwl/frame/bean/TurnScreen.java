package com.zxwl.frame.bean;

public class TurnScreen

//        extends TimerTaskBase
{
    public SplitScreenInfo splitScreenInfo;
    public int errorCount = 0;
    public String target = null;

//    public TurnScreen(SplitScreenInfo splitScreeInfo) {
//        this.timeInterval = 10000;
//        this.splitScreenInfo = splitScreeInfo;
//    }

//    @Override
//    public void run() {
//        try {
//            if (splitScreenInfo.getSplitFlag() != null) {
//                if (splitScreenInfo.getSplitFlag()) {
//                    List<String> subScreenList = new ArrayList<String>(splitScreenInfo.getSubScreenSize());
//                    for (int i = 0; i < splitScreenInfo.getSubScreenSize(); i++) {
//                        subScreenList.add(splitScreenInfo.getScreen(i).getNextUri());
//                    }
//                    String smcConfId = splitScreenInfo.getSmcConfId();
//                    IConferenceManager conferenceManager = (IConferenceManager) SpringContextUtil.getBean("managerService");
//                    Integer resultCode = conferenceManager.splitScreen(smcConfId, "0", splitScreenInfo.getPresenceMode(), subScreenList);
//                    if (resultCode != 0) {
//                        if (errorCount >= 2 && errorCount <= 20)//连续2次失败，第3次写错误日志
//                        {
//                            String inParams = "smcConfId:" + smcConfId + " target:" + target + " presenceMode:" + splitScreenInfo.getPresenceMode();
//                            ITpLogService itpLogService = TpLogService.getInstance();
//                            itpLogService.addTpLog(1, "admin", "setContinuousPresenceEx", resultCode, inParams);
//                            //this.unregisterTask();
//                        }
//                        errorCount++;
//                    } else {
//                        errorCount = 0;
//                    }
//                }
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public String getTarget() {
//        return target;
//    }
//
//    public void setTarget(String target) {
//        this.target = target;
//    }
}
