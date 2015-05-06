package com.example.pm25.model;

import com.example.pm25.R.string;
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
	private static SharedPreferences shared = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
	private static SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE).edit();
	private static final String INTERESTED = "interested";
	
	public static void savaCacheDetails(City city, Station station, String response){
		if (response!=null && response.trim().length()!=0 && !response.contains(PM25APIKeys.ERROR.toString())) {
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
				String stationName;
		if (station==null) {
			stationName = "";
		} else {
			stationName = station.getName();
		}
		return shared.getString(city.getCityName() + stationName, null);
	}

	public static String loadInterestedCities(){
		return shared.getString(INTERESTED, "");
	}
	
	public static void saveInterestedCities(String cityStr){
		editor.putString(INTERESTED, cityStr);
		editor.commit();
	}
	
	
	
}
