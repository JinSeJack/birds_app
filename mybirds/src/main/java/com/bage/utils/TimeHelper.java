package com.bage.utils;

import java.util.Calendar;

public class TimeHelper {

	public static String getCurrentTime(){
		Calendar c = Calendar.getInstance();//���Զ�ÿ��ʱ���򵥶��޸�
		int year = c.get(Calendar.YEAR); 
		String y = year + "";
		int month = c.get(Calendar.MONTH)+1; 
		String m = month + "";
		if(month < 10){
			m = "0" + m;
		}
		int date = c.get(Calendar.DATE); 
		String d = date + "";
		if(date < 10){
			d = "0" + d;
		}
		int hour = c.get(Calendar.HOUR_OF_DAY); 
		String h = hour + "";
		if(hour < 10){
			h = "0" + h;
		}
		int minute = c.get(Calendar.MINUTE);
		String mi = minute + "";
		if(minute < 10){
			mi = "0" + mi;
		}
		int second = c.get(Calendar.SECOND); 
		String s = second + "";
		if(second < 10){
			s = "0" + s;
		}
		return y + m + d + h + mi + s; 

	}
	public static int isLater(String time1,String time2){
		return time1.compareTo(time2);
	
	}
}
