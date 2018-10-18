package org.sxchinacourt.bean;

import java.io.Serializable;

/**
 * Created by baggio on 2017/2/6.
 */

public class TaskBean implements Serializable{
    private static final long serialVersionUID = -4973493973474949453L;

    public static final int STATUS_TYPE_NORMAL_TASK = 1;
    public static final int STATUS_TYPE_CIRCULATE_TASK = 2;
    public static final int STATUS_TYPE_REJECT_TASK = 3;
    public static final int STATUS_TYPE_HANGING_TASK = 4;
    public static final int STATUS_TYPE_NOTICE_TASK = 9;
    public static final int STATUS_TYPE_TO_SIGN_TASK = 11;

    /**
     * 序号
     */
    private String mNo;
    /**
     * 任务
     */
    private int mTaskId;
    /**
     *流程实例 ID
     */
    private int mProcessInstanceId;
    /**
     * 状态（1=正常任务，2=传阅任务，3=驳回修改任务，4=挂起等待任务，9=通知任务，11=加签任务）
     */
    private int mStatus;
    /**
     * 任务创建者
     */
    private String mOwner;
    /**
     * 任务创建者用户名
     */
    private String mOwnerName;
    /**
     * 任务标题
     */
    private String mTitle;
    /**
     * 流程组名称
     */
    private String mProcessGroup;
    /**
     * 任务达到时间
     */
    private String mBeginTime;
    /**
     * 读取时间
     */
    private String mReadTime;
    /**
     * 是否读取
     */
    private boolean isRead;
    /**
     * 优先级（0=低，1=无，2=中，3=高）
     */
    private int mPriority;
    /**
     * 流程模型 ID
     */
    private String mProcessDefId;
    /**
     * 节点模型 ID
     */
    private String mActivityDefId;
    /**
     * 流程任务标题
     */
    private String mWftitle;
    /**
     * 节点名称
     */
    private String mStepname;
    /**
     * 该节点激活应用中定义的‘激活 URL’值
     */
    private String mBindUrl;

    public void setNo(String mNo) {
        this.mNo = mNo;
    }


    public int getTaskId() {
        return mTaskId;
    }

    public void setTaskId(int mTaskId) {
        this.mTaskId = mTaskId;
    }

    public int getProcessInstanceId() {
        return mProcessInstanceId;
    }

    public void setProcessInstanceId(int mProcessInstanceId) {
        this.mProcessInstanceId = mProcessInstanceId;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String mOwner) {
        this.mOwner = mOwner;
    }

    public String getOwnerName() {
        return mOwnerName;
    }

    public void setOwnerName(String mOwnerName) {
        this.mOwnerName = mOwnerName;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getProcessGroup() {
        return mProcessGroup;
    }

    public void setProcessGroup(String mProcessGroup) {
        this.mProcessGroup = mProcessGroup;
    }

    public String getBeginTime() {
        return mBeginTime;
    }

    public void setBeginTime(String mBeginTime) {
        this.mBeginTime = mBeginTime;
    }

    public String getReadTime() {
        return mReadTime;
    }

    public void setReadTime(String mReadTime) {
        this.mReadTime = mReadTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int mPriority) {
        this.mPriority = mPriority;
    }

    public String getProcessDefId() {
        return mProcessDefId;
    }

    public void setProcessDefId(String mProcessDefId) {
        this.mProcessDefId = mProcessDefId;
    }

    public String getActivityDefId() {
        return mActivityDefId;
    }

    public void setActivityDefId(String mActivityDefId) {
        this.mActivityDefId = mActivityDefId;
    }

    public String getWftitle() {
        return mWftitle;
    }

    public void setWftitle(String mWftitle) {
        this.mWftitle = mWftitle;
    }

    public String getStepname() {
        return mStepname;
    }

    public void setStepname(String mStepname) {
        this.mStepname = mStepname;
    }

    public String getBindUrl() {
        return mBindUrl;
    }

    public void setBindUrl(String mBindUrl) {
        this.mBindUrl = mBindUrl;
    }
}
