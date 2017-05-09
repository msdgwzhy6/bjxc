package com.zxwl.frame.bean;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/5/5 19:31
 * ClassName: ${Class_Name}
 */

public class Test {

    /**
     * ip :
     * lastState : -1
     * mcuName :
     * siteId : 3575
     * siteInfo : {"dialingMode":"0","dtmf":"","from":"0","isLockVideoSource":"","mcuId":"","name":"杨柳","participantType":"","rate":"1920k","status":"","type":"7","uri":"0724131","videoFormat":"","videoProtocol":""}
     * siteStatus : {"callFailedReason":{"discription":"","errCode":"110"},"isDataOnline":"","isLocalVideoOpen":"0","isMute":"1","isQuiet":"1","mcuId":"","name":"杨柳","participantType":"0","screens":"0","status":"3","type":"7","uri":"0724131","videoSource":"","volume":"65535"}
     * sound :
     * terminalName : 杨柳
     * unitId : 6525
     * unitName : 鄂州市级单位
     */

    public String ip;
    public String lastState;
    public String mcuName;
    public String siteId;
    public SiteInfoBean siteInfo;
    public SiteStatusBean siteStatus;
    public String sound;
    public String terminalName;
    public String unitId;
    public String unitName;

    public static class SiteInfoBean {
        /**
         * dialingMode : 0
         * dtmf :
         * from : 0
         * isLockVideoSource :
         * mcuId :
         * name : 杨柳
         * participantType :
         * rate : 1920k
         * status :
         * type : 7
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

    public static class SiteStatusBean {
        /**
         * callFailedReason : {"discription":"","errCode":"110"}
         * isDataOnline :
         * isLocalVideoOpen : 0
         * isMute : 1
         * isQuiet : 1
         * mcuId :
         * name : 杨柳
         * participantType : 0
         * screens : 0
         * status : 3
         * type : 7
         * uri : 0724131
         * videoSource :
         * volume : 65535
         */

        public CallFailedReasonBean callFailedReason;


        public static class CallFailedReasonBean {
            /**
             * discription :
             * errCode : 110
             */

            public String discription;
            public String errCode;
        }
    }

//    {
//        "conference": [
//        {
//            "accessCode": "02710134",
//                "auxVideoFormat": "",
//                "auxVideoProtocol": "",
//                "beginTime": "",
//                "billCode": "",
//                "chairmanPassword": "",
//                "chairmanUuid": "",
//                "commonUuid": "",
//                "confId": "2758",
//                "confMediaType": "",
//                "conferenceNotice": "",
//                "continuousPresenceMode": "",
//                "cpResouce": "",
//                "duration": "",
//                "isDataConference": "",
//                "isLiveBroadcast": "",
//                "isRecording": "",
//                "mainMcuId": "",
//                "mainSiteUri": "",
//                "maxSitesCount": "",
//                "mediaEncryptType": "",
//                "name": "",
//                "password": "",
//                "presentation": "",
//                "rate": "",
//                "recordParam": "",
//                "recorderAddr": "",
//                "reservedCsdPorts": "",
//                "sites": [
//            {
//                "dialingMode": "",
//                    "dtmf": "",
//                    "from": "",
//                    "isLockVideoSource": "",
//                    "mcuId": "",
//                    "name": "下属单位人员",
//                    "participantType": "",
//                    "rate": "",
//                    "status": "",
//                    "type": "",
//                    "uri": "13579",
//                    "videoFormat": "",
//                    "videoProtocol": ""
//            },
//            {
//                "dialingMode": "",
//                    "dtmf": "",
//                    "from": "",
//                    "isLockVideoSource": "",
//                    "mcuId": "",
//                    "name": "随州市委视频会议室",
//                    "participantType": "",
//                    "rate": "",
//                    "status": "",
//                    "type": "",
//                    "uri": "72201106",
//                    "videoFormat": "",
//                    "videoProtocol": ""
//            },
//            {
//                "dialingMode": "",
//                    "dtmf": "",
//                    "from": "",
//                    "isLockVideoSource": "",
//                    "mcuId": "",
//                    "name": "李",
//                    "participantType": "",
//                    "rate": "",
//                    "status": "",
//                    "type": "",
//                    "uri": "12322",
//                    "videoFormat": "",
//                    "videoProtocol": ""
//            },
//            {
//                "dialingMode": "",
//                    "dtmf": "",
//                    "from": "",
//                    "isLockVideoSource": "",
//                    "mcuId": "",
//                    "name": "夏帆",
//                    "participantType": "",
//                    "rate": "",
//                    "status": "",
//                    "type": "",
//                    "uri": "71131001",
//                    "videoFormat": "",
//                    "videoProtocol": ""
//            }
//            ],
//            "status": ""
//        },
//        {
//            "beginTime": "2017-05-09 16-21-07",
//                "broadcast": "",
//                "chair": "",
//                "endTime": "2017-05-09 17-15-07",
//                "id": "2758",
//                "isAudioSwitch": "0",
//                "isLock": "0",
//                "isLockPresentation": "",
//                "lockPresentation": "",
//                "name": "2222",
//                "presentation": "",
//                "speaking": "",
//                "status": "3",
//                "switchGate": "100"
//        },
//        [
//        {
//            "ip": "",
//                "lastState": "-1",
//                "mcuName": "",
//                "siteId": "3573",
//                "siteInfo": {
//            "dialingMode": "0",
//                    "dtmf": "",
//                    "from": "0",
//                    "isLockVideoSource": "",
//                    "mcuId": "",
//                    "name": "下属单位人员",
//                    "participantType": "",
//                    "rate": "1920k",
//                    "status": "",
//                    "type": "7",
//                    "uri": "13579",
//                    "videoFormat": "",
//                    "videoProtocol": ""
//        },
//            "siteStatus": {
//            "callFailedReason": {
//                "discription": "",
//                        "errCode": "110"
//            },
//            "isDataOnline": "",
//                    "isLocalVideoOpen": "0",
//                    "isMute": "1",
//                    "isQuiet": "1",
//                    "mcuId": "",
//                    "name": "下属单位人员",
//                    "participantType": "0",
//                    "screens": "0",
//                    "status": "3",
//                    "type": "7",
//                    "uri": "13579",
//                    "videoSource": "",
//                    "volume": "65535"
//        },
//            "sound": "",
//                "terminalName": "下属单位人员",
//                "unitId": "6526",
//                "unitName": "中共鄂州市委"
//        },
//        {
//            "ip": "",
//                "lastState": "-1",
//                "mcuName": "",
//                "siteId": "3476",
//                "siteInfo": {
//            "dialingMode": "0",
//                    "dtmf": "",
//                    "from": "0",
//                    "isLockVideoSource": "",
//                    "mcuId": "",
//                    "name": "随州市委视频会议室",
//                    "participantType": "",
//                    "rate": "1920k",
//                    "status": "",
//                    "type": "4",
//                    "uri": "72201106",
//                    "videoFormat": "",
//                    "videoProtocol": ""
//        },
//            "siteStatus": {
//            "callFailedReason": {
//                "discription": "",
//                        "errCode": "109"
//            },
//            "isDataOnline": "",
//                    "isLocalVideoOpen": "0",
//                    "isMute": "1",
//                    "isQuiet": "1",
//                    "mcuId": "",
//                    "name": "随州市委视频会议室",
//                    "participantType": "0",
//                    "screens": "0",
//                    "status": "3",
//                    "type": "4",
//                    "uri": "72201106",
//                    "videoSource": "",
//                    "volume": "65535"
//        },
//            "sound": "",
//                "terminalName": "随州市委视频会议室",
//                "unitId": "6783",
//                "unitName": "随州"
//        },
//        {
//            "ip": "",
//                "lastState": "-1",
//                "mcuName": "",
//                "siteId": "3578",
//                "siteInfo": {
//            "dialingMode": "0",
//                    "dtmf": "",
//                    "from": "0",
//                    "isLockVideoSource": "",
//                    "mcuId": "",
//                    "name": "李",
//                    "participantType": "",
//                    "rate": "1920k",
//                    "status": "",
//                    "type": "4",
//                    "uri": "12322",
//                    "videoFormat": "",
//                    "videoProtocol": ""
//        },
//            "siteStatus": {
//            "callFailedReason": {
//                "discription": "",
//                        "errCode": "109"
//            },
//            "isDataOnline": "",
//                    "isLocalVideoOpen": "0",
//                    "isMute": "1",
//                    "isQuiet": "1",
//                    "mcuId": "",
//                    "name": "李",
//                    "participantType": "0",
//                    "screens": "0",
//                    "status": "3",
//                    "type": "4",
//                    "uri": "12322",
//                    "videoSource": "",
//                    "volume": "65535"
//        },
//            "sound": "",
//                "terminalName": "李",
//                "unitId": "6530",
//                "unitName": "中共鄂州市委办公室"
//        },
//        {
//            "ip": "",
//                "lastState": "-1",
//                "mcuName": "",
//                "siteId": "3471",
//                "siteInfo": {
//            "dialingMode": "0",
//                    "dtmf": "",
//                    "from": "0",
//                    "isLockVideoSource": "",
//                    "mcuId": "",
//                    "name": "夏帆",
//                    "participantType": "",
//                    "rate": "1920k",
//                    "status": "",
//                    "type": "7",
//                    "uri": "71131001",
//                    "videoFormat": "",
//                    "videoProtocol": ""
//        },
//            "siteStatus": {
//            "callFailedReason": {
//                "discription": "",
//                        "errCode": "110"
//            },
//            "isDataOnline": "",
//                    "isLocalVideoOpen": "0",
//                    "isMute": "1",
//                    "isQuiet": "1",
//                    "mcuId": "",
//                    "name": "夏帆",
//                    "participantType": "0",
//                    "screens": "0",
//                    "status": "3",
//                    "type": "7",
//                    "uri": "71131001",
//                    "videoSource": "",
//                    "volume": "65535"
//        },
//            "sound": "",
//                "terminalName": "夏帆",
//                "unitId": "6528",
//                "unitName": "梁子湖"
//        }
//        ],
//        4
//    ]
//    }
}
