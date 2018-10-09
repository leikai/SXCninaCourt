package org.sxchinacourt.bean;

import java.util.List;

/**
 * Created by DongZi on 2017/10/23.
 */

public class CourtBean {
    private String court;
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }


    @Override
    public String toString() {
        return "CourtBean{" +
                "court='" + court + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
