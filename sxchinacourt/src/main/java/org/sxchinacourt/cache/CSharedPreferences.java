package org.sxchinacourt.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.dataformat.DataFormat;
import org.sxchinacourt.dataparser.DataParser;

/**
 * Created by baggio on 2017/2/6.
 */

public class CSharedPreferences {
    private static final String FILENAME = "sxchinacourt_sp";
    private static final CSharedPreferences mInstance = new CSharedPreferences();
    private static final String FIELD_CURRENTUSER = "currentUser";
    private static final String FIELD_CURRENTTOKEN = "currentToken";
    private static final String FIELD_CURSERVERINFO = "curserverinfo";

    private CSharedPreferences() {
    }

    public static CSharedPreferences getInstance() {
        return mInstance;
    }

    public void clear() {
        SharedPreferences sps = CApplication.getInstance()
                .getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        Editor editor = sps.edit();
        editor.remove(FIELD_CURRENTUSER);
        editor.commit();
    }

    public void setCurrentUser(UserNewBean user) {
        SharedPreferences sps = CApplication.getInstance()
                .getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        Editor editor = sps.edit();
        if (user != null) {
            try {
                editor.putString(FIELD_CURRENTUSER, DataFormat.formatUserNew(user)
                        .toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            editor.remove(FIELD_CURRENTUSER);
        }
        editor.commit();
    }

    public UserNewBean getCurrentUser() {
        SharedPreferences sps = CApplication.getInstance()
                .getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        if (!sps.contains(FIELD_CURRENTUSER)) {
            return null;
        }
        String data = sps.getString(FIELD_CURRENTUSER, null);
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        try {
            return (UserNewBean) DataParser.factory(DataParser.PARSER_TYPE_USER_NEW)
                    .parser(new JSONObject(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setToken(String token) {
        if (!TextUtils.isEmpty(token)) {
            SharedPreferences sps = CApplication.getInstance()
                    .getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
            Editor editor = sps.edit();
            editor.putString(FIELD_CURRENTTOKEN, token);
            editor.commit();
        }
    }

    public String getToken() {
        SharedPreferences sps = CApplication.getInstance()
                .getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        if (!sps.contains(FIELD_CURSERVERINFO)) {
            return null;
        }
        String token = sps.getString(FIELD_CURRENTTOKEN, null);
        if (TextUtils.isEmpty(token)) {
            return null;
        }
        return token;
    }

    public void saveServerInfo(String json) {
        if (!TextUtils.isEmpty(json)) {
            SharedPreferences sps = CApplication.getInstance()
                    .getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
            Editor editor = sps.edit();
            editor.putString(FIELD_CURSERVERINFO, json);
            editor.commit();
        }
    }

    public JSONObject getServerInfo() throws JSONException {
        SharedPreferences sps = CApplication.getInstance()
                .getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        if (!sps.contains(FIELD_CURSERVERINFO)) {
            return null;
        }
        String data = sps.getString(FIELD_CURSERVERINFO, null);
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        return new JSONObject(data);
    }
}
