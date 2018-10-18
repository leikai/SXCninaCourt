package org.sxchinacourt.dataparser;

import org.json.JSONException;
import org.json.JSONObject;
import org.sxchinacourt.bean.TaskBean;

/**
 *
 * @author baggio
 * @date 2017/2/6
 */

public class TaskParser extends DataParser {
    /**
     * no 序号
     * taskId 任务 ID
     * processInstanceId 流程实例 ID
     * status 状态（1=正常任务，2=传阅任务，3=驳回修改任务，4=挂起等待任务，9=通知任务，11=加签任务）
     * owner 任务创建者
     * ownerName 任务创建者用户名
     * title 任务标题
     * processGroup 流程组名称
     * beginTime 任务达到时间
     * isRead 是否读取
     * readTime 读取时间
     * priority 优先级（0=低，1=无，2=中，3=高）
     * processDefId 流程模型 ID
     * activityDefId 节点模型 ID
     * wftitle 流程任务标题
     * stepname 节点名称
     * bindUrl 该节点激活应用中定义的‘激活 URL’值
     */
    public static final String NO = "no";
    public static final String TASKID = "taskId";
    public static final String PROCESSINSTANCEID = "processInstanceId";
    public static final String STATUS = "status";
    public static final String OWNER = "owner";
    public static final String OWNERNAME = "ownerName";
    public static final String TITLE = "title";
    public static final String PROCESSGROUP = "processGroup";
    public static final String BEGINTIME = "beginTime";
    public static final String ISREAD = "isRead";
    public static final String READTIME = "readTime";
    public static final String PRIORITY = "priority";
    public static final String PROCESSDEFID = "processDefId";
    public static final String ACTIVITYDEFID = "activityDefId";
    public static final String WFTITLE = "wftitle";
    public static final String STEPNAME = "stepname";
    public static final String BINDURL = "bindUrl";

    @Override
    public Object parser(JSONObject jsonObject) throws JSONException {
        TaskBean task = new TaskBean();
        if (jsonObject.has(NO)) {
            task.setNo(jsonObject.getString(NO));
        }
        if (jsonObject.has(TASKID)) {
            task.setTaskId(jsonObject.getInt(TASKID));
        }
        if (jsonObject.has(PROCESSINSTANCEID)) {
            task.setProcessInstanceId(jsonObject.getInt(PROCESSINSTANCEID));
        }
        if (jsonObject.has(STATUS)) {
            task.setStatus(jsonObject.getInt(STATUS));
        }
        if (jsonObject.has(OWNER)) {
            task.setOwner(jsonObject.getString(OWNER));
        }
        if (jsonObject.has(OWNERNAME)) {
            task.setOwnerName(jsonObject.getString(OWNERNAME));
        }
        if (jsonObject.has(TITLE)) {
            task.setTitle(jsonObject.getString(TITLE));
        }
        if (jsonObject.has(PROCESSGROUP)) {
            task.setProcessGroup(jsonObject.getString(PROCESSGROUP));
        }
        if (jsonObject.has(BEGINTIME)) {
            task.setBeginTime(jsonObject.getString(BEGINTIME));
        }
        if (jsonObject.has(ISREAD)) {
            task.setRead(jsonObject.getBoolean(ISREAD));
        }
        if (jsonObject.has(READTIME)) {
            task.setReadTime(jsonObject.getString(READTIME));
        }
        if (jsonObject.has(PRIORITY)) {
            task.setPriority(jsonObject.getInt(PRIORITY));
        }
        if (jsonObject.has(PROCESSDEFID)) {
            task.setProcessDefId(jsonObject.getString(PROCESSDEFID));
        }
        if (jsonObject.has(ACTIVITYDEFID)) {
            task.setActivityDefId(jsonObject.getString(ACTIVITYDEFID));
        }
        if (jsonObject.has(WFTITLE)) {
            task.setWftitle(jsonObject.getString(WFTITLE));
        }
        if (jsonObject.has(STEPNAME)) {
            task.setStepname(jsonObject.getString(STEPNAME));
        }
        if (jsonObject.has(BINDURL)) {
            task.setBindUrl(jsonObject.getString(BINDURL));
        }
        return task;
    }
}
