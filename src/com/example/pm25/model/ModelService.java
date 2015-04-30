package com.example.pm25.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.example.pm25.connectivity.ConnectService;
import com.example.pm25.model.db.DBService;
import com.example.pm25.util.MyLog;
import com.example.pm25.util.myComponent.CityComparator;
import com.example.pm25.util.myComponent.MyApplication;

/**
 * @author Administrator
 *
 */
public class ModelService {
	
	private static DBService dbService = DBService.getInstance(MyApplication.getContext());
	
	/**
	 * 调用数据获取，
	 * <b>注</b>：此方法会开启线程，需要使用ModelCallBackListener进行回调
	 */
	//TODO synchronized在activity手动加锁
	public synchronized static void getCities(final ModelCallBackListener<City> listener){
		new Thread(new Runnable() {
			public void run() {
				List<City> cities = new LinkedList<>();
				// 从数据库获取数据
				//TODO 可能新增数据，考虑判断如果联网就直接连
				if (dbService.isCityTableEmpty()) {
					fetchData(listener);
				}
				cities = dbService.loadCities();
				// 通知回调
				listener.onFinish(cities);
			}
		}).start();
	}
	
	private static void fetchData(final ModelCallBackListener<City> listener) {
		//如果数据库中没有数据，从网络获取
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
}
