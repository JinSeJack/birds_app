package com.bage.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonUtils {

	public static <T> T toBeen(String jsonStr,Class<T> classOfT){
		Gson gson = new Gson();
		T t = gson.fromJson(jsonStr, classOfT);
		return t;
	}
	public static <T> String BeantoJsonStr(T t){
		Gson gson = new Gson();
		String jsonStr = gson.toJson(t);		
		return jsonStr;
	}


	/**
	 * 解析list
	 *
	 * @param jsonStr
	 * @param typeOfT
	 * @param <T>
	 * @return
	 */
	public static <T> T fromJson(String jsonStr, Type typeOfT) {
		Gson gson = new Gson();
		T t = gson.fromJson(jsonStr, typeOfT);
		return t;
	}


}
