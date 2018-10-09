package org.sxchinacourt.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by 殇冰无恨 on 2017/12/19.
 */

@Entity
public class Msgmachinedb {
    @Id
    private Long id;
    @Property(nameInDb = "ANSWERID")
    private String answerId;
    @Property(nameInDb = "ANSWERTIME")
    private String answerTime;
    @Property(nameInDb = "ANSWERTYPE")
    private String answerType;
    @Property(nameInDb = "DELFLAG")
    private String del_flag;
    @Property(nameInDb = "FILEPATH")
    private String filePath;
    @Property(nameInDb = "FLAG")
    private String flag;
    @Property(nameInDb = "IDNUMBER")
    private String idNumber;
    @Property(nameInDb = "JUDGEID")
    private String judgeId;
    @Property(nameInDb = "JUDGENAME")
    private String judgeName;
    @Property(nameInDb = "OID")
    private String oid;
    @Property(nameInDb = "PERSONNAME")
    private String personName;
    @Property(nameInDb = "PHONEFLAG")
    private String phone_flag;
    @Property(nameInDb = "TEXTCONTENT")
    private String textContent;
    @Generated(hash = 2002452797)
    public Msgmachinedb(Long id, String answerId, String answerTime,
            String answerType, String del_flag, String filePath, String flag,
            String idNumber, String judgeId, String judgeName, String oid,
            String personName, String phone_flag, String textContent) {
        this.id = id;
        this.answerId = answerId;
        this.answerTime = answerTime;
        this.answerType = answerType;
        this.del_flag = del_flag;
        this.filePath = filePath;
        this.flag = flag;
        this.idNumber = idNumber;
        this.judgeId = judgeId;
        this.judgeName = judgeName;
        this.oid = oid;
        this.personName = personName;
        this.phone_flag = phone_flag;
        this.textContent = textContent;
    }
    @Generated(hash = 2037644220)
    public Msgmachinedb() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAnswerId() {
        return this.answerId;
    }
    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }
    public String getAnswerTime() {
        return this.answerTime;
    }
    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }
    public String getAnswerType() {
        return this.answerType;
    }
    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }
    public String getDel_flag() {
        return this.del_flag;
    }
    public void setDel_flag(String del_flag) {
        this.del_flag = del_flag;
    }
    public String getFilePath() {
        return this.filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getFlag() {
        return this.flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }
    public String getIdNumber() {
        return this.idNumber;
    }
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
    public String getJudgeId() {
        return this.judgeId;
    }
    public void setJudgeId(String judgeId) {
        this.judgeId = judgeId;
    }
    public String getJudgeName() {
        return this.judgeName;
    }
    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }
    public String getOid() {
        return this.oid;
    }
    public void setOid(String oid) {
        this.oid = oid;
    }
    public String getPersonName() {
        return this.personName;
    }
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    public String getPhone_flag() {
        return this.phone_flag;
    }
    public void setPhone_flag(String phone_flag) {
        this.phone_flag = phone_flag;
    }
    public String getTextContent() {
        return this.textContent;
    }
    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

}
