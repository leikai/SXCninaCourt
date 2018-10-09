package org.sxchinacourt.bean;

import java.io.Serializable;

/**
 * Created by DongZi on 2017/9/29.
 */

public class ParameterBean implements Serializable {
    private String nums;

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    @Override
    public String toString() {
        return "ParameterBean{" +
                "nums='" + nums + '\'' +
                '}';
    }
}
