package org.sxchinacourt.dataparser;

import android.util.Log;

import com.google.android.gms.playlog.internal.LogEvent;

import org.json.JSONException;
import org.json.JSONObject;
import org.sxchinacourt.bean.SubViewComponent;

/**
 * Created by baggio on 2017/3/2.
 */

public class SubViewComponentParser extends DataParser {
    public static final String SUBVIEW = "subview";
    public static final String NAME = "name";
    public static final String INITVALUE = "initvalue";

    @Override
    public SubViewComponent parser(JSONObject jsonObject) throws JSONException {
        SubViewComponent component = new SubViewComponent();
        //jsonObject.has() 判断json是否包括某个KEY
        if (jsonObject.has(SUBVIEW)) {
            component.setViewName(jsonObject.getString(SUBVIEW));
        }
        if (jsonObject.has(NAME)) {
            component.setName(jsonObject.getString(NAME));
        }
        if (jsonObject.has(INITVALUE)) {
            component.setInitValue(jsonObject.getString(INITVALUE));
        }
        return component;
    }
}
