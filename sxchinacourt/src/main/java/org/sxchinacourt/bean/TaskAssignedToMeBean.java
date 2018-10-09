package org.sxchinacourt.bean;

import java.io.Serializable;

/**
 * Created by baggio on 2017/2/27.
 */

public class TaskAssignedToMeBean implements Serializable{
    private static final long serialVersionUID = -1063949470775758422L;
    private String mOId;
    private String mTitle;
    private String mNote;

    public String getOId() {
        return mOId;
    }

    public void setOId(String mOId) {
        this.mOId = mOId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String mNote) {
        this.mNote = mNote;
    }
}
