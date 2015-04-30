package com.example.pm25.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.example.pm25.connectivity.ConnectService;
import com.example.pm25.model.db.DBService;
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
				cities = dbService.loadCities();
				if (cities.size() == 0) {
					//如果数据库中没有数据，从网络获取
					cities = ConnectService.getCities(listener);
					for (City city : cities) {
						dbService.saveCity(city);
					}
				}
				// 通知回调
				listener.onFinish(cities);
			}
		}).start();
	}
}
