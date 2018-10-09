package org.sxchinacourt.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 殇冰无恨 on 2018/3/12.
 */
@Entity
public class Msgleavedb {
    @Id
    private Long id;
    @Property(nameInDb = "APPLICANT")
    private String applicant;//申请人
    @Property(nameInDb = "APPLICATIONDATA")
    private String applicationData;//申请日期
    @Property(nameInDb = "LEAVETYPE")
    private String leaveType;//请假类型
    @Property(nameInDb = "LEAVEDATA")
    private String leaveData;//请假日期
    @Property(nameInDb = "LEAVEDAYS")
    private String leaveDays;//请假天数
    @Property(nameInDb = "LEAVEREASONS")
    private String leaveReasons;//请假原因
    @Generated(hash = 51622816)
    public Msgleavedb(Long id, String applicant, String applicationData,
            String leaveType, String leaveData, String leaveDays,
            String leaveReasons) {
        this.id = id;
        this.applicant = applicant;
        this.applicationData = applicationData;
        this.leaveType = leaveType;
        this.leaveData = leaveData;
        this.leaveDays = leaveDays;
        this.leaveReasons = leaveReasons;
    }
    @Generated(hash = 865319862)
    public Msgleavedb() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getApplicant() {
        return this.applicant;
    }
    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }
    public String getApplicationData() {
        return this.applicationData;
    }
    public void setApplicationData(String applicationData) {
        this.applicationData = applicationData;
    }
    public String getLeaveType() {
        return this.leaveType;
    }
    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }
    public String getLeaveData() {
        return this.leaveData;
    }
    public void setLeaveData(String leaveData) {
        this.leaveData = leaveData;
    }
    public String getLeaveDays() {
        return this.leaveDays;
    }
    public void setLeaveDays(String leaveDays) {
        this.leaveDays = leaveDays;
    }
    public String getLeaveReasons() {
        return this.leaveReasons;
    }
    public void setLeaveReasons(String leaveReasons) {
        this.leaveReasons = leaveReasons;
    }
}
