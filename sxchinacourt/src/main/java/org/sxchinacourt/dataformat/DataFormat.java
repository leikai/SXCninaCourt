package org.sxchinacourt.dataformat;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sxchinacourt.bean.AssignSubTaskBean;
import org.sxchinacourt.bean.AssignTaskBean;
import org.sxchinacourt.bean.DepartmentBean;
import org.sxchinacourt.bean.RoleBean;
import org.sxchinacourt.bean.TaskBean;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.dataparser.AssignTaskParser;
import org.sxchinacourt.dataparser.DepartmentParser;
import org.sxchinacourt.dataparser.RoleParser;
import org.sxchinacourt.dataparser.TaskParser;
import org.sxchinacourt.dataparser.UserNewParser;
import org.sxchinacourt.dataparser.UserParser;

/**
 *
 * @author baggio
 * @date 2017/2/6
 */

public class DataFormat {

    public static JSONObject formatUserNew(UserNewBean user) throws JSONException {
        JSONObject json = new JSONObject();
        if (!TextUtils.isEmpty(user.getOid())) {
            json.put(UserNewParser.OID, user.getOid());
        }
        if (!TextUtils.isEmpty(user.getDeptcode())) {
            json.put(UserNewParser.DEPTCODE, user.getDeptcode());
        }
        if (!TextUtils.isEmpty(user.getDeptid())) {
            json.put(UserNewParser.DEPTID, user.getDeptid());
        }
        if (!TextUtils.isEmpty(user.getDeptname())) {
            json.put(UserNewParser.DEPTNAME, user.getDeptname());
        }
        if (!TextUtils.isEmpty(user.getDeptoaid())) {
            json.put(UserNewParser.DEPTOAID, user.getDeptoaid());
        }
        if (!TextUtils.isEmpty(user.getEmpEmail())) {
            json.put(UserNewParser.EMPEMAIL, user.getEmpEmail());
        }
        if (!TextUtils.isEmpty(user.getEmpMobilephone())) {
            json.put(UserNewParser.EMPMOBILEPHONE, user.getEmpMobilephone());
        }
        if (!TextUtils.isEmpty(user.getEmpid())) {
            json.put(UserNewParser.EMPID, user.getEmpid());
        }
        if (!TextUtils.isEmpty(user.getEmpname())) {
            json.put(UserNewParser.EMPNAME, user.getEmpname());
        }
        if (!TextUtils.isEmpty(user.getNote())) {
            json.put(UserNewParser.NOTE, user.getNote());
        }
        if (!TextUtils.isEmpty(user.getOaid())) {
            json.put(UserNewParser.OAID, user.getOaid());
        }
        if (!TextUtils.isEmpty(user.getRoleid())) {
            json.put(UserNewParser.ROLEID, user.getRoleid());
        }
        if (!TextUtils.isEmpty(user.getUserName())) {
            json.put(UserNewParser.USERNAME, user.getUserName());
        }
        if (!TextUtils.isEmpty(user.getUserPassword())) {
            json.put(UserNewParser.USERPASSWORD, user.getUserName());
        }
        if (!TextUtils.isEmpty(user.getOrgcode())) {
            json.put(UserNewParser.ORGCODE, user.getOrgcode());
        }
        if (!TextUtils.isEmpty(user.getOrgid())) {
            json.put(UserNewParser.ORGID, user.getOrgid());
        }
        if (!TextUtils.isEmpty(user.getOrgname())) {
            json.put(UserNewParser.ORGNAME, user.getOrgname());
        }
        if (!TextUtils.isEmpty(user.getHandPassword())) {
            json.put(UserNewParser.HANDPASSWORD, user.getHandPassword());
        }
        if (!TextUtils.isEmpty(user.getRelation())) {
            json.put(UserNewParser.RELATION, user.getRelation());
        }
        if (!TextUtils.isEmpty(user.getSigninfo())) {
            json.put(UserNewParser.SIGNINFO, user.getSigninfo());
        }
        if (!TextUtils.isEmpty(user.getSignpwd())) {
            json.put(UserNewParser.SIGNPWD, user.getSignpwd());
        }
        return json;
    }



    public static JSONObject formatUser(UserBean user) throws JSONException {
        JSONObject json = new JSONObject();
        json.put(UserParser.ID, user.getId());
        if (!TextUtils.isEmpty(user.getUserId())) {
            json.put(UserParser.USERID, user.getUserId());
        }
        if (!TextUtils.isEmpty(user.getUserName())) {
            json.put(UserParser.USERNAME, user.getUserName());
        }
        if (!TextUtils.isEmpty(user.getUserNo())) {
            json.put(UserParser.USERNO, user.getUserNo());
        }
        if (!TextUtils.isEmpty(user.getCompanyName())) {
            json.put(UserParser.COMPANYNAME, user.getCompanyName());
        }
        if (!TextUtils.isEmpty(user.getDepartmentFullName())) {
            json.put(UserParser.DEPARTMENTFULLNAME, user.getDepartmentFullName());
        }
        if (!TextUtils.isEmpty(user.getEmail())) {
            json.put(UserParser.EMAIL, user.getEmail());
        }
        if (!TextUtils.isEmpty(user.getExtend1())) {
            json.put(UserParser.EXTEND1, user.getExtend1());
        }
        if (!TextUtils.isEmpty(user.getExtend2())) {
            json.put(UserParser.EXTEND2, user.getExtend2());
        }
        if (!TextUtils.isEmpty(user.getExtend3())) {
            json.put(UserParser.EXTEND3, user.getExtend3());
        }
        if (!TextUtils.isEmpty(user.getExtend4())) {
            json.put(UserParser.EXTEND4, user.getExtend4());
        }
        if (!TextUtils.isEmpty(user.getExtend5())) {
            json.put(UserParser.EXTEND5, user.getExtend5());
        }
        if (!TextUtils.isEmpty(user.getMobile())) {
            json.put(UserParser.MOBILE, user.getMobile());
        }
        if (!TextUtils.isEmpty(user.getTel())) {
            json.put(UserParser.TEL, user.getTel());
        }
        if (!TextUtils.isEmpty(user.getUserMap())) {
            json.put(UserParser.USERMAP, user.getUserMap());
        }
        if (user.getDepartment() != null) {
            json.put(UserParser.DEPARTMENT, formatDepartment(user.getDepartment()));
        }
        if (user.getRole() != null) {
            json.put(UserParser.ROLE, formatRole(user.getRole()));
        }
        if (!TextUtils.isEmpty(user.getRoleName())) {
            json.put(UserParser.ROLENAME, user.getRoleName());
        }
        if (!TextUtils.isEmpty(user.getCourtoaid())) {
            json.put(UserParser.COURTOAID, user.getCourtoaid());
        }
        return json;
    }

    public static JSONObject formatDepartment(DepartmentBean department) throws JSONException {
        JSONObject json = new JSONObject();
        if (!TextUtils.isEmpty(department.getId())) {
            json.put(DepartmentParser.ID, department.getId());
        }
        if (!TextUtils.isEmpty(department.getDepartmentFullId())) {
            json.put(DepartmentParser.DEPARTMENTFULLID, department.getDepartmentFullId());
        }
        if (!TextUtils.isEmpty(department.getDepartmentName())) {
            json.put(DepartmentParser.DEPARTMENTNAME, department.getDepartmentName());
        }
        if (!TextUtils.isEmpty(department.getDepartmentFullName())) {
            json.put(DepartmentParser.DEPARTMENTFULLNAME, department.getDepartmentFullName());
        }
        if (!TextUtils.isEmpty(department.getDepartmentNo())) {
            json.put(DepartmentParser.DEPARTMENTNO, department.getDepartmentNo());
        }
        if (!TextUtils.isEmpty(department.getDepartmentZone())) {
            json.put(DepartmentParser.DEPARTMENTZONE, department.getDepartmentZone());
        }
        if (!TextUtils.isEmpty(department.getParentDepartmentId())) {
            json.put(DepartmentParser.PARENTDEPARTMENTID, department.getParentDepartmentId());
        }
        if (!TextUtils.isEmpty(department.getLayer())) {
            json.put(DepartmentParser.LAYER, department.getLayer());
        }
        if (!TextUtils.isEmpty(department.getExtend1())) {
            json.put(DepartmentParser.EXTEND1, department.getExtend1());
        }
        if (!TextUtils.isEmpty(department.getExtend2())) {
            json.put(DepartmentParser.EXTEND2, department.getExtend2());
        }
        return json;
    }

    public static JSONObject formatRole(RoleBean role) throws JSONException {
        JSONObject json = new JSONObject();
        if (!TextUtils.isEmpty(role.getId())) {
            json.put(RoleParser.ID, role.getId());
        }
        if (!TextUtils.isEmpty(role.getRoleName())) {
            json.put(RoleParser.ROLENAME, role.getRoleName());
        }
        if (!TextUtils.isEmpty(role.getGroupName())) {
            json.put(RoleParser.GROUPNAME, role.getGroupName());
        }
        return json;
    }

    public static JSONObject formatTask(TaskBean task) throws JSONException {
        JSONObject json = new JSONObject();
        json.put(TaskParser.TASKID, task.getTaskId());
        if (!TextUtils.isEmpty(task.getActivityDefId())) {
            json.put(TaskParser.ACTIVITYDEFID, task.getActivityDefId());
        }
        if (!TextUtils.isEmpty(task.getBeginTime())) {
            json.put(TaskParser.BEGINTIME, task.getBeginTime());
        }
        if (!TextUtils.isEmpty(task.getBindUrl())) {
            json.put(TaskParser.BINDURL, task.getBindUrl());
        }
        if (!TextUtils.isEmpty(task.getOwner())) {
            json.put(TaskParser.OWNER, task.getOwner());
        }
        if (!TextUtils.isEmpty(task.getOwnerName())) {
            json.put(TaskParser.OWNERNAME, task.getOwnerName());
        }
        if (!TextUtils.isEmpty(task.getProcessDefId())) {
            json.put(TaskParser.PROCESSDEFID, task.getProcessDefId());
        }
        if (!TextUtils.isEmpty(task.getProcessGroup())) {
            json.put(TaskParser.PROCESSGROUP, task.getProcessGroup());
        }
        if (!TextUtils.isEmpty(task.getReadTime())) {
            json.put(TaskParser.READTIME, task.getReadTime());
        }
        if (!TextUtils.isEmpty(task.getStepname())) {
            json.put(TaskParser.STEPNAME, task.getStepname());
        }
        if (!TextUtils.isEmpty(task.getTitle())) {
            json.put(TaskParser.TITLE, task.getTitle());
        }
        if (!TextUtils.isEmpty(task.getWftitle())) {
            json.put(TaskParser.WFTITLE, task.getWftitle());
        }
        json.put(TaskParser.PROCESSINSTANCEID, task.getProcessInstanceId());
        json.put(TaskParser.PRIORITY, String.valueOf(task.getPriority()));
        json.put(TaskParser.STATUS, String.valueOf(task.getStatus()));
        json.put(TaskParser.ISREAD, task.isRead() ? true : false);
        return json;
    }

    public static JSONObject formatAssignTask(AssignTaskBean assignTask) throws JSONException {
        JSONObject json = new JSONObject();
        if (!TextUtils.isEmpty(assignTask.getWTitle())) {
            json.put(AssignTaskParser.WTITLE, assignTask.getWTitle());
        }
        if (!TextUtils.isEmpty(assignTask.getNote())) {
            json.put(AssignTaskParser.NOTE, assignTask.getNote());
        }
        if (!TextUtils.isEmpty(assignTask.getSTime())) {
            json.put(AssignTaskParser.STIME, assignTask.getSTime());
        }
        if (!TextUtils.isEmpty(assignTask.getETime())) {
            json.put(AssignTaskParser.ETIME, assignTask.getETime());
        }
        if (!TextUtils.isEmpty(assignTask.getAlertTime())) {
            json.put(AssignTaskParser.ALERTTIME, assignTask.getAlertTime());
        }
        if (!TextUtils.isEmpty(assignTask.getWContent())) {
            json.put(AssignTaskParser.WCONTENT, assignTask.getWContent());
        }
        if (!TextUtils.isEmpty(assignTask.getWsMan())) {
            json.put(AssignTaskParser.WSMAN, assignTask.getWsMan());
        }
        if (!TextUtils.isEmpty(assignTask.getWsManId())) {
            json.put(AssignTaskParser.WSMANID, assignTask.getWsManId());
        }
        json.put(AssignTaskParser.WSMANOAID, assignTask.getWsManOAId());
        if (assignTask.getDetails() != null) {
            JSONArray jsonArray = new JSONArray();
            for (AssignSubTaskBean subTask : assignTask.getDetails()) {
                JSONObject jsonObject = formatAssignSubTask(subTask);
                jsonArray.put(jsonObject);
            }
            json.put(AssignTaskParser.DETAILS, jsonArray);
        }
        return json;
    }

    public static JSONObject formatAssignSubTask(AssignSubTaskBean assignSubTask) throws
            JSONException {
        JSONObject json = new JSONObject();
        if (!TextUtils.isEmpty(assignSubTask.getSTime())) {
            json.put(AssignTaskParser.STIME, assignSubTask.getSTime());
        }
        if (!TextUtils.isEmpty(assignSubTask.getETime())) {
            json.put(AssignTaskParser.ETIME, assignSubTask.getETime());
        }
        if (!TextUtils.isEmpty(assignSubTask.getContent())) {
            json.put(AssignTaskParser.WCONTENT, assignSubTask.getContent());
        }
        if (!TextUtils.isEmpty(assignSubTask.getWsMan())) {
            json.put(AssignTaskParser.WSMAN, assignSubTask.getWsMan());
        }
        if (!TextUtils.isEmpty(assignSubTask.getWsManId())) {
            json.put(AssignTaskParser.WSMANID, assignSubTask.getWsManId());
        }
        json.put(AssignTaskParser.WSMANOAID, assignSubTask.getWsManOAId());
        return json;
    }
}
