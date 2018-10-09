package org.sxchinacourt.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * @author lk
 */
public class UserNewBean implements Serializable {
    private String deptcode;

    private String deptid;

    private String deptname;

    private String deptoaid;

    private String empEmail;

    private String empMobilephone;

    private String empid;

    private String empname;

    private String handPassword;

    private String note;

    private String oaid;

    private String oid;

    private String orgcode;

    private String orgid;

    private String orgname;

    private String relation;

    private String roleid;

    private String signinfo;

    private String signpwd;

    private String userName;

    private String userPassword;

    public void setDeptcode(String deptcode){
        this.deptcode = deptcode;
    }
    public String getDeptcode(){
        return this.deptcode;
    }
    public void setDeptid(String deptid){
        this.deptid = deptid;
    }
    public String getDeptid(){
        return this.deptid;
    }
    public void setDeptname(String deptname){
        this.deptname = deptname;
    }
    public String getDeptname(){
        return this.deptname;
    }
    public void setDeptoaid(String deptoaid){
        this.deptoaid = deptoaid;
    }
    public String getDeptoaid(){
        return this.deptoaid;
    }
    public void setEmpEmail(String empEmail){
        this.empEmail = empEmail;
    }
    public String getEmpEmail(){
        return this.empEmail;
    }
    public void setEmpMobilephone(String empMobilephone){
        this.empMobilephone = empMobilephone;
    }
    public String getEmpMobilephone(){
        return this.empMobilephone;
    }
    public void setEmpid(String empid){
        this.empid = empid;
    }
    public String getEmpid(){
        return this.empid;
    }
    public void setEmpname(String empname){
        this.empname = empname;
    }
    public String getEmpname(){
        return this.empname;
    }
    public void setHandPassword(String handPassword){
        this.handPassword = handPassword;
    }
    public String getHandPassword(){
        return this.handPassword;
    }
    public void setNote(String note){
        this.note = note;
    }
    public String getNote(){
        return this.note;
    }
    public void setOaid(String oaid){
        this.oaid = oaid;
    }
    public String getOaid(){
        return this.oaid;
    }
    public void setOid(String oid){
        this.oid = oid;
    }
    public String getOid(){
        return this.oid;
    }
    public void setOrgcode(String orgcode){
        this.orgcode = orgcode;
    }
    public String getOrgcode(){
        return this.orgcode;
    }
    public void setOrgid(String orgid){
        this.orgid = orgid;
    }
    public String getOrgid(){
        return this.orgid;
    }
    public void setOrgname(String orgname){
        this.orgname = orgname;
    }
    public String getOrgname(){
        return this.orgname;
    }
    public void setRelation(String relation){
        this.relation = relation;
    }
    public String getRelation(){
        return this.relation;
    }
    public void setRoleid(String roleid){
        this.roleid = roleid;
    }
    public String getRoleid(){
        return this.roleid;
    }
    public void setSigninfo(String signinfo){
        this.signinfo = signinfo;
    }
    public String getSigninfo(){
        return this.signinfo;
    }
    public void setSignpwd(String signpwd){
        this.signpwd = signpwd;
    }
    public String getSignpwd(){
        return this.signpwd;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
    public void setUserPassword(String userPassword){
        this.userPassword = userPassword;
    }
    public String getUserPassword(){
        return this.userPassword;
    }


    public void copyUserInfo(UserNewBean user) {
        setOid(user.getOid());
        if (!TextUtils.isEmpty(user.getDeptcode())) {
            setDeptcode(user.getDeptcode());
        }
        if (!TextUtils.isEmpty(user.getDeptid())) {
            setDeptid(user.getDeptid());
        }
        if (!TextUtils.isEmpty(user.getDeptname())) {
            setDeptname(user.getDeptname());
        }
        if (!TextUtils.isEmpty(user.getDeptoaid())) {
            setDeptoaid(user.getDeptoaid());
        }
        if (!TextUtils.isEmpty(user.getEmpEmail())) {
            setEmpEmail(user.getEmpEmail());
        }
        if (!TextUtils.isEmpty(user.getEmpMobilephone())) {
            setEmpMobilephone(user.getEmpMobilephone());
        }
        if (!TextUtils.isEmpty(user.getEmpid())) {
            setEmpid(user.getEmpid());
        }
        if (!TextUtils.isEmpty(user.getEmpname())) {
            setEmpname(user.getEmpname());
        }
        if (!TextUtils.isEmpty(user.getNote())) {
            setNote(user.getNote());
        }
        if (!TextUtils.isEmpty(user.getOaid())) {
            setOaid(user.getOaid());
        }
        if (!TextUtils.isEmpty(user.getRoleid())) {
            setRoleid(user.getRoleid());
        }
        if (!TextUtils.isEmpty(user.getUserName())) {
            setUserName(user.getUserName());
        }
        if (!TextUtils.isEmpty(user.getUserPassword())) {
            setUserPassword(user.getUserPassword());
        }
        if (!TextUtils.isEmpty(user.getOrgcode())) {
            setOrgcode(user.getOrgcode());
        }
        if (!TextUtils.isEmpty(user.getOrgid())) {
            setOrgid(user.getOrgid());
        }
        if (!TextUtils.isEmpty(user.getOrgname())) {
            setOrgname(user.getOrgname());
        }
        if (!TextUtils.isEmpty(user.getRelation())) {
            setRelation(user.getRelation());
        }
        if (!TextUtils.isEmpty(user.getHandPassword())) {
            setHandPassword(user.getHandPassword());
        }
        if (!TextUtils.isEmpty(user.getSigninfo())) {
            setSigninfo(user.getSigninfo());
        }
        if (!TextUtils.isEmpty(user.getSignpwd())) {
            setSignpwd(user.getSignpwd());
        }
    }

}
