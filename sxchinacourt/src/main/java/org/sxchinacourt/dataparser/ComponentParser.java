package org.sxchinacourt.dataparser;

import org.json.JSONException;
import org.json.JSONObject;
import org.sxchinacourt.bean.Component;
import org.sxchinacourt.bean.SelectViewComponent;
import org.sxchinacourt.bean.SubViewComponent;
import org.sxchinacourt.bean.ViewComponent;
import org.sxchinacourt.bean.ViewComponents;

/**
 *
 * @author baggio
 * @date 2017/3/2
 */

public class ComponentParser {
    public static final String TEXT = "text";
    public static final String LABELVALUE = "labelvalue";
    public static final String HINT = "hint";

    public static Component parser(ViewComponents components, JSONObject jsonObject) throws
            JSONException {
        Component component;
        if (jsonObject.has(SubViewComponentParser.SUBVIEW)) {
            if (jsonObject.getString(SubViewComponentParser.SUBVIEW).equals
                    (ViewComponent.VIEW_TYPE_SELECTVIEW)) {
                component = (SelectViewComponent) DataParser.factory
                        (DataParser
                                .PARSER_TYPE_SELECTVIEWCOMPONENTPARSER).parser(jsonObject);
            } else {
                component = (SubViewComponent) DataParser.factory
                        (DataParser
                                .PARSER_TYPE_SUBVIEWCOMPONENTPARSER).parser(jsonObject);
            }
            components.addSubViewComponent(component);
        } else {
            component = (ViewComponent) DataParser.factory(DataParser
                    .PARSER_TYPE_VIEWCOMPONENT).parser(jsonObject);
        }
        components.addComponent(component);
        if (jsonObject.has(TEXT)) {
            component.setText(jsonObject.getString(TEXT));
        }
        if (jsonObject.has(LABELVALUE)) {
            component.setLabelValue(jsonObject.getString(LABELVALUE));
        }
        if (jsonObject.has(HINT)) {
            component.setHintText(jsonObject.getString(HINT));
        }
        return component;
    }
}
