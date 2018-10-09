package org.sxchinacourt.dataparser;

import org.json.JSONException;
import org.json.JSONObject;
import org.sxchinacourt.bean.OBDataBean;

/**
 * Created by baggio on 2017/2/17.
 */

public class OBDataParser extends DataParser {
    public static final String ID = "ID";             //自动编号
    public static final String ORGNO = "ORGNO";          //
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
    //    private String mSubPicture;
    public static final String CONTENT = "CONTENT";
    //    private boolean mIsClose;
//    private boolean mIsTalk;
    public static final String ARCHIVES = "ARCHIVES";
    //    private String mSecurityList;
//    private String mYDFW;
//    private boolean mIsCopy;
//    private String mZYTS;
//    private String mISZD;
//    private String mBTJC;
//    private String mSource;
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
