package com.zxwl.frame.bean;

import java.io.Serializable;

/**
 * Created by asus-pc on 2017/4/27.
 */

public class Employee implements Serializable{
    /**
     * effectTime :
     * email :
     * failureTime :
     * fileUrl :
     * id : 3886
     * job : 123213
     * mobilePhone : 12312
     * name : qweqwe
     * orderId : 1
     * orgName : 中共鄂州市委
     * orgNo : 6526
     * password : 123456
     * seqId : 123123
     * sex :
     * tel : 12312
     * terminalId : 3566
     * type : 1
     * typeName : 话机
     * userName : qwerqwe
     */
    private String effectTime;
    private String email;
    private String failureTime;
    private String fileUrl;
    private String id;
    private String job;
    private String mobilePhone;
    private String name;
    private String orderId;
    private String orgName;
    private String orgNo;
    private String password;
    private String seqId;
    private String sex;
    private String tel;
    private String terminalId;
    private String type;
    private String typeName;
    private String userName;
    private boolean ischecked = false;
    private boolean isClicked = false;

    public String getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(String effectTime) {
        this.effectTime = effectTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFailureTime() {
        return failureTime;
    }

    public void setFailureTime(String failureTime) {
        this.failureTime = failureTime;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSeqId() {
        return seqId;
    }

    public void setSeqId(String seqId) {
        this.seqId = seqId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }


    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "effectTime='" + effectTime + '\'' +
                ", email='" + email + '\'' +
                ", failureTime='" + failureTime + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", id='" + id + '\'' +
                ", job='" + job + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", name='" + name + '\'' +
                ", orderId='" + orderId + '\'' +
                ", orgName='" + orgName + '\'' +
                ", orgNo='" + orgNo + '\'' +
                ", password='" + password + '\'' +
                ", seqId='" + seqId + '\'' +
                ", sex='" + sex + '\'' +
                ", tel='" + tel + '\'' +
                ", terminalId='" + terminalId + '\'' +
                ", type='" + type + '\'' +
                ", typeName='" + typeName + '\'' +
                ", userName='" + userName + '\'' +
                ", ischecked=" + ischecked +
                ", isClicked=" + isClicked +
                '}';
    }
}
