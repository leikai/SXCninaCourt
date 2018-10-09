package org.sxchinacourt.bean;

/**
 * Created by 殇冰无恨 on 2017/11/24.
 */

public class MessagemachineBean {
    private String answerId;

    private String answerTime;

    private String answerType;

    private String filePath;

    private String flag;

    private String idNumber;

    private String judgeId;

    private String judgeName;

    private String oid;

    private String personName;

    private String textContent;

    public MessagemachineBean() {
    }

    public MessagemachineBean(String answerId, String answerTime, String answerType, String filePath, String flag, String idNumber, String judgeId, String judgeName, String oid, String personName, String textContent) {
        this.answerId = answerId;
        this.answerTime = answerTime;
        this.answerType = answerType;
        this.filePath = filePath;
        this.flag = flag;
        this.idNumber = idNumber;
        this.judgeId = judgeId;
        this.judgeName = judgeName;
        this.oid = oid;
        this.personName = personName;
        this.textContent = textContent;
    }

    public void setAnswerId(String answerId){
        this.answerId = answerId;
    }
    public String getAnswerId(){
        return this.answerId;
    }
    public void setAnswerTime(String answerTime){
        this.answerTime = answerTime;
    }
    public String getAnswerTime(){
        return this.answerTime;
    }
    public void setAnswerType(String answerType){
        this.answerType = answerType;
    }
    public String getAnswerType(){
        return this.answerType;
    }
    public void setFilePath(String filePath){
        this.filePath = filePath;
    }
    public String getFilePath(){
        return this.filePath;
    }
    public void setFlag(String flag){
        this.flag = flag;
    }
    public String getFlag(){
        return this.flag;
    }
    public void setIdNumber(String idNumber){
        this.idNumber = idNumber;
    }
    public String getIdNumber(){
        return this.idNumber;
    }
    public void setJudgeId(String judgeId){
        this.judgeId = judgeId;
    }
    public String getJudgeId(){
        return this.judgeId;
    }
    public void setJudgeName(String judgeName){
        this.judgeName = judgeName;
    }
    public String getJudgeName(){
        return this.judgeName;
    }
    public void setOid(String oid){
        this.oid = oid;
    }
    public String getOid(){
        return this.oid;
    }
    public void setPersonName(String personName){
        this.personName = personName;
    }
    public String getPersonName(){
        return this.personName;
    }
    public void setTextContent(String textContent){
        this.textContent = textContent;
    }
    public String getTextContent(){
        return this.textContent;
    }
}
