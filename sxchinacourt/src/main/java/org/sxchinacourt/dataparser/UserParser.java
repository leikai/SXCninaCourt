package org.sxchinacourt.dataparser;

import org.json.JSONException;
import org.json.JSONObject;
import org.sxchinacourt.bean.DepartmentBean;
import org.sxchinacourt.bean.RoleBean;
import org.sxchinacourt.bean.UserBean;

/**
 * Created by baggio on 2017/2/6.
 */

public class UserParser extends DataParser {
    public static final String ID = "id";
    public static final String USERID = "userId";
    public static final String USERNAME = "userName";
    public static final String USERNO = "userNo";
    public static final String TEL = "tel";
    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";
    public static final String COMPANYNAME = "companyName";
    public static final String DEPARTMENTNAME = "departmentName";
    public static final String DEPARTMENTFULLNAME = "departmentFullName";
    public static final String USERMAP = "userMap";
    public static final String EXTEND1 = "extend1";
    public static final String EXTEND2 = "extend2";
    public static final String EXTEND3 = "extend3";
    public static final String EXTEND4 = "extend4";
    public static final String EXTEND5 = "extend5";
    public static final String DEPARTMENT = "department";
    public static final String ROLE = "role";
    public static final String ROLENAME = "roleName";
    public static final String COURTOAID = "courtoaid";
    public static final String OAID = "oaid";

    @Override
    public UserBean parser(JSONObject jsonObject) throws JSONException {
        UserBean user = new UserBean();
        user.setId(jsonObject.getInt(ID));
        if (jsonObject.has(USERID)) {
            user.setUserId(jsonObject.getString(USERID));
        }
        if (jsonObject.has(USERNAME)) {
            user.setUserName(jsonObject.getString(USERNAME));
        }
        if (jsonObject.has(USERNO)) {
            user.setUserNo(jsonObject.getString(USERNO));
        }
        if (jsonObject.has(TEL)) {
            user.setTel(jsonObject.getString(TEL));
        }
        if (jsonObject.has(MOBILE)) {
            user.setMobile(jsonObject.getString(MOBILE));
        }
        if (jsonObject.has(EMAIL)) {
            user.setEmail(jsonObject.getString(EMAIL));
        }
        if (jsonObject.has(COMPANYNAME)) {
            user.setCompanyName(jsonObject.getString(COMPANYNAME));
        }
        if (jsonObject.has(DEPARTMENTNAME)) {
            user.setDepartmentName(jsonObject.getString(DEPARTMENTNAME));
        }
        if (jsonObject.has(DEPARTMENTFULLNAME)) {
            user.setDepartmentFullName(jsonObject.getString(DEPARTMENTFULLNAME));
        }
        if (jsonObject.has(EXTEND1)) {
            user.setExtend1(jsonObject.getString(EXTEND1));
        }
        if (jsonObject.has(EXTEND2)) {
            user.setExtend2(jsonObject.getString(EXTEND2));
        }
        if (jsonObject.has(EXTEND3)) {
            user.setExtend3(jsonObject.getString(EXTEND3));
        }
        if (jsonObject.has(EXTEND4)) {
            user.setExtend4(jsonObject.getString(EXTEND4));
        }
        if (jsonObject.has(EXTEND5)) {
            user.setExtend5(jsonObject.getString(EXTEND5));
        }
        if (jsonObject.has(USERMAP)) {
            user.setUserMap(jsonObject.getString(USERMAP));
        }
        if (jsonObject.has(ROLENAME)) {
            user.setRoleName(jsonObject.getString(ROLENAME));
        }
        if (jsonObject.has(DEPARTMENT)) {
            JSONObject departmentJson = jsonObject.getJSONObject(DEPARTMENT);
            DepartmentBean department = (DepartmentBean) DataParser.factory(PARSER_TYPE_DEPARTMENT).parser
                    (departmentJson);
            user.setDepartment(department);
        }
        if (jsonObject.has(ROLE)) {
            JSONObject roleJson = jsonObject.getJSONObject(ROLE);
            RoleBean role = (RoleBean) DataParser.factory(PARSER_TYPE_ROLE).parser(roleJson);
            user.setRole(role);
        }
        if (jsonObject.has(COURTOAID)) {
            user.setCourtoaid(jsonObject.getString(COURTOAID));
        }
        if (jsonObject.has(OAID)) {
            user.setCourtoaid(jsonObject.getString(OAID));
        }
        return user;
    }
}
