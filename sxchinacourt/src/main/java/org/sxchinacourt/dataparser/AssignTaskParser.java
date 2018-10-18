package org.sxchinacourt.dataparser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author baggio
 * @date 2017/2/24
 */

public class AssignTaskParser extends DataParser {
    /**
     * 任务标题
     */
    public static final String WTITLE = "wtitle";
    /**
     * 布置人姓名
     */
    public static final String WSMAN = "wsman";
    /**
     * 布置人id  getUserInfo接口获取的 courtoaid的值
     */
    public static final String WSMANID = "wsmanid";
    /**
     * 布置人工作流id  getUserInfo接口获取的 id的值
     */
    public static final String WSMANOAID = "wsmanoaid";
    /**
     * 选择的执行人姓名，多人用,隔开
     */
    public static final String NOTE = "note";
    /**
     * 开始日期
     */
    public static final String STIME = "stime";
    /**
     * 结束日期
     */
    public static final String ETIME = "etime";
    /**
     * 提醒日期
     */
    public static final String ALERTTIME = "alerttime";
    /**
     * 任务内容
     */
    public static final String WCONTENT = "wcontent";
    /**
     * 执行人明细
     */
    public static final String DETAILS = "details";
    @Override
    public Object parser(JSONObject jsonObject) throws JSONException {
        return null;
    }
}
