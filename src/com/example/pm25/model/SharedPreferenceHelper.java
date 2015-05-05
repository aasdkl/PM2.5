package com.example.pm25.model;

import com.example.pm25.util.myComponent.MyApplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

final public class SharedPreferenceHelper {

	private static final String FILE_NAME = "PM25";
	private static Context context = MyApplication.getContext();
	
	public static void savaCacheDetails(String cityName, String stationName, String response){
		if (response!=null && response.trim().length()!=0 && !response.contains("error")) {
			SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE).edit();
			editor.putString(cityName + stationName, response);
			editor.commit();
		}
	}
	
	public static String loadCacheDetails(String cityName, String stationName){
		SharedPreferences shared = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
		return shared.getString(cityName+stationName, null);
	}
	
	
}
