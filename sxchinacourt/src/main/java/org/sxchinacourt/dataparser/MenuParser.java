package org.sxchinacourt.dataparser;

import org.json.JSONException;
import org.json.JSONObject;
import org.sxchinacourt.bean.MenuBean;

/**
 *
 * @author baggio
 * @date 2017/2/20
 */

public class MenuParser extends DataParser {
    /**
     * 菜单名称
     */
    public static String MENUNAME = "menuName";
    /**
     * 一个 int 值，菜单的动作代码
     */
    public static String MENUTYPE = "menuType";
    /**
     * 是否选择
     */
    public static String ISCHECKED = "isChecked";
    /**
     * 留言内容
     */
    public static String OPINION = "opinion";

    @Override
    public MenuBean parser(JSONObject jsonObject) throws JSONException {
        MenuBean menu = new MenuBean();
        if (jsonObject.has(MENUNAME)) {
            menu.setMenuName(jsonObject.getString(MENUNAME));
        }
        if (jsonObject.has(MENUTYPE)) {
            menu.setMenuType(jsonObject.getInt(MENUTYPE));
        }
        if (jsonObject.has(ISCHECKED)) {
            menu.setChecked(jsonObject.getBoolean(ISCHECKED));
        }
        if (jsonObject.has(OPINION)) {
            menu.setOpinion(jsonObject.getString(OPINION));
        }
        return menu;
    }
}
