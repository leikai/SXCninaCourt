package org.sxchinacourt.dataparser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by baggio on 2017/2/24.
 */

public class AssignSubTaskParser extends DataParser {
    public final static String ETIME = "etime"; //结束日期
    public final static String STIME = "stime"; //开始日期
    public static final String WSMAN = "wsman";//:布置人姓名
    public static final String WSMANID = "wsmanid";//布置人id  getUserInfo接口获取的 courtoaid的值
    public static final String WSMANOAID = "wsmanoaid";//布置人工作流id  getUserInfo接口获取的 id的值
    @Override
    public Object parser(JSONObject jsonObject) throws JSONException {
        return null;
    }
}
