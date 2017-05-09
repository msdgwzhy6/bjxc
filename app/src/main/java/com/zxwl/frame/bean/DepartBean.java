package com.zxwl.frame.bean;

/**
 * Created by asus-pc on 2017/5/8.
 */
public class DepartBean {
    public String deptId;
    public String deptName;
    public String employeeId;
    public String employeeName;
    public String id;
    public String numCount;
    public String phoneId;
    public String siteName;
    public String unitId;
    public String unitName;

    public String getDeptId() {
        return deptId;
    }
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumCount() {
        return numCount;
    }

    public void setNumCount(String numCount) {
        this.numCount = numCount;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public String toString() {
        return "DepartBean{" +
                "deptId='" + deptId + '\'' +
                ", deptName='" + deptName + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", id='" + id + '\'' +
                ", numCount='" + numCount + '\'' +
                ", phoneId='" + phoneId + '\'' +
                ", siteName='" + siteName + '\'' +
                ", unitId='" + unitId + '\'' +
                ", unitName='" + unitName + '\'' +
                '}';
    }
}
