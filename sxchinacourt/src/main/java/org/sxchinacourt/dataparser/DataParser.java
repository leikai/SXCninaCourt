package org.sxchinacourt.dataparser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by baggio on 2017/2/6.
 */

public abstract class DataParser {
    public final static int PARSER_TYPE_USER = 0;
    public final static int PARSER_TYPE_TASK = 1;
    public final static int PARSER_TYPE_DEPARTMENT = 2;
    public final static int PARSER_TYPE_ROLE = 3;
    public final static int PARSER_TYPE_OBDATA = 4;
    public final static int PARSER_TYPE_VIEWCOMPONENT = 5;
    public final static int PARSER_TYPE_PROCESSOPINION = 6;
    public final static int PARSER_TYPE_MENU = 7;
    public final static int PARSER_TYPE_TASKICREATED = 8;
    public final static int PARSER_TYPE_TASKASSIGNEDTOME = 9;
    public final static int PARSER_TYPE_SUBVIEWCOMPONENTPARSER = 10;
    public final static int PARSER_TYPE_SELECTVIEWCOMPONENTPARSER = 11;
    public final static int PARSER_TYPE_TOKEN = 12;
    public final static int PARSER_TYPE_USER_NEW = 13;

    public abstract Object parser(JSONObject jsonObject) throws JSONException;

    public static DataParser factory(int type) {
        DataParser parser = null;
        switch (type) {
            case PARSER_TYPE_TASK:
                parser = new TaskParser();
                break;
            case PARSER_TYPE_DEPARTMENT:
                parser = new DepartmentParser();
                break;
            case PARSER_TYPE_ROLE:
                parser = new RoleParser();
                break;
            case PARSER_TYPE_OBDATA:
                parser = new OBDataParser();
                break;
            case PARSER_TYPE_VIEWCOMPONENT:
                parser = new ViewComponentParser();
                break;
            case PARSER_TYPE_PROCESSOPINION:
                parser = new ProcessOpinionParser();
                break;
            case PARSER_TYPE_MENU:
                parser = new MenuParser();
                break;
            case PARSER_TYPE_TASKICREATED:
                parser = new TaskICreatedParser();
                break;
            case PARSER_TYPE_TASKASSIGNEDTOME:
                parser = new TaskAssignedToMeParser();
                break;
            case PARSER_TYPE_SUBVIEWCOMPONENTPARSER:
                parser = new SubViewComponentParser();
                break;
            case PARSER_TYPE_SELECTVIEWCOMPONENTPARSER:
                parser = new SelectViewComponentParser();
                break;
            case PARSER_TYPE_TOKEN:
                parser = new SelectViewComponentParser();
                break;
            case PARSER_TYPE_USER_NEW:
                parser = new UserNewParser();
                break;
        }
        return parser;
    }


}
