package com.zxwl.frame.bean;

/**
 * 主席轮询 定时器
 *
 * @author QYW
 * @date 2016-11-22 上午11:55:42
 */
public class TurnChairScreen
//        extends TimerTaskBase
{
    public static ChairPollInfo chairPollInfo;
    public int errorCount = 0;

//    public TurnChairScreen(ChairPollInfo chairPollInfo) {
//        this.timeInterval = 10000;
//        this.chairPollInfo = chairPollInfo;
//    }
//
//    @Override
//    public void run() {
//        try {
//            if (chairPollInfo != null) {
//                if (chairPollInfo.getPollFlag() != null) {
//                    if (chairPollInfo.getPollFlag()) {
//                        String smcConfId = chairPollInfo.getSmcConfId();
//                        String siteUri = chairPollInfo.getSiteUri();
//                        String videoSourceUri = chairPollInfo.getNextUri();
//                        int isLock = 0;
//                        IConferenceManager conferenceManager = (IConferenceManager) SpringContextUtil.getBean("managerService");
//                        Integer resultCode = conferenceManager.setVideoSource(smcConfId, siteUri, videoSourceUri, isLock);
//                        if (resultCode != 0) {
//
//                            if (errorCount >= 2 && errorCount <= 20)//连续2次失败，第3次写错误日志
//                            {
//                                String inParams = "smcConfId:" + smcConfId + " siteUri:" + siteUri + " videoSourceUri:" + videoSourceUri + " isLock:" + isLock;
//                                ITpLogService tpLogService = (ITpLogService) SpringContextUtil.getBean("tpLogService");
//                                tpLogService.addTpLog(1, "admin", "setVideoSourceEx", resultCode, inParams);
//                            }
//                            errorCount++;
//                        } else {
//                            errorCount = 0;
//                        }
//                    }
//
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
