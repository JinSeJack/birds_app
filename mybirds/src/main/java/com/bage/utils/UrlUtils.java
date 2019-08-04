package com.bage.utils;

import android.content.Context;

import com.bage.mybirds.R;

public class UrlUtils {

    private static String urlPre = null;
    private static String urlPre2 = null;

    public static String getControllerUrl(Context context, String controllerName, String methodName) {
        if (urlPre == null) {
            String IPAddress = context.getResources().getString(R.string.IPAddress);
            String port = context.getResources().getString(R.string.port);
            String serverName = context.getResources().getString(R.string.serverName);
            urlPre = "http://" + IPAddress + ":" + port + "/" + serverName;
        }
        String url = urlPre + "/" + controllerName + "/" + methodName;
        return url;
    }

    /**
     * 最后有斜杠
     * @param context
     * @return
     */
    public static String getFilePreUrl(Context context) {
        if (urlPre2 == null) {
            String IPAddress = context.getResources().getString(R.string.IPAddress);
            String port = context.getResources().getString(R.string.port);
            String serverName = context.getResources().getString(R.string.serverName);
            urlPre2 = "http://" + IPAddress + ":" + port + "/" + serverName+"/user/picture/";
        }
        return urlPre2;
    }

}
