package org.sxchinacourt.dataparser;

import org.json.JSONException;
import org.json.JSONObject;
import org.sxchinacourt.bean.ProcessOpinion;

/**
 *
 * @author baggio
 * @date 2017/2/20
 */

public class ProcessOpinionParser extends DataParser {
    public static final String TASKID = "taskId";
    /**
     * 留言人
     */
    public static final String CREATEUSER = "createUser";
    /**
     * 留言日期
     */
    public static final String CREATEDATE = "createDate";
    /**
     * 选择的审核菜单名
     */
    public static final String MENUNAME = "menuName";
    /**
     * 节点名
     */
    public static final String STEPNAME = "stepName";
    /**
     * 留言内容
     */
    public static final String OPINION = "opinion";

    @Override
    public ProcessOpinion parser(JSONObject jsonObject) throws JSONException {
        ProcessOpinion processOpinion = new ProcessOpinion();
        if (jsonObject.has(TASKID)) {
            processOpinion.setTaskId(jsonObject.getString(TASKID));
        }
        if (jsonObject.has(CREATEUSER)) {
            processOpinion.setCreateUser(jsonObject.getString(CREATEUSER));
        }
        if (jsonObject.has(CREATEDATE)) {
            processOpinion.setCreateDate(jsonObject.getString(CREATEDATE));
        }
        if (jsonObject.has(MENUNAME)) {
            processOpinion.setMenuName(jsonObject.getString(MENUNAME));
        }
        if (jsonObject.has(STEPNAME)) {
            processOpinion.setStepName(jsonObject.getString(STEPNAME));
        }
        if (jsonObject.has(OPINION)) {
            processOpinion.setOpinion(jsonObject.getString(OPINION));
        }
        return processOpinion;
    }
}
