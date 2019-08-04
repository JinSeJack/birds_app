package com.bage.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

public class LogUtils {


	public static boolean isDegugging = true;
	
	public static void syso(String str){
		if(isDegugging){
			System.out.print(str);
		}		
	}
	public static void sysoln(String str){
		if(isDegugging){
			System.out.println(str);
		}
	}
	public static void sysolist(List<Object> list){
		if(isDegugging){
			for (Object obj : list) {
				System.out.println(obj.toString());
			}
			System.out.println();
		}
	}
	public static void sysolist(Object []arr){
		if(isDegugging){
			for (Object obj : arr) {
				System.out.println(obj.toString());
			}
			System.out.println();
		}
	}

	public static void shownToast(Context context,String str){
		if(isDegugging){
			Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
		}
	}
	
}
