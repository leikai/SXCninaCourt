package org.sxchinacourt.bean;

import android.text.TextUtils;
import android.util.Log;

import java.io.Serializable;

/**
 *
 * @author baggio
 * @date 2017/2/6
 */

public class UserBean implements Serializable {
    private static final long serialVersionUID = -2415833265553362629L;
    private int mId;
    private String mUserNo;
    private String mTel;
    private String mMobile;
    private String mEmail;
    private String mCompanyName;
    private String mDepartmentName;
    private String mDepartmentFullName;
    private String mUserMap;
    private String mExtend1;
    private String mExtend2;
    private String mExtend3;
    private String mExtend4;
    private String mExtend5;
    private String mUserId;
    private String mPwd;
    private String mSessionId;
    private String mUserName;
    private String mDepartmentId;
    private String mRoleName;
    private String mCourtoaid;
    private DepartmentBean mDepartment;
    private RoleBean mRole;
    private boolean mIsChecked;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setPwd(String mPwd) {
        this.mPwd = mPwd;
    }

    public String getPwd() {
        return mPwd;
    }

    public void setSessionId(String mSession) {
        this.mSessionId = mSession;
    }

    public String getSessionId() {
        return mSessionId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getDepartmentId() {
        return mDepartmentId;
    }

    public void setDepartmentId(String mDepartmentId) {
        this.mDepartmentId = mDepartmentId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getUserNo() {
        return mUserNo;
    }

    public void setUserNo(String mUserNo) {
        this.mUserNo = mUserNo;
    }

    public String getTel() {
        return mTel;
    }

    public void setTel(String mTel) {
        this.mTel = mTel;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mMobile) {
        this.mMobile
                = mMobile;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public void setCompanyName(String mCompanyName) {
        this.mCompanyName = mCompanyName;
    }

    public String getDepartmentName() {
        return mDepartmentName;
    }

    public void setDepartmentName(String mDepartmentName) {
        this.mDepartmentName = mDepartmentName;
    }

    public String getDepartmentFullName() {
        return mDepartmentFullName;
    }

    public void setDepartmentFullName(String mDepartmentFullName) {
        this.mDepartmentFullName = mDepartmentFullName;
    }

    public String getUserMap() {
        return mUserMap;
    }

    public void setUserMap(String mUserMap) {
        this.mUserMap = mUserMap;
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

    public String getExtend3() {
        return mExtend3;
    }

    public void setExtend3(String mExtend3) {
        this.mExtend3 = mExtend3;
    }

    public String getExtend4() {
        return mExtend4;
    }

    public void setExtend4(String mExtend4) {
        this.mExtend4 = mExtend4;
    }

    public String getExtend5() {
        return mExtend5;
    }

    public void setExtend5(String mExtend5) {
        this.mExtend5 = mExtend5;
    }

    public void setCourtoaid(String mCourtoaid) {
        this.mCourtoaid = mCourtoaid;
    }

    public String getCourtoaid() {
        return mCourtoaid;
    }

    public void setDepartment(DepartmentBean mDepartment) {
        this.mDepartment = mDepartment;
    }

    public DepartmentBean getDepartment() {
        return mDepartment;
    }

    public void setRole(RoleBean mRole) {
        this.mRole = mRole;
    }

    public RoleBean getRole() {
        return mRole;
    }


    public String getRoleName() {
        return mRoleName;
    }

    public void setRoleName(String mRoleName) {
        this.mRoleName = mRoleName;
    }


    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean checked) {
        mIsChecked = checked;
    }

    public void copyUserInfo(UserBean user) {
        if (user !=null){
            int i = user.getId();
            Log.e("",""+i);

            if (!TextUtils.isEmpty(String.valueOf(user.getId()))){
                setId(user.getId());
            }
            if (!TextUtils.isEmpty(user.getCourtoaid())) {
                setCourtoaid(user.getCourtoaid());
            }
            if (!TextUtils.isEmpty(user.getUserId())) {
                setUserId(user.getUserId());
            }
            if (!TextUtils.isEmpty(user.getUserNo())) {
                setUserNo(user.getUserNo());
            }
            if (!TextUtils.isEmpty(user.getTel())) {
                setTel(user.getTel());
            }
            if (!TextUtils.isEmpty(user.getMobile())) {
                setMobile(user.getMobile());
            }
            if (!TextUtils.isEmpty(user.getEmail())) {
                setEmail(user.getEmail());
            }
            if (!TextUtils.isEmpty(user.getCompanyName())) {
                setCompanyName(user.getCompanyName());
            }
            if (!TextUtils.isEmpty(user.getDepartmentName())) {
                setDepartmentName(user.getDepartmentName());
            }
            if (!TextUtils.isEmpty(user.getDepartmentFullName())) {
                setDepartmentFullName(user.getDepartmentFullName());
            }
            if (!TextUtils.isEmpty(user.getUserMap())) {
                setUserMap(user.getUserMap());
            }
            if (!TextUtils.isEmpty(user.getExtend1())) {
                setExtend1(user.getExtend1());
            }
            if (!TextUtils.isEmpty(user.getExtend2())) {
                setExtend1(user.getExtend2());
            }
            if (!TextUtils.isEmpty(user.getExtend3())) {
                setExtend1(user.getExtend3());
            }
            if (!TextUtils.isEmpty(user.getExtend4())) {
                setExtend1(user.getExtend4());
            }
            if (!TextUtils.isEmpty(user.getExtend5())) {
                setExtend1(user.getExtend5());
            }
            if (!TextUtils.isEmpty(user.getSessionId())) {
                setSessionId(user.getSessionId());
            }
            if (!TextUtils.isEmpty(user.getUserName())) {
                setUserName(user.getUserName());
            }
            if (!TextUtils.isEmpty(user.getDepartmentId())) {
                setDepartmentId(user.getDepartmentId());
            }
            if (!TextUtils.isEmpty(user.getRoleName())) {
                setRoleName(user.getRoleName());
            }
            if (!TextUtils.isEmpty(user.getToken())) {
                setRoleName(user.getToken());
            }
        }

    }
}
