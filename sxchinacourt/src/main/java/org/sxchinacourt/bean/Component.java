package org.sxchinacourt.bean;

import android.view.View;

/**
 * Created by baggio on 2017/3/2.
 */

public class Component {
    //对应后台的view 标签常量
    public static final String VIEW_TYPE_HIDDENVIEW = "hiddenview";
    public static final String VIEW_TYPE_TEXTVIEW = "textview";
    public static final String VIEW_TYPE_EDITVIEW = "editview";
    public static final String VIEW_TYPE_DATEVIEW = "dateview";
    public static final String VIEW_TYPE_SELECTVIEW = "selectview";
    public static final String VIEW_TYPE_ATTACHMENTVIEW = "attachmentview";
    public static final String VIEW_TYPE_SIGNVIEW = "signview";
    public static final String VIEW_TYPE_RADIOVIEW = "radioview";
    public static final String  VIEW_TYPE_IMAGEVIEW = "imageview";

    protected String mViewName;
    protected String mLabelValue;
    protected String mText;
    protected String mHintText;
    protected View mView;

    public String getViewName() {
        return mViewName;
    }

    public void setViewName(String mView) {
        this.mViewName = mView;
    }

    public String getLabelValue() {
        return mLabelValue;
    }

    public void setLabelValue(String mLabelValue) {
        this.mLabelValue = mLabelValue;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public void setView(View mView) {
        this.mView = mView;
    }

    public View getView() {
        return mView;
    }

    public void setHintText(String mHintText) {
        this.mHintText = mHintText;
    }

    public String getHintText() {
        return mHintText;
    }
}
