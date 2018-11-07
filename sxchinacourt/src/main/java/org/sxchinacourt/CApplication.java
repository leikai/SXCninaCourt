package org.sxchinacourt;

import android.app.Application;

import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.cache.CSharedPreferences;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.util.network.SSLRequest;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import cn.jpush.android.api.JPushInterface;


/**
 *
 * @author baggio
 * @date 2017/2/6
 */

public class CApplication extends Application {
    public static CApplication mInstance;
    private UserNewBean mCurrentUser;
    private String mToken;
    private String mEmployeeID;


    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        mInstance = this;
        try {
            //证书验证
            SSLRequest.allowAllSSL(WebServiceUtil.BASE_SERVER_URL,
                    getBaseContext());
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        mCurrentUser = CSharedPreferences.getInstance().getCurrentUser();
        mToken = CSharedPreferences.getInstance().getToken();

    }

    public static CApplication getInstance() {
        return mInstance;
    }

    public UserNewBean getCurrentUser() {
        return mCurrentUser;
    }

    public void setUser(UserNewBean user) {
        if (user != null) {
            mCurrentUser = user;
            CSharedPreferences.getInstance().setCurrentUser(user);
        }
    }

    public String getCurrentToken() {
        return mToken;
    }
    public void setToken(String token) {
        if (token != null) {
            mToken = token;
            CSharedPreferences.getInstance().setToken(token);
        }
    }

    public String getCurrentEmployeeID() {
        return mEmployeeID;
    }
    public void setEmployeeID(String employeeID) {
        if (employeeID != null) {
            mEmployeeID = employeeID;
            CSharedPreferences.getInstance().setEmployeeID(employeeID);
        }
    }



    @Override
    public void onTerminate() {
        super.onTerminate();
        mCurrentUser = null;
        mInstance = null;
    }

}
