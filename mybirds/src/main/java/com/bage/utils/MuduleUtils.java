package com.bage.utils;

import android.content.Context;

import com.bage.mybirds.R;

/**
 * Created by bage on 2016/3/18.
 */
public class MuduleUtils {

    private static String moduleName = null;
    private static String cacheFolder = null;
    private static String photoFolder = null;
    private static String audioFolder = null;

    public static String getModuleName(Context context) {
        if (moduleName == null) {
            moduleName = context.getResources().getString(R.string.moduleName);
        }
        return moduleName;
    }

    public static String getCacheFolder(Context context) {
        if (cacheFolder == null) {
            cacheFolder = context.getResources().getString(R.string.cacheFolder);
        }
        return cacheFolder;
    }

    public static String getAudioFolder(Context context) {
        if (audioFolder == null) {
            audioFolder = context.getResources().getString(R.string.audioFolder);
        }
        return audioFolder;
    }

    public static String getPhotoFolder(Context context) {
        if (photoFolder == null) {
            photoFolder = context.getResources().getString(R.string.photoFolder);
        }
        return photoFolder;
    }
}
