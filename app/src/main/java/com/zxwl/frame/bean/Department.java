package com.zxwl.frame.bean;


import com.zxwl.frame.views.treeListView.bean.TreeNodeId;
import com.zxwl.frame.views.treeListView.bean.TreeNodeLabel;
import com.zxwl.frame.views.treeListView.bean.TreeNodePid;

/**
 * 参会人员的bean
 */
public class Department {
    @TreeNodeId
    private String id;

    @TreeNodePid
    private String pId;

    @TreeNodeLabel
    private String departmentName;

    private String isParent;

    public Department(){

    }

    public Department(String id, String parentId,String departmentName) {
        super();
        this.id = id;
        this.pId = parentId;
        this.departmentName = departmentName;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getpId() {
        return pId;
    }


    public void setpId(String pId) {
        this.pId = pId;
    }


    public String getDepartmentName() {
        return departmentName;
    }


    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }


    @Override
    public String toString() {
        return "Department{" +
                "id='" + id + '\'' +
                ", pId='" + pId + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", isParent='" + isParent + '\'' +
                '}';
    }

//    public String deptId;
//    public String deptName;
//    public String employeeId;
//    public String employeeName;
//    public String id;
//    public String numCount;
//    public String phoneId;
//    public String siteName;
//    public String unitId;
//    public String unitName;
//
//    public Department(String deptId, String deptName, String employeeId, String employeeName, String id, String numCount, String phoneId, String siteName, String unitId, String unitName) {
//        this.deptId = deptId;
//        this.deptName = deptName;
//        this.employeeId = employeeId;
//        this.employeeName = employeeName;
//        this.id = id;
//        this.numCount = numCount;
//        this.phoneId = phoneId;
//        this.siteName = siteName;
//        this.unitId = unitId;
//        this.unitName = unitName;
//    }
    /**
     * areaType : 1
     * createOperatorId :
     * createTime : 2014-06-06
     * departmentName : 湖北省委
     * deptItems : 10000012;10000016;10000017;
     * id : 1
     * isParent : 1
     * orderId : 0
     * parentId : 0
     * propertyEmail :
     * propertyId :
     * propertyName :
     * pwd : vtM/Gdsnxg1t52vR8zwWQQ==
     * smcAcount : zongbuEsdk
     * smcPwd : Change_Me
     * state : 0
     * treeParentId : 2673
     */

////    @TreeNodeId
//    private String id;
//
////    @TreeNodePid
//    private String pId;
//
////    @TreeNodeLabel
//    private String departmentName;
//
//    public Department() {}
//
//    public Department(String id, String parentId, String departmentName) {
//        super();
//        this.id = id;
//        this.pId = parentId;
//        this.departmentName = departmentName;
//    }
//
//
//    public String getId() {
//        return id;
//    }
//
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//
//    public String getpId() {
//        return pId;
//    }
//
//
//    public void setpId(String pId) {
//        this.pId = pId;
//    }
//
//
//    public String getDepartmentName() {
//        return departmentName;
//    }
//
//
//    public void setDepartmentName(String departmentName) {
//        this.departmentName = departmentName;
//    }
//
//    @Override
//    public String toString() {
//        return "Department{" +
//                "id='" + id + '\'' +
//                ", pId='" + pId + '\'' +
//                ", departmentName='" + departmentName + '\'' +
//                '}';
//    }

}
 
