package com.example.pm25.model;

import com.example.pm25.po.City;
import com.example.pm25.po.Station;
import com.example.pm25.util.PM25APIKeys;
import com.example.pm25.util.myComponent.MyApplication;

import android.R.id;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

final public class SharedPreferenceHelper {

	private static final String FILE_NAME = "PM25";
	private static Context context = MyApplication.getContext();
	
	public static void savaCacheDetails(City city, Station station, String response){
		if (response!=null && response.trim().length()!=0 && !response.contains(PM25APIKeys.ERROR.toString())) {
			SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE).edit();
			String stationName;
			if (station==null) {
				stationName = "";
			} else {
				stationName = station.getName();
			}
			editor.putString(city.getCityName() + stationName, response);
			editor.commit();
		}
	}
	
	public static String loadCacheDetails(City city, Station station){
		SharedPreferences shared = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
		String stationName;
		if (station==null) {
			stationName = "";
		} else {
			stationName = station.getName();
		}
		return shared.getString(city.getCityName() + stationName, null);
	}

	
	
}
