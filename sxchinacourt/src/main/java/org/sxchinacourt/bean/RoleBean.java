package org.sxchinacourt.bean;

import java.io.Serializable;

/**
 *
 * @author baggio
 * @date 2017/2/8
 */

public class RoleBean implements Serializable{
    private static final long serialVersionUID = -2856663900544568497L;
    private String mId;
    private String mRoleName;
    private String mGroupName;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getRoleName() {
        return mRoleName;
    }

    public void setRoleName(String mRoleName) {
        this.mRoleName = mRoleName;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String mGroupName) {
        this.mGroupName = mGroupName;
    }
}
