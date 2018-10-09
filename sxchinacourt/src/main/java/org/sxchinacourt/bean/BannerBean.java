package org.sxchinacourt.bean;

import java.io.Serializable;

/**
 * Created by DongZi on 2017/9/29.
 */

public class BannerBean implements Serializable {
    private String attname;

    private String fdid;

    private String oid;

    private String photopath;

    private String realname;

    private String title;

    public String getAttname() {
        return attname;
    }

    public void setAttname(String attname) {
        this.attname = attname;
    }

    public String getFdid() {
        return fdid;
    }

    public void setFdid(String fdid) {
        this.fdid = fdid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getPhotopath() {
        return photopath;
    }

    public void setPhotopath(String photopath) {
        this.photopath = photopath;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "BannerBean{" +
                "attname='" + attname + '\'' +
                ", fdid='" + fdid + '\'' +
                ", oid='" + oid + '\'' +
                ", photopath='" + photopath + '\'' +
                ", realname='" + realname + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
