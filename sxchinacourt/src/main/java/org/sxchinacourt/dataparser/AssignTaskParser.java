package org.sxchinacourt.dataparser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by baggio on 2017/2/24.
 */

public class AssignTaskParser extends DataParser {
    public static final String WTITLE = "wtitle";//任务标题
    public static final String WSMAN = "wsman";//:布置人姓名
    public static final String WSMANID = "wsmanid";//布置人id  getUserInfo接口获取的 courtoaid的值
    public static final String WSMANOAID = "wsmanoaid";//布置人工作流id  getUserInfo接口获取的 id的值
    public static final String NOTE = "note";//选择的执行人姓名，多人用,隔开
    public static final String STIME = "stime";//开始日期
    public static final String ETIME = "etime";//结束日期
    public static final String ALERTTIME = "alerttime";//提醒日期
    public static final String WCONTENT = "wcontent";//任务内容
    public static final String DETAILS = "details";//执行人明细
    @Override
    public Object parser(JSONObject jsonObject) throws JSONException {
        return null;
    }
}
