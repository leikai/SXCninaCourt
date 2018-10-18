package org.sxchinacourt.dataparser;

import org.json.JSONException;
import org.json.JSONObject;
import org.sxchinacourt.bean.OBDataBean;

/**
 *
 * @author baggio
 * @date 2017/2/17
 */

public class OBDataParser extends DataParser {
    /**
     * 自动编号
     */
    public static final String ID = "ID";
    public static final String ORGNO = "ORGNO";
    public static final String CREATEDATE = "CREATEDATE";
    public static final String CREATEUSER = "CREATEUSER";
    public static final String UPDATEDATE = "UPDATEDATE";
    public static final String UPDATEUSER = "UPDATEUSER";
    public static final String TITLE = "TITLE";
    public static final String PREPICTURE = "PREPICTURE";
    public static final String RELEASEDATE = "RELEASEDATE";
    public static final String RELEASEUSER = "RELEASEUSER";
    public static final String RELEASEDEPARTMENT = "RELEASEDEPARTMENT";
    public static final String SUBTITLE = "SUBTITLE";
    public static final String CONTENT = "CONTENT";
    public static final String ARCHIVES = "ARCHIVES";
    public static final String DISPLAYTITLE = "DISPLAYTITLE";
    public static final String DEPTID = "DEPTID";

    @Override
    public OBDataBean parser(JSONObject jsonObject) throws JSONException {
        OBDataBean OBData = new OBDataBean();
        if (jsonObject.has(ID)) {
            OBData.setId(jsonObject.getInt(ID));
        }
        if (jsonObject.has(CREATEDATE)) {
            OBData.setCreateDate(jsonObject.getString(CREATEDATE));
        }
        if (jsonObject.has(CREATEUSER)) {
            OBData.setCreateUser(jsonObject.getString(CREATEUSER));
        }
        if (jsonObject.has(CONTENT)) {
            OBData.setContent(jsonObject.getString(CONTENT));
        }
        if (jsonObject.has(TITLE)) {
            OBData.setTitle(jsonObject.getString(TITLE));
        }
        if (jsonObject.has(DISPLAYTITLE)) {
            OBData.setDisplayTitle(jsonObject.getString(DISPLAYTITLE));
        }
        if (jsonObject.has(ARCHIVES)) {
            OBData.setArchives(jsonObject.getString(ARCHIVES));
        }
        if (jsonObject.has(DEPTID)) {
            OBData.setDeptId(jsonObject.getString(DEPTID));
        }
        return OBData;
    }
}
