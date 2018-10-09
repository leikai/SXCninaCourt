package org.sxchinacourt.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by baggio on 2017/2/8.
 */

public  class  DepartmentBean implements Serializable {
    private static final long serialVersionUID = -4854451883462054811L;
    private String mId;
    private String mDepartmentName;    //联系人分组名称
    private String mDepartmentNo;
    private String mDepartmentFullName;
    private String mDepartmentFullId;
    private String mParentDepartmentId;
    private String mLayer;
    private String mDepartmentZone;
    private String mExtend1;
    private String mExtend2;
    private List<UserBean> mUsers;
    private boolean mUserListIsExpanded;
    private boolean mSelectAll;
    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getDepartmentName() {
        return mDepartmentName;
    }

    public void setDepartmentName(String mDepartmentName) {
        this.mDepartmentName = mDepartmentName;
    }

    public String getDepartmentNo() {
        return mDepartmentNo;
    }

    public void setDepartmentNo(String mDepartmentNo) {
        this.mDepartmentNo = mDepartmentNo;
    }

    public String getDepartmentFullName() {
        return mDepartmentFullName;
    }

    public void setDepartmentFullName(String mDepartmentFullName) {
        this.mDepartmentFullName = mDepartmentFullName;
    }

    public String getDepartmentFullId() {
        return mDepartmentFullId;
    }

    public void setDepartmentFullId(String mDepartmentFullId) {
        this.mDepartmentFullId = mDepartmentFullId;
    }

    public String getParentDepartmentId() {
        return mParentDepartmentId;
    }

    public void setParentDepartmentId(String mParentDepartmentId) {
        this.mParentDepartmentId = mParentDepartmentId;
    }

    public String getLayer() {
        return mLayer;
    }

    public void setLayer(String mLayer) {
        this.mLayer = mLayer;
    }

    public String getDepartmentZone() {
        return mDepartmentZone;
    }

    public void setDepartmentZone(String mDepartmentZone) {
        this.mDepartmentZone = mDepartmentZone;
    }

    public String getExtend1() {
        return mExtend1;
    }

    public void setExtend1(String mExtend1) {
        this.mExtend1 = mExtend1;
    }

    public String getExtend2() {
        return mExtend2;
    }

    public void setExtend2(String mExtend2) {
        this.mExtend2 = mExtend2;
    }

    public void setUsers(List<UserBean> mUsers) {
        this.mUsers = mUsers;
    }

    public List<UserBean> getUsers() {
        return mUsers;
    }

    public void setUserListIsExpanded(boolean mUserListIsExpanded) {
        this.mUserListIsExpanded = mUserListIsExpanded;
    }

    public boolean isUserListIsExpanded() {
        return mUserListIsExpanded;
    }

    public void setSelectAll(boolean mSelectAll) {
        this.mSelectAll = mSelectAll;
    }

    public boolean isSelectAll() {
        return mSelectAll;
    }
}
