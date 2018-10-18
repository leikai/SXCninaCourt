package org.sxchinacourt.dataparser;

import org.json.JSONException;
import org.json.JSONObject;
import org.sxchinacourt.bean.DepartmentBean;

/**
 *
 * @author baggio
 * @date 2017/2/8
 */

public class DepartmentParser extends DataParser {
    public static final String ID = "id";
    public static final String DEPARTMENTNAME = "departmentName";
    public static final String DEPARTMENTNO = "departmentNo";
    public static final String DEPARTMENTFULLNAME = "departmentFullName";
    public static final String DEPARTMENTFULLID = "departmentFullId";
    public static final String PARENTDEPARTMENTID = "parentDepartmentId";
    public static final String LAYER = "layer";
    public static final String DEPARTMENTZONE = "departmentZone";
    public static final String EXTEND1 = "extend1";
    public static final String EXTEND2 = "extend2";

    @Override
    public DepartmentBean parser(JSONObject jsonObject) throws JSONException {
        DepartmentBean department = new DepartmentBean();
        if (jsonObject.has(ID)) {
            department.setId(jsonObject.getString(ID));
        }
        if (jsonObject.has(DEPARTMENTNAME)) {
            department.setDepartmentName(jsonObject.getString(DEPARTMENTNAME));
        }
        if (jsonObject.has(DEPARTMENTNO)) {
            department.setDepartmentNo(jsonObject.getString(DEPARTMENTNO));
        }
        if (jsonObject.has(DEPARTMENTFULLNAME)) {
            department.setDepartmentFullName(jsonObject.getString(DEPARTMENTFULLNAME));
        }
        if (jsonObject.has(DEPARTMENTFULLID)) {
            department.setDepartmentFullId(jsonObject.getString(DEPARTMENTFULLID));
        }
        if (jsonObject.has(PARENTDEPARTMENTID)) {
            department.setParentDepartmentId(jsonObject.getString(PARENTDEPARTMENTID));
        }
        if (jsonObject.has(LAYER)) {
            department.setLayer(jsonObject.getString(LAYER));
        }
        if (jsonObject.has(DEPARTMENTZONE)) {
            department.setDepartmentZone(jsonObject.getString(DEPARTMENTZONE));
        }
        if (jsonObject.has(EXTEND1)) {
            department.setExtend1(jsonObject.getString(EXTEND1));
        }
        if (jsonObject.has(EXTEND2)) {
            department.setExtend2(EXTEND2);
        }
        return department;
    }
}
