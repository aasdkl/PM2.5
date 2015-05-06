package com.example.pm25.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.R.bool;
import android.R.id;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.PerformanceTestCase;
import android.text.format.DateFormat;

import com.example.pm25.connectivity.ConnectService;
import com.example.pm25.connectivity.JsonTools;
import com.example.pm25.model.db.DBService;
import com.example.pm25.po.City;
import com.example.pm25.po.Station;
import com.example.pm25.po.AirQuality;
import com.example.pm25.util.Constants;
import com.example.pm25.util.myComponent.MyApplication;

/**
 * @author Administrator
 *
 */
public class ModelService {
	//TODO 由于可能新增数据，所以方法考虑判断如果联网就直接连
	
	private static DBService dbService = DBService.getInstance(MyApplication.getContext());
	
	/**
	 * 调用数据获取城市列表
	 * <b>注</b>：此方法会开启线程，需要使用ModelCallBackListener进行回调
	 */
	public synchronized static void getCities(final ModelCallBackListener<City> listener){
		new Thread(new Runnable() {
			public void run() {
				List<City> cities = new LinkedList<>();
				// 从数据库获取数据
				if (dbService.isCityTableEmpty()) {
					fetchCityToDB(listener);
				}
				cities = dbService.loadCities();
				// 通知回调
				listener.onFinish(cities);
			}
		}).start();
	}
	

	/**
	 * 调用数据获取Stations数据
	 * <b>注</b>：此方法会开启线程，需要使用ModelCallBackListener进行回调
	 * @param city 
	 */	
	public synchronized static void getStations(final City city, final ModelCallBackListener<Station> listener) {
		new Thread(new Runnable() {
			public void run() {
				List<Station> stations = new LinkedList<>();
				// 从数据库获取station的数据
				if (dbService.isCityStationEmpty(city)) {
					fetchStationToDB(city, listener);
				}

				stations = dbService.loadStations(city);
				// 通知回调
				listener.onFinish(stations);
			}
		}).start();
	}

	/**
	 * 调用数据获取数据
	 * <b>注</b>：此方法会开启线程，需要使用ModelCallBackListener进行回调
	 * @param city 
	 * @param station 
	 */
	public synchronized static void getDetails(final City city, final Station station, final ModelCallBackListener<AirQuality> listener) {
		
		new Thread(new Runnable() {
			public void run() {
				List<AirQuality> qualities = new LinkedList<>();

				AirQuality oldData = getOldData(city, station, listener);

				// 如果上次sharedDatas里面数据为空，或者sharedDatas时间和实际的时间相差>1小时
				if (oldData==null || isDataTooOld(oldData)) {
					// 网络获取数据
					qualities = ConnectService.getQualities(city, station, listener);
				} else {
					// 否则返回上次的数据
					qualities.add(oldData);
				}
				
				listener.onFinish(qualities);
			}

		}).start();

	}
	
	/**
	 * 判断sharedDatas中存储的旧数据时间和实际的时间相差是否>1小时
	 * @param oldData
	 * @return
	 */
	private static boolean isDataTooOld(AirQuality oldData) {
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Constants.LOCALE);
		Date old = null;
		try {
			old = dataFormat.parse(oldData.getTime_point());
		} catch (ParseException e) {
			e.printStackTrace();
		}							// 1H
		int delay = (int) ((new Date().getTime() - old.getTime()) / (Constants.AN_HOUR));

		return (delay > 1) ? true : false;
	}
	
	private static AirQuality getOldData(City city, Station station, 
			ModelCallBackListener<AirQuality> listener) {
		String dataJsonStr = SharedPreferenceHelper.loadCacheDetails(city, station);
		if (dataJsonStr != null) {
			return JsonTools.parseQuality(city, station, dataJsonStr, listener);
		} else {
			return null;
		}
	}
	
	/**
	 * 此时数据库没有数据，需要从网络获取并存到数据库
	 * @param city 
	 * @param listener
	 */
	private static void fetchStationToDB(City city, final ModelCallBackListener<Station> listener) {
		List<Station> stations = ConnectService.getStations(city, listener);
		for (Station station : stations) {
			dbService.saveStation(station);
		}

		stations.clear();
		stations = null;
	}
	
	/**
	 * 此时数据库没有数据，需要从网络获取并存到数据库
	 * @param listener
	 */
	private static void fetchCityToDB(final ModelCallBackListener<City> listener) {
		List<City> cities = new LinkedList<>();
		cities = ConnectService.getCities(listener);
		String nowSpell = "#";
		for (City city : cities) {
			// 增加用于分隔拼音首字母的隔板
			if (!city.getCitySpell().startsWith(nowSpell)) {
				nowSpell = city.getCitySpell().substring(0,1);
				City spellHeader = new City(nowSpell);
				spellHeader.setCitySpell(nowSpell);
				dbService.saveCity(spellHeader);
			}
			dbService.saveCity(city);
		}
		cities.clear();
		cities = null;
	}

	
	//TODO
	public static List<City> getInterestedCities() {
		return null;
	}


	public static void removeInterested(City selectedCity) {
		// TODO Auto-generated method stub
		
	}


	public static void addInterested(City selectedCity) {
		// TODO Auto-generated method stub
		
	}
	

}
