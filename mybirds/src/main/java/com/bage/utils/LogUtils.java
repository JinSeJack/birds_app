package com.bage.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import java.util.List;

public class LogUtils {


    public static boolean isDegugging = true;

    public static void syso(String str) {
        if (isDegugging) {
            System.out.print(str);
        }
    }

    public static void sysoln(String str) {
        if (isDegugging) {
            System.out.println(str);
        }
    }

    public static void sysolist(List<Object> list) {
        if (isDegugging) {
            for (Object obj : list) {
                System.out.println(obj.toString());
            }
            System.out.println();
        }
    }

    public static void sysolist(Object[] arr) {
        if (isDegugging) {
            for (Object obj : arr) {
                System.out.println(obj.toString());
            }
            System.out.println();
        }
    }

    public static void shownToast(Context context, String text) {
        Toast toast = null;
        if (isDegugging) {
            try {
                if (toast != null) {
                    toast.setText(text);
                } else {
                    toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                }
                toast.show();
            } catch (Exception e) {
                //解决在子线程中调用Toast的异常情况处理
                Looper.prepare();
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }
//		if(isDegugging){
//			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
//		}
    }

}
