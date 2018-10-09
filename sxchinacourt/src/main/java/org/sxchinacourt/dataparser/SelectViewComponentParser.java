package org.sxchinacourt.dataparser;

import org.json.JSONException;
import org.json.JSONObject;
import org.sxchinacourt.bean.SelectViewComponent;

/**
 * Created by baggio on 2017/3/2.
 */

public class SelectViewComponentParser extends SubViewComponentParser {
    public static final String DEFAULTVALUE = "defaultvalue";

    @Override
    public SelectViewComponent parser(JSONObject jsonObject) throws JSONException {
        SelectViewComponent component = new SelectViewComponent();
//        ComponentParser.parser(component, jsonObject);
        if (jsonObject.has(SUBVIEW)) {
            component.setViewName(jsonObject.getString(SUBVIEW));
        }
        if (jsonObject.has(NAME)) {
            component.setName(jsonObject.getString(NAME));
        }
        if (jsonObject.has(DEFAULTVALUE)) {
            component.setDefaultItem(jsonObject.getString(DEFAULTVALUE));
        }
        return component;
    }
}
