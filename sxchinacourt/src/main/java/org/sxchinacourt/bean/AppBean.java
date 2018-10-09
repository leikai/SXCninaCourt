package org.sxchinacourt.bean;

/**
 * Created by baggio on 2017/2/3.
 */

public class AppBean {
    public static final int FRAGMENT_TO_DO_TASK = 0;
    public static final int FRAGMENT_HISTORY_TASK = 1;
    public static final int FRAGMENT_NOTICE_TASK = 2;
    public static final int FRAGMENT_MANAGER_TASK = 3;
    public static final int FRAGMENT_EMAIL_TASK =4;
    public static final int FRAGMENT_CABINET_TASK = 5;//云柜
    public static final int FRAGMENT_MESSAGE_MACHINE = 6;//留言机
    private String mName;
    private int mFragmentIndex;
    private int mResourceId;
    private int mBgColor;

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }

    public void setResourceId(int mResourceId) {
        this.mResourceId = mResourceId;
    }

    public int getResourceId() {
        return mResourceId;
    }

    public void setFragmentIndex(int fragmentIndex) {
        this.mFragmentIndex = fragmentIndex;
    }

    public int getFragmentIndex() {
        return mFragmentIndex;
    }

    public void setBgColor(int mBgColor) {
        this.mBgColor = mBgColor;
    }

    public int getBgColor() {
        return mBgColor;
    }
}
