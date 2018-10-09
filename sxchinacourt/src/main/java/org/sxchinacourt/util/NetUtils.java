package org.sxchinacourt.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Email：312607360@qq.com
 * Created by 温瑞壮 on 2017/9/14.
 * Description：
 */
public class NetUtils {
    /**
     * 描述：判断网络是否有效.
     *
     * @param context
     *            上下文
     * @return true, if is network available
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 判断网络是否是wifi
     * @param context
     */
    public static boolean isWifiAvailable(Context context) {
        boolean isWifi = true;
        //判断有没有活动网络
        ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo=manager.getActiveNetworkInfo();
        if (activeNetworkInfo==null )
        {//无网络
            isWifi = false;
        }else
        {
            //判断打开的是什么网络
            NetworkInfo wifiNetworkInfo=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetworkInfo!=null && wifiNetworkInfo.isConnected())
            {
                isWifi = true;
            }else{
                isWifi = false;
            }
        }
        return isWifi;
    }
}
