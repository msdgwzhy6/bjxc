package com.zxwl.frame.bean;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/24 17:34
 * ClassName: ${Class_Name}
 */

public class UserInfo {
//    private Integer id;
//    private Integer state = 0;
//    private String name;
//    private String account;
//    private String password; // 明码密码，用于实体属性
//    private String password1; // 加密密码，用于数据库读写
//    private String telephone; // 联系电话
//    private String description;
//    private String position;
//    private String positioncn;
//    private Integer operatorType;//0视频管理员1普通人员
//    private Integer deptId;
//    private Integer EmployeeIdOa;
//    private String email;
//    private Integer operatorOrgNo;
//    private String empName;
//    private Integer unitId;
//    private Integer roleId;

    public String id; //主键
    public String state ; //状态:0:未删除，1已删除
    public String name;//操作员名称
    public String account;//登陆账号
    public String password; // 明码密码，用于实体属性
    public String password1; // 加密密码，用于数据库读写
    public String telephone; // 联系电话
    public String description;//描述
    public String position;//所属位置
    public String positioncn;//所属位置位置对应值
    public String operatorType;//0视频管理员1普通人员
    public String deptId;//单位id
    public String EmployeeIdOa;//关联的OA信息ID
    public String email;//邮箱
    public String operatorOrgNo;//工号
    public String empName;//关联OA信息的人员名称
    public String unitId;//单位或部门Id
    public String roleId;//角色id



    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", state=" + state +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", password1='" + password1 + '\'' +
                ", telephone='" + telephone + '\'' +
                ", description='" + description + '\'' +
                ", position='" + position + '\'' +
                ", positioncn='" + positioncn + '\'' +
                ", operatorType=" + operatorType +
                ", deptId=" + deptId +
                ", EmployeeIdOa=" + EmployeeIdOa +
                ", email='" + email + '\'' +
                ", operatorOrgNo=" + operatorOrgNo +
                ", empName='" + empName + '\'' +
                ", unitId=" + unitId +
                ", roleId=" + roleId +
                '}';
    }
}
