package org.sxchinacourt.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baggio on 2017/2/24.
 */

public class AssignTaskBean {
    /**
     * wtitle:任务标题
     */
    private String mWTitle;
    /**
     * wsman:布置人姓名
     */
    private String mWsMan;
    /**
     * wsmanid:布置人id  getUserInfo接口获取的 courtoaid的值
     */
    private String mWsManId;
    /**
     * wsmanoaid:布置人工作流id  getUserInfo接口获取的 id的值
     */
    private int mWsManOAId;
    /**
     * note:选择的执行人姓名，多人用,隔开
     */
    private String mNote;
    /**
     * stime:开始日期
     */
    private String mSTime;
    /**
     * etime:结束日期
     */
    private String mETime;
    /**
     * alerttime:提醒日期
     */
    private String mAlertTime;
    /**
     * wcontent:任务内容
     */
    private String mWContent;
    /**
     * 执行人明细
     */
    private List<AssignSubTaskBean> mDetails;

    public AssignTaskBean() {
        mDetails = new ArrayList<>();
    }

    public String getWTitle() {
        return mWTitle;
    }

    public void setWTitle(String mWTitle) {
        this.mWTitle = mWTitle;
    }

    public String getWsMan() {
        return mWsMan;
    }

    public void setWsMan(String mWsMan) {
        this.mWsMan = mWsMan;
    }

    public String getWsManId() {
        return mWsManId;
    }

    public void setWsManId(String mWsManId) {
        this.mWsManId = mWsManId;
    }

    public int getWsManOAId() {
        return mWsManOAId;
    }

    public void setWsManOAId(int mWsManOAId) {
        this.mWsManOAId = mWsManOAId;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String mNote) {
        this.mNote = mNote;
    }

    public String getSTime() {
        return mSTime;
    }

    public void setSTime(String mSTime) {
        this.mSTime = mSTime;
    }

    public String getETime() {
        return mETime;
    }

    public void setETime(String mETime) {
        this.mETime = mETime;
    }

    public String getAlertTime() {
        return mAlertTime;
    }

    public void setAlertTime(String mAlertTime) {
        this.mAlertTime = mAlertTime;
    }

    public String getWContent() {
        return mWContent;
    }

    public void setWContent(String mWContent) {
        this.mWContent = mWContent;
    }

    public List<AssignSubTaskBean> getDetails() {
        return mDetails;
    }

    public void setDetails(List<AssignSubTaskBean> mDetails) {
        this.mDetails = mDetails;
    }

    public void addSubTask(AssignSubTaskBean subTask) {
        mDetails.add(subTask);
    }

    public void removeSubTask(AssignSubTaskBean subTask) {
        mDetails.remove(subTask);
    }

    public int getSubTaskSize() {
        return mDetails.size();
    }

    public AssignSubTaskBean getSubTask(int i) {
        return mDetails.get(i);
    }
}
