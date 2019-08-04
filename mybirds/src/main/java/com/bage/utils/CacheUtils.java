package com.bage.utils;

/**
 * Created by bage on 2016/3/18.
 */

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * Description:<br>
 * This is a description of the class
 * <br>
 * User: bage<br>
 * Date: 2015-12-01 <br>
 * Copyright @ 2015 www.bage.com<br>
 */
public class CacheUtils {

    /**
     * 获取音频文件的缓存目录<br>
     * 优先考虑存放在SD卡上，没有绑定SD卡则放在机身储存
     * @param context 上下文
     * @param andioName 音频文件名(包含后缀名)
     * @return 频频文件的缓存目录
     */
    public static String getAndioCachePath(Context context, String andioName) {
        // getCachePath 如果可以，会优先返回带SD卡的path，否则返回机身的path
        // 优先考虑存在内存卡
        if (SystemUtils.SDCardIsMounted()) {
            return getAndioSDCardCachePath(context, andioName);
        } else {
            // 否则缓存到机身存储
            return getAndioPhoneCachePath(context, andioName);
        }
    }
    /**
     * 获取图片文件的缓存目录<br>
     * 优先考虑存放在SD卡上，没有绑定SD卡则放在机身储存
     * @param context 上下文
     * @param photoName 图片文件名(包含后缀名)
     * @return 图片文件的缓存目录
     */
    public static String getPhotoCachePath(Context context, String photoName) {
        // getCachePath 如果可以，会优先返回带SD卡的path，否则返回机身的path
        // 优先考虑存在内存卡
        if (SystemUtils.SDCardIsMounted()) {
            return getPhotoSDCardCachePath(context, photoName);
        } else {
            // 否则缓存到机身存储
            return getPhotoPhoneCachePath(context, photoName);
        }
    }

    /**
     * 获取机身存储的路径
     * @param context 上下文
     * @param fileName 文件名字
     * @return
     */
    private static String getPhotoPhoneCachePath(Context context, String fileName) {
        // 项目文件夹
        String ModuleFolder = getPhoneModuleFolder(context);
        // 总缓存文件夹
        String cacheFolder = getPhoneCacheFolder(context, ModuleFolder);
        // 当前缓存文件夹
        String currentCacheFolder = getPhonePhotoFolder(context, cacheFolder);
        // 真正的文件路径
        String path = currentCacheFolder + "/" + fileName;
        return null;
    }

    private static String getPhonePhotoFolder(Context context, String parentFolder) {
        File file;
        String cacheFolder = parentFolder + "/" + MuduleUtils.getPhotoFolder(context);
        file = new File(cacheFolder);
        if (!file.exists()) { // 如果cache文件夹不存在，则创建
            file.mkdirs();
        }
        return cacheFolder;
    }

    private static String getPhotoSDCardCachePath(Context context, String parentFolder) {
        File file;
        String cacheFolder = parentFolder + "/" + MuduleUtils.getPhotoFolder(context);
        file = new File(cacheFolder);
        if (!file.exists()) { // 如果cache文件夹不存在，则创建
            file.mkdirs();
        }
        return cacheFolder;
    }

    public static String getCachePath(Context context, String fileName) {
        // 优先考虑存在内存卡
        if (SystemUtils.SDCardIsMounted()) {
            return getAndioSDCardCachePath(context, fileName);
        } else {
            // 否则缓存到机身存储
            return getAndioPhoneCachePath(context, fileName);
        }
    }

    private static String getAndioPhoneCachePath(Context context, String fileName) {

        // 项目文件夹
        String ModuleFolder = getPhoneModuleFolder(context);
        // 总缓存文件夹
        String cacheFolder = getPhoneCacheFolder(context, ModuleFolder);
        // 当前缓存文件夹
        String currentCacheFolder = getPhoneAudioFolder(context, cacheFolder);
        // 真正的文件路径
        String path = currentCacheFolder + "/" + fileName;
        return path;
    }
    private static String getAndioSDCardCachePath(Context context, String fileName) {
        // 总文件夹
        String ModuleFolder = getSDCardModuleFolder(context);
        // 总缓存文件夹
        String cacheFolder = getSDCardCacheFolder(context, ModuleFolder);
        // 当前缓存文件夹
        String currentCacheFolder = getSDCardAudioFolder(context, cacheFolder);
        // 真正的文件路径
        String path = currentCacheFolder + "/" + fileName;
        return path;
    }

    /**
     * 获取SD卡音频文件的文件夹路径
     * @param context 上下文
     * @param parentFolder 父文件夹
     * @return SD卡音频文件的文件夹路径
     */
    @NonNull
    private static String getSDCardAudioFolder(Context context, String parentFolder) {
        File file;
        String currentCacheFolder = parentFolder + "/" + MuduleUtils.getAudioFolder(context);
        file = new File(currentCacheFolder);
        if (!file.exists()) { // 如果cache文件夹不存在，则创建
            file.mkdirs();
        }
        return currentCacheFolder;
    }

    /**
     * 获取SD卡cache文件夹路径
     * @param context 上下文
     * @param parentFolder 父文件夹
     * @return SD卡cache文件夹路径
     */
    @NonNull
    private static String getSDCardCacheFolder(Context context, String parentFolder) {
        File file;
        String cacheFolder = parentFolder + "/" + MuduleUtils.getCacheFolder(context);
        file = new File(cacheFolder);
        if (!file.exists()) { // 如果cache文件夹不存在，则创建
            file.mkdirs();
        }
        return cacheFolder;
    }

    /**
     * 获取SD卡的根目录
     * @param context 上下文
     * @return SD卡的根目录
     */
    @NonNull
    private static String getSDCardModuleFolder(Context context) {
        String ModuleFolder = Environment.getExternalStorageDirectory() + "/" + MuduleUtils.getModuleName(context);
        File file = new File(ModuleFolder);
        if (!file.exists()) { // 如果整个项目文件夹不存在，则创建
            file.mkdirs();
        }
        return ModuleFolder;
    }

    /**
     * 获取机身储存音频文件的文件夹路径
     * @param context 上下文
     * @param parentFolder 父文件夹
     * @return 机身储存音频文件的文件夹路径
     */
    @NonNull
    private static String getPhoneAudioFolder(Context context, String parentFolder) {
        File file;
        String currentCacheFolder = parentFolder + "/" + MuduleUtils.getAudioFolder(context);
        file = new File(currentCacheFolder);
        if (!file.exists()) { // 如果cache文件夹不存在，则创建
            file.mkdirs();
        }
        return currentCacheFolder;
    }

    /**
     * 获取机身储存cache文件夹路径
     * @param context 上下文
     * @param parentFolder 父文件夹
     * @return 机身储存cache文件夹路径
     */
    @NonNull
    private static String getPhoneCacheFolder(Context context, String parentFolder) {
        File file;
        String cacheFolder = parentFolder + "/" + MuduleUtils.getCacheFolder(context);
        file = new File(cacheFolder);
        if (!file.exists()) { // 如果cache文件夹不存在，则创建
            file.mkdirs();
        }
        return cacheFolder;
    }

    /**
     * 获取机身储存的根目录
     * @param context 上下文
     * @return 机身储存的根目录
     */
    @NonNull
    private static String getPhoneModuleFolder(Context context) {
        String ModuleFolder = context.getFilesDir() + "/" + MuduleUtils.getModuleName(context);
        File file = new File(ModuleFolder);
        if (!file.exists()) { // 如果整个项目文件夹不存在，则创建
            file.mkdirs();
        }
        return ModuleFolder;
    }

}

