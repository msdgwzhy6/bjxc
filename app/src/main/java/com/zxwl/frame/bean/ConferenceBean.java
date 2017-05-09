package com.zxwl.frame.bean;

import java.util.List;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/5/8 10:35
 * ClassName: ${Class_Name}
 */

public class ConferenceBean {
    public String accessCode;
    public String auxVideoFormat;
    public String auxVideoProtocol;
    public String beginTime;
    public String billCode;
    public String chairmanPassword;
    public String chairmanUuid;
    public String commonUuid;
    public String confId;
    public String confMediaType;
    public String conferenceNotice;
    public String continuousPresenceMode;
    public String cpResouce;
    public String duration;
    public String isDataConference;
    public String isLiveBroadcast;
    public String isRecording;
    public String mainMcuId;
    public String mainSiteUri;
    public String maxSitesCount;
    public String mediaEncryptType;
    public String name;
    public String password;
    public String presentation;
    public String rate;
    public String recordParam;
    public String recorderAddr;
    public String reservedCsdPorts;
    public String status;
    public String broadcast;
    public String chair;
    public String endTime;
    public String id;
    public String isAudioSwitch;
    public String isLock;
    public String isLockPresentation;
    public String lockPresentation;
    public String speaking;
    public String switchGate;
    public List<SitesBean> sites;

    public static class SitesBean {
        /**
         * dialingMode :
         * dtmf :
         * from :
         * isLockVideoSource :
         * mcuId :
         * name : 杨柳
         * participantType :
         * rate :
         * status :
         * type :
         * uri : 0724131
         * videoFormat :
         * videoProtocol :
         */

        public String dialingMode;
        public String dtmf;
        public String from;
        public String isLockVideoSource;
        public String mcuId;
        public String name;
        public String participantType;
        public String rate;
        public String status;
        public String type;
        public String uri;
        public String videoFormat;
        public String videoProtocol;
    }
}
