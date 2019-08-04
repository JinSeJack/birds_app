package com.bage.utils;

import android.graphics.Bitmap;

import com.bage.mybirds.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by bage on 2016/6/6.
 */
public class MyImageLoaderUtils {
    private static DisplayImageOptions options;
    private static String url = null;

    public static DisplayImageOptions getOption() {
        // Get singleton instance
        if (options == null) {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.logo)
                    .showImageForEmptyUri(R.drawable.logo)
                    .showImageOnFail(R.drawable.logo)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(20))
                    .build();
        }
        return options;
    }

    public static DisplayImageOptions getUserOption() {
        // Get singleton instance
        if (options == null) {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.user_loading)
                    .showImageForEmptyUri(R.drawable.user_null)
                    .showImageOnFail(R.drawable.user_null)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(20))
                    .build();
        }
        return options;
    }

    public static DisplayImageOptions getCarOption() {
        // Get singleton instance
        if (options == null) {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.car_loading)
                    .showImageForEmptyUri(R.drawable.car_null)
                    .showImageOnFail(R.drawable.car_null)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(20))
                    .build();
        }
        return options;
    }

}
