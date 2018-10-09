package org.sxchinacourt.bean;

/**
 * Created by 殇冰无恨 on 2017/10/12.
 */

public class CeShiBean {
    private String mobiles;
    private String smsContent;
    private String mans;

    @Override
    public String toString() {
        return "CeShiBean{" +
                "mobiles='" + mobiles + '\'' +
                ", smsContent='" + smsContent + '\'' +
                ", mans='" + mans + '\'' +
                '}';
    }

    public String getMobiles() {
        return mobiles;
    }

    public void setMobiles(String mobiles) {
        this.mobiles = mobiles;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getMans() {
        return mans;
    }

    public void setMans(String mans) {
        this.mans = mans;
    }
}
