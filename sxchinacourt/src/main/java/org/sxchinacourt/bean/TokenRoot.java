package org.sxchinacourt.bean;

/**
 * @author lk
 */
public class TokenRoot {
    private boolean opresult;

    private String msg;

    private String msginfo;

    public void setOpresult(boolean opresult){
        this.opresult = opresult;
    }
    public boolean getOpresult(){
        return this.opresult;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setMsginfo(String msginfo){
        this.msginfo = msginfo;
    }
    public String getMsginfo(){
        return this.msginfo;
    }
}
