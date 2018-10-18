package org.sxchinacourt.bean;

/**
 *
 * @author 殇冰无恨
 * @date 2017/10/11
 */

public class DepartmentForCabinetBean {
    private int DepartmentID;

    private String DepartmentName;

    private int DepartmentType;

    private int CourtID;

    private int ParentID;

    private int SortID;

    private String CreateTime;

    private String TDHID;

    private String TDHbmdm;

    private String TDHdwdm;

    private String TDHbmid;

    private String TDHbmmc;

    private String TDHsfjy;

    private String TDHlastupdate;

    private String DepUserName;

    private String DepPwd;

    public void setDepartmentID(int DepartmentID){
        this.DepartmentID = DepartmentID;
    }
    public int getDepartmentID(){
        return this.DepartmentID;
    }
    public void setDepartmentName(String DepartmentName){
        this.DepartmentName = DepartmentName;
    }
    public String getDepartmentName(){
        return this.DepartmentName;
    }
    public void setDepartmentType(int DepartmentType){
        this.DepartmentType = DepartmentType;
    }
    public int getDepartmentType(){
        return this.DepartmentType;
    }
    public void setCourtID(int CourtID){
        this.CourtID = CourtID;
    }
    public int getCourtID(){
        return this.CourtID;
    }
    public void setParentID(int ParentID){
        this.ParentID = ParentID;
    }
    public int getParentID(){
        return this.ParentID;
    }
    public void setSortID(int SortID){
        this.SortID = SortID;
    }
    public int getSortID(){
        return this.SortID;
    }
    public void setCreateTime(String CreateTime){
        this.CreateTime = CreateTime;
    }
    public String getCreateTime(){
        return this.CreateTime;
    }
    public void setTDHID(String TDHID){
        this.TDHID = TDHID;
    }
    public String getTDHID(){
        return this.TDHID;
    }
    public void setTDHbmdm(String TDHbmdm){
        this.TDHbmdm = TDHbmdm;
    }
    public String getTDHbmdm(){
        return this.TDHbmdm;
    }
    public void setTDHdwdm(String TDHdwdm){
        this.TDHdwdm = TDHdwdm;
    }
    public String getTDHdwdm(){
        return this.TDHdwdm;
    }
    public void setTDHbmid(String TDHbmid){
        this.TDHbmid = TDHbmid;
    }
    public String getTDHbmid(){
        return this.TDHbmid;
    }
    public void setTDHbmmc(String TDHbmmc){
        this.TDHbmmc = TDHbmmc;
    }
    public String getTDHbmmc(){
        return this.TDHbmmc;
    }
    public void setTDHsfjy(String TDHsfjy){
        this.TDHsfjy = TDHsfjy;
    }
    public String getTDHsfjy(){
        return this.TDHsfjy;
    }
    public void setTDHlastupdate(String TDHlastupdate){
        this.TDHlastupdate = TDHlastupdate;
    }
    public String getTDHlastupdate(){
        return this.TDHlastupdate;
    }
    public void setDepUserName(String DepUserName){
        this.DepUserName = DepUserName;
    }
    public String getDepUserName(){
        return this.DepUserName;
    }
    public void setDepPwd(String DepPwd){
        this.DepPwd = DepPwd;
    }
    public String getDepPwd(){
        return this.DepPwd;
    }
}
