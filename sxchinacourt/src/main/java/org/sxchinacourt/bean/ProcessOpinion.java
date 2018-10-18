package org.sxchinacourt.bean;

/**
 *
 * @author baggio
 * @date 2017/2/20
 */

public class ProcessOpinion {
    /**
     * 任务 ID
     */
    private String mTaskId;
    /**
     * 留言人
     */
    private String mCreateUser;
    /**
     * 留言日期
     */
    private String mCreateDate;
    /**
     * 选择的审核菜单名
     */
    private String mMenuName;
    /**
     * 节点名
     */
    private String mStepName;
    /**
     * 留言内容
     */
    private String mOpinion;

    public String getTaskId() {
        return mTaskId;
    }

    public void setTaskId(String mTaskId) {
        this.mTaskId = mTaskId;
    }

    public String getCreateUser() {
        return mCreateUser;
    }

    public void setCreateUser(String mCreateUser) {
        this.mCreateUser = mCreateUser;
    }

    public String getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(String mCreateDate) {
        this.mCreateDate = mCreateDate;
    }

    public String getMenuName() {
        return mMenuName;
    }

    public void setMenuName(String mMenuName) {
        this.mMenuName = mMenuName;
    }

    public String getStepName() {
        return mStepName;
    }

    public void setStepName(String mStepName) {
        this.mStepName = mStepName;
    }

    public String getOpinion() {
        return mOpinion;
    }

    public void setOpinion(String mOpinion) {
        this.mOpinion = mOpinion;
    }
}
