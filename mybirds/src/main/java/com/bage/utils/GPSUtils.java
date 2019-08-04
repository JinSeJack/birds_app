package com.bage.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * Created by bage on 2016/3/16.
 */
public class GPSUtils {

    private Context mContext;
    private LocationManager locationManager;

    public GPSUtils(Context context) {
        super();
        mContext = context;
        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

    }

    /**
     * 　　* 获取地址的经纬度
     * 　　*
     * 　　* @return 维度_经度
     */
    public String getGpsAddress() {
        // 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
        List<String> lp = locationManager.getAllProviders();
        for (String item : lp) {
            LogUtils.sysoln("可用位置服务：" + item);
        }

        Criteria criteria = new Criteria();
        criteria.setCostAllowed(false);
        // 设置位置服务免费
        criteria.setAccuracy(Criteria.ACCURACY_COARSE); // 设置水平位置精度
        // getBestProvider 只有允许访问调用活动的位置供应商将被返回
        String providerName = locationManager.getBestProvider(criteria, true);
        LogUtils.sysoln("------位置服务：" + providerName);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Location location = locationManager.getLastKnownLocation(providerName);
        if (location != null) {
            return "纬度:" + location.getLatitude() + " 经度:"
                    + location.getLongitude();
        } else {
            LogUtils.shownToast(mContext, "1.请检查网络连接 \n2.请打开我的位置");
            return "未能获取到当前位置，请检测以下设置：\n1.检查网络连接 \n2.打开我的位置(GPS)";
        }

    }
}
